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

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.builder.TargetHardwareDefinitionBuilder;

public class TargetHardwareDefinitionBuilderTest {
	private TargetHardwareDefinitionBuilder first;
	private TargetHardwareDefinitionBuilder second;

	@BeforeMethod
	public void setup() {
		first = new TargetHardwareDefinitionBuilder();
		first.setThwId("first");
		first.getPositions().add("one");
		first.getPositions().add("two");
		
		second = new TargetHardwareDefinitionBuilder();
		second.setThwId("first");
		second.getPositions().add("one");
		second.getPositions().add("two");		
	}

	@Test
	public void getId() {
		com.arinc.arinc838.ThwDefinition jaxbDef = new com.arinc.arinc838.ThwDefinition();
		jaxbDef.setThwId("test");
		TargetHardwareDefinitionBuilder xmlDef = new TargetHardwareDefinitionBuilder(
				jaxbDef);

		assertEquals(xmlDef.getThwId(), jaxbDef.getThwId());
	}

	@Test
	public void setId() {
		com.arinc.arinc838.ThwDefinition jaxbDef = new com.arinc.arinc838.ThwDefinition();
		jaxbDef.setThwId("test");
		TargetHardwareDefinitionBuilder xmlDef = new TargetHardwareDefinitionBuilder(
				jaxbDef);

		String value = "new test";

		xmlDef.setThwId(value);

		assertEquals(xmlDef.getThwId(), value);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetIdInvalid()
	{
		// invalid str64k
		first.setThwId("1 > 2");
	}

	@Test
	public void getPositions() {
		com.arinc.arinc838.ThwDefinition jaxbDef = new ThwDefinition();
		jaxbDef.setThwId("id");
		jaxbDef.getThwPosition().add("one");
		jaxbDef.getThwPosition().add("two");
		TargetHardwareDefinitionBuilder xmlDef = new TargetHardwareDefinitionBuilder(
				jaxbDef);

		assertEquals(xmlDef.getPositions().size(), 2);
		assertEquals(xmlDef.getPositions().get(0), "one");
		assertEquals(xmlDef.getPositions().get(1), "two");
	}

	@Test
	public void getPositionsWithNoPositions() {
		com.arinc.arinc838.ThwDefinition jaxbDef = new ThwDefinition();
		jaxbDef.setThwId("id");
		TargetHardwareDefinitionBuilder xmlDef = new TargetHardwareDefinitionBuilder(
				jaxbDef);

		assertEquals(xmlDef.getPositions().size(), 0);
	}


	@Test
	public void equalsFailsIfPositionsAreNotSame() {
		second.getPositions().clear();
		second.getPositions().add("two");
		second.getPositions().add("one");

		assertNotEquals(first, second);
	}

	@Test
	public void testBuildReturnsProperJaxbObject() {
		ThwDefinition def = first.buildXml();
		
		assertEquals(def.getThwId(), first.getThwId());
		
		for(int i = 0; i < def.getThwPosition().size(); i++){
			assertEquals(def.getThwPosition().get(i), first.getPositions().get(i));
		}
	}
	
	@Test
	public void testHashCode(){
		assertEquals(first.hashCode(), first.getThwId().hashCode());
	}
	
	@Test 
	public void testHashCodeWithNoId(){
		assertEquals(new TargetHardwareDefinitionBuilder().hashCode(), 0);
	}
	
	@Test
	public void testEquals(){
		assertEquals(first, second);	
	}
}
