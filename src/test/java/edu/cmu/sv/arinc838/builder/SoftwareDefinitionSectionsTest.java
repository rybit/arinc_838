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

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.arinc.arinc838.FileDefinition;
import com.arinc.arinc838.IntegrityDefinition;
import com.arinc.arinc838.SdfSections;
import com.arinc.arinc838.SoftwareDescription;
import com.arinc.arinc838.ThwDefinition;

import static org.mockito.Mockito.*;

import edu.cmu.sv.arinc838.builder.SoftwareDefinitionSectionsBuilder;


public class SoftwareDefinitionSectionsTest {

	@Test
	public void getFileDefinitions() {
		SdfSections jaxbSdfSections = new SdfSections();
		jaxbSdfSections.setSoftwareDescription(mock(SoftwareDescription.class));
		com.arinc.arinc838.FileDefinition fileDef = new com.arinc.arinc838.FileDefinition();
		fileDef.setFileName("file");

		jaxbSdfSections.getFileDefinitions().add(fileDef);
		jaxbSdfSections.getFileDefinitions().add(fileDef);

		SoftwareDefinitionSectionsBuilder xmlSoftwareDefinitionSections = new SoftwareDefinitionSectionsBuilder(
				jaxbSdfSections);

		assertEquals(xmlSoftwareDefinitionSections.getFileDefinitions().size(),
				2);

		assertEquals(xmlSoftwareDefinitionSections.getFileDefinitions().get(0)
				.getFileName(), "file");
		assertEquals(xmlSoftwareDefinitionSections.getFileDefinitions().get(1)
				.getFileName(), "file");
	}

	@Test
	public void addFileDefinition() {
		SdfSections jaxbSdfSections = new SdfSections();
		jaxbSdfSections
				.setSoftwareDescription(mock(com.arinc.arinc838.SoftwareDescription.class));
		SoftwareDefinitionSectionsBuilder xmlSoftwareDefinitionSections = new SoftwareDefinitionSectionsBuilder(
				jaxbSdfSections);
		FileDefinition expectedFileDefinition = new FileDefinition();
		xmlSoftwareDefinitionSections.getFileDefinitions().add(
				expectedFileDefinition);
		FileDefinition actualFileDefinition = xmlSoftwareDefinitionSections
				.getFileDefinitions().get(0);
		assertEquals(actualFileDefinition, expectedFileDefinition);
	}

	@Test
	public void getLspIntegrityDefinition() {
		com.arinc.arinc838.IntegrityDefinition integrity = new com.arinc.arinc838.IntegrityDefinition();
		integrity.setIntegrityType(9);
		integrity.setIntegrityValue("test");

		SdfSections jaxbSections = new SdfSections();
		jaxbSections.setLspIntegrityDefinition(integrity);
		jaxbSections
				.setSoftwareDescription(mock(com.arinc.arinc838.SoftwareDescription.class));

		SoftwareDefinitionSectionsBuilder xmlSections = new SoftwareDefinitionSectionsBuilder(
				jaxbSections);

		assertEquals(
				xmlSections.getLspIntegrityDefinition().getIntegrityType(),
				integrity.getIntegrityType());
		assertEquals(xmlSections.getLspIntegrityDefinition()
				.getIntegrityValue(), integrity.getIntegrityValue());
	}

	@Test
	public void setLspIntegrityDefinition() {
		com.arinc.arinc838.IntegrityDefinition integrity = new com.arinc.arinc838.IntegrityDefinition();
		integrity.setIntegrityType(9);
		integrity.setIntegrityValue("test");

		SdfSections jaxbSections = new SdfSections();
		jaxbSections.setLspIntegrityDefinition(integrity);
		jaxbSections
				.setSoftwareDescription(mock(com.arinc.arinc838.SoftwareDescription.class));

		SoftwareDefinitionSectionsBuilder xmlSections = new SoftwareDefinitionSectionsBuilder(
				jaxbSections);

		IntegrityDefinition newDef = mock(IntegrityDefinition.class);
		when(newDef.getIntegrityType()).thenReturn(10l);
		when(newDef.getIntegrityValue()).thenReturn("new test");

		xmlSections.setLspIntegrityDefinition(newDef);

		assertEquals(
				xmlSections.getLspIntegrityDefinition().getIntegrityType(),
				newDef.getIntegrityType());
		assertEquals(xmlSections.getLspIntegrityDefinition()
				.getIntegrityValue(), newDef.getIntegrityValue());
	}

	@Test
	public void getSdfIntegrityDefinition() {
		com.arinc.arinc838.IntegrityDefinition integrity = new com.arinc.arinc838.IntegrityDefinition();
		integrity.setIntegrityType(9);
		integrity.setIntegrityValue("test");

		SdfSections jaxbSections = new SdfSections();
		jaxbSections.setSdfIntegrityDefinition(integrity);
		jaxbSections
				.setLspIntegrityDefinition(mock(com.arinc.arinc838.IntegrityDefinition.class));
		jaxbSections
				.setSoftwareDescription(mock(com.arinc.arinc838.SoftwareDescription.class));

		SoftwareDefinitionSectionsBuilder xmlSections = new SoftwareDefinitionSectionsBuilder(
				jaxbSections);

		assertEquals(
				xmlSections.getSdfIntegrityDefinition().getIntegrityType(),
				integrity.getIntegrityType());
		assertEquals(xmlSections.getSdfIntegrityDefinition()
				.getIntegrityValue(), integrity.getIntegrityValue());
	}

	@Test
	public void setSdfIntegrityDefinition() {
		com.arinc.arinc838.IntegrityDefinition integrity = new com.arinc.arinc838.IntegrityDefinition();
		integrity.setIntegrityType(9);
		integrity.setIntegrityValue("test");

		SdfSections jaxbSections = new SdfSections();
		jaxbSections.setSdfIntegrityDefinition(integrity);
		jaxbSections
				.setSoftwareDescription(mock(com.arinc.arinc838.SoftwareDescription.class));

		SoftwareDefinitionSectionsBuilder xmlSections = new SoftwareDefinitionSectionsBuilder(
				jaxbSections);

		IntegrityDefinition newDef = mock(IntegrityDefinition.class);
		when(newDef.getIntegrityType()).thenReturn(10l);
		when(newDef.getIntegrityValue()).thenReturn("new test");

		xmlSections.setSdfIntegrityDefinition(newDef);

		assertEquals(
				xmlSections.getSdfIntegrityDefinition().getIntegrityType(),
				newDef.getIntegrityType());
		assertEquals(xmlSections.getSdfIntegrityDefinition()
				.getIntegrityValue(), newDef.getIntegrityValue());
	}

	@Test
	public void getSoftwareDescription() {
		SoftwareDescription desc = mock(SoftwareDescription.class);
		when(desc.getSoftwarePartnumber()).thenReturn("part");
		when(desc.getSoftwareTypeDescription()).thenReturn("desc");
		when(desc.getSoftwareTypeId()).thenReturn(7l);

		SdfSections jaxbSections = new SdfSections();
		jaxbSections.setSoftwareDescription(desc);

		SoftwareDefinitionSectionsBuilder xmlSections = new SoftwareDefinitionSectionsBuilder(
				jaxbSections);

		assertEquals(xmlSections.getSoftwareDescription()
				.getSoftwarePartnumber(), desc.getSoftwarePartnumber());
		assertEquals(xmlSections.getSoftwareDescription()
				.getSoftwareTypeDescription(),
				desc.getSoftwareTypeDescription());
		assertEquals(xmlSections.getSoftwareDescription().getSoftwareTypeId(),
				desc.getSoftwareTypeId());
	}

	@Test
	public void setSoftwareDescription() {
		SoftwareDescription desc = mock(SoftwareDescription.class);
		when(desc.getSoftwarePartnumber()).thenReturn("part");
		when(desc.getSoftwareTypeDescription()).thenReturn("desc");
		when(desc.getSoftwareTypeId()).thenReturn(7l);

		SdfSections jaxbSections = new SdfSections();
		jaxbSections.setSoftwareDescription(desc);

		SoftwareDefinitionSectionsBuilder xmlSections = new SoftwareDefinitionSectionsBuilder(
				jaxbSections);

		SoftwareDescription newDesc = new SoftwareDescriptionBuilder(desc).build();
		newDesc.setSoftwarePartnumber("new part");
		newDesc.setSoftwareTypeDescription("new desc");
		newDesc.setSoftwareTypeId(10l);

		xmlSections.setSoftwareDescription(newDesc);

		assertEquals(xmlSections.getSoftwareDescription()
				.getSoftwarePartnumber(), newDesc.getSoftwarePartnumber());
		assertEquals(xmlSections.getSoftwareDescription()
				.getSoftwareTypeDescription(),
				newDesc.getSoftwareTypeDescription());
		assertEquals(xmlSections.getSoftwareDescription().getSoftwareTypeId(),
				newDesc.getSoftwareTypeId());
	}

	@Test
	public void getTargetHardwareDefinitions() {
		SdfSections jaxbSdfSections = new SdfSections();
		jaxbSdfSections.setSoftwareDescription(mock(SoftwareDescription.class));
		ThwDefinition hardwareDef = new ThwDefinition();
		hardwareDef.setThwId("hardware");

		jaxbSdfSections.getThwDefinitions().add(hardwareDef);
		jaxbSdfSections.getThwDefinitions().add(hardwareDef);

		SoftwareDefinitionSectionsBuilder xmlSoftwareDefinitionSections = new SoftwareDefinitionSectionsBuilder(
				jaxbSdfSections);

		assertEquals(xmlSoftwareDefinitionSections
				.getTargetHardwareDefinitions().size(), 2);

		assertEquals(xmlSoftwareDefinitionSections
				.getTargetHardwareDefinitions().get(0).getThwId(), "hardware");
		assertEquals(xmlSoftwareDefinitionSections
				.getTargetHardwareDefinitions().get(1).getThwId(), "hardware");
	}

	@Test
	public void addTargetHardwareDefinitions() {
		SdfSections jaxbSdfSections = new SdfSections();
		jaxbSdfSections.setSoftwareDescription(mock(SoftwareDescription.class));
		SoftwareDefinitionSectionsBuilder xmlSoftwareDefinitionSections = new SoftwareDefinitionSectionsBuilder(
				jaxbSdfSections);

		ThwDefinition expectedHardwareDefinition = new TargetHardwareDefinitionBuilder(
				new ThwDefinition()).build();
		xmlSoftwareDefinitionSections.getTargetHardwareDefinitions().add(
				expectedHardwareDefinition);
		ThwDefinition actualHardwareDefinition = xmlSoftwareDefinitionSections
				.getTargetHardwareDefinitions().get(0);
		assertEquals(actualHardwareDefinition, expectedHardwareDefinition);
	}

	@Test
	public void equalsReturnsTrueForSameObjects() {
		SoftwareDefinitionSectionsBuilder first = new SoftwareDefinitionSectionsBuilder();

		assertEquals(first, first);
	}

	@Test
	public void equalsComparesAllChildren() {
		SoftwareDefinitionSectionsBuilder first = new SoftwareDefinitionSectionsBuilder();
		SoftwareDefinitionSectionsBuilder second = new SoftwareDefinitionSectionsBuilder();

		SoftwareDescription desc = mock(SoftwareDescription.class);
		first.setSoftwareDescription(desc);
		second.setSoftwareDescription(desc);

		IntegrityDefinition integrity = mock(IntegrityDefinition.class);

		first.setLspIntegrityDefinition(integrity);
		first.setSdfIntegrityDefinition(integrity);
		second.setLspIntegrityDefinition(integrity);
		second.setSdfIntegrityDefinition(integrity);

		FileDefinition fileDef = mock(FileDefinition.class);
		first.getFileDefinitions().add(fileDef);
		second.getFileDefinitions().add(fileDef);

		ThwDefinition hardwareDef = mock(ThwDefinition.class);
		first.getTargetHardwareDefinitions().add(hardwareDef);
		second.getTargetHardwareDefinitions().add(hardwareDef);

		assertEquals(first, second);
	}

	@Test
	public void hashcodeIsCombinationOfChildrensHashCodes() {
		SoftwareDefinitionSectionsBuilder first = new SoftwareDefinitionSectionsBuilder();
		SoftwareDescription desc = mock(SoftwareDescription.class);
		first.setSoftwareDescription(desc);

		IntegrityDefinition integrity = mock(IntegrityDefinition.class);

		first.setLspIntegrityDefinition(integrity);
		first.setSdfIntegrityDefinition(integrity);

		FileDefinition fileDef = mock(FileDefinition.class);
		first.getFileDefinitions().add(fileDef);

		ThwDefinition hardwareDef = mock(ThwDefinition.class);
		first.getTargetHardwareDefinitions().add(hardwareDef);

		assertEquals(first.hashCode(), first.getSoftwareDescription()
				.hashCode()
				^ first.getLspIntegrityDefinition().hashCode()
				^ first.getSdfIntegrityDefinition().hashCode()
				^ first.getFileDefinitions().hashCode()
				^ first.getTargetHardwareDefinitions().hashCode());
	}
}
