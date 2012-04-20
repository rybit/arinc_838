/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 6, 2012
 */
package edu.cmu.sv.arinc838.writer;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.testng.annotations.Test;

import com.arinc.arinc838.FileDefinition;
import com.arinc.arinc838.IntegrityDefinition;
import com.arinc.arinc838.SdfFile;
import com.arinc.arinc838.SoftwareDescription;
import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.util.Converter;
import edu.cmu.sv.arinc838.validation.DataValidator;
import edu.cmu.sv.arinc838.validation.ReferenceData;
import edu.cmu.sv.arinc838.validation.SoftwareDefinitionFileValidator;

public class XdfWriterTest {

	@Test
	public void xdfWriteTest() throws Exception {
		SdfFile file = getTestFile();
		File writtenXmlFile = new File(
				ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE.replace("-", "") + ".XDF");

		try {
			// write the file based on the input
			XdfWriter writer = new XdfWriter();
			writer.write(writtenXmlFile, file);

			// read the file back in
			SdfFile jaxbFile = readJaxb(writtenXmlFile);

			// verify the files match
			verifyMatch(file, writtenXmlFile.getName(), jaxbFile,
					writtenXmlFile.getName());

		} finally {
			writtenXmlFile.delete();
		}
	}

	@Test
	public void testGetFileName() throws Exception {
		SdfFile file = getTestFile();

		SoftwareDefinitionFileDao sdfDao = new SoftwareDefinitionFileDao(file, "");
		XdfWriter writer = new XdfWriter();
		String fileName = writer.getFilename(sdfDao);
		assertEquals(fileName, sdfDao.getXmlFileName());
	}

	private void verifyMatch(SdfFile file1, String fileName1, SdfFile file2,
			String fileName2) throws Exception {
		// verify validity of both files
		SoftwareDefinitionFileValidator validator = new SoftwareDefinitionFileValidator(
				new DataValidator());
		List<Exception> errors = validator.validateSdfFile(
				new SoftwareDefinitionFileDao(file1, ""), fileName1, null);
		assertEquals(errors.size(), 0);
		assertEquals(validator.validateXmlFileHeader(new File (fileName1)).size(), 0);
		errors = validator.validateSdfFile(
				new SoftwareDefinitionFileDao(file2, ""), fileName2, null);
		assertEquals(errors.size(), 0);
		assertEquals(validator.validateXmlFileHeader(new File (fileName2)).size(), 0);

		assertEquals(file1.getFileFormatVersion(), file2.getFileFormatVersion());

		assertEquals(file1.getLspIntegrityDefinition().getIntegrityType(),
				file2.getLspIntegrityDefinition().getIntegrityType());
		assertEquals(file1.getLspIntegrityDefinition().getIntegrityValue(),
				file2.getLspIntegrityDefinition().getIntegrityValue());
		assertEquals(file1.getSoftwareDescription().getSoftwarePartnumber(),
				file2.getSoftwareDescription().getSoftwarePartnumber());
		assertEquals(file1.getSoftwareDescription()
				.getSoftwareTypeDescription(), file2.getSoftwareDescription()
				.getSoftwareTypeDescription());
		assertEquals(file1.getSoftwareDescription().getSoftwareTypeId(), file2
				.getSoftwareDescription().getSoftwareTypeId());

		assertEquals(file1.getFileDefinitions().size(), file2
				.getFileDefinitions().size());
		List<FileDefinition> file1FileDefs = file1.getFileDefinitions();
		List<FileDefinition> file2FileDefs = file2.getFileDefinitions();
		for (int i = 0; i < file1FileDefs.size(); i++) {
			FileDefinition fdef1 = file1FileDefs.get(i);
			FileDefinition fdef2 = file2FileDefs.get(i);
			assertEquals(fdef1.getFileName(), fdef2.getFileName());
			assertEquals(fdef1.getFileSize(), fdef2.getFileSize());
			assertEquals(fdef1.getFileIntegrityDefinition().getIntegrityType(),
					fdef2.getFileIntegrityDefinition().getIntegrityType());
			assertEquals(
					fdef1.getFileIntegrityDefinition().getIntegrityValue(),
					fdef2.getFileIntegrityDefinition().getIntegrityValue());
		}

		assertEquals(file1.getThwDefinitions().size(), file2
				.getThwDefinitions().size());
		List<ThwDefinition> file1ThwDefs = file1.getThwDefinitions();
		List<ThwDefinition> file2ThwDefs = file2.getThwDefinitions();
		for (int i = 0; i < file1ThwDefs.size(); i++) {
			ThwDefinition tdef1 = file1ThwDefs.get(i);
			ThwDefinition tdef2 = file2ThwDefs.get(i);
			assertEquals(tdef1.getThwId(), tdef2.getThwId());
		}
	}

	private SdfFile readJaxb(File writtenXmlFile) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(SdfFile.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return (SdfFile) jaxbUnmarshaller.unmarshal(writtenXmlFile);
	}

	private static SdfFile getTestFile() {
		SdfFile swDefFile = new SdfFile();
		IntegrityDefinition lspInteg = new IntegrityDefinition();
		lspInteg.setIntegrityType(2);
		lspInteg.setIntegrityValue(Converter.hexToBytes("DEADBEEF"));
		IntegrityDefinition sdfInteg = new IntegrityDefinition();
		sdfInteg.setIntegrityType(2);
		sdfInteg.setIntegrityValue(Converter.hexToBytes("DEADBEEF"));

		swDefFile.setLspIntegrityDefinition(lspInteg);
		swDefFile.setSdfIntegrityDefinition(sdfInteg);

		SoftwareDescription swDesc = new SoftwareDescription();
		swDesc.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		swDesc.setSoftwareTypeDescription("type");
		swDesc.setSoftwareTypeId(Converter.hexToBytes("DEADBEEF"));

		swDefFile.setSoftwareDescription(swDesc);

		FileDefinition file = new FileDefinition();
		file.setFileIntegrityDefinition(lspInteg);
		file.setFileName("test.rom");
		file.setFileSize(42);
		file.setFileLoadable(true);

		swDefFile.getFileDefinitions().add(file);

		swDefFile
				.setFileFormatVersion(SoftwareDefinitionFileDao.DEFAULT_FILE_FORMAT_VERSION);
		return swDefFile;
	}

	@Test
	public void testBuildsCorrectFileName() throws Exception {
		SoftwareDefinitionFileDao builder = new SoftwareDefinitionFileDao(getTestFile(), "");

		XdfWriter writer = new XdfWriter();

		String tempPath = System.getProperty("java.io.tmpdir");
		String filename = tempPath + builder.getXmlFileName();

		writer.write(tempPath, builder);

		File file = new File(filename);
		assertTrue(file.exists());
		file.delete();
	}
}
