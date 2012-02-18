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
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.arinc.arinc838.FileDefinition;
import com.arinc.arinc838.IntegrityDefinition;
import com.arinc.arinc838.SdfFile;
import com.arinc.arinc838.SdfSections;

import edu.cmu.sv.arinc838.builder.IntegrityDefinitionBuilder.IntegrityType;
import edu.cmu.sv.arinc838.validation.DataValidator;
import edu.cmu.sv.arinc838.validation.ReferenceData;


import edu.cmu.sv.arinc838.xml.XdfWriter;

public class SoftwareDefinitionFileBuilderTest {
	private SdfFile swDefFile;
	private SdfSections swDefSects;
	private SoftwareDefinitionFileBuilder swDefFileBuilder;

	@BeforeMethod
	public void beforeMethod() {
		swDefFile = new SdfFile();
		swDefSects = mock(SdfSections.class);
		com.arinc.arinc838.SoftwareDescription desc = mock(com.arinc.arinc838.SoftwareDescription.class);
		when(desc.getSoftwarePartnumber()).thenReturn(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		when(desc.getSoftwareTypeDescription()).thenReturn("desc");
		when(swDefSects.getSoftwareDescription()).thenReturn(desc);
		IntegrityDefinition integDef = new IntegrityDefinition();
		integDef.setIntegrityType(IntegrityType.CRC16.getType());
		integDef.setIntegrityValue("0xABCD");
		when(swDefSects.getLspIntegrityDefinition()).thenReturn(integDef);
		when(swDefSects.getSdfIntegrityDefinition()).thenReturn(integDef);
		List<FileDefinition> fileDefs = new ArrayList<FileDefinition>();
		FileDefinition fileDef = new FileDefinition();
		fileDef.setFileIntegrityDefinition(integDef);
		fileDef.setFileName("a name");
		fileDef.setFileSize(1234);
		fileDefs.add(fileDef);
		when(swDefSects.getFileDefinitions()).thenReturn(fileDefs);
		

		swDefFile.setFileFormatVersion(SoftwareDefinitionFileBuilder.DEFAULT_FILE_FORMAT_VERSION);
		swDefFile.setSdfSections(swDefSects);
		swDefFileBuilder = new SoftwareDefinitionFileBuilder(swDefFile);
	}

	@Test
	public void getFileFormatVersion() {
		assertEquals(swDefFileBuilder.getFileFormatVersion(),
				swDefFile.getFileFormatVersion());
	}

	@Test
	public void getSoftwareDefinitionSections() {
		assertEquals(swDefFileBuilder.getSoftwareDefinitionSections()
				.getSoftwareDescription().getSoftwarePartNumber(), swDefFile
				.getSdfSections().getSoftwareDescription()
				.getSoftwarePartnumber());
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
	public void setSoftwareDefinitionSections() {

		assertEquals(swDefFileBuilder.getSoftwareDefinitionSections()
				.getSoftwareDescription().getSoftwarePartNumber(), swDefFile
				.getSdfSections().getSoftwareDescription()
				.getSoftwarePartnumber(),
				"Exepected sofware part numbers to be equal");

		assertEquals(swDefFileBuilder.getSoftwareDefinitionSections()
				.getSoftwareDescription().getSoftwareTypeId(), swDefFile
				.getSdfSections().getSoftwareDescription().getSoftwareTypeId(),
				"Expecteed software type IDs to be equal");

		SoftwareDefinitionSectionsBuilder tmp = new SoftwareDefinitionSectionsBuilder();
		SoftwareDescriptionBuilder swDestmp = new SoftwareDescriptionBuilder();
		swDestmp.setSoftwarePartNumber(DataValidator.generateSoftwarePartNumber("YZT??-ABCD-EFGH"));
		tmp.setSoftwareDescription(swDestmp);

		swDefFileBuilder.setSoftwareDefinitionSections(tmp);

		assertEquals(swDefFileBuilder.getSoftwareDefinitionSections(), tmp,
				"Expected the set to value to equal the returned value");

		assertNotEquals(swDefFileBuilder.getSoftwareDefinitionSections()
				.getSoftwareDescription().getSoftwarePartNumber(), swDefFile
				.getSdfSections().getSoftwareDescription()
				.getSoftwarePartnumber(),
				"Exepected sofware part numbers to not be equal");
	}

	@Test
	public void testBuildAddsFileFormatVersion() {
		SdfFile file = swDefFileBuilder.build();

		assertEquals(swDefFile.getFileFormatVersion(),
				file.getFileFormatVersion());
	}

	@Test
	public void testBuildAddsSection() {
		SdfFile file = swDefFileBuilder.build();

		assertEquals(file.getSdfSections().getSoftwareDescription()
				.getSoftwarePartnumber(), swDefSects.getSoftwareDescription()
				.getSoftwarePartnumber());
	}
}
