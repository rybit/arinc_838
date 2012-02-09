package edu.cmu.sv.arinc838.xml;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.arinc.arinc838.ThwDefinition;

public class XmlTargetHardwareDefinitionTest {
	@Test
	public void getId() {
		com.arinc.arinc838.ThwDefinition jaxbDef = new com.arinc.arinc838.ThwDefinition();
		jaxbDef.setThwId("test");
		XmlTargetHardwareDefinition xmlDef = new XmlTargetHardwareDefinition(
				jaxbDef);

		assertEquals(xmlDef.getId(), jaxbDef.getThwId());
	}

	@Test
	public void setId() {
		com.arinc.arinc838.ThwDefinition jaxbDef = new com.arinc.arinc838.ThwDefinition();
		jaxbDef.setThwId("test");
		XmlTargetHardwareDefinition xmlDef = new XmlTargetHardwareDefinition(
				jaxbDef);

		String value = "new test";

		xmlDef.setId(value);

		assertEquals(xmlDef.getId(), value);
	}

	@Test
	public void getPositions() {
		com.arinc.arinc838.ThwDefinition jaxbDef = new ThwDefinition();
		jaxbDef.getThwPosition().add("one");
		jaxbDef.getThwPosition().add("two");
		XmlTargetHardwareDefinition xmlDef = new XmlTargetHardwareDefinition(
				jaxbDef);
		
		assertEquals(xmlDef.getPositions().size(), 2);
		assertEquals(xmlDef.getPositions().get(0), "one");
		assertEquals(xmlDef.getPositions().get(1), "two");		
	}

	@Test
	public void getPositionsWithNoPositions() {
		com.arinc.arinc838.ThwDefinition jaxbDef = new ThwDefinition();
		XmlTargetHardwareDefinition xmlDef = new XmlTargetHardwareDefinition(
				jaxbDef);
		
		assertEquals(xmlDef.getPositions().size(), 0);		
	}	
}
