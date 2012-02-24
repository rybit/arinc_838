/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 12, 2012
 */
package edu.cmu.sv.arinc838.builder;

import java.io.IOException;

import com.arinc.arinc838.SdfFile;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.validation.DataValidator;

public class SoftwareDefinitionFileBuilder implements Builder<SdfFile> {

	/**
	 * The default file format version as defined in the spec. Value is {@value}
	 */
	public static final long DEFAULT_FILE_FORMAT_VERSION = 528384;	
	public static final int BINARY_FILE_FORMAT_VERSION_LOCATION = 4;
	public static final int SOFTWARE_DESCRIPTION_POINTER_LOCATION = 8;
	public static final int TARGET_DEFINITIONS_POINTER_LOCATION = 12;
	public static final int FILE_DEFINITIONS_POINTER_LOCATION = 16;
	public static final int SDF_INTEGRITY_POINTER_LOCATION = 20;
	public static final int LSP_INTEGRITY_POINTER_LOCATION = 24;

	private long fileFormatVersion;
	private SoftwareDefinitionSectionsBuilder sections;

	public SoftwareDefinitionFileBuilder(SdfFile swDefFile) {
		this.initialize(swDefFile);
	}

	public SoftwareDefinitionFileBuilder() {
		;
	}

	public void initialize(SdfFile swDefFile) {
		fileFormatVersion = swDefFile.getFileFormatVersion();
		sections = new SoftwareDefinitionSectionsBuilder(swDefFile.getSdfSections());
	}

	public long getFileFormatVersion() {
		return fileFormatVersion;
	}

	public void setFileFormatVersion(long value) {
		fileFormatVersion = DataValidator.validateFileFormatVersion(value);
	}

	public SoftwareDefinitionSectionsBuilder getSoftwareDefinitionSections() {
		return sections;
	}

	public void setSoftwareDefinitionSections(
			SoftwareDefinitionSectionsBuilder value) {
		this.sections = value;
	}

	@Override
	public SdfFile buildXml() {
		SdfFile file = new SdfFile();
		file.setFileFormatVersion(this.getFileFormatVersion());
		file.setSdfSections(sections.buildXml());

		return file;
	}
	
	@Override
	public void buildBinary(BdfFile file) throws IOException {
		
		writeFileFormatVersion(file);
		
		sections.buildBinary(file);		
	}

	private void writeFileFormatVersion(BdfFile file) throws IOException {
		file.seek(BINARY_FILE_FORMAT_VERSION_LOCATION);
		file.writeUint32(this.fileFormatVersion);
	}
}
