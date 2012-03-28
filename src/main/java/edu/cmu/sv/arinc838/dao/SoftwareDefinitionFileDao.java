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
import java.util.Arrays;
import java.util.List;

import com.arinc.arinc838.FileDefinition;
import com.arinc.arinc838.SdfFile;
import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.util.Converter;
import edu.cmu.sv.arinc838.validation.DataValidator;

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

	public SoftwareDefinitionFileDao() {
		;
	}

	public SoftwareDefinitionFileDao(SdfFile swDefFile) {
		this.initialize(swDefFile);
	}

	public SoftwareDefinitionFileDao(BdfFile bdfFile) throws IOException {
		this.initialize(bdfFile);
	}

	@SuppressWarnings("unchecked")
	public void initialize(SdfFile swDefFile) {
		List<FileDefinition> fileDefs = (List<FileDefinition>) DataValidator.validateList1(swDefFile
				.getFileDefinitions());

		fileDefinitions.clear();
		for (FileDefinition fileDef : fileDefs) {
			fileDefinitions.add(new FileDefinitionDao(fileDef));
		}

		thwDefinitions.clear();
		for (ThwDefinition thwDef : swDefFile.getThwDefinitions()) {
			thwDefinitions.add(new TargetHardwareDefinitionDao(thwDef));
		}

		softwareDescription = new SoftwareDescriptionDao(swDefFile.getSoftwareDescription());
		lspIntegrityDefinition = new IntegrityDefinitionDao(swDefFile.getLspIntegrityDefinition());
		sdfIntegrityDefinition = new IntegrityDefinitionDao(swDefFile.getSdfIntegrityDefinition());
	}

	public void initialize(BdfFile file) throws IOException {

		file.seek(BdfFile.FILE_FORMAT_VERSION_LOCATION);
		byte[] fileFormatVersion = file.readHexbin32();
		if (!Arrays.equals(fileFormatVersion, SoftwareDefinitionFileDao.DEFAULT_FILE_FORMAT_VERSION)) {
			throw new IllegalArgumentException("File format not recognized. Expected: "
					+ SoftwareDefinitionFileDao.DEFAULT_FILE_FORMAT_VERSION + " Got: " + fileFormatVersion);
		}

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

	public byte[] getFileFormatVersion() {
		return DEFAULT_FILE_FORMAT_VERSION;
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
