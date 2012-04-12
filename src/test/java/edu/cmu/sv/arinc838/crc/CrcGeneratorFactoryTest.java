package edu.cmu.sv.arinc838.crc;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class CrcGeneratorFactoryTest {

	@Test
	public void testFactoryReturns16BitGenerator() throws Exception {
		assertEquals(new Crc16Generator().getClass(),
				new CrcGeneratorFactory().getCrc16Generator().getClass());
	}
	
	@Test
	public void testFactoryReturns32BitGenerator() throws Exception {
		assertEquals(new Crc32Generator().getClass(),
				new CrcGeneratorFactory().getCrc32Generator().getClass());
	}
	
	@Test
	public void testFactoryReturns64BitGenerator() throws Exception {
		assertEquals(new Crc64Generator().getClass(),
				new CrcGeneratorFactory().getCrc64Generator().getClass());
	}
}
