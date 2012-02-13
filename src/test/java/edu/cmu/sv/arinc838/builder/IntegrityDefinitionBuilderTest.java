package edu.cmu.sv.arinc838.builder;

import static org.testng.Assert.*;

import org.testng.annotations.*;
import com.arinc.arinc838.IntegrityDefinition;

import edu.cmu.sv.arinc838.builder.IntegrityDefinitionBuilder;

public class IntegrityDefinitionBuilderTest {

	private IntegrityDefinition integDef;
	private IntegrityDefinitionBuilder builder;

	@BeforeMethod
	public void setup() {
		integDef = new IntegrityDefinition();
		integDef.setIntegrityType(1234);
		integDef.setIntegrityValue("fml");

		builder = new IntegrityDefinitionBuilder(integDef);
	}

	@Test
	public void testXmlConstructor() {

		assertEquals(integDef.getIntegrityType(), builder.getIntegrityType());
		assertEquals(integDef.getIntegrityValue(), builder.getIntegrityValue());
	}

	@Test
	public void testSetIntegrityType() {
		IntegrityDefinition def = builder.build();

		assertEquals(def.getIntegrityType(), builder.getIntegrityType());
		
		long newType = def.getIntegrityType() + 1;
		builder.setIntegrityType(newType);
		def = builder.build();
		
		assertEquals(def.getIntegrityType(), builder.getIntegrityType());
	}
	
	@Test
	public void testSetIntegrityValue() {
		IntegrityDefinition def = builder.build();

		assertEquals(def.getIntegrityValue(), builder.getIntegrityValue());
		
		// <old_string&new_string>
		String newType = "&lt" + def.getIntegrityValue() + "&ampnew_string&gt";
		builder.setIntegrityValue(newType);
		def = builder.build();
		
		assertEquals(def.getIntegrityValue(), builder.getIntegrityValue());
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetIntegrityTypeInvalid()
	{
		builder.setIntegrityType(-1);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetIntegrityValueNull()
	{
		builder.setIntegrityValue(null);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetIntegrityValueInvalid()
	{
		builder.setIntegrityValue("abc & 123 &lt&gt <<");
	}
}
