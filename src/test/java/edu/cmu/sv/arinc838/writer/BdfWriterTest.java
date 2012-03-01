package edu.cmu.sv.arinc838.writer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;

public class BdfWriterTest {
	
	@Test
	public void testBuildsCorrectFileName(){
		SoftwareDefinitionFileBuilder builder = mock(SoftwareDefinitionFileBuilder.class);
		when(builder.getBinaryFileName()).thenReturn("binary");
		
		BdfWriter writer = new BdfWriter();
		
		assertEquals(writer.getFileNameAndPath("path", builder), "pathbinary");
	}
}
