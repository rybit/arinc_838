/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 11, 2012
 */
package edu.cmu.sv.arinc838.builder;

import com.arinc.arinc838.SdfFile;

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
