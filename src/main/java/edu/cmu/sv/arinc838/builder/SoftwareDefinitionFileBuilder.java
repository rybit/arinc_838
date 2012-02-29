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
import java.util.ArrayList;
import java.util.List;

import com.arinc.arinc838.FileDefinition;
import com.arinc.arinc838.SdfFile;
import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.validation.DataValidator;

public class SoftwareDefinitionFileBuilder implements Builder<SdfFile> {

	/**
	 * The default file format version as defined in the spec. Value is {@value}
	 */
	public static final long DEFAULT_FILE_FORMAT_VERSION = 528384;

	private List<FileDefinitionBuilder> fileDefinitions = new ArrayList<FileDefinitionBuilder>();
	private List<TargetHardwareDefinitionBuilder> thwDefinitions = new ArrayList<TargetHardwareDefinitionBuilder>();
	private SoftwareDescriptionBuilder softwareDescription;
	private IntegrityDefinitionBuilder lspIntegrityDefinition;
	private IntegrityDefinitionBuilder sdfIntegrityDefinition;
	public SoftwareDefinitionFileBuilder(SdfFile swDefFile) {
		this.initialize(swDefFile);
	}

	public SoftwareDefinitionFileBuilder() {
		;
	}

	@SuppressWarnings("unchecked")
	public void initialize(SdfFile swDefFile) {
		
		//TODO Write a test to verify that the file matches the final
		//fileFormatVersion = swDefFile.getFileFormatVersion();
		List<FileDefinition> fileDefs = (List<FileDefinition>) DataValidator
				.validateList1(swDefFile.getFileDefinitions());

		for (FileDefinition fileDef : fileDefs) {
			fileDefinitions.add(new FileDefinitionBuilder(fileDef));
		}

		for (ThwDefinition thwDef : swDefFile.getThwDefinitions()) {
			thwDefinitions.add(new TargetHardwareDefinitionBuilder(thwDef));
		}

		softwareDescription = new SoftwareDescriptionBuilder(
				swDefFile.getSoftwareDescription());
		lspIntegrityDefinition = new IntegrityDefinitionBuilder(
				swDefFile.getLspIntegrityDefinition());
		sdfIntegrityDefinition = new IntegrityDefinitionBuilder(
				swDefFile.getSdfIntegrityDefinition());
	}

	public long getFileFormatVersion() {
		return DEFAULT_FILE_FORMAT_VERSION;
	}


	public SoftwareDescriptionBuilder getSoftwareDescription() {
		return softwareDescription;
	}

	public void setSoftwareDescription(SoftwareDescriptionBuilder sd) {
		softwareDescription = sd;
	}

	public List<TargetHardwareDefinitionBuilder> getTargetHardwareDefinitions() {
		return thwDefinitions;
	}

	public List<FileDefinitionBuilder> getFileDefinitions() {
		return fileDefinitions;
	}

	public IntegrityDefinitionBuilder getSdfIntegrityDefinition() {
		return this.sdfIntegrityDefinition;
	}

	public void setSdfIntegrityDefinition(IntegrityDefinitionBuilder value) {
		this.sdfIntegrityDefinition = value;
	}

	public IntegrityDefinitionBuilder getLspIntegrityDefinition() {
		return this.lspIntegrityDefinition;
	}

	public void setLspIntegrityDefinition(IntegrityDefinitionBuilder value) {
		this.lspIntegrityDefinition = value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SdfFile buildXml() {
		SdfFile file = new SdfFile();
		file.setFileFormatVersion(this.getFileFormatVersion());

		// we have to re-validate this as a LIST1 since it can be modified
		// without a set method to verify its validity prior to building
		List<FileDefinitionBuilder> fileDefsValidated = (List<FileDefinitionBuilder>) DataValidator
				.validateList1(fileDefinitions);
		for (FileDefinitionBuilder fileDef : fileDefsValidated) {
			file.getFileDefinitions().add(fileDef.buildXml());
		}

		for (TargetHardwareDefinitionBuilder thwDef : thwDefinitions) {
			file.getThwDefinitions().add(thwDef.buildXml());
		}

		file.setLspIntegrityDefinition(lspIntegrityDefinition.buildXml());
		file.setSdfIntegrityDefinition(sdfIntegrityDefinition.buildXml());
		file.setSoftwareDescription(softwareDescription.buildXml());
		
		return file;
	}

	@Override
	public int buildBinary(BdfFile file) throws IOException {
		file.writePlaceholder();
		file.writeFileFormatVersion(getFileFormatVersion());
		file.writePlaceholder();
		file.writePlaceholder();
		file.writePlaceholder();
		file.writePlaceholder();
		file.writePlaceholder();
		this.getSoftwareDescription().buildBinary(file);
	
		for (int i=0; i<this.getTargetHardwareDefinitions().size(); i++) {
			this.getTargetHardwareDefinitions().get(i).buildBinary(file);
		}
			
		for (int i=0; i<this.getFileDefinitions().size(); i++) {
			this.getFileDefinitions().get(i).buildBinary(file);
		}
		
		this.getSdfIntegrityDefinition().buildBinary(file);
		this.getLspIntegrityDefinition().buildBinary(file);
		
		file.seek(0);
		file.writeUint32(file.length());
		
	
		return 0;
	}
}
