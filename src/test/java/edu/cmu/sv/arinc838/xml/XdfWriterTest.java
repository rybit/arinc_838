/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 6, 2012
 */
package edu.cmu.sv.arinc838.xml;

import static org.testng.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.testng.annotations.Test;

import com.arinc.arinc838.FileDefinition;
import com.arinc.arinc838.IntegrityDefinition;
import com.arinc.arinc838.SdfFile;
import com.arinc.arinc838.SdfSections;
import com.arinc.arinc838.SoftwareDescription;
import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;

public class XdfWriterTest {

	@Test
	public void xdfWriteTest() throws Exception {
		SdfFile file = getTestFile();
		File writtenXmlFile = File.createTempFile("test_xdf_writer", "xml");
		// write the file based on the input
		new XdfWriter(file).write(writtenXmlFile.getAbsolutePath());

		// read the file back in
		SdfFile jaxbFile = readJaxb(writtenXmlFile);

		// verify the files match
		vefifyMatch(file, jaxbFile);
	}

	private void vefifyMatch(SdfFile file1, SdfFile file2) {
		assertEquals(file1.getFileFormatVersion(), file2.getFileFormatVersion());

		assertEquals(file1.getSdfSections().getLspIntegrityDefinition()
				.getIntegrityType(), file2.getSdfSections()
				.getLspIntegrityDefinition().getIntegrityType());
		assertEquals(file1.getSdfSections().getLspIntegrityDefinition()
				.getIntegrityValue(), file2.getSdfSections()
				.getLspIntegrityDefinition().getIntegrityValue());
		assertEquals(file1.getSdfSections().getSoftwareDescription()
				.getSoftwarePartnumber(), file2.getSdfSections()
				.getSoftwareDescription().getSoftwarePartnumber());
		assertEquals(file1.getSdfSections().getSoftwareDescription()
				.getSoftwareTypeDescription(), file2.getSdfSections()
				.getSoftwareDescription().getSoftwareTypeDescription());
		assertEquals(file1.getSdfSections().getSoftwareDescription()
				.getSoftwareTypeId(), file2.getSdfSections()
				.getSoftwareDescription().getSoftwareTypeId());

		assertEquals(file1.getSdfSections().getFileDefinitions().size(), file2
				.getSdfSections().getFileDefinitions().size());
		List<FileDefinition> file1FileDefs = file1.getSdfSections()
				.getFileDefinitions();
		List<FileDefinition> file2FileDefs = file2.getSdfSections()
				.getFileDefinitions();
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

		assertEquals(file1.getSdfSections().getThwDefinitions().size(), file2
				.getSdfSections().getThwDefinitions().size());
		List<ThwDefinition> file1ThwDefs = file1.getSdfSections()
				.getThwDefinitions();
		List<ThwDefinition> file2ThwDefs = file2.getSdfSections()
				.getThwDefinitions();
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
		SdfSections swDefSects = new SdfSections();
		IntegrityDefinition lspInteg = new IntegrityDefinition();
		lspInteg.setIntegrityType(2);
		lspInteg.setIntegrityValue("DEADBEEF");
		IntegrityDefinition sdfInteg = new IntegrityDefinition();
		sdfInteg.setIntegrityType(2);
		sdfInteg.setIntegrityValue("DEADBEEF");

		swDefSects.setLspIntegrityDefinition(lspInteg);
		swDefSects.setSdfIntegrityDefinition(sdfInteg);

		SoftwareDescription swDesc = new SoftwareDescription();
		swDesc.setSoftwarePartnumber("1234");
		swDesc.setSoftwareTypeDescription("type");
		swDesc.setSoftwareTypeId(2);

		swDefSects.setSoftwareDescription(swDesc);

		swDefFile.setFileFormatVersion(SoftwareDefinitionFileBuilder.DEFAULT_FILE_FORMAT_VERSION);
		swDefFile.setSdfSections(swDefSects);
		return swDefFile;
	}
}
