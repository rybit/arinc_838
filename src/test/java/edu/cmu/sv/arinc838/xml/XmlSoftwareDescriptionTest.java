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
}
