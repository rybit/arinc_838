package edu.cmu.sv.arinc838.builder;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.dao.TargetHardwareDefinitionDao;

public class TargetHardwareDefinitionBuilderBinaryTest {
	
	private TargetHardwareDefinitionDao thwDao;
	
	@BeforeMethod
	public void setup() {
		thwDao = new TargetHardwareDefinitionDao();
		thwDao.setThwId("ID3");
		thwDao.getPositions().add("R");
		thwDao.getPositions().add("L");
	}
	
	@Test
	public void buildBinary() throws FileNotFoundException, IOException {
		BdfFile bdfFile = new BdfFile(File.createTempFile("tmpFile", ".bdf"));
		int bytesWritten = new TargetHardwareDefinitionBuilder().buildBinary(thwDao, bdfFile);

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
		thwDao.setIsLast(true);
		BdfFile bdfFile = new BdfFile(File.createTempFile("tmpFile", ".bdf"));
		int bytesWritten = new TargetHardwareDefinitionBuilder().buildBinary(thwDao, bdfFile);

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
		new TargetHardwareDefinitionBuilder().buildBinary(thwDao, bdfFile);

		bdfFile.seek(0); //return to start of file
		bdfFile.readUint32(); //parent object reads the pointers
		
		TargetHardwareDefinitionDao thDao2 = new TargetHardwareDefinitionDao (bdfFile);
		assertEquals(thDao2.getThwId(), thwDao.getThwId());
		assertEquals(thDao2.getPositions().size(), thwDao.getPositions().size());
		for(int i=0; i<thDao2.getPositions().size(); i++) {
			assertEquals(thDao2.getPositions().get(i), thwDao.getPositions().get(i));
		}
	}
}
