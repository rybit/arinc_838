package edu.cmu.sv.arinc838.builder;

import static org.testng.Assert.*;
import org.testng.annotations.Test;
import com.arinc.arinc838.IntegrityDefinition;

import edu.cmu.sv.arinc838.builder.IntegrityDefinitionBuilder;

public class IntegrityDefinitionBuilderTest {

	@Test
	public void testXmlConstructor() {
		IntegrityDefinition integDef = new IntegrityDefinition();
		integDef.setIntegrityType(1234);
		integDef.setIntegrityValue("fml");

		IntegrityDefinitionBuilder builder = new IntegrityDefinitionBuilder(integDef);
		assertEquals(integDef.getIntegrityType(), builder.getIntegrityType());
		assertEquals(integDef.getIntegrityValue(), builder.getIntegrityValue());
	}

	@Test
	public void testBuild() {
		throw new RuntimeException("Test not implemented");
	}
}
