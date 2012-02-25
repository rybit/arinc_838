/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 12, 2012
 */
package edu.cmu.sv.arinc838.builder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.arinc.arinc838.FileDefinition;
import com.arinc.arinc838.SdfFile;
import com.arinc.arinc838.SoftwareDescription;
import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.builder.IntegrityDefinitionBuilder.IntegrityType;
import edu.cmu.sv.arinc838.validation.DataValidator;
import edu.cmu.sv.arinc838.validation.ReferenceData;

public class SoftwareDefinitionFileBuilderTest {
	private SdfFile swDefFile;
	private SoftwareDefinitionFileBuilder swDefFileBuilder;
	private com.arinc.arinc838.IntegrityDefinition integrity;	
	private SoftwareDescription description;
	private com.arinc.arinc838.FileDefinition fileDef;
	private com.arinc.arinc838.ThwDefinition hardwareDef;

	@BeforeMethod
	public void beforeMethod() {
		
		integrity = new com.arinc.arinc838.IntegrityDefinition();
		integrity.setIntegrityType(IntegrityType.CRC16.getType());
		integrity.setIntegrityValue("0xABCD");

		description = new SoftwareDescription();
		description.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		description.setSoftwareTypeDescription("desc");
		description.setSoftwareTypeId(10l);

		fileDef = new com.arinc.arinc838.FileDefinition();
		fileDef.setFileName("file");
		fileDef.setFileIntegrityDefinition(integrity);
		fileDef.setFileSize(1234);
		List<FileDefinition> fileDefs = new ArrayList<FileDefinition>();
		fileDefs.add(fileDef);
		
		
		hardwareDef = new ThwDefinition();
		hardwareDef.setThwId("hardware");
		
		swDefFile = new SdfFile();
		
		
		swDefFile
				.setFileFormatVersion(SoftwareDefinitionFileBuilder.DEFAULT_FILE_FORMAT_VERSION);
		swDefFile.setSdfIntegrityDefinition(integrity);
		swDefFile.setLspIntegrityDefinition(integrity);
		swDefFile.setSoftwareDescription(description);

		swDefFile.getFileDefinitions().add(fileDef);
		swDefFile.getFileDefinitions().add(fileDef);
		
		swDefFile.getThwDefinitions().add(hardwareDef);
		swDefFile.getThwDefinitions().add(hardwareDef);
		
		swDefFileBuilder = new SoftwareDefinitionFileBuilder(swDefFile);
	}

	@Test
	public void getFileFormatVersion() {
		assertEquals(swDefFileBuilder.getFileFormatVersion(),
				swDefFile.getFileFormatVersion());
	}

	@Test
	public void getSoftwareDefinitionSections() {
		assertEquals(swDefFileBuilder.getSoftwareDescription().getSoftwarePartNumber(), 
				swDefFile.getSoftwareDescription().getSoftwarePartnumber());
	}

	@Test
	public void setFileFormatVersion() {
		// This is kind of dumb because the file format version can only have 1
		// value
		swDefFileBuilder
				.setFileFormatVersion(SoftwareDefinitionFileBuilder.DEFAULT_FILE_FORMAT_VERSION);
		assertEquals(SoftwareDefinitionFileBuilder.DEFAULT_FILE_FORMAT_VERSION,
				swDefFileBuilder.getFileFormatVersion());
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void setFileFormatVersionInvalid() {
		swDefFileBuilder
				.setFileFormatVersion(SoftwareDefinitionFileBuilder.DEFAULT_FILE_FORMAT_VERSION + 1);

	}

	@Test
	public void testBuildAddsFileFormatVersion() {
		SdfFile file = swDefFileBuilder.buildXml();

		assertEquals(swDefFile.getFileFormatVersion(),
				file.getFileFormatVersion());
	}


	@Test
	public void testDefaultConstructor() {
		SoftwareDefinitionFileBuilder builder = new SoftwareDefinitionFileBuilder();

		assertEquals(builder.getFileFormatVersion(), 0);

	}


	@Test
	public void testFileLengthIsSet() throws Exception {

		SoftwareDefinitionFileBuilder fileBuilder = new SoftwareDefinitionFileBuilder();
		fileBuilder
				.setFileFormatVersion(SoftwareDefinitionFileBuilder.DEFAULT_FILE_FORMAT_VERSION);

		BdfFile file = mock(BdfFile.class);

		fileBuilder.buildBinary(file);

		verify(file).writeFileFormatVersion(
				SoftwareDefinitionFileBuilder.DEFAULT_FILE_FORMAT_VERSION);

	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testFileDefinitionsEmpty()
	{
		SdfFile newSdfFile = swDefFileBuilder.buildXml();
		newSdfFile.getFileDefinitions().clear();
		
		new SoftwareDefinitionFileBuilder(newSdfFile);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testFileDefinitionsEmptyAtBuild()
	{
		swDefFileBuilder.getFileDefinitions().clear();
		
		swDefFileBuilder.buildXml();
	}
	
	@Test
	public void getFileDefinitions() {
		assertEquals(swDefFileBuilder.getFileDefinitions().size(),
				2);

		assertEquals(swDefFileBuilder.getFileDefinitions().get(0)
				.getFileName(), "file");
		assertEquals(swDefFileBuilder.getFileDefinitions().get(1)
				.getFileName(), "file");
	}

	@Test
	public void addFileDefinition() {
		swDefFileBuilder.getFileDefinitions().clear();

		FileDefinitionBuilder expected = mock(FileDefinitionBuilder.class);

		swDefFileBuilder.getFileDefinitions().add(expected);
		FileDefinitionBuilder actualFileDefinition = swDefFileBuilder
				.getFileDefinitions().get(0);
		assertEquals(actualFileDefinition, expected);
	}

	@Test
	public void getLspIntegrityDefinition() {
		assertEquals(swDefFileBuilder.getLspIntegrityDefinition()
				.getIntegrityType(), integrity.getIntegrityType());
		assertEquals(swDefFileBuilder.getLspIntegrityDefinition()
				.getIntegrityValue(), integrity.getIntegrityValue());
	}

	@Test
	public void setLspIntegrityDefinition() {

		IntegrityDefinitionBuilder newDef = mock(IntegrityDefinitionBuilder.class);
		when(newDef.getIntegrityType()).thenReturn(10l);
		when(newDef.getIntegrityValue()).thenReturn("new test");

		swDefFileBuilder.setLspIntegrityDefinition(newDef);

		assertEquals(swDefFileBuilder.getLspIntegrityDefinition()
				.getIntegrityType(), newDef.getIntegrityType());
		assertEquals(swDefFileBuilder.getLspIntegrityDefinition()
				.getIntegrityValue(), newDef.getIntegrityValue());
	}

	@Test
	public void getSdfIntegrityDefinition() {
		assertEquals(swDefFileBuilder.getSdfIntegrityDefinition()
				.getIntegrityType(), integrity.getIntegrityType());
		assertEquals(swDefFileBuilder.getSdfIntegrityDefinition()
				.getIntegrityValue(), integrity.getIntegrityValue());
	}

	@Test
	public void setSdfIntegrityDefinition() {

		IntegrityDefinitionBuilder newDef = mock(IntegrityDefinitionBuilder.class);
		when(newDef.getIntegrityType()).thenReturn(10l);
		when(newDef.getIntegrityValue()).thenReturn("new test");

		swDefFileBuilder.setSdfIntegrityDefinition(newDef);

		assertEquals(swDefFileBuilder.getSdfIntegrityDefinition()
				.getIntegrityType(), newDef.getIntegrityType());
		assertEquals(swDefFileBuilder.getSdfIntegrityDefinition()
				.getIntegrityValue(), newDef.getIntegrityValue());
	}

	@Test
	public void getSoftwareDescription() {
		assertEquals(swDefFileBuilder.getSoftwareDescription()
				.getSoftwarePartNumber(), description.getSoftwarePartnumber());
		assertEquals(swDefFileBuilder.getSoftwareDescription()
				.getSoftwareTypeDescription(),
				description.getSoftwareTypeDescription());
		assertEquals(swDefFileBuilder.getSoftwareDescription()
				.getSoftwareTypeId(), description.getSoftwareTypeId());
	}

	@Test
	public void setSoftwareDescription() {
		SoftwareDescriptionBuilder newDesc = new SoftwareDescriptionBuilder();
		newDesc.setSoftwarePartNumber(DataValidator.generateSoftwarePartNumber("YZT??-ABCD-EFGH"));

		newDesc.setSoftwareTypeDescription("new desc");
		newDesc.setSoftwareTypeId(10l);

		swDefFileBuilder.setSoftwareDescription(newDesc);

		assertEquals(swDefFileBuilder.getSoftwareDescription()
				.getSoftwarePartNumber(), newDesc.getSoftwarePartNumber());
		assertEquals(swDefFileBuilder.getSoftwareDescription()
				.getSoftwareTypeDescription(),
				newDesc.getSoftwareTypeDescription());
		assertEquals(swDefFileBuilder.getSoftwareDescription()
				.getSoftwareTypeId(), newDesc.getSoftwareTypeId());
	}

	@Test
	public void getTargetHardwareDefinitions() {
		
		assertEquals(swDefFileBuilder
				.getTargetHardwareDefinitions().size(), 2);

		assertEquals(swDefFileBuilder
				.getTargetHardwareDefinitions().get(0).getId(), "hardware");
		assertEquals(swDefFileBuilder
				.getTargetHardwareDefinitions().get(1).getId(), "hardware");
	}

	@Test
	public void addTargetHardwareDefinitions() {
		swDefFileBuilder.getTargetHardwareDefinitions().clear();
		
		TargetHardwareDefinitionBuilder expectedHardwareDefinition = new TargetHardwareDefinitionBuilder();

		swDefFileBuilder.getTargetHardwareDefinitions().add(
				expectedHardwareDefinition);
		TargetHardwareDefinitionBuilder actualHardwareDefinition = swDefFileBuilder
				.getTargetHardwareDefinitions().get(0);
		assertEquals(actualHardwareDefinition, expectedHardwareDefinition);
	}
}
