package edu.cmu.sv.arinc838.writer;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.io.File;

import org.mockito.InOrder;
import org.testng.annotations.Test;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;

public class BdfWriterTest {

	@Test
	public void xdfWriteTest() throws Exception {
		BdfFile writtenFile = mock(BdfFile.class);
		SoftwareDefinitionFileBuilder expected = mock(SoftwareDefinitionFileBuilder.class);

		BdfWriter writer = new BdfWriter();

		writer.write(writtenFile, expected);

		InOrder order = inOrder(writtenFile, expected);

		order.verify(expected).buildBinary(writtenFile);
		order.verify(writtenFile).close();
	}

	@Test
	public void testWriteReturnsFileName() throws Exception {
		SoftwareDefinitionFileBuilder builder = mock(SoftwareDefinitionFileBuilder.class);
		when(builder.getBinaryFileName()).thenReturn("binary file");

		String tempPath = System.getProperty("java.io.tmpdir");
		
		BdfWriter writer = new BdfWriter();
		
		String fileName = writer.write(tempPath, builder);
		
		assertEquals(fileName, tempPath+builder.getBinaryFileName());
		
		new File(fileName).delete();
	}	
}
