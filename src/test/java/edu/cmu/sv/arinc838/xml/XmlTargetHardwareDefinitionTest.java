package edu.cmu.sv.arinc838.xml;

import static org.testng.Assert.*;

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
	
	@Test
	public void equalsUsesIdAndPositions(){
		XmlTargetHardwareDefinition first = new XmlTargetHardwareDefinition();
		first.setId("first");
		first.getPositions().add("one");
		first.getPositions().add("two");
		
		XmlTargetHardwareDefinition second = new XmlTargetHardwareDefinition();
		second.setId("first");
		second.getPositions().add("one");
		second.getPositions().add("two");
		
		assertEquals(first, second);
	}
	
	@Test
	public void equalsFailsIfIdIsDifferent(){
		XmlTargetHardwareDefinition first = new XmlTargetHardwareDefinition();
		first.setId("first");
		first.getPositions().add("one");
		first.getPositions().add("two");
		
		XmlTargetHardwareDefinition second = new XmlTargetHardwareDefinition();
		second.setId("second");
		second.getPositions().add("one");
		second.getPositions().add("two");
		
		assertNotEquals(first, second);
	}
	
	@Test
	public void equalsFailsIfPositionsAreNotSame(){
		XmlTargetHardwareDefinition first = new XmlTargetHardwareDefinition();
		first.setId("first");
		first.getPositions().add("one");
		first.getPositions().add("two");
		
		XmlTargetHardwareDefinition second = new XmlTargetHardwareDefinition();
		second.setId("first");
		second.getPositions().add("two");
		second.getPositions().add("one");
		
		assertNotEquals(first, second);
	}
		
	@Test
	public void equalsFailsForDifferentTypes(){
		XmlTargetHardwareDefinition first = new XmlTargetHardwareDefinition();
		first.setId("first");
		first.getPositions().add("one");
		first.getPositions().add("two");
		
		assertNotEquals(first, new Object());
	}
	
	@Test
	public void equalsWorksForSameObject(){
		XmlTargetHardwareDefinition first = new XmlTargetHardwareDefinition();
		first.setId("first");
		first.getPositions().add("one");
		first.getPositions().add("two");
		
		assertEquals(first, first);
	}
	
	@Test
	public void hashcodeIsCombinationOfIdAndPositions(){
		XmlTargetHardwareDefinition first = new XmlTargetHardwareDefinition();
		first.setId("first");
		first.getPositions().add("one");
		first.getPositions().add("two");
		
		assertEquals(first.hashCode(), first.getId().hashCode() ^ first.getPositions().hashCode());
	}
}
