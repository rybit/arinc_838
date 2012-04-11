package edu.cmu.sv.arinc838.crc;

import static org.testng.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Map;

import org.testng.annotations.Test;

public class Crc32GeneratorTest {
	@Test
	public void calculateCrcTest() throws Exception {
		Map<BigInteger, byte[]> expectedCrcs = CrcGeneratorTestCommon
				.getExpectedCrcs("crc32");
		for (BigInteger expectedCrc : expectedCrcs.keySet()) {
			long crc = Crc32Generator.calculateCrc(expectedCrcs
					.get(expectedCrc));
			assertEquals(crc, expectedCrc.longValue());
		}
	}
}