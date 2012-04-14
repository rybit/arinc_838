package edu.cmu.sv.arinc838.crc;

import static org.testng.AssertJUnit.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import org.testng.annotations.Test;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.dao.FileDefinitionDao;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;

public class LspCrcCalculatorTest {
	@Test
	public void testCanCalculateCrc() throws Exception {
		String path = "src/test/resources/crc_test_files";
		SoftwareDefinitionFileDao dao = new SoftwareDefinitionFileDao();

		dao.setPath(path);

		FileDefinitionDao file1 = new FileDefinitionDao();
		file1.setFileName("CRC_T10A.rom");

		FileDefinitionDao file2 = new FileDefinitionDao();
		file2.setFileName("CRC_T09A.rom");

		dao.getFileDefinitions().add(file1);
		dao.getFileDefinitions().add(file2);

		LspCrcCalculator calc = new LspCrcCalculator();

		BdfFile file = new BdfFile(File.createTempFile("prefix", "suffix"));

		BdfFile fileOne = new BdfFile(new File(path, file1.getFileName()));
		BdfFile fileTwo = new BdfFile(new File(path, file2.getFileName()));

		file.writeStr64k("i am data");

		byte[] bb = new byte[(int) (fileOne.length() + fileTwo.length() + file
				.length())];
		byte[] fileOneBytes = fileOne.readAll();		
		int pointer = 0;
		
		for(int i = 0; i < fileOneBytes.length; i++){
			bb[pointer] = fileOneBytes[i];
			pointer++;
		}		

		byte[] fileTwoBytes = fileTwo.readAll();
		for(int i = 0; i < fileTwoBytes.length; i++){
			bb[pointer] = fileTwoBytes[i];
			pointer++;
		}

		byte[] fileBytes = file.readAll();
		for(int i = 0; i < file.length(); i++){
			bb[pointer] = fileBytes[i];
			pointer++;
		}

		CrcGenerator crc = mock(CrcGenerator.class);
		when(crc.calculateCrc(bb)).thenReturn(17L);

		assertEquals(calc.calculateCrc(file, dao, crc), 17L);
	}
}
