package edu.cmu.sv.arinc838.builder;

import static org.testng.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.annotations.Test;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.builder.IntegrityDefinitionBuilder.IntegrityType;
import edu.cmu.sv.arinc838.util.Converter;

public class IntegrityDefinitionBuilderBinaryTest {


	@Test
	public void buildBinaryCRC16() throws FileNotFoundException, IOException {
		BdfFile bdfFile = new BdfFile(File.createTempFile("tmpFile", ".bdf"));
		
		IntegrityDefinitionBuilder integ = new IntegrityDefinitionBuilder();
		integ.setIntegrityType(IntegrityType.CRC16.getType());
		integ.setIntegrityValue(Converter.hexToBytes("ABCD"));

		int bytesWritten = integ.buildBinary(bdfFile);
		
		bdfFile.seek(0);
		
		assertEquals(bytesWritten, 10);
		assertEquals(bdfFile.readUint32(), IntegrityType.CRC16.getType());
		assertEquals(bdfFile.readUTF(), "ABCD");
	}
	
	@Test
	public void buildBinaryCRC32() throws FileNotFoundException, IOException {
		BdfFile bdfFile = new BdfFile(File.createTempFile("tmpFile", ".bdf"));
		
		IntegrityDefinitionBuilder integ = new IntegrityDefinitionBuilder();
		integ.setIntegrityType(IntegrityType.CRC32.getType());
		integ.setIntegrityValue(Converter.hexToBytes("DEADBEEF"));

		int bytesWritten = integ.buildBinary(bdfFile);
		
		bdfFile.seek(0);
		
		assertEquals(bytesWritten, 14);
		assertEquals(bdfFile.readUint32(), IntegrityType.CRC32.getType());
		assertEquals(bdfFile.readUTF(), "DEADBEEF");
	}
	
	@Test
	public void buildBinaryCRC64() throws FileNotFoundException, IOException {
		BdfFile bdfFile = new BdfFile(File.createTempFile("tmpFile", ".bdf"));
		
		IntegrityDefinitionBuilder integ = new IntegrityDefinitionBuilder();
		integ.setIntegrityType(IntegrityType.CRC64.getType());
		integ.setIntegrityValue(Converter.hexToBytes("DEADBEEFDEADBEEF"));

		int bytesWritten = integ.buildBinary(bdfFile);
		
		bdfFile.seek(0);
		
		assertEquals(bytesWritten, 22);
		assertEquals(bdfFile.readUint32(), IntegrityType.CRC64.getType());
		assertEquals(bdfFile.readUTF(), "DEADBEEFDEADBEEF");
	}
}
