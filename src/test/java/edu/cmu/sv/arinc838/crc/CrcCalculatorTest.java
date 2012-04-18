package edu.cmu.sv.arinc838.crc;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao.IntegrityType;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.util.Converter;
import edu.cmu.sv.arinc838.validation.ReferenceData;

public class CrcCalculatorTest {

	private SoftwareDefinitionFileDao sdf;
	private BdfFile bdf;

	@BeforeTest
	public void setUpBeforeTest() throws FileNotFoundException {
		sdf = ReferenceData.SDF_TEST_FILE;
		bdf = new BdfFile(new File(
				"src/test/resources/ACM47-1234-5678/ACM4712345678.BDF"));
	}

	@Test
	public void testCalculateLspCrc() throws IOException {
		long crc = CrcCalculator.calculateLspCrc(sdf, bdf);
		long expectedCrc = Converter.checksumBytesToLong(sdf
				.getLspIntegrityDefinition());
		assertEquals(crc, expectedCrc, "LSP CRC did not calculate correctly");
	}

	@Test
	public void testCalculateSdfCrc() throws IOException {
		long crc = CrcCalculator.calculateSdfCrc(sdf, bdf);
		long expectedCrc = Converter.checksumBytesToLong(sdf
				.getSdfIntegrityDefinition());
		assertEquals(crc, expectedCrc, "SDF CRC did not calculate correctly");

	}

	@Test
	public void testCalculateCrc16() throws IOException {
		File file = new File("src/test/resources/crc_test_files/CRC_T02A.rom");

		byte[] data = CrcCalculator.readFile(file);

		IntegrityDefinitionDao integ = new IntegrityDefinitionDao();
		integ.setIntegrityType(IntegrityType.CRC16.getType());
		integ.setIntegrityValue(Converter.hexToBytes("1DA3"));
		long crc = CrcCalculator.calculateCrc(integ, data);
		
		long expectedCrc = Converter.checksumBytesToLong(integ);
		assertEquals(crc, expectedCrc, "CRC16 did not calculate correctly");
	}
	
	@Test
	public void testCalculateCrc32() throws IOException {
		File file = new File("src/test/resources/crc_test_files/CRC_T02A.rom");

		byte[] data = CrcCalculator.readFile(file);

		IntegrityDefinitionDao integ = new IntegrityDefinitionDao();
		integ.setIntegrityType(IntegrityType.CRC32.getType());
		integ.setIntegrityValue(Converter.hexToBytes("322AB4A6"));
		long crc = CrcCalculator.calculateCrc(integ, data);
		
		long expectedCrc = Converter.checksumBytesToLong(integ);
		assertEquals(crc, expectedCrc, "CRC32 did not calculate correctly");
	}
	
	@Test
	public void testCalculateCrc64() throws IOException {
		File file = new File("src/test/resources/crc_test_files/CRC_T02A.rom");

		byte[] data = CrcCalculator.readFile(file);

		IntegrityDefinitionDao integ = new IntegrityDefinitionDao();
		integ.setIntegrityType(IntegrityType.CRC64.getType());
		integ.setIntegrityValue(Converter.hexToBytes("034528B5989BED4D"));
		long crc = CrcCalculator.calculateCrc(integ, data);
		
		long expectedCrc = Converter.checksumBytesToLong(integ);
		assertEquals(crc, expectedCrc, "CRC64 did not calculate correctly");
	}
}
