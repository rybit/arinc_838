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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.arinc.arinc838.SoftwareDescription;

import edu.cmu.sv.arinc838.builder.SoftwareDescriptionBuilder;
import edu.cmu.sv.arinc838.validation.DataValidator;
import edu.cmu.sv.arinc838.validation.ReferenceData;
import static org.testng.Assert.*;

public class SoftwareDescriptionBuilderTest {

	private SoftwareDescriptionBuilder first;
	private SoftwareDescriptionBuilder second;

	@BeforeMethod
	public void setup() {
		first = new SoftwareDescriptionBuilder();
		first.setSoftwarePartNumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		first.setSoftwareTypeDescription("description");
		first.setSoftwareTypeId(10l);

		second = new SoftwareDescriptionBuilder();
		second.setSoftwarePartNumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		second.setSoftwareTypeDescription("description");
		second.setSoftwareTypeId(10l);
	}

	@Test
	public void getSoftwarePartnumber() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		jaxbDesc.setSoftwareTypeDescription("test");
		SoftwareDescriptionBuilder xmlDesc = new SoftwareDescriptionBuilder(
				jaxbDesc);

		assertEquals(jaxbDesc.getSoftwarePartnumber(),
				xmlDesc.getSoftwarePartNumber());
	}

	@Test
	public void getSoftwareTypeDescription() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
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
		jaxbDesc.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		jaxbDesc.setSoftwareTypeDescription("test");
		SoftwareDescriptionBuilder xmlDesc = new SoftwareDescriptionBuilder(
				jaxbDesc);

		assertEquals(jaxbDesc.getSoftwareTypeId(), xmlDesc.getSoftwareTypeId());
	}

	@Test
	public void setSoftwarePartNumberTest() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		jaxbDesc.setSoftwareTypeDescription("test");
		SoftwareDescriptionBuilder xmlDesc = new SoftwareDescriptionBuilder(
				jaxbDesc);

		String value = DataValidator.generateSoftwarePartNumber("8GC??-0987-PLMT");
		
		xmlDesc.setSoftwarePartNumber(value);

		assertEquals(value, xmlDesc.getSoftwarePartNumber());
	}

	@Test
	public void setSoftwareTypeDescription() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
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
		jaxbDesc.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		jaxbDesc.setSoftwareTypeDescription("test");
		jaxbDesc.setSoftwareTypeId(7);
		SoftwareDescriptionBuilder xmlDesc = new SoftwareDescriptionBuilder(
				jaxbDesc);

		long value = 21;

		xmlDesc.setSoftwareTypeId(value);

		assertEquals(value, xmlDesc.getSoftwareTypeId());
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
