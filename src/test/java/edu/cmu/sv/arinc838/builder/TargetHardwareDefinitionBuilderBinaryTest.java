package edu.cmu.sv.arinc838.builder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.cmu.sv.arinc838.binary.BdfFile;

public class TargetHardwareDefinitionBuilderBinaryTest {
	
	private TargetHardwareDefinitionBuilder thwDefBuilder;
	
	@BeforeMethod
	public void setup() {
		thwDefBuilder = new TargetHardwareDefinitionBuilder();
		thwDefBuilder.setId("ID3");
		thwDefBuilder.getPositions().add("R");
		thwDefBuilder.getPositions().add("L");
	}
	
	@Test
	public void buildBinary() throws FileNotFoundException, IOException {
		BdfFile bdfFile = new BdfFile(File.createTempFile("tmpFile", ".bdf"));
		int bytesWritten = thwDefBuilder.buildBinary(bdfFile);

		// 4 ptr to next + 5 thwID + 4 positions length +
		// 2(4 next position ptr + 3 thw postion) =
		// 13 + 2*7 = 13 + 14 = 27
		assertEquals(bytesWritten, 27);
		
		bdfFile.seek(0);

		long nextThwPointer = bdfFile.readUint32();
		assertEquals(bytesWritten, nextThwPointer);
		assertEquals(bdfFile.readStr64k(), "ID3");
		assertEquals(bdfFile.readUint32(), 2); // 2 thw-positions
		assertEquals(bdfFile.readUint32(), 20); // pointer to next thw-position
		assertEquals(bdfFile.readStr64k(), "R");
		assertEquals(bdfFile.readUint32(), 0); // pointer to next thw-position
		assertEquals(bdfFile.readStr64k(), "L");
	}

	@Test
	public void buildBinaryIsLast() throws FileNotFoundException, IOException {
		thwDefBuilder.setIsLast(true);
		BdfFile bdfFile = new BdfFile(File.createTempFile("tmpFile", ".bdf"));
		int bytesWritten = thwDefBuilder.buildBinary(bdfFile);

		// 4 ptr to next + 5 thwID + 4 positions length +
		// 2(4 next position ptr + 3 thw postion) =
		// 13 + 2*7 = 13 + 14 = 27
		assertEquals(bytesWritten, 27);
		
		bdfFile.seek(0);

		long nextThwPointer = bdfFile.readUint32();
		assertEquals(nextThwPointer, 0);
	}
	
	@Test
	public void targetHardwareDefinitionBuilderBdfFile() throws FileNotFoundException, IOException {
		BdfFile bdfFile = new BdfFile(File.createTempFile("tmpFile", ".bdf"));
		thwDefBuilder.buildBinary(bdfFile);

		bdfFile.seek(0); //return to start of file
		bdfFile.readUint32(); //parent object reads the pointers
		
		TargetHardwareDefinitionBuilder thwDefBuilder2 = new TargetHardwareDefinitionBuilder(bdfFile);
		assertEquals(thwDefBuilder2.getId(), thwDefBuilder.getId());
		assertEquals(thwDefBuilder2.getPositions().size(), thwDefBuilder.getPositions().size());
		for(int i=0; i<thwDefBuilder2.getPositions().size(); i++) {
			assertEquals(thwDefBuilder2.getPositions().get(i), thwDefBuilder.getPositions().get(i));
		}
	}
}
