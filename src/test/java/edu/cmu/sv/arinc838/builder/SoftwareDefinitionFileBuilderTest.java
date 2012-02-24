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

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.arinc.arinc838.FileDefinition;
import com.arinc.arinc838.IntegrityDefinition;
import com.arinc.arinc838.SdfFile;
import com.arinc.arinc838.SdfSections;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.builder.IntegrityDefinitionBuilder.IntegrityType;
import edu.cmu.sv.arinc838.validation.DataValidator;
import edu.cmu.sv.arinc838.validation.ReferenceData;

public class SoftwareDefinitionFileBuilderTest {
	private SdfFile swDefFile;
	private SdfSections swDefSects;
	private SoftwareDefinitionFileBuilder swDefFileBuilder;

	@BeforeMethod
	public void beforeMethod() {
		swDefFile = new SdfFile();
		swDefSects = mock(SdfSections.class);
		com.arinc.arinc838.SoftwareDescription desc = mock(com.arinc.arinc838.SoftwareDescription.class);
		when(desc.getSoftwarePartnumber()).thenReturn(
				ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
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

		swDefFile
				.setFileFormatVersion(SoftwareDefinitionFileBuilder.DEFAULT_FILE_FORMAT_VERSION);
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
		swDestmp.setSoftwarePartNumber(DataValidator
				.generateSoftwarePartNumber("YZT??-ABCD-EFGH"));
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
		SdfFile file = swDefFileBuilder.buildXml();

		assertEquals(swDefFile.getFileFormatVersion(),
				file.getFileFormatVersion());
	}

	@Test
	public void testBuildAddsSection() {
		SdfFile file = swDefFileBuilder.buildXml();

		assertEquals(file.getSdfSections().getSoftwareDescription()
				.getSoftwarePartnumber(), swDefSects.getSoftwareDescription()
				.getSoftwarePartnumber());
	}

	@Test
	public void testDefaultConstructor() {
		SoftwareDefinitionFileBuilder builder = new SoftwareDefinitionFileBuilder();

		assertEquals(builder.getFileFormatVersion(), 0);
		assertNull(builder.getSoftwareDefinitionSections());
	}

	@Test
	public void testBuildBinaryCallsSections() throws IOException {
		SoftwareDefinitionSectionsBuilder sections = mock(SoftwareDefinitionSectionsBuilder.class);

		SoftwareDefinitionFileBuilder fileBuilder = new SoftwareDefinitionFileBuilder();
		fileBuilder.setSoftwareDefinitionSections(sections);

		edu.cmu.sv.arinc838.binary.BdfFile file = mock(BdfFile.class);

		fileBuilder.buildBinary(file);

		verify(sections).buildBinary(file);
	}

	@Test
	public void testBuildBinaryAddsFileFormatVersion() throws Exception {
		long expectedFileFormatVersion = SoftwareDefinitionFileBuilder.DEFAULT_FILE_FORMAT_VERSION;

		SoftwareDefinitionSectionsBuilder sections = mock(SoftwareDefinitionSectionsBuilder.class);

		SoftwareDefinitionFileBuilder fileBuilder = new SoftwareDefinitionFileBuilder();
		fileBuilder.setSoftwareDefinitionSections(sections);
		fileBuilder.setFileFormatVersion(expectedFileFormatVersion);

		edu.cmu.sv.arinc838.binary.BdfFile file = new BdfFile(
				File.createTempFile("tmp", ".tmp"));

		fileBuilder.buildBinary(file);

		file.seek(SoftwareDefinitionFileBuilder.BINARY_FILE_FORMAT_VERSION_LOCATION);
		assertEquals(file.readUint32(), expectedFileFormatVersion);
	}
	
	
	
//	@Test
//	public void testFileLengthIsSet() throws Exception{
//		SoftwareDefinitionSectionsBuilder sections = mock(SoftwareDefinitionSectionsBuilder.class);
//		
//		SoftwareDefinitionFileBuilder fileBuilder = new SoftwareDefinitionFileBuilder();
//		fileBuilder.setSoftwareDefinitionSections(sections);
//	}
}
