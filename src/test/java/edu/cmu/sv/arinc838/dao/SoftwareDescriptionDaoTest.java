/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 7, 2012
 */
package edu.cmu.sv.arinc838.dao;

import static org.testng.Assert.*;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.Test;

import com.arinc.arinc838.SoftwareDescription;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.util.Converter;
import edu.cmu.sv.arinc838.validation.ReferenceData;

public class SoftwareDescriptionDaoTest {

	@Test
	public void testSoftwarePartNumberAccessor() {
		SoftwareDescriptionDao sdDao = new SoftwareDescriptionDao();
		sdDao.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);

		assertEquals(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE, sdDao.getSoftwarePartnumber());

		sdDao.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE + "_NOT");
		assertNotEquals(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE, sdDao.getSoftwarePartnumber());
	}

	@Test
	public void testSoftwareTypeDescriptionAccessor() {
		String refVal = "sometestvalue";

		SoftwareDescriptionDao sdDao = new SoftwareDescriptionDao();
		sdDao.setSoftwareTypeDescription(refVal);
		assertEquals(refVal, sdDao.getSoftwareTypeDescription());
		
		sdDao.setSoftwareTypeDescription(refVal + "--");
		assertNotEquals(refVal, sdDao.getSoftwareTypeDescription());
	}

	@Test
	public void testSoftwareTypeAccessor() {
		byte[] refVal = new byte[] { 1, 2, 3 };
		SoftwareDescriptionDao sdDao = new SoftwareDescriptionDao();

		sdDao.setSoftwareTypeId(refVal);
		assertEquals(refVal, sdDao.getSoftwareTypeId());
		
		sdDao.setSoftwareTypeId(new byte[] { 1 });
		assertNotEquals(refVal, sdDao.getSoftwareTypeId());
	}

	@Test 
	public void testCanConstructFromXml () {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		jaxbDesc.setSoftwareTypeDescription("test");
		jaxbDesc.setSoftwareTypeId(new byte[] {1,2,3,4});
		
		SoftwareDescriptionDao sdDao = new SoftwareDescriptionDao(jaxbDesc);
		
		assertEquals(jaxbDesc.getSoftwarePartnumber(), sdDao.getSoftwarePartnumber());
		assertEquals(jaxbDesc.getSoftwareTypeDescription(), sdDao.getSoftwareTypeDescription());
		assertEquals(jaxbDesc.getSoftwareTypeId(), sdDao.getSoftwareTypeId());
	}
	
	@Test
	public void testCanConstructFromBinary() throws IOException {
		String partNumber = ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE;
		String description = "description";
		byte[] typeId = Converter.hexToBytes("0000000A");

		BdfFile file = new BdfFile(File.createTempFile("prefix", "suffix"));
		file.writeStr64k(partNumber);
		file.writeStr64k(description);
		file.writeHexbin32(typeId);
		file.seek(0);

		SoftwareDescriptionDao desc = new SoftwareDescriptionDao(file);

		assertEquals(desc.getSoftwarePartnumber(), partNumber);
		assertEquals(desc.getSoftwareTypeDescription(), description);
		assertEquals(desc.getSoftwareTypeId(), typeId);
	}

	@Test
	public void testhashcode() {
		SoftwareDescriptionDao first = new SoftwareDescriptionDao();
		first.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		first.setSoftwareTypeDescription("description");
		first.setSoftwareTypeId(Converter.hexToBytes("0000000A"));

		assertEquals(first.hashCode(), first.getSoftwarePartnumber().hashCode());
	}

	@Test
	public void testhashcodeWithNoPartNumber() {
		SoftwareDescriptionDao desc = new SoftwareDescriptionDao();

		assertEquals(desc.hashCode(), 0);
	}

	@Test
	public void testEquals() {
		SoftwareDescriptionDao first = new SoftwareDescriptionDao();
		first.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		first.setSoftwareTypeDescription("description");
		first.setSoftwareTypeId(Converter.hexToBytes("0000000A"));

		SoftwareDescriptionDao second = new SoftwareDescriptionDao();
		second.setSoftwarePartnumber(first.getSoftwarePartnumber());
		second.setSoftwareTypeDescription(first.getSoftwareTypeDescription());
		second.setSoftwareTypeId(first.getSoftwareTypeId());
		
		assertTrue(first.equals(second));
		assertTrue (second.equals(first));
		assertFalse (first.equals(null));
		assertFalse (first.equals(new Object ()));
	}
}
