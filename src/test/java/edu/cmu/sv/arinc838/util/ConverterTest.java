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

}
