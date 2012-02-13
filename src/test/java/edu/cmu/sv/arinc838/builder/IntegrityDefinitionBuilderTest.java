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
	public void testGetIntegrityType() {
		IntegrityDefinition def = builder.build();

		assertEquals(def.getIntegrityType(), builder.getIntegrityType());
		assertEquals(def.getIntegrityValue(), builder.getIntegrityValue());
	}

	@Test
	public void testSetIntegrityType() {
		builder.setIntegrityType(12);

		assertEquals(builder.getIntegrityType(), 12);
	}

	@Test
	public void testSetIntegrityValue() {
		builder.setIntegrityValue("val");

		assertEquals(builder.getIntegrityValue(), "val");
	}
}
