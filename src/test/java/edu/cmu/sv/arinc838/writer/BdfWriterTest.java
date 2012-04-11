package edu.cmu.sv.arinc838.writer;

//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

import static org.testng.Assert.assertEquals;

import java.io.File;

import org.mockito.InOrder;
import org.testng.annotations.Test;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;

public class BdfWriterTest {
	@Test
	public void bdfWriteTest() throws Exception {
		BdfFile writtenFile = mock(BdfFile.class);
		SoftwareDefinitionFileDao expected = mock(SoftwareDefinitionFileDao.class);
		SoftwareDefinitionFileBuilder mockedBuilder = mock (SoftwareDefinitionFileBuilder.class);
		
		BdfWriter writer = new BdfWriter();

		writer.write(writtenFile, mockedBuilder, expected);

		InOrder order = inOrder(writtenFile, mockedBuilder);
		order.verify(mockedBuilder).buildBinary(expected, writtenFile);
		order.verify(writtenFile).close();
	}

	@Test
	public void testWriteReturnsFileName() throws Exception {
		SoftwareDefinitionFileDao sdfDao = mock(SoftwareDefinitionFileDao.class);
		when(sdfDao.getBinaryFileName()).thenReturn("binary file");

		BdfWriter writer = new BdfWriter();
		String fileName = writer.getFilename(sdfDao);
		assertEquals(fileName, sdfDao.getBinaryFileName());
		
		new File(fileName).delete();
	}	
}
