package edu.cmu.sv.arinc838.validation;

import static org.testng.AssertJUnit.*;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

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
		assertEquals("hello&amp&ampthere", DataValidator.validateStr64k("hello&amp&ampthere"));
		
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

}
