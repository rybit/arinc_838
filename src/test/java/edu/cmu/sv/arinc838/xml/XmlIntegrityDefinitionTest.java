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
import static org.testng.Assert.assertNotEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.arinc.arinc838.IntegrityDefinition;

public class XmlIntegrityDefinitionTest {

	private IntegrityDefinition fileInteg;
	private XmlIntegrityDefinition xmlIntegDef;

	@BeforeMethod
	public void setUp() {
		fileInteg = new IntegrityDefinition();
		fileInteg.setIntegrityType(3);
		fileInteg.setIntegrityValue("bleh");

		xmlIntegDef = new XmlIntegrityDefinition(fileInteg);
	}

	@Test
	public void getIntegrityType() {
		assertEquals(xmlIntegDef.getIntegrityType(),
				fileInteg.getIntegrityType());
	}

	@Test
	public void getIntegrityValue() {
		assertEquals(xmlIntegDef.getIntegrityValue(),
				fileInteg.getIntegrityValue());
	}

	@Test
	public void setIntegrityType() {
		assertEquals(xmlIntegDef.getIntegrityType(),
				fileInteg.getIntegrityType());
		long newType = xmlIntegDef.getIntegrityType() - 1;
		xmlIntegDef.setIntegrityType(newType);

		assertEquals(xmlIntegDef.getIntegrityType(), newType);
		assertNotEquals(xmlIntegDef.getIntegrityType(),
				fileInteg.getIntegrityType());
	}

	@Test
	public void setIntegrityValue() {
		assertEquals(xmlIntegDef.getIntegrityValue(),
				fileInteg.getIntegrityValue());
		String newValue = xmlIntegDef.getIntegrityValue() + "some_new_String";
		xmlIntegDef.setIntegrityValue(newValue);

		assertEquals(xmlIntegDef.getIntegrityValue(), newValue);
		assertNotEquals(xmlIntegDef.getIntegrityValue(),
				fileInteg.getIntegrityValue());
	}
	
	@Test
	public void testEquals()
	{
		assertNotEquals(xmlIntegDef, new Object());
		XmlIntegrityDefinition match = new XmlIntegrityDefinition(fileInteg);
		assertEquals(match, xmlIntegDef);
		
		XmlIntegrityDefinition noMatch1 = new XmlIntegrityDefinition();
		noMatch1.setIntegrityType(fileInteg.getIntegrityType() - 1);
		noMatch1.setIntegrityValue(fileInteg.getIntegrityValue());
		assertNotEquals(noMatch1, xmlIntegDef, "Objects should not be equal with different integrity types");
		
		XmlIntegrityDefinition noMatch2 = new XmlIntegrityDefinition();
		noMatch2.setIntegrityType(fileInteg.getIntegrityType());
		noMatch2.setIntegrityValue(fileInteg.getIntegrityValue() + "asdf");
		assertNotEquals(noMatch2, xmlIntegDef, "Objects should not be equal with different integrity values");
	}
	
	@Test
	public void testHashcode()
	{
		XmlIntegrityDefinition a = new XmlIntegrityDefinition();
		a.setIntegrityType(2);
		a.setIntegrityValue("00012345");
		
		XmlIntegrityDefinition b = new XmlIntegrityDefinition();
		b.setIntegrityType(2);
		b.setIntegrityValue("00012345");
		
		XmlIntegrityDefinition c = new XmlIntegrityDefinition();
		c.setIntegrityType(6);
		c.setIntegrityValue("00000012345");
		
		assertEquals(a.hashCode(), b.hashCode());
		assertNotEquals(b.hashCode(), c.hashCode());
	}
}
