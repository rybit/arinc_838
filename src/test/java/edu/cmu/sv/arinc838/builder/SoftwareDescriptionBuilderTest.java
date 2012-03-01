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

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.mockito.InOrder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.arinc.arinc838.SoftwareDescription;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.builder.SoftwareDescriptionBuilder;
import edu.cmu.sv.arinc838.util.Converter;
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
		first.setSoftwareTypeId(Converter.hexToBytes("0000000A"));

		second = new SoftwareDescriptionBuilder();
		second.setSoftwarePartNumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		second.setSoftwareTypeDescription("description");
		second.setSoftwareTypeId(Converter.hexToBytes("0000000A"));
	}

	@Test
	public void getSoftwarePartnumber() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		jaxbDesc.setSoftwareTypeDescription("test");
		jaxbDesc.setSoftwareTypeId(new byte[] {1,2,3,4});
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
		jaxbDesc.setSoftwareTypeId(new byte[] {1,2,3,4});
		SoftwareDescriptionBuilder xmlDesc = new SoftwareDescriptionBuilder(
				jaxbDesc);

		assertEquals(jaxbDesc.getSoftwareTypeDescription(),
				xmlDesc.getSoftwareTypeDescription());
	}

	@Test
	public void getSoftwareTypeId() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwareTypeId(Converter.hexToBytes("00000007"));
		jaxbDesc.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		jaxbDesc.setSoftwareTypeDescription("test");
		jaxbDesc.setSoftwareTypeId(new byte[] {1,2,3,4});
		SoftwareDescriptionBuilder xmlDesc = new SoftwareDescriptionBuilder(
				jaxbDesc);

		assertEquals(jaxbDesc.getSoftwareTypeId(), xmlDesc.getSoftwareTypeId());
	}

	@Test
	public void setSoftwarePartNumberTest() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		jaxbDesc.setSoftwareTypeDescription("test");
		jaxbDesc.setSoftwareTypeId(new byte[] {1,2,3,4});
		SoftwareDescriptionBuilder xmlDesc = new SoftwareDescriptionBuilder(
				jaxbDesc);

		String value = DataValidator
				.generateSoftwarePartNumber("8GC??-0987-PLMT");

		xmlDesc.setSoftwarePartNumber(value);

		assertEquals(value, xmlDesc.getSoftwarePartNumber());
	}

	@Test
	public void setSoftwareTypeDescription() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		jaxbDesc.setSoftwareTypeDescription("test");
		jaxbDesc.setSoftwareTypeId(new byte[] {1,2,3,4});
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
		jaxbDesc.setSoftwareTypeId(Converter.hexToBytes("00000007"));
		SoftwareDescriptionBuilder xmlDesc = new SoftwareDescriptionBuilder(
				jaxbDesc);

		byte[] value = new byte[] { 1, 2, 3, 4 };

		xmlDesc.setSoftwareTypeId(value);

		assertEquals(value, xmlDesc.getSoftwareTypeId());
	}

	@Test
	public void testBuildCreatesProperJaxbObject() {
		SoftwareDescription desc = first.buildXml();

		assertEquals(desc.getSoftwareTypeId(), first.getSoftwareTypeId());
		assertEquals(desc.getSoftwareTypeDescription(),
				first.getSoftwareTypeDescription());
		assertEquals(desc.getSoftwarePartnumber(),
				first.getSoftwarePartNumber());
	}

	@Test
	public void testBuildBinaryWritesSoftwareTypeDescription()
			throws IOException {
		BdfFile file = mock(BdfFile.class);

		InOrder order = inOrder(file);

		first.buildBinary(file);

		order.verify(file).writeSoftwareDescriptionPointer();
		order.verify(file).writeStr64k(first.getSoftwarePartNumber());
		order.verify(file).writeStr64k(first.getSoftwareTypeDescription());
		order.verify(file).write(first.getSoftwareTypeId());
		order.verifyNoMoreInteractions();
	}
}
