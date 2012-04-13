package edu.cmu.sv.arinc838.util;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class ConverterTest {
	@Test
	public void hexToBytes() {
		assertEquals(Converter.hexToBytes("DEADBEEF"), new byte[] { (byte) 222,
				(byte) 173, (byte) 190, (byte) 239 });
	}
	
	@Test
	public void bytesToHex() {
		assertEquals(Converter.bytesToHex(new byte[] { (byte) 222,
				(byte) 173, (byte) 190, (byte) 239 }), "DEADBEEF");
	}

	@Test
	public void testIntToBytes() throws Exception {
		assertEquals(Converter.longToBytes(65535), new byte[] { (byte) 0,
			(byte) 0, (byte) -1, (byte) -1 }, "FFFF");
	}
	
	@Test
	public void testBytesToInt() throws Exception {
		assertEquals(Converter.bytesToLong(new byte[] { (byte) 0, (byte) 0, (byte) -1, (byte) -1 }), 
				65535, "FFFF");
	}
}
