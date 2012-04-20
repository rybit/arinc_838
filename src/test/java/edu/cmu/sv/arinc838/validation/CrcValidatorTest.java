package edu.cmu.sv.arinc838.validation;

import static org.testng.Assert.assertEquals;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import edu.cmu.sv.arinc838.util.Converter;

public class CrcValidatorTest {

	private static final int CRC16 = 0x1DA3;
	private static final long CRC32 = 0x322AB4A6;
	private static final BigInteger CRC64 = new BigInteger(
			Converter.hexToBytes("034528B5989BED4D"));

	private File crcTestFile = new File("src/test/resources/crc_test_files",
			"CRC_T02A.rom");
	private byte[] crcData;

	@BeforeTest
	public void setUp() throws IOException {
		DataInputStream dis = new DataInputStream(new FileInputStream(
				crcTestFile));

		crcData = new byte[(int) crcTestFile.length()];

		int bytesRead = dis.read(crcData);

		assertEquals((int) crcTestFile.length(), bytesRead,
				"Did not read file " + crcTestFile + " correctly.");
		dis.close();
	}
	
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateCrc16NullData() {
		CrcValidator.validateCrc16(-1, null);
	}

	@Test
	public void testValidateCrc16() {
		assertEquals(CrcValidator.validateCrc16(CRC16, crcData), CRC16, "CRC 16 did not validate");
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateCrc32NullData() {
		CrcValidator.validateCrc32(-1, null);
	}
	
	@Test
	public void testValidateCrc32() {
		assertEquals(CrcValidator.validateCrc32(CRC32, crcData), CRC32, "CRC 32 did not validate");
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testValidateCrc64NullData() {
		CrcValidator.validateCrc64(-1, null);
	}
	
	@Test
	public void testValidateCrc64() {
		assertEquals(CrcValidator.validateCrc64(CRC64.longValue(), crcData), CRC64.longValue(), "CRC 64 did not validate");
	}
}
