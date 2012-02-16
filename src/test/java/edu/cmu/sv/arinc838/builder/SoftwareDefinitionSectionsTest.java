/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 7, 2012
 */
package edu.cmu.sv.arinc838.builder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.arinc.arinc838.SdfSections;
import com.arinc.arinc838.SoftwareDescription;
import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.builder.IntegrityDefinitionBuilder.IntegrityType;

public class SoftwareDefinitionSectionsTest {

	private com.arinc.arinc838.IntegrityDefinition integrity;
	private SdfSections jaxbSections;
	private SoftwareDescription description;
	private com.arinc.arinc838.FileDefinition fileDef;
	private com.arinc.arinc838.ThwDefinition hardwareDef;
	private SoftwareDefinitionSectionsBuilder xmlSoftwareDefinitionSections;

	@BeforeMethod
	private void setup() {
		integrity = new com.arinc.arinc838.IntegrityDefinition();
		integrity.setIntegrityType(IntegrityType.CRC16.getType());
		integrity.setIntegrityValue("0xABCD");

		description = new SoftwareDescription();
		description.setSoftwarePartnumber("part");
		description.setSoftwareTypeDescription("desc");
		description.setSoftwareTypeId(10l);

		fileDef = new com.arinc.arinc838.FileDefinition();
		fileDef.setFileName("file");
		fileDef.setFileIntegrityDefinition(integrity);
		
		hardwareDef = new ThwDefinition();
		hardwareDef.setThwId("hardware");

		jaxbSections = new SdfSections();
		jaxbSections.setSdfIntegrityDefinition(integrity);
		jaxbSections.setLspIntegrityDefinition(integrity);
		jaxbSections.setSoftwareDescription(description);

		jaxbSections.getFileDefinitions().add(fileDef);
		jaxbSections.getFileDefinitions().add(fileDef);
		
		jaxbSections.getThwDefinitions().add(hardwareDef);
		jaxbSections.getThwDefinitions().add(hardwareDef);

		xmlSoftwareDefinitionSections = new SoftwareDefinitionSectionsBuilder(
				jaxbSections);
	}

	@Test
	public void getFileDefinitions() {
		assertEquals(xmlSoftwareDefinitionSections.getFileDefinitions().size(),
				2);

		assertEquals(xmlSoftwareDefinitionSections.getFileDefinitions().get(0)
				.getFileName(), "file");
		assertEquals(xmlSoftwareDefinitionSections.getFileDefinitions().get(1)
				.getFileName(), "file");
	}

	@Test
	public void addFileDefinition() {
		xmlSoftwareDefinitionSections.getFileDefinitions().clear();

		FileDefinitionBuilder expected = mock(FileDefinitionBuilder.class);

		xmlSoftwareDefinitionSections.getFileDefinitions().add(expected);
		FileDefinitionBuilder actualFileDefinition = xmlSoftwareDefinitionSections
				.getFileDefinitions().get(0);
		assertEquals(actualFileDefinition, expected);
	}

	@Test
	public void getLspIntegrityDefinition() {
		assertEquals(xmlSoftwareDefinitionSections.getLspIntegrityDefinition()
				.getIntegrityType(), integrity.getIntegrityType());
		assertEquals(xmlSoftwareDefinitionSections.getLspIntegrityDefinition()
				.getIntegrityValue(), integrity.getIntegrityValue());
	}

	@Test
	public void setLspIntegrityDefinition() {

		IntegrityDefinitionBuilder newDef = mock(IntegrityDefinitionBuilder.class);
		when(newDef.getIntegrityType()).thenReturn(10l);
		when(newDef.getIntegrityValue()).thenReturn("new test");

		xmlSoftwareDefinitionSections.setLspIntegrityDefinition(newDef);

		assertEquals(xmlSoftwareDefinitionSections.getLspIntegrityDefinition()
				.getIntegrityType(), newDef.getIntegrityType());
		assertEquals(xmlSoftwareDefinitionSections.getLspIntegrityDefinition()
				.getIntegrityValue(), newDef.getIntegrityValue());
	}

	@Test
	public void getSdfIntegrityDefinition() {
		assertEquals(xmlSoftwareDefinitionSections.getSdfIntegrityDefinition()
				.getIntegrityType(), integrity.getIntegrityType());
		assertEquals(xmlSoftwareDefinitionSections.getSdfIntegrityDefinition()
				.getIntegrityValue(), integrity.getIntegrityValue());
	}

	@Test
	public void setSdfIntegrityDefinition() {

		IntegrityDefinitionBuilder newDef = mock(IntegrityDefinitionBuilder.class);
		when(newDef.getIntegrityType()).thenReturn(10l);
		when(newDef.getIntegrityValue()).thenReturn("new test");

		xmlSoftwareDefinitionSections.setSdfIntegrityDefinition(newDef);

		assertEquals(xmlSoftwareDefinitionSections.getSdfIntegrityDefinition()
				.getIntegrityType(), newDef.getIntegrityType());
		assertEquals(xmlSoftwareDefinitionSections.getSdfIntegrityDefinition()
				.getIntegrityValue(), newDef.getIntegrityValue());
	}

	@Test
	public void getSoftwareDescription() {
		assertEquals(xmlSoftwareDefinitionSections.getSoftwareDescription()
				.getSoftwarePartNumber(), description.getSoftwarePartnumber());
		assertEquals(xmlSoftwareDefinitionSections.getSoftwareDescription()
				.getSoftwareTypeDescription(),
				description.getSoftwareTypeDescription());
		assertEquals(xmlSoftwareDefinitionSections.getSoftwareDescription()
				.getSoftwareTypeId(), description.getSoftwareTypeId());
	}

	@Test
	public void setSoftwareDescription() {
		SoftwareDescriptionBuilder newDesc = new SoftwareDescriptionBuilder();
		newDesc.setSoftwarePartNumber("new part");

		newDesc.setSoftwareTypeDescription("new desc");
		newDesc.setSoftwareTypeId(10l);

		xmlSoftwareDefinitionSections.setSoftwareDescription(newDesc);

		assertEquals(xmlSoftwareDefinitionSections.getSoftwareDescription()
				.getSoftwarePartNumber(), newDesc.getSoftwarePartNumber());
		assertEquals(xmlSoftwareDefinitionSections.getSoftwareDescription()
				.getSoftwareTypeDescription(),
				newDesc.getSoftwareTypeDescription());
		assertEquals(xmlSoftwareDefinitionSections.getSoftwareDescription()
				.getSoftwareTypeId(), newDesc.getSoftwareTypeId());
	}

	@Test
	public void getTargetHardwareDefinitions() {
		
		assertEquals(xmlSoftwareDefinitionSections
				.getTargetHardwareDefinitions().size(), 2);

		assertEquals(xmlSoftwareDefinitionSections
				.getTargetHardwareDefinitions().get(0).getId(), "hardware");
		assertEquals(xmlSoftwareDefinitionSections
				.getTargetHardwareDefinitions().get(1).getId(), "hardware");
	}

	@Test
	public void addTargetHardwareDefinitions() {
		xmlSoftwareDefinitionSections.getTargetHardwareDefinitions().clear();
		
		TargetHardwareDefinitionBuilder expectedHardwareDefinition = new TargetHardwareDefinitionBuilder();

		xmlSoftwareDefinitionSections.getTargetHardwareDefinitions().add(
				expectedHardwareDefinition);
		TargetHardwareDefinitionBuilder actualHardwareDefinition = xmlSoftwareDefinitionSections
				.getTargetHardwareDefinitions().get(0);
		assertEquals(actualHardwareDefinition, expectedHardwareDefinition);
	}
}
