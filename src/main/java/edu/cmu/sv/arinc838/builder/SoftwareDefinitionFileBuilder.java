package edu.cmu.sv.arinc838.builder;

import com.arinc.arinc838.SdfFile;
import com.arinc.arinc838.SdfSections;

public class SoftwareDefinitionFileBuilder implements Builder<SdfFile>{

	private String fileFormatVersion;
	private SoftwareDefinitionSectionsBuilder sections;

	public SoftwareDefinitionFileBuilder(SdfFile swDefFile) {
		fileFormatVersion = swDefFile.getFileFormatVersion();
		sections = new SoftwareDefinitionSectionsBuilder(swDefFile.getSdfSections());
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

	public void setSoftwareDefinitionSections(SoftwareDefinitionSectionsBuilder value) {
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
