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
import static org.testng.AssertJUnit.fail;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import edu.cmu.sv.arinc838.builder.IntegrityDefinitionBuilder.IntegrityType;
import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;

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

		assertEquals((long) 0, DataValidator.validateUint32(0));
		assertEquals((long) 1234, DataValidator.validateUint32(1234));
		long max = (long) Math.pow(2, 32);
		assertEquals(max, DataValidator.validateUint32(max));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateUint32Negative() {
		DataValidator.validateUint32(-1);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateUint32OutOfRange() {
		DataValidator.validateUint32(Long.MAX_VALUE);
	}

	@Test
	public void testValidateStr64k() {
		String inputStr = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789 -_=+`~'\"[]{}\\|;:,./?!@#$%^*()";
		assertEquals(inputStr, DataValidator.validateStr64k(inputStr));
		assertEquals(str64kMax, DataValidator.validateStr64k(str64kMax));
		assertEquals("", DataValidator.validateStr64k(""));

		assertEquals("&lt", DataValidator.validateStr64k("&lt"));
		assertEquals("hello&lt", DataValidator.validateStr64k("hello&lt"));
		assertEquals("&gt", DataValidator.validateStr64k("&gt"));
		assertEquals("&gtthere", DataValidator.validateStr64k("&gtthere"));
		assertEquals("&amp", DataValidator.validateStr64k("&amp"));
		assertEquals("hello&amp&ampthere",
				DataValidator.validateStr64k("hello&amp&ampthere"));

	}

	@Test
	public void testValidateStr64kWithNonEscapedChars() {

		try {
			DataValidator.validateStr64k("1 > 2");
			fail("Did not throw IllegalArguementExcetion for non-escaped >");
		} catch (IllegalArgumentException e) {
		}

		try {
			DataValidator.validateStr64k("1 < 2");
			fail("Did not throw IllegalArguementExcetion for non-escaped <");
		} catch (IllegalArgumentException e) {
		}

		try {
			DataValidator.validateStr64k("Mumford & Sons");
			fail("Did not throw IllegalArguementExcetion for non-escaped &");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateStr64kTooLarge() {
		DataValidator.validateStr64k(str64kMax + "Y");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateStr64kNull() {
		DataValidator.validateStr64k(null);
	}

	@Test
	public void testValidateFileFormatVersion() {
		assertEquals(
				SoftwareDefinitionFileBuilder.DEFAULT_FILE_FORMAT_VERSION,
				DataValidator
						.validateFileFormatVersion(SoftwareDefinitionFileBuilder.DEFAULT_FILE_FORMAT_VERSION));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateFileFormatVersionInvalid() {
		DataValidator
				.validateFileFormatVersion(SoftwareDefinitionFileBuilder.DEFAULT_FILE_FORMAT_VERSION + 1);
	}

	@Test
	public void testValidateIntegrityType() {
		assertEquals(IntegrityType.CRC16.getType(),
				DataValidator.validateIntegrityType(IntegrityType.CRC16
						.getType()));
		assertEquals(IntegrityType.CRC32.getType(),
				DataValidator.validateIntegrityType(IntegrityType.CRC32
						.getType()));
		assertEquals(IntegrityType.CRC64.getType(),
				DataValidator.validateIntegrityType(IntegrityType.CRC64
						.getType()));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateIntegrityTypeInvalid() {
		DataValidator.validateIntegrityType(-1);
	}

	@Test
	public void testValidateIntegrityValue() {
		assertEquals("0xABCD", DataValidator.validateIntegrityValue("0xABCD"));
		assertEquals("0xABCDEF",
				DataValidator.validateIntegrityValue("0xABCDEF"));
		assertEquals("0xABCDEF0123",
				DataValidator.validateIntegrityValue("0xABCDEF0123"));
		assertEquals("0x456789fFBC",
				DataValidator.validateIntegrityValue("0x456789fFBC"));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateIntegrityValueNull() {
		DataValidator.validateIntegrityValue(null);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateIntegrityValueWrongSize() {
		DataValidator.validateIntegrityValue("0xAB");
	}

	@Test
	public void testValidateIntegrityValueInvalidCharacters() {
		try {
			DataValidator.validateIntegrityValue("0xabCDEG");
			fail("Did not throw IllegalArguementExcetion for invalid characters");
		} catch (IllegalArgumentException e) {
		}

		try {
			DataValidator.validateIntegrityValue("%57X");
			fail("Did not throw IllegalArguementExcetion for invalid characters");
		} catch (IllegalArgumentException e) {
		}
		try {
			DataValidator.validateIntegrityValue("0x12ZZZ678af");
			fail("Did not throw IllegalArguementExcetion for invalid characters");
		} catch (IllegalArgumentException e) {
		}
		try {
			DataValidator.validateIntegrityValue("0 x2ZZZ678af");
			fail("Did not throw IllegalArguementExcetion for invalid characters");
		} catch (IllegalArgumentException e) {
		}
		try {
			DataValidator.validateIntegrityValue("0x4567 89fFBC");
			fail("Did not throw IllegalArguementExcetion for invalid characters");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testValidateList1() {
		List<String> list1 = new ArrayList<String>();
		list1.add("hello");
		assertEquals(list1, DataValidator.validateList1(list1));

		list1.add("world");
		list1.add("!");

		assertEquals(list1, DataValidator.validateList1(list1));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateList1Null() {
		DataValidator.validateList1(null);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateList1EmptyList() {
		DataValidator.validateList1(new ArrayList<String>());
	}


	@Test
	public void testValidateSoftwarePartNumber() {
		assertEquals("ACM47-1234-5678",
				DataValidator.validateSoftwarePartNumber("ACM47-1234-5678"));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateSoftwarePartNumberNull() {
		DataValidator.validateSoftwarePartNumber(null);
	}

	@Test
	public void testValidateSoftwarePartNumberInvalidFormat() {
		try {
			DataValidator.validateSoftwarePartNumber("ACM4-7-1234-5678");
			fail("Did not fail on invalid format");
		} catch (IllegalArgumentException e) {
		}

		try {
			DataValidator.validateSoftwarePartNumber("ACM47-12 34-5678");
			fail("Did not fail on embedded spaces");
		} catch (IllegalArgumentException e) {
		}

		try {
			DataValidator.validateSoftwarePartNumber("ACM47-123I-5678");
			fail("Did not fail on illegal character I");
		} catch (IllegalArgumentException e) {
		}

		try {
			DataValidator.validateSoftwarePartNumber("ACM47-123q-5678");
			fail("Did not fail on illegal character Q");
		} catch (IllegalArgumentException e) {
		}

		try {
			DataValidator.validateSoftwarePartNumber("ACM47-O234-5678");
			fail("Did not fail on illegal character O");
		} catch (IllegalArgumentException e) {
		}

		try {
			DataValidator.validateSoftwarePartNumber("ACM47-1234-5Z78");
			fail("Did not fail on illegal character Z");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void generateSoftwarePartNumber() {
		assertEquals("ACM47-1234-5678",
				DataValidator.generateSoftwarePartNumber("ACM??-1234-5678"));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void generateSoftwarePartNumberNull() {
		DataValidator.generateSoftwarePartNumber(null);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void generateSoftwarePartNumberInvalid() {
		DataValidator.generateSoftwarePartNumber("ACM47-O234-5678");
	}

}
