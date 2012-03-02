package edu.cmu.sv.arinc838.builder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.builder.IntegrityDefinitionBuilder.IntegrityType;
import edu.cmu.sv.arinc838.util.Converter;

public class FileDefinitionBuilderBinaryTest {
	
	private FileDefinitionBuilder fileDefBuilder;
	
	@BeforeMethod
	public void setup() {
		fileDefBuilder = new FileDefinitionBuilder();
		fileDefBuilder.setFileLoadable(true);
		fileDefBuilder.setFileName("someFile.bin");
		fileDefBuilder.setFileSize(123456);
		IntegrityDefinitionBuilder integ = new IntegrityDefinitionBuilder();
		integ.setIntegrityType(IntegrityType.CRC32.getType());
		integ.setIntegrityValue(Converter.hexToBytes("DEADBEEF"));
		fileDefBuilder.setFileIntegrityDefinition(integ);
		
	}
	
	@Test
	public void buildBinary() throws FileNotFoundException, IOException {
		BdfFile bdfFile = new BdfFile(File.createTempFile("tmpFile", ".bdf"));
		int bytesWritten = fileDefBuilder.buildBinary(bdfFile);

		// 4 ptr to next + 1 is loadable + 14 file name + 4 file size + 10 CRC32 integrity
		// 4 + 1 + 14 + 4 + 14 = 37
		assertEquals(bytesWritten, 33);
		
		bdfFile.seek(0);

		long nextFileDefPointer = bdfFile.readUint32();
		assertEquals(bytesWritten, nextFileDefPointer);
		assertEquals(bdfFile.readBoolean(), true); // file is loadable
		assertEquals(bdfFile.readStr64k(), "someFile.bin"); // file name
		assertEquals(bdfFile.readUint32(), 123456); // file size
		assertEquals(bdfFile.readUint32(), IntegrityType.CRC32.getType()); // integrity type
		assertEquals(bdfFile.readShort(), 4); // integrity value length
		byte[] integValue = new byte[4];
		bdfFile.read(integValue);
		assertEquals(integValue, Converter.hexToBytes("DEADBEEF"));
	}

	@Test
	public void buildBinaryIsLast() throws FileNotFoundException, IOException {
		fileDefBuilder.setIsLast(true);
		BdfFile bdfFile = new BdfFile(File.createTempFile("tmpFile", ".bdf"));
		int bytesWritten = fileDefBuilder.buildBinary(bdfFile);

		// 4 ptr to next + 1 is loadable + 14 file name + 4 file size + 10 CRC32 integrity
		// 4 + 1 + 14 + 4 + 14 = 37
		assertEquals(bytesWritten, 33);
		
		bdfFile.seek(0);

		long nextFileDefPointer = bdfFile.readUint32();
		assertEquals(nextFileDefPointer, 0);
	}	
}
