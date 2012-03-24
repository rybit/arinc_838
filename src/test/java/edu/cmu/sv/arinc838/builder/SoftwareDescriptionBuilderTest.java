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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.mockito.InOrder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.arinc.arinc838.SoftwareDescription;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.builder.SoftwareDescriptionDao;
import edu.cmu.sv.arinc838.util.Converter;
import edu.cmu.sv.arinc838.validation.DataValidator;
import edu.cmu.sv.arinc838.validation.ReferenceData;
import static org.testng.Assert.*;

public class SoftwareDescriptionBuilderTest {

	private SoftwareDescriptionDao first;
	private SoftwareDescriptionDao second;

	@BeforeMethod
	public void setup() {
		first = new SoftwareDescriptionDao();
		first.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		first.setSoftwareTypeDescription("description");
		first.setSoftwareTypeId(Converter.hexToBytes("0000000A"));

		second = new SoftwareDescriptionDao();
		second.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		second.setSoftwareTypeDescription("description");
		second.setSoftwareTypeId(Converter.hexToBytes("0000000A"));
	}

	@Test
	public void getSoftwarePartnumber() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		jaxbDesc.setSoftwareTypeDescription("test");
		jaxbDesc.setSoftwareTypeId(new byte[] {1,2,3,4});
		SoftwareDescriptionDao xmlDesc = new SoftwareDescriptionDao(
				jaxbDesc);

		assertEquals(jaxbDesc.getSoftwarePartnumber(),
				xmlDesc.getSoftwarePartnumber());
	}

	@Test
	public void getSoftwareTypeDescription() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		jaxbDesc.setSoftwareTypeDescription("test");
		jaxbDesc.setSoftwareTypeId(new byte[] {1,2,3,4});
		SoftwareDescriptionDao xmlDesc = new SoftwareDescriptionDao(
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
		SoftwareDescriptionDao xmlDesc = new SoftwareDescriptionDao(
				jaxbDesc);

		assertEquals(jaxbDesc.getSoftwareTypeId(), xmlDesc.getSoftwareTypeId());
	}

	@Test
	public void setSoftwarePartNumberTest() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		jaxbDesc.setSoftwareTypeDescription("test");
		jaxbDesc.setSoftwareTypeId(new byte[] {1,2,3,4});
		SoftwareDescriptionDao xmlDesc = new SoftwareDescriptionDao(
				jaxbDesc);

		String value = DataValidator
				.generateSoftwarePartNumber("8GC??-0987-PLMT");

		xmlDesc.setSoftwarePartnumber(value);

		assertEquals(value, xmlDesc.getSoftwarePartnumber());
	}

	@Test
	public void setSoftwareTypeDescription() {
		SoftwareDescription jaxbDesc = new SoftwareDescription();
		jaxbDesc.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		jaxbDesc.setSoftwareTypeDescription("test");
		jaxbDesc.setSoftwareTypeId(new byte[] {1,2,3,4});
		SoftwareDescriptionDao xmlDesc = new SoftwareDescriptionDao(
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
		SoftwareDescriptionDao xmlDesc = new SoftwareDescriptionDao(
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
				first.getSoftwarePartnumber());
	}
	
	@Test
	public void testBuildBinary() throws FileNotFoundException, IOException
	{
		BdfFile file = new BdfFile(File.createTempFile("tmp", "bin"));
		int bytesWritten = first.buildBinary(file);
		
		// 2 + "MMMCC-SSSS-SSSS".length + 2 + "description".length + 0x0000000A length in bytes
		// 17 + 13 + 4
		assertEquals(bytesWritten, 34);
		file.seek(0);
		assertEquals(file.readStr64k(), first.getSoftwarePartnumber());
		assertEquals(file.readStr64k(), first.getSoftwareTypeDescription());
		byte[] typeId = new byte[4];
		file.read(typeId);
		assertEquals(typeId, first.getSoftwareTypeId());
		
	}

	@Test
	public void testBuildBinaryWritesSoftwareTypeDescription()
			throws IOException {
		BdfFile file = mock(BdfFile.class);

		InOrder order = inOrder(file);

		first.buildBinary(file);

		order.verify(file).writeSoftwareDescriptionPointer();
		order.verify(file).writeStr64k(first.getSoftwarePartnumber());
		order.verify(file).writeStr64k(first.getSoftwareTypeDescription());
		order.verify(file).writeHexbin32(first.getSoftwareTypeId());
		order.verify(file).getFilePointer();
		order.verifyNoMoreInteractions();
	}
	
	@Test
	public void testCanConstructFromBinary()
			throws IOException {
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
	public void testhashcode(){
		assertEquals(first.hashCode(), first.getSoftwarePartnumber().hashCode());
	}
	
	@Test
	public void testhashcodeWithNoPartNumber(){
		SoftwareDescriptionDao desc = new SoftwareDescriptionDao();
		
		assertEquals(desc.hashCode(), 0);	
	}
	
	@Test
	public void testEquals(){
		assertEquals(first, second);	
	}
}
