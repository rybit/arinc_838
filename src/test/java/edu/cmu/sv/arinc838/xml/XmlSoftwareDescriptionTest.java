package edu.cmu.sv.arinc838.xml;

import org.testng.annotations.Test;
import com.arinc.arinc838.SoftwareDescription;
import static org.testng.Assert.*;

public class XmlSoftwareDescriptionTest {

	@Test
	public void getSoftwarePartnumber() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwarePartnumber("test");
		XmlSoftwareDescription xmlDesc = new XmlSoftwareDescription(jaxbDesc);

		assertEquals(jaxbDesc.getSoftwarePartnumber(),
				xmlDesc.getSoftwarePartNumber());
	}

	@Test
	public void getSoftwareTypeDescription() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwareTypeDescription("test");
		XmlSoftwareDescription xmlDesc = new XmlSoftwareDescription(jaxbDesc);

		assertEquals(jaxbDesc.getSoftwareTypeDescription(),
				xmlDesc.getSoftwareTypeDescription());
	}

	@Test
	public void getSoftwareTypeId() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwareTypeId(7);
		XmlSoftwareDescription xmlDesc = new XmlSoftwareDescription(jaxbDesc);

		assertEquals(jaxbDesc.getSoftwareTypeId(), xmlDesc.getSoftwareTypeId());
	}

	@Test
	public void setSoftwarePartNumberTest() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwarePartnumber("test");
		XmlSoftwareDescription xmlDesc = new XmlSoftwareDescription(jaxbDesc);

		String value = "set test";
		
		xmlDesc.setSoftwarePartNumber(value);
		
		assertEquals(value, xmlDesc.getSoftwarePartNumber());
	}

	@Test
	public void setSoftwareTypeDescription() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwareTypeDescription("test");
		XmlSoftwareDescription xmlDesc = new XmlSoftwareDescription(jaxbDesc);

		String value = "set test";
		
		xmlDesc.setSoftwareTypeDescription(value);
		
		assertEquals(value, xmlDesc.getSoftwareTypeDescription());
	}

	@Test
	public void setSoftwareTypeId() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwareTypeId(7);
		XmlSoftwareDescription xmlDesc = new XmlSoftwareDescription(jaxbDesc);

		long value = 21;
		
		xmlDesc.setSoftwareTypeId(value);
		
		assertEquals(value, xmlDesc.getSoftwareTypeId());
	}
	
	@Test
	public void equalsSucceedsInUsingAllThreePropertiesForTestingEquality(){
		XmlSoftwareDescription first = new XmlSoftwareDescription();
		first.setSoftwarePartNumber("part");
		first.setSoftwareTypeDescription("description");
		first.setSoftwareTypeId(10l);
		
		XmlSoftwareDescription second = new XmlSoftwareDescription();
		second.setSoftwarePartNumber("part");
		second.setSoftwareTypeDescription("description");
		second.setSoftwareTypeId(10l);
		
		assertEquals(first, second);
	}
	
	@Test
	public void equalsFailsInUsingAllThreePropertiesForTestingEquality(){
		XmlSoftwareDescription first = new XmlSoftwareDescription();
		first.setSoftwarePartNumber("part");
		first.setSoftwareTypeDescription("description");
		first.setSoftwareTypeId(10l);
		
		XmlSoftwareDescription second = new XmlSoftwareDescription();
		second.setSoftwarePartNumber("part2");
		second.setSoftwareTypeDescription("description");
		second.setSoftwareTypeId(10l);
		
		assertNotEquals(first, second);
	}
	
	@Test
	public void equalsSucceedsIfObjectIsTheSame(){
		XmlSoftwareDescription first = new XmlSoftwareDescription();
		first.setSoftwarePartNumber("part");
		first.setSoftwareTypeDescription("description");
		first.setSoftwareTypeId(10l);
		
		assertEquals(first, first);
	}
	
	@Test
	public void equalsFailsIfObjectIsADifferentType(){
		XmlSoftwareDescription first = new XmlSoftwareDescription();
		first.setSoftwarePartNumber("part");
		first.setSoftwareTypeDescription("description");
		first.setSoftwareTypeId(10l);		
		
		assertNotEquals(first, new Object());
	}
	
	@Test
	public void hashCodeIsCombinationOfThreePropertiesHashCodes(){
		XmlSoftwareDescription first = new XmlSoftwareDescription();
		first.setSoftwarePartNumber("part");
		first.setSoftwareTypeDescription("description");
		first.setSoftwareTypeId(10l);	
		
		assertEquals(first.hashCode(), first.getSoftwarePartNumber().hashCode() ^ first.getSoftwareTypeDescription().hashCode() ^ first.getSoftwareTypeId());
	}
}
