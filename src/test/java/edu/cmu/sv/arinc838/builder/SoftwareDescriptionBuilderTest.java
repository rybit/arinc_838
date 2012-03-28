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

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.mockito.InOrder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.arinc.arinc838.SoftwareDescription;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.dao.SoftwareDescriptionDao;
import edu.cmu.sv.arinc838.util.Converter;
import edu.cmu.sv.arinc838.validation.ReferenceData;

public class SoftwareDescriptionBuilderTest {

	private SoftwareDescriptionDao first;
	private SoftwareDescriptionBuilder builder;

	@BeforeMethod
	public void setup() {
		first = new SoftwareDescriptionDao();
		first.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		first.setSoftwareTypeDescription("description");
		first.setSoftwareTypeId(Converter.hexToBytes("0000000A"));

		builder = new SoftwareDescriptionBuilder();
	}

	@Test
	public void testBuildCreatesProperJaxbObject() {
		SoftwareDescription desc = builder.buildXml(first);

		assertEquals(desc.getSoftwareTypeId(), first.getSoftwareTypeId());
		assertEquals(desc.getSoftwareTypeDescription(), first.getSoftwareTypeDescription());
		assertEquals(desc.getSoftwarePartnumber(), first.getSoftwarePartnumber());
	}

	@Test
	public void testBuildBinary() throws FileNotFoundException, IOException {
		BdfFile file = new BdfFile(File.createTempFile("tmp", "bin"));
		int bytesWritten = builder.buildBinary(first, file);

		// 2 + "MMMCC-SSSS-SSSS".length + 2 + "description".length + 0x0000000A
		// length in bytes
		// 17 + 13 + 4
		assertEquals(bytesWritten, 34);
		file.seek(0);
		assertEquals(file.readStr64k(), first.getSoftwarePartnumber());
		assertEquals(file.readStr64k(), first.getSoftwareTypeDescription());
		byte[] typeId = new byte[4];
		file.read(typeId);
		assertEquals(typeId, first.getSoftwareTypeId());
		
		file.close();
	}

	@Test
	public void testBuildBinaryWritesSoftwareTypeDescription() throws IOException {
		BdfFile file = mock(BdfFile.class);

		InOrder order = inOrder(file);

		builder.buildBinary(first, file);

		order.verify(file).writeSoftwareDescriptionPointer();
		order.verify(file).writeStr64k(first.getSoftwarePartnumber());
		order.verify(file).writeStr64k(first.getSoftwareTypeDescription());
		order.verify(file).writeHexbin32(first.getSoftwareTypeId());
		order.verify(file).getFilePointer();
		order.verifyNoMoreInteractions();
	}
}
