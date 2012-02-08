/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 7, 2012
 */
package edu.cmu.sv.arinc838.xml;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.arinc.arinc838.SdfSections;

import edu.cmu.sv.arinc838.specification.FileDefinition;

public class XmlSoftwareDefinitionSectionsTest {

	@Test
	public void fileDefinitions() {
		SdfSections sdfSections = new SdfSections();
		XmlSoftwareDefinitionSections xmlSoftwareDefinitionSections = new XmlSoftwareDefinitionSections(
				sdfSections);
		XmlFileDefinition expectedFileDefinition = new XmlFileDefinition(new com.arinc.arinc838.FileDefinition());
		xmlSoftwareDefinitionSections.getFileDefinitions().add(expectedFileDefinition);
		FileDefinition actualFileDefinition = xmlSoftwareDefinitionSections.getFileDefinitions().get(0);
		assertEquals(actualFileDefinition, expectedFileDefinition);
	}

	@Test
	public void getLspIntegrityDefinition() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void getSdfIntegrityDefinition() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void getSoftwareDescription() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void getThwDefinitions() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void setFileDefinitions() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void setLspIntegrityDefinition() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void setSdfIntegrityDefinition() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void setSoftwareDescription() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void setTargetHardwareDefinitions() {
		throw new RuntimeException("Test not implemented");
	}
}
