package edu.cmu.sv.arinc838.builder;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.arinc.arinc838.SdfFile;
import com.arinc.arinc838.SdfSections;

import edu.cmu.sv.arinc838.specification.SoftwareDefinitionFile;
import edu.cmu.sv.arinc838.specification.SoftwareDefinitionSections;
import edu.cmu.sv.arinc838.xml.XmlSoftwareDefinitionSections;

public class SoftwareDefinitionFileBuilder implements SoftwareDefinitionFile {

	private String fileFormatVersion;
	private SoftwareDefinitionSections sections;

	public SoftwareDefinitionFileBuilder(SdfFile swDefFile) {
		this.fileFormatVersion = swDefFile.getFileFormatVersion();
		this.sections = new XmlSoftwareDefinitionSections(swDefFile.getSdfSections());
	}

	@Override
	public String getFileFormatVersion() {
		return fileFormatVersion;
	}

	@Override
	public void setFileFormatVersion(String value) {
		this.fileFormatVersion = value;
	}

	@Override
	public SoftwareDefinitionSections getSoftwareDefinitionSections() {
		return sections;
	}

	@Override
	public void setSoftwareDefinitionSections(SoftwareDefinitionSections value) {
		this.sections = value;
	}

	public SdfFile build() {
		SdfFile file = new SdfFile();
		file.setFileFormatVersion(this.getFileFormatVersion());
		
		return file;
	}
}
