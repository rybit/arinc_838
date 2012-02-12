package edu.cmu.sv.arinc838.builder;

import com.arinc.arinc838.SdfFile;
import com.arinc.arinc838.SdfSections;

public class SoftwareDefinitionFileBuilder implements Builder<SdfFile>{

	private String fileFormatVersion;
	private SdfSections sections;

	public SoftwareDefinitionFileBuilder(SdfFile swDefFile) {
		fileFormatVersion = swDefFile.getFileFormatVersion();
		sections = new SoftwareDefinitionSectionsBuilder(swDefFile.getSdfSections()).build();
	}

	public String getFileFormatVersion() {
		return fileFormatVersion;
	}

	public void setFileFormatVersion(String value) {
		this.fileFormatVersion = value;
	}

	public SdfSections getSoftwareDefinitionSections() {
		return sections;
	}

	public void setSoftwareDefinitionSections(SdfSections value) {
		this.sections = value;
	}

	@Override
	public SdfFile build() {
		SdfFile file = new SdfFile();
		file.setFileFormatVersion(this.getFileFormatVersion());
		file.setSdfSections(new SoftwareDefinitionSectionsBuilder(sections).build());
		
		return file;
	}
}
