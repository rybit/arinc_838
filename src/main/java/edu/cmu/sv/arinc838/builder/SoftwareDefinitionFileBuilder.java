package edu.cmu.sv.arinc838.builder;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.arinc.arinc838.SdfFile;

public class SoftwareDefinitionFileBuilder implements Builder<SdfFile> {

	private String fileFormatVersion;
	private SoftwareDefinitionSectionsBuilder sections;

	public SoftwareDefinitionFileBuilder(SdfFile swDefFile) {
		this.initialize(swDefFile);
	}

	public SoftwareDefinitionFileBuilder() {
		;
	}

	public void initialize(SdfFile swDefFile) {
		fileFormatVersion = swDefFile.getFileFormatVersion();
		sections = new SoftwareDefinitionSectionsBuilder(
				swDefFile.getSdfSections());
	}

	public String getFileFormatVersion() {
		return fileFormatVersion;
	}

	public void setFileFormatVersion(String value) {
		this.fileFormatVersion = value;
	}

	public SoftwareDefinitionSectionsBuilder getSoftwareDefinitionSections() {
		return sections;
	}

	public void setSoftwareDefinitionSections(
			SoftwareDefinitionSectionsBuilder value) {
		this.sections = value;
	}

	@Override
	public SdfFile build() {
		SdfFile file = new SdfFile();
		file.setFileFormatVersion(this.getFileFormatVersion());
		file.setSdfSections(sections.build());

		return file;
	}
}
