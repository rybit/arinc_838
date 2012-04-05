package edu.cmu.sv.arinc838.reader;

import static org.testng.Assert.*;
import java.io.File;
import java.util.ArrayList;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.cmu.sv.arinc838.dao.FileDefinitionDao;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.dao.TargetHardwareDefinitionDao;
import edu.cmu.sv.arinc838.validation.ReferenceData;

public class XdfReaderTest {

	@Test
	public void testRead() throws Exception {
		XdfReader reader = new XdfReader();
		ArrayList<Exception> errorList = new ArrayList<Exception>();
		SoftwareDefinitionFileDao sdfDao = reader.read(
				"src/test/resources/ACM4712345678.XDF", errorList);
		assertEquals(errorList.size(), 0, "Unexpected errors during read");

		assertEquals(sdfDao.getFileFormatVersion(),
				ReferenceData.SDF_TEST_FILE.getFileFormatVersion());
		assertEquals(sdfDao.getSoftwareDescription().getSoftwarePartnumber(),
				ReferenceData.SDF_TEST_FILE.getSoftwareDescription()
						.getSoftwarePartnumber());
		assertEquals(sdfDao.getSoftwareDescription()
				.getSoftwareTypeDescription(), ReferenceData.SDF_TEST_FILE
				.getSoftwareDescription().getSoftwareTypeDescription());
		assertEquals(sdfDao.getSoftwareDescription().getSoftwareTypeId(),
				ReferenceData.SDF_TEST_FILE.getSoftwareDescription()
						.getSoftwareTypeId());

		for (int i = 0; i < ReferenceData.SDF_TEST_FILE
				.getTargetHardwareDefinitions().size(); i++) {
			TargetHardwareDefinitionDao actual = sdfDao
					.getTargetHardwareDefinitions().get(i);
			TargetHardwareDefinitionDao expected = ReferenceData.SDF_TEST_FILE
					.getTargetHardwareDefinitions().get(i);
			assertEquals(actual.getThwId(), expected.getThwId());
			for (int j = 0; j < expected.getPositions().size(); j++) {
				String actPos = actual.getPositions().get(j);
				String expPos = expected.getPositions().get(j);
				assertEquals(actPos, expPos);
			}
		}

		for (int i = 0; i < ReferenceData.SDF_TEST_FILE.getFileDefinitions()
				.size(); i++) {
			FileDefinitionDao actual = sdfDao.getFileDefinitions().get(i);
			FileDefinitionDao expected = ReferenceData.SDF_TEST_FILE
					.getFileDefinitions().get(i);
			assertEquals(actual.getFileName(), expected.getFileName());
			assertEquals(actual.getFileSize(), expected.getFileSize());
			assertEquals(
					actual.getFileIntegrityDefinition().getIntegrityType(),
					expected.getFileIntegrityDefinition().getIntegrityType());
			assertEquals(actual.getFileIntegrityDefinition()
					.getIntegrityValue(), expected.getFileIntegrityDefinition()
					.getIntegrityValue());
		}

		assertEquals(sdfDao.getSdfIntegrityDefinition().getIntegrityType(),
				ReferenceData.SDF_TEST_FILE.getSdfIntegrityDefinition()
						.getIntegrityType());
		assertEquals(sdfDao.getSdfIntegrityDefinition().getIntegrityValue(),
				ReferenceData.SDF_TEST_FILE.getSdfIntegrityDefinition()
						.getIntegrityValue());

		assertEquals(sdfDao.getLspIntegrityDefinition().getIntegrityType(),
				ReferenceData.SDF_TEST_FILE.getLspIntegrityDefinition()
						.getIntegrityType());
		assertEquals(sdfDao.getLspIntegrityDefinition().getIntegrityValue(),
				ReferenceData.SDF_TEST_FILE.getLspIntegrityDefinition()
						.getIntegrityValue());
	}

	@Test
	public void testReadErrors() throws Exception {
		XdfReader reader = new XdfReader();
		ArrayList<Exception> errorList = new ArrayList<Exception>();
		SoftwareDefinitionFileDao sdfDao = reader.read(
				"src/test/resources/error/ACM4712345678.XDF", errorList);

		assertEquals(errorList.size(), 2,
				"Did not get expected number of errors");
	}
}
