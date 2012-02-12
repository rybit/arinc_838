package edu.cmu.sv.arinc838.builder;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.arinc.arinc838.SoftwareDescription;

import edu.cmu.sv.arinc838.builder.SoftwareDescriptionBuilder;
import static org.testng.Assert.*;

public class SoftwareDescriptionBuilderTest {

	private SoftwareDescriptionBuilder first;
	private SoftwareDescriptionBuilder second;

	@BeforeMethod
	public void setup() {
		first = new SoftwareDescriptionBuilder();
		first.setSoftwarePartNumber("part");
		first.setSoftwareTypeDescription("description");
		first.setSoftwareTypeId(10l);

		second = new SoftwareDescriptionBuilder();
		second.setSoftwarePartNumber("part");
		second.setSoftwareTypeDescription("description");
		second.setSoftwareTypeId(10l);
	}

	@Test
	public void getSoftwarePartnumber() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwarePartnumber("test");
		SoftwareDescriptionBuilder xmlDesc = new SoftwareDescriptionBuilder(
				jaxbDesc);

		assertEquals(jaxbDesc.getSoftwarePartnumber(),
				xmlDesc.getSoftwarePartNumber());
	}

	@Test
	public void getSoftwareTypeDescription() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwareTypeDescription("test");
		SoftwareDescriptionBuilder xmlDesc = new SoftwareDescriptionBuilder(
				jaxbDesc);

		assertEquals(jaxbDesc.getSoftwareTypeDescription(),
				xmlDesc.getSoftwareTypeDescription());
	}

	@Test
	public void getSoftwareTypeId() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwareTypeId(7);
		SoftwareDescriptionBuilder xmlDesc = new SoftwareDescriptionBuilder(
				jaxbDesc);

		assertEquals(jaxbDesc.getSoftwareTypeId(), xmlDesc.getSoftwareTypeId());
	}

	@Test
	public void setSoftwarePartNumberTest() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwarePartnumber("test");
		SoftwareDescriptionBuilder xmlDesc = new SoftwareDescriptionBuilder(
				jaxbDesc);

		String value = "set test";

		xmlDesc.setSoftwarePartNumber(value);

		assertEquals(value, xmlDesc.getSoftwarePartNumber());
	}

	@Test
	public void setSoftwareTypeDescription() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwareTypeDescription("test");
		SoftwareDescriptionBuilder xmlDesc = new SoftwareDescriptionBuilder(
				jaxbDesc);

		String value = "set test";

		xmlDesc.setSoftwareTypeDescription(value);

		assertEquals(value, xmlDesc.getSoftwareTypeDescription());
	}

	@Test
	public void setSoftwareTypeId() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwareTypeId(7);
		SoftwareDescriptionBuilder xmlDesc = new SoftwareDescriptionBuilder(
				jaxbDesc);

		long value = 21;

		xmlDesc.setSoftwareTypeId(value);

		assertEquals(value, xmlDesc.getSoftwareTypeId());
	}

	@Test
	public void equalsSucceedsInUsingAllThreePropertiesForTestingEquality() {
		assertEquals(first, second);
	}

	@Test
	public void equalsFailsInUsingAllThreePropertiesForTestingEquality() {
		second.setSoftwarePartNumber("part2");
		assertNotEquals(first, second);
	}

	@Test
	public void equalsSucceedsIfObjectIsTheSame() {
		assertEquals(first, first);
	}

	@Test
	public void equalsFailsIfObjectIsADifferentType() {
		assertNotEquals(first, new Object());
	}

	@Test
	public void hashCodeIsCombinationOfThreePropertiesHashCodes() {
		assertEquals(
				first.hashCode(),
				first.getSoftwarePartNumber().hashCode()
						^ first.getSoftwareTypeDescription().hashCode()
						^ first.getSoftwareTypeId());
	}

	@Test
	public void testBuildCreatesProperJaxbObject() {
		SoftwareDescription desc = first.build();

		assertEquals(desc.getSoftwareTypeId(), first.getSoftwareTypeId());
		assertEquals(desc.getSoftwareTypeDescription(),
				first.getSoftwareTypeDescription());
		assertEquals(desc.getSoftwarePartnumber(),
				first.getSoftwarePartNumber());
	}
}
