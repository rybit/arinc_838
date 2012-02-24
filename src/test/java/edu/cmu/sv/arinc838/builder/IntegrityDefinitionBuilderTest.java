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

import org.testng.annotations.*;
import com.arinc.arinc838.IntegrityDefinition;

import edu.cmu.sv.arinc838.builder.IntegrityDefinitionBuilder;
import edu.cmu.sv.arinc838.builder.IntegrityDefinitionBuilder.IntegrityType;

public class IntegrityDefinitionBuilderTest {

	private IntegrityDefinition integDef;
	private IntegrityDefinitionBuilder builder;

	@BeforeMethod
	public void setup() {
		integDef = new IntegrityDefinition();
		integDef.setIntegrityType(IntegrityType.CRC16.getType());
		integDef.setIntegrityValue("0xDEADBEEF");

		builder = new IntegrityDefinitionBuilder(integDef);
	}

	@Test
	public void testXmlConstructor() {

		assertEquals(integDef.getIntegrityType(), builder.getIntegrityType());
		assertEquals(integDef.getIntegrityValue(), builder.getIntegrityValue());
	}

	@Test
	public void testSetIntegrityType() {
		IntegrityDefinition def = builder.buildXml();

		assertEquals(def.getIntegrityType(), builder.getIntegrityType());
		
		long newType = IntegrityType.CRC64.getType();
		builder.setIntegrityType(newType);
		
		assertNotEquals(def.getIntegrityType(), builder.getIntegrityType());
		
		def = builder.buildXml();
		
		assertEquals(def.getIntegrityType(), builder.getIntegrityType());
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetIntegrityTypeInvalid()
	{
		builder.setIntegrityType(IntegrityType.CRC64.getType() + 123);
	}
	
	@Test
	public void testSetIntegrityValue() {
		IntegrityDefinition def = builder.buildXml();

		assertEquals(def.getIntegrityValue(), builder.getIntegrityValue());
		
		builder.setIntegrityValue("0xFEED");
		def = builder.buildXml();
		
		assertEquals(def.getIntegrityValue(), builder.getIntegrityValue());
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetIntegrityValueNull()
	{
		builder.setIntegrityValue(null);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetIntegrityValueInvalid()
	{
		builder.setIntegrityValue("0xA<CDZZ&4");
	}
	
	@Test
	public void testIntegrityTypeEnum()
	{
		assertEquals(2, IntegrityDefinitionBuilder.IntegrityType.CRC16.getType());
		assertEquals(3, IntegrityDefinitionBuilder.IntegrityType.CRC32.getType());
		assertEquals(6, IntegrityDefinitionBuilder.IntegrityType.CRC64.getType());		
	}
	
	@Test
	public void testIntegrityTypeEnumFromLong()
	{
		assertEquals(IntegrityDefinitionBuilder.IntegrityType.CRC16, IntegrityDefinitionBuilder.IntegrityType.fromLong(2));
		assertEquals(IntegrityDefinitionBuilder.IntegrityType.CRC32, IntegrityDefinitionBuilder.IntegrityType.fromLong(3));
		assertEquals(IntegrityDefinitionBuilder.IntegrityType.CRC64, IntegrityDefinitionBuilder.IntegrityType.fromLong(6));
		assertNull(IntegrityDefinitionBuilder.IntegrityType.fromLong(-1));
	}
}
