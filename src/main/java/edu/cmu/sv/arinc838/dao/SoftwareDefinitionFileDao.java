/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 12, 2012
 */
package edu.cmu.sv.arinc838.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.arinc.arinc838.FileDefinition;
import com.arinc.arinc838.SdfFile;
import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.util.Converter;

public class SoftwareDefinitionFileDao {

	/**
	 * The default file format version as defined in the spec. Value is {@value}
	 */
	public static final byte[] DEFAULT_FILE_FORMAT_VERSION = Converter.hexToBytes("00008100");

	private List<FileDefinitionDao> fileDefinitions = new ArrayList<FileDefinitionDao>();
	private List<TargetHardwareDefinitionDao> thwDefinitions = new ArrayList<TargetHardwareDefinitionDao>();
	private SoftwareDescriptionDao softwareDescription;
	private IntegrityDefinitionDao lspIntegrityDefinition;
	private IntegrityDefinitionDao sdfIntegrityDefinition;
	private byte[] fileFormatVersion = DEFAULT_FILE_FORMAT_VERSION;
	private String path;

	public SoftwareDefinitionFileDao() {
		;
	}

	public SoftwareDefinitionFileDao(SdfFile swDefFile, String path) {
		this.initialize(swDefFile);
		this.path = path;
	}

	public SoftwareDefinitionFileDao(BdfFile bdfFile, String path) throws IOException {
		this.initialize(bdfFile);
		this.path = path;
	}

	public void initialize(SdfFile swDefFile) {
		fileDefinitions.clear();
		for (FileDefinition fileDef : swDefFile.getFileDefinitions()) {
			fileDefinitions.add(new FileDefinitionDao(fileDef));
		}

		thwDefinitions.clear();
		for (ThwDefinition thwDef : swDefFile.getThwDefinitions()) {
			thwDefinitions.add(new TargetHardwareDefinitionDao(thwDef));
		}

		softwareDescription = new SoftwareDescriptionDao(swDefFile.getSoftwareDescription());
		lspIntegrityDefinition = new IntegrityDefinitionDao(swDefFile.getLspIntegrityDefinition());
		sdfIntegrityDefinition = new IntegrityDefinitionDao(swDefFile.getSdfIntegrityDefinition());
		fileFormatVersion = swDefFile.getFileFormatVersion();
	}
	
	public void initialize(SoftwareDefinitionFileDao sdfFile) {
		fileDefinitions.clear();
		for (FileDefinitionDao fileDef : sdfFile.getFileDefinitions()) {
			fileDefinitions.add(fileDef);
		}

		thwDefinitions.clear();
		for (TargetHardwareDefinitionDao thwDef : sdfFile.getTargetHardwareDefinitions()) {
			thwDefinitions.add(thwDef);
		}

		softwareDescription = sdfFile.getSoftwareDescription();
		lspIntegrityDefinition = sdfFile.getLspIntegrityDefinition();
		sdfIntegrityDefinition = sdfFile.getSdfIntegrityDefinition();		
		fileFormatVersion = sdfFile.getFileFormatVersion();
	}

	public void initialize(BdfFile file) throws IOException {

		file.seek(BdfFile.FILE_FORMAT_VERSION_LOCATION);
		fileFormatVersion = file.readHexbin32();


		file.seek(file.readSoftwareDescriptionPointer());
		softwareDescription = new SoftwareDescriptionDao(file);

		this.getTargetHardwareDefinitions().clear();
		file.seek(file.readTargetDefinitionsPointer());
		long targetHardwareCount = file.readUint32();
		for (int i = 0; i < targetHardwareCount; i++) {
			long nextHardware = file.readUint32();
			TargetHardwareDefinitionDao hardware = new TargetHardwareDefinitionDao(file);
			this.getTargetHardwareDefinitions().add(hardware);
			file.seek(nextHardware);
		}

		this.getFileDefinitions().clear();
		file.seek(file.readFileDefinitionsPointer());
		long fileDefinitionCount = file.readUint32();
		for (int i = 0; i < fileDefinitionCount; i++) {
			long nextFile = file.readUint32();
			FileDefinitionDao fileDefinition = new FileDefinitionDao(file);
			this.getFileDefinitions().add(fileDefinition);
			file.seek(nextFile);
		}

		file.seek(file.readSdfIntegrityDefinitionPointer());
		this.setSdfIntegrityDefinition(new IntegrityDefinitionDao(file));

		file.seek(file.readLspIntegrityDefinitionPointer());
		this.setLspIntegrityDefinition(new IntegrityDefinitionDao(file));
	}
	
	public String getPath(){
		return path;
	}
	
	public void setPath(String value){
		path = value;
	}

	public byte[] getFileFormatVersion() {
		return fileFormatVersion;
	}
	
	public void setFileFormatVersion(byte[] fileFormatVersion) {
		this.fileFormatVersion = fileFormatVersion;
	}

	public SoftwareDescriptionDao getSoftwareDescription() {
		return softwareDescription;
	}

	public void setSoftwareDescription(SoftwareDescriptionDao sd) {
		softwareDescription = sd;
	}

	public List<TargetHardwareDefinitionDao> getTargetHardwareDefinitions() {
		return thwDefinitions;
	}

	public List<FileDefinitionDao> getFileDefinitions() {
		return fileDefinitions;
	}

	public IntegrityDefinitionDao getSdfIntegrityDefinition() {
		return this.sdfIntegrityDefinition;
	}

	public void setSdfIntegrityDefinition(IntegrityDefinitionDao value) {
		this.sdfIntegrityDefinition = value;
	}

	public IntegrityDefinitionDao getLspIntegrityDefinition() {
		return this.lspIntegrityDefinition;
	}

	public void setLspIntegrityDefinition(IntegrityDefinitionDao value) {
		this.lspIntegrityDefinition = value;
	}

	public String getBinaryFileName() {
		return getSoftwareDescription().getSoftwarePartnumber().replace("-", "") + ".BDF";
	}

	public String getXmlFileName() {
		return getSoftwareDescription().getSoftwarePartnumber().replace("-", "") + ".XDF";
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && this == obj
				|| (obj instanceof SoftwareDefinitionFileDao && equals((SoftwareDefinitionFileDao) obj));
	}

	public boolean equals(SoftwareDefinitionFileDao obj) {
		return obj != null
				&& this == obj
				|| (this.getSoftwareDescription().equals(obj.getSoftwareDescription())
						&& this.getFileFormatVersion().equals(obj.getFileFormatVersion())
						&& this.getTargetHardwareDefinitions().equals(obj.getTargetHardwareDefinitions())
						&& this.getFileDefinitions().equals(obj.getFileDefinitions())
						&& this.getSdfIntegrityDefinition().equals(obj.getSdfIntegrityDefinition()) && this
						.getLspIntegrityDefinition().equals(obj.getLspIntegrityDefinition()));
	}

	@Override
	public int hashCode() {
		if (this.getSoftwareDescription() != null && this.getSoftwareDescription().getSoftwarePartnumber() != null) {
			return this.getXmlFileName().hashCode();
		}
		return 0;
	}
}
