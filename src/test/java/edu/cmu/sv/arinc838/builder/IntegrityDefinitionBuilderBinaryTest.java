package edu.cmu.sv.arinc838.builder;

import static org.testng.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.annotations.Test;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.builder.IntegrityDefinitionDao.IntegrityType;
import edu.cmu.sv.arinc838.util.Converter;

public class IntegrityDefinitionBuilderBinaryTest {

	@Test
	public void buildBinaryCRC16() throws FileNotFoundException, IOException {
		BdfFile bdfFile = new BdfFile(File.createTempFile("tmpFile", ".bdf"));

		IntegrityDefinitionDao integ = new IntegrityDefinitionDao();
		integ.setIntegrityType(IntegrityType.CRC16.getType());
		integ.setIntegrityValue(Converter.hexToBytes("ABCD"));

		int bytesWritten = integ.buildBinary(bdfFile);

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

		int bytesWritten = integ.buildBinary(bdfFile);

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

		int bytesWritten = integ.buildBinary(bdfFile);

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
		IntegrityDefinitionDao integDefBuilder = new IntegrityDefinitionDao();
		integDefBuilder.setIntegrityType(IntegrityType.CRC64.getType());
		integDefBuilder.setIntegrityValue(Converter.hexToBytes("DEADBEEFDEADBEEF"));
		integDefBuilder.buildBinary(bdfFile);
		
		bdfFile.seek(0);
		
		IntegrityDefinitionDao integDefBuilder2 = new IntegrityDefinitionDao(bdfFile);
		assertEquals(integDefBuilder2.getIntegrityType(), integDefBuilder.getIntegrityType()); 
		assertEquals(integDefBuilder2.getIntegrityValue(), integDefBuilder.getIntegrityValue()); 
	}
}
