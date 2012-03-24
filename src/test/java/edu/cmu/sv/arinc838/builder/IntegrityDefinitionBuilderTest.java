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

import edu.cmu.sv.arinc838.builder.IntegrityDefinitionDao;
import edu.cmu.sv.arinc838.builder.IntegrityDefinitionDao.IntegrityType;
import edu.cmu.sv.arinc838.util.Converter;

public class IntegrityDefinitionBuilderTest {

	private IntegrityDefinition integDef;
	private IntegrityDefinitionDao builder;

	@BeforeMethod
	public void setup() {
		integDef = new IntegrityDefinition();
		integDef.setIntegrityType(IntegrityType.CRC16.getType());
		integDef.setIntegrityValue(Converter.hexToBytes("DEADBEEF"));

		builder = new IntegrityDefinitionDao(integDef);
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
		
		builder.setIntegrityValue(Converter.hexToBytes("DEADBEEF"));
		def = builder.buildXml();
		
		assertEquals(def.getIntegrityValue(), builder.getIntegrityValue());
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetIntegrityValueNull()
	{
		builder.setIntegrityValue(null);
	}
	
	@Test
	public void testIntegrityTypeEnum()
	{
		assertEquals(2, IntegrityDefinitionDao.IntegrityType.CRC16.getType());
		assertEquals(3, IntegrityDefinitionDao.IntegrityType.CRC32.getType());
		assertEquals(6, IntegrityDefinitionDao.IntegrityType.CRC64.getType());		
	}
	
	@Test
	public void testIntegrityTypeEnumFromLong()
	{
		assertEquals(IntegrityDefinitionDao.IntegrityType.CRC16, IntegrityDefinitionDao.IntegrityType.fromLong(2));
		assertEquals(IntegrityDefinitionDao.IntegrityType.CRC32, IntegrityDefinitionDao.IntegrityType.fromLong(3));
		assertEquals(IntegrityDefinitionDao.IntegrityType.CRC64, IntegrityDefinitionDao.IntegrityType.fromLong(6));
		assertNull(IntegrityDefinitionDao.IntegrityType.fromLong(-1));
	}
	
	@Test
	public void testHashCode(){
		assertEquals(builder.hashCode(), builder.getIntegrityValue().hashCode());
	}
	
	@Test 
	public void testHashCodeWithNoIntegrity(){
		assertEquals(new IntegrityDefinitionDao().hashCode(), 0);
	}
	
	@Test
	public void testEquals(){
		IntegrityDefinitionDao second = new IntegrityDefinitionDao(integDef);
		
		assertEquals(builder, second);	
	}
}
