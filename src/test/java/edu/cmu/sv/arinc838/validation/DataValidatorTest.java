/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 12, 2012
 */
package edu.cmu.sv.arinc838.validation;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import edu.cmu.sv.arinc838.dao.FileDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao.IntegrityType;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.util.Converter;

public class DataValidatorTest {

	private String str64kMax = "";

	@BeforeTest
	public void beforeTest() {
		for (int i = 0; i < 65535; i++) {
			str64kMax += "X";
		}
	}

	@Test
	public void testValidateUint32() {

		assertEquals((long) 0, new DataValidator().validateUint32(0));
		assertEquals((long) 1234, new DataValidator().validateUint32(1234));
		long max = (long) Math.pow(2, 32);
		assertEquals(max, new DataValidator().validateUint32(max));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateUint32Negative() {
		new DataValidator().validateUint32(-1);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateUint32OutOfRange() {
		new DataValidator().validateUint32(Long.MAX_VALUE);
	}

	@Test
	public void testValidateStr64kBinary() {
		String inputStr = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789 -_=+`~'\"[]{}\\|;:,./?!@#$%^*()";
		assertEquals(inputStr,
				new DataValidator().validateStr64kBinary(inputStr));
		assertEquals(str64kMax,
				new DataValidator().validateStr64kBinary(str64kMax));
		assertEquals("", new DataValidator().validateStr64kBinary(""));

		assertEquals("&lt", new DataValidator().validateStr64kBinary("&lt"));
		assertEquals("hello&lt",
				new DataValidator().validateStr64kBinary("hello&lt"));
		assertEquals("&gt", new DataValidator().validateStr64kBinary("&gt"));
		assertEquals("&gtthere",
				new DataValidator().validateStr64kBinary("&gtthere"));
		assertEquals("&amp", new DataValidator().validateStr64kBinary("&amp"));
		assertEquals("hello&amp&ampthere",
				new DataValidator().validateStr64kBinary("hello&amp&ampthere"));

	}

	@Test
	public void testValidateStr64kXmlMaxSizeWithEscapedChars() {
		// this creates a max-sized string, with an escaped '&' as the last
		// character.This will make the absolute length > than the max string
		// length, but the true length will be = the max length, and should
		// still be valid
		String str64kMaxWithEscaped = str64kMax.substring(0,
				str64kMax.length() - 1) + "&amp";
		assertTrue(new DataValidator().validateStr64kXml(str64kMaxWithEscaped)
				.isEmpty());
	}

	@Test
	public void testValidateStr64kXml() {
		String inputStr = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789 -_=+`~'\"[]{}\\|;:,./?!@#$%^*()";
		assertTrue(new DataValidator().validateStr64kXml(inputStr).isEmpty());
		assertTrue(new DataValidator().validateStr64kXml(str64kMax).isEmpty());
		assertTrue(new DataValidator().validateStr64kXml("").isEmpty());
		assertTrue(new DataValidator().validateStr64kXml("&lt").isEmpty());
		assertTrue(new DataValidator().validateStr64kXml("hello&lt").isEmpty());
		assertTrue(new DataValidator().validateStr64kXml("&gt").isEmpty());
		assertTrue(new DataValidator().validateStr64kXml("&gtthere").isEmpty());
		assertTrue(new DataValidator().validateStr64kXml("&amp").isEmpty());
		assertTrue(new DataValidator().validateStr64kXml("hello&amp&ampthere")
				.isEmpty());
	}

	@Test
	public void testValidateStr64kXmlWithNonEscapedChars() {
		assertEquals(
				"Did not throw IllegalArgumentException for non-escaped >", 1,
				new DataValidator().validateStr64kXml("1 > 2").size());
		assertEquals(
				"Did not throw IllegalArgumentException for non-escaped <", 1,
				new DataValidator().validateStr64kXml("1 < 2").size());
		assertEquals(
				"Did not throw IllegalArgumentException for non-escaped &", 1,
				new DataValidator().validateStr64kXml("Mumford & Sons").size());
	}

	@Test
	public void testValidateStr64kXmlTooLarge() {
		assertEquals(1, new DataValidator().validateStr64kXml(str64kMax + "Y")
				.size());
	}

	@Test
	public void testValidateStr64kXmlMultipleErrors() {
		// too large, and unescaped XML
		assertEquals(2, new DataValidator().validateStr64kXml(str64kMax + "&")
				.size());
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateStr64kBinaryTooLarge() {
		new DataValidator().validateStr64kBinary(str64kMax + "Y");
	}

	@Test
	public void testValidateStr64kXmlNull() {
		assertEquals(1, new DataValidator().validateStr64kXml(null).size());
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateStr64kBinaryNull() {
		new DataValidator().validateStr64kBinary(null);
	}

	@Test
	public void testValidateFileFormatVersion() {
		assertEquals(
				SoftwareDefinitionFileDao.DEFAULT_FILE_FORMAT_VERSION,
				new DataValidator()
						.validateFileFormatVersion(SoftwareDefinitionFileDao.DEFAULT_FILE_FORMAT_VERSION
								.clone()));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateFileFormatVersionInvalid() {
		new DataValidator()
				.validateFileFormatVersion(new byte[] { 1, 2, 3, 4 });
	}

	@Test
	public void testValidateIntegrityType() {
		assertEquals(IntegrityType.CRC16.getType(),
				new DataValidator().validateIntegrityType(IntegrityType.CRC16
						.getType()));
		assertEquals(IntegrityType.CRC32.getType(),
				new DataValidator().validateIntegrityType(IntegrityType.CRC32
						.getType()));
		assertEquals(IntegrityType.CRC64.getType(),
				new DataValidator().validateIntegrityType(IntegrityType.CRC64
						.getType()));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateIntegrityTypeInvalid() {
		new DataValidator().validateIntegrityType(-1);
	}

	@Test
	public void testValidateIntegrityValue() {
		assertEquals(Converter.hexToBytes("ABCD"),
				new DataValidator().validateIntegrityValue(Converter
						.hexToBytes("ABCD")));
		assertEquals(Converter.hexToBytes("ABCDEF01"),
				new DataValidator().validateIntegrityValue(Converter
						.hexToBytes("ABCDEF01")));
		assertEquals(Converter.hexToBytes("DEADBEEFDEADBEEF"),
				new DataValidator().validateIntegrityValue(Converter
						.hexToBytes("DEADBEEFDEADBEEF")));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateIntegrityValueNull() {
		new DataValidator().validateIntegrityValue(null);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateIntegrityValueWrongSize() {
		new DataValidator().validateIntegrityValue(Converter.hexToBytes("ABC"));
	}

	@Test
	public void testValidateList1() {
		List<String> list1 = new ArrayList<String>();
		list1.add("hello");
		assertEquals(list1, new DataValidator().validateList1(list1));

		list1.add("world");
		list1.add("!");

		assertEquals(list1, new DataValidator().validateList1(list1));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateList1Null() {
		new DataValidator().validateList1(null);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateList1EmptyList() {
		new DataValidator().validateList1(new ArrayList<String>());
	}

	@Test
	public void testValidateSoftwarePartNumber() {
		assertEquals("ACM47-1234-5678",
				new DataValidator()
						.validateSoftwarePartNumber("ACM47-1234-5678"));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateSoftwarePartNumberNull() {
		new DataValidator().validateSoftwarePartNumber(null);
	}

	@Test
	public void testValidateSoftwarePartNumberInvalidFormat() {
		try {
			new DataValidator().validateSoftwarePartNumber("ACM4-7-1234-5678");
			fail("Did not fail on invalid format");
		} catch (IllegalArgumentException e) {
		}

		try {
			new DataValidator().validateSoftwarePartNumber("ACm47-1234-5678");
			fail("Did not fail on lower case letter");
		} catch (IllegalArgumentException e) {
		}

		try {
			new DataValidator().validateSoftwarePartNumber("1CM37-1234-5678");
		} catch (IllegalArgumentException e) {
			fail("Failed incorrectly with a number first " + e.toString());
		}

		try {
			new DataValidator().validateSoftwarePartNumber("ACM23-1234-5678");
			fail("Did not fail on invalid check characters");
		} catch (IllegalArgumentException e) {
		}

		try {
			new DataValidator().validateSoftwarePartNumber("ACM47-12 34-5678");
			fail("Did not fail on embedded spaces");
		} catch (IllegalArgumentException e) {
		}

		try {
			new DataValidator().validateSoftwarePartNumber("ACM47-123I-5678");
			fail("Did not fail on illegal character I");
		} catch (IllegalArgumentException e) {
		}

		try {
			new DataValidator().validateSoftwarePartNumber("ACM47-123Q-5678");
			fail("Did not fail on illegal character Q");
		} catch (IllegalArgumentException e) {
		}

		try {
			new DataValidator().validateSoftwarePartNumber("ACM47-O234-5678");
			fail("Did not fail on illegal character O");
		} catch (IllegalArgumentException e) {
		}

		try {
			new DataValidator().validateSoftwarePartNumber("ACM47-1234-5Z78");
			fail("Did not fail on illegal character Z");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void generateSoftwarePartNumber() {
		assertEquals("ACM47-1234-5678",
				new DataValidator()
						.generateSoftwarePartNumber("ACM??-1234-5678"));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void generateSoftwarePartNumberNull() {
		new DataValidator().generateSoftwarePartNumber(null);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void generateSoftwarePartNumberInvalid() {
		new DataValidator().generateSoftwarePartNumber("ACM47-O234-5678");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateHexbin32Null() {
		new DataValidator().validateHexbin32((byte[]) null);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateHexbin32Not4Bytes() {
		new DataValidator().validateHexbin32(new byte[] { 1, 2, 3 });
	}

	@Test
	public void testValidateHexbin32() {
		byte[] hexBin32 = new byte[] { 1, 2, 3, 4 };
		assertEquals(hexBin32, new DataValidator().validateHexbin32(hexBin32));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateHexbin64kNull() {
		new DataValidator().validateHexbin64k((byte[]) null);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateHexbin64kOutOfRange() {
		new DataValidator()
				.validateHexbin64k(new byte[DataValidator.HEXBIN64K_MAX_LENGTH + 1]);
	}

	@Test
	public void testValidateHexbin64k() {
		byte[] hexBin64k = new byte[DataValidator.HEXBIN64K_MAX_LENGTH];
		Arrays.fill(hexBin64k, (byte) 0xAB);
		assertEquals(hexBin64k,
				new DataValidator().validateHexbin64k(hexBin64k));
	}

	@Test
	public void testValidateDataFileNameNull() {
		assertEquals(1, new DataValidator().validateDataFileName(null).size());
	}

	@Test
	public void testValidateDataFileNameTooLong() {
		String longName = "";
		for (int i = 0; i <= 251; i++) {
			longName += "A";
		}
		longName += ".bin";
		assertEquals(1, new DataValidator().validateDataFileName(longName)
				.size());
	}

	@Test
	public void testValidateDataFileName() {
		assertTrue(new DataValidator().validateDataFileName("someFile.txt")
				.isEmpty());
	}

	@Test
	public void testValidateDataFileNameInvalidExtensions() {
		DataValidator dataVal = new DataValidator();
		for (String extension : DataValidator.INVALID_DATA_FILE_EXTENSIONS) {
			assertEquals(
					"Did not throw exception for invalid data file extension '"
							+ extension.toLowerCase() + "'",
					1,
					dataVal.validateDataFileName(
							"bad_file." + extension.toLowerCase()).size());

			assertEquals(
					"Did not throw exception for invalid data file extension '"
							+ extension.toUpperCase() + "'",
					1,
					dataVal.validateDataFileName(
							"bad_file." + extension.toUpperCase()).size());

		}
	}

	@Test
	public void testValidateDataFileNameInvalidCharacters() {
		DataValidator dataVal = new DataValidator();

		// check invalid characters
		for (char invalid : new char[] { '"', '\'', '`', '*', '<', '>', ':',
				';', '#', '?', '/', '\\', '|', '~', '!', '@', '$', '%', '^',
				'&', '+', '=', ',' }) {
			assertEquals("Did not throw exception for invalid character ' "
					+ invalid + " '", 1,
					dataVal.validateDataFileName("bad" + invalid + "file.txt")
							.size());

		}

		// check whitespace
		assertEquals("Did not throw exception for whitespace character", 1,
				dataVal.validateDataFileName("bad  file.txt").size());
		assertEquals("Did not throw exception for whitespace character", 1,
				dataVal.validateDataFileName("bad\tfile.txt").size());
		assertEquals("Did not throw exception for whitespace character", 1,
				dataVal.validateDataFileName("bad \nfile.txt").size());
		assertEquals("Did not throw exception for whitespace character", 1,
				dataVal.validateDataFileName("bad\rfile.txt").size());
		assertEquals("Did not throw exception for whitespace character", 1,
				dataVal.validateDataFileName("bad\ffile.txt").size());
	}

	@Test
	public void testValidateDataFileNamesAreUniqueDuplicateNames() {
		ArrayList<FileDefinitionDao> files = new ArrayList<FileDefinitionDao>();
		FileDefinitionDao f1 = new FileDefinitionDao();
		f1.setFileName("abc1.txt");
		files.add(f1);

		FileDefinitionDao f2 = new FileDefinitionDao();
		f2.setFileName("abc2.txt");
		files.add(f2);

		FileDefinitionDao f3 = new FileDefinitionDao();
		f3.setFileName("abc1.txt");
		files.add(f3);
		List<Exception> errors = new DataValidator()
				.validateDataFileNamesAreUnique(files);
		assertEquals(1, errors.size());
	}

	@Test
	public void testValidateDataFileNamesAreUnique() {
		ArrayList<FileDefinitionDao> files = new ArrayList<FileDefinitionDao>();
		FileDefinitionDao f1 = new FileDefinitionDao();
		f1.setFileName("abc1.txt");
		files.add(f1);

		FileDefinitionDao f2 = new FileDefinitionDao();
		f2.setFileName("abc2.txt");
		files.add(f2);

		FileDefinitionDao f3 = new FileDefinitionDao();
		f3.setFileName("abc3.txt");
		files.add(f3);

		List<Exception> errors = new DataValidator()
				.validateDataFileNamesAreUnique(files);
		assertTrue(errors.isEmpty());
	}
	
	@Test   
	public void testValidateXmlHeaderNamespacesNoErrors () throws Exception {
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		                "<sdf:sdf-file xmlns:sdf=\"http://www.arinc.com/arinc838\"" + 
					      " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
					      " xsi:schemaLocation=\"http://www.arinc.com/arinc838\"/>";
		
		InputStream inStream = new ByteArrayInputStream(header.getBytes()); 
		XMLStreamReader xsr = XMLInputFactory.newInstance().createXMLStreamReader(inStream);
		xsr.nextTag();
		
		List<Exception> errors = new DataValidator().validateXmlHeaderNamespaces(xsr);
		assertEquals(0, errors.size());
	}
	
	@Test  (expectedExceptions = XMLStreamException.class) 
	public void testValidateXmlHeaderNamespacesTooFew () throws Exception {
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		                "<sdf:sdf-file xmlns:sdf=\"http://www.arinc.com/arinc838\"" + 
					      " xsi:schemaLocation=\"http://www.arinc.com/arinc838\"/>";
		
		InputStream inStream = new ByteArrayInputStream(header.getBytes()); 
		XMLStreamReader xsr = XMLInputFactory.newInstance().createXMLStreamReader(inStream);
		xsr.nextTag();
	}
	
	@Test   
	public void testValidateXmlHeaderNamespacesTooMany () throws Exception {
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		                "<sdf:sdf-file xmlns:sdf=\"http://www.arinc.com/arinc838\"" + 
					      " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
					      " xmlns:vamps=\"http://www.nosferatu.com\"" +
					      " xsi:schemaLocation=\"http://www.arinc.com/arinc838\"/>";
		
		InputStream inStream = new ByteArrayInputStream(header.getBytes()); 
		XMLStreamReader xsr = XMLInputFactory.newInstance().createXMLStreamReader(inStream);
		xsr.nextTag();
		
		List<Exception> errors = new DataValidator().validateXmlHeaderNamespaces(xsr);
		assertEquals(1, errors.size());
		assertEquals(IllegalArgumentException.class, errors.get(0).getClass());
	}
	
	@Test   
	public void testValidateXmlHeaderAttributesNoErrors () throws Exception {
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		                "<sdf:sdf-file xmlns:sdf=\"http://www.arinc.com/arinc838\"" + 
					      " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
					      " xsi:schemaLocation=\"http://www.arinc.com\"/>";
		
		InputStream inStream = new ByteArrayInputStream(header.getBytes()); 
		XMLStreamReader xsr = XMLInputFactory.newInstance().createXMLStreamReader(inStream);
		xsr.nextTag();
		
		List<Exception> errors = new DataValidator().validateXmlHeaderAttributes(xsr);
		assertEquals(0, errors.size());
	}
	
	@Test   
	public void testValidateXmlHeaderAttributesTooFew () throws Exception {
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		                "<sdf:sdf-file xmlns:sdf=\"http://www.arinc.com/arinc838\"" + 
					      " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>";
		
		InputStream inStream = new ByteArrayInputStream(header.getBytes()); 
		XMLStreamReader xsr = XMLInputFactory.newInstance().createXMLStreamReader(inStream);
		xsr.nextTag();
		
		List<Exception> errors = new DataValidator().validateXmlHeaderAttributes(xsr);
		assertEquals(1, errors.size());
	}
}
