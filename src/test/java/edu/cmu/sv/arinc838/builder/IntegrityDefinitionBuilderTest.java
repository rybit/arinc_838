/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 7, 2012
 */
package edu.cmu.sv.arinc838.builder;

import static org.testng.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.arinc.arinc838.IntegrityDefinition;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao.IntegrityType;
import edu.cmu.sv.arinc838.util.Converter;

public class IntegrityDefinitionBuilderTest {

	private IntegrityDefinition integDef;
	private IntegrityDefinitionDao integDao;

	@BeforeMethod
	public void setup() {
		integDef = new IntegrityDefinition();
		integDef.setIntegrityType(IntegrityType.CRC16.getType());
		integDef.setIntegrityValue(Converter.hexToBytes("DEADBEEF"));

		integDao = new IntegrityDefinitionDao(integDef);
	}

	@Test
	public void testXmlBuilder() {
		IntegrityDefinitionBuilder integBuilder = new IntegrityDefinitionBuilder();
		IntegrityDefinition built = integBuilder.buildXml(integDao);
		
		assertEquals(built.getIntegrityType(), integDef.getIntegrityType());
		assertEquals(built.getIntegrityValue(), integDef.getIntegrityValue ());
	}

	@Test
	public void buildBinaryCRC16() throws FileNotFoundException, IOException {
		BdfFile bdfFile = new BdfFile(File.createTempFile("tmpFile", ".bdf"));

		IntegrityDefinitionDao integ = new IntegrityDefinitionDao();
		integ.setIntegrityType(IntegrityType.CRC16.getType());
		integ.setIntegrityValue(Converter.hexToBytes("ABCD"));

		int bytesWritten = new IntegrityDefinitionBuilder().buildBinary(integ, bdfFile);

		bdfFile.seek(0);

		// 4 bytes integ type + 4 bytes integ value (2 bytes for length, 2 for
		// byte array)
		assertEquals(bytesWritten, 8);
		assertEquals(bdfFile.readUint32(), IntegrityType.CRC16.getType());
		assertEquals(bdfFile.readShort(), 2);

		byte[] integValue = new byte[2];
		bdfFile.read(integValue);

		assertEquals(integValue, Converter.hexToBytes("ABCD"));
	}

	@Test
	public void buildBinaryCRC32() throws FileNotFoundException, IOException {
		BdfFile bdfFile = new BdfFile(File.createTempFile("tmpFile", ".bdf"));

		IntegrityDefinitionDao integ = new IntegrityDefinitionDao();
		integ.setIntegrityType(IntegrityType.CRC32.getType());
		integ.setIntegrityValue(Converter.hexToBytes("DEADBEEF"));

		int bytesWritten = new IntegrityDefinitionBuilder().buildBinary(integ, bdfFile);

		bdfFile.seek(0);

		// 4 bytes integ type + 6 bytes integ value (2 bytes for length, 4 for
		// byte array)
		assertEquals(bytesWritten, 10);
		assertEquals(bdfFile.readUint32(), IntegrityType.CRC32.getType());
		assertEquals(bdfFile.readShort(), 4);

		byte[] integValue = new byte[4];
		bdfFile.read(integValue);

		assertEquals(integValue, Converter.hexToBytes("DEADBEEF"));
	}

	@Test
	public void buildBinaryCRC64() throws FileNotFoundException, IOException {
		BdfFile bdfFile = new BdfFile(File.createTempFile("tmpFile", ".bdf"));

		IntegrityDefinitionDao integ = new IntegrityDefinitionDao();
		integ.setIntegrityType(IntegrityType.CRC64.getType());
		integ.setIntegrityValue(Converter.hexToBytes("DEADBEEFDEADBEEF"));

		int bytesWritten = new IntegrityDefinitionBuilder().buildBinary(integ, bdfFile);

		bdfFile.seek(0);

		// 4 bytes integ type + 10 bytes integ value (2 bytes for length, 8 for
		// byte array)
		assertEquals(bytesWritten, 14);
		assertEquals(bdfFile.readUint32(), IntegrityType.CRC64.getType());
		assertEquals(bdfFile.readShort(), 8);

		byte[] integValue = new byte[8];
		bdfFile.read(integValue);

		assertEquals(integValue, Converter.hexToBytes("DEADBEEFDEADBEEF"));
	}
	
	@Test
	public void integrityDefinitionBuilderBdfFile() throws IOException {
		BdfFile bdfFile = new BdfFile(File.createTempFile("tmpFile", ".bdf"));
		IntegrityDefinitionDao integDao = new IntegrityDefinitionDao();
		integDao.setIntegrityType(IntegrityType.CRC64.getType());
		integDao.setIntegrityValue(Converter.hexToBytes("DEADBEEFDEADBEEF"));
		
		new IntegrityDefinitionBuilder().buildBinary(integDao, bdfFile);
		
		bdfFile.seek(0);
		
		IntegrityDefinitionDao integDefBuilder2 = new IntegrityDefinitionDao(bdfFile);
		assertEquals(integDefBuilder2.getIntegrityType(), integDao.getIntegrityType()); 
		assertEquals(integDefBuilder2.getIntegrityValue(), integDao.getIntegrityValue()); 
	}
}
