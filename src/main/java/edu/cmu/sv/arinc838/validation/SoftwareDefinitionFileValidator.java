/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Mar 26, 2012
 */
package edu.cmu.sv.arinc838.validation;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import edu.cmu.sv.arinc838.dao.FileDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.dao.SoftwareDescriptionDao;
import edu.cmu.sv.arinc838.dao.TargetHardwareDefinitionDao;

public class SoftwareDefinitionFileValidator {

	private DataValidator dataVal;

	public SoftwareDefinitionFileValidator(DataValidator dataVal) {
		this.dataVal = dataVal;
	}

	/**
	 * Validates that the header conforms to the spec. This includes the attributes (e.g. schemaLocation)
	 * the namespace (e.g. xsi, sdf) and the encoding/version (utf-8, 1.0).
	 * 
	 * @param xmlFile
	 * @return
	 */
	public List<Exception> validateXmlFileHeader(File xmlFile) {
		XMLStreamReader xsr;
		List<Exception> errors = new ArrayList<Exception>();

		try {
			xsr = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(xmlFile));
			xsr.nextTag(); // move to the root
		} catch (Exception e) {
			errors.add(e);
			return errors; // can't work if the xsr can't be created
		}

		if (!DataValidator.XML_ENCODING.equalsIgnoreCase(xsr.getCharacterEncodingScheme())) {
			Exception e = new IllegalArgumentException("The XML Encoding is wrong." + 
					"Expected: " + DataValidator.XML_ENCODING + 
					" found: " + xsr.getCharacterEncodingScheme());
			errors.add(e);
		}
		if (!DataValidator.XML_VERSION.equalsIgnoreCase(xsr.getVersion())) {
			Exception e = new IllegalArgumentException("The XML version is wrong." + 
					"Expected: " + DataValidator.XML_VERSION + 
					" found: " + xsr.getVersion());
			errors.add(e);
		}

		// attribute check
		errors.addAll(dataVal.validateXmlHeaderAttributes(xsr));

		// namespace check
		errors.addAll(dataVal.validateXmlHeaderNamespaces(xsr));

		return errors;
	}

	public List<Exception> validateSdfFile(SoftwareDefinitionFileDao sdfDao, String sourceFile) {
		List<Exception> errors = new ArrayList<Exception>();

		try {
			dataVal.validateFileFormatVersion(sdfDao.getFileFormatVersion());
		} catch (IllegalArgumentException e) {
			errors.add(e);
		}

		errors.addAll(validateSoftwareDescription(sdfDao.getSoftwareDescription(), sourceFile));
		errors.addAll(validateTargetHardwareDefinitions(sdfDao.getTargetHardwareDefinitions()));
		errors.addAll(validateFileDefinitions(sdfDao.getFileDefinitions()));
		errors.addAll(validateSdfIntegrityDefinition(sdfDao.getSdfIntegrityDefinition()));
		errors.addAll(validateLspIntegrityDefinition(sdfDao.getLspIntegrityDefinition()));

		return errors;
	}

	public List<Exception> validateSoftwareDescription(SoftwareDescriptionDao softwareDesc, String sourceFile) {
		List<Exception> errors = new ArrayList<Exception>();

		try {
			dataVal.validateSoftwarePartNumber(softwareDesc.getSoftwarePartnumber());
		} catch (IllegalArgumentException e) {
			errors.add(e);
		}
		String partNumberAsFile = softwareDesc.getSoftwarePartnumber().replace("-", "");
		String partNumberAsXDF = partNumberAsFile + ".XDF";
		String partNumberAsBDF = partNumberAsFile + ".BDF";
		if (!partNumberAsXDF.equals(sourceFile) && !partNumberAsBDF.equals(sourceFile)) {
			errors.add(new IllegalArgumentException(
					"Source file name did not match software part number. File name was '"
							+ sourceFile + "', expected '" + partNumberAsXDF
							+ "' or '" + partNumberAsBDF + "'."));
		}

		errors.addAll(dataVal.validateStr64kXml(softwareDesc.getSoftwareTypeDescription()));
		try {
			dataVal.validateHexbin32(softwareDesc.getSoftwareTypeId());
		} catch (IllegalArgumentException e) {
			errors.add(e);
		}

		return errors;
	}

	public List<Exception> validateTargetHardwareDefinitions(List<TargetHardwareDefinitionDao> thwDefs) {
		List<Exception> errors = new ArrayList<Exception>();

		for (TargetHardwareDefinitionDao thwDef : thwDefs) {
			if (thwDef.getPositions() == null) {
				continue;
			}
			for (String position : thwDef.getPositions()) {
				errors.addAll(dataVal.validateStr64kXml(position));
			}
		}

		return errors;
	}

	public List<Exception> validateFileDefinitions(List<FileDefinitionDao> fileDefs) {
		List<Exception> errors = new ArrayList<Exception>();

		try {
			dataVal.validateList1(fileDefs);
		} catch (IllegalArgumentException e) {
			errors.add(e);
		}

		errors.addAll(dataVal.validateDataFileNamesAreUnique(fileDefs));

		for (FileDefinitionDao fileDef : fileDefs) {
			errors.addAll(validateFileDefinition(fileDef));
		}

		return errors;
	}

	public List<Exception> validateSdfIntegrityDefinition(IntegrityDefinitionDao sdfInteg) {
		return validateIntegrityDefinition(sdfInteg);
	}

	public List<Exception> validateLspIntegrityDefinition(IntegrityDefinitionDao lspInteg) {
		return validateIntegrityDefinition(lspInteg);
	}

	public List<Exception> validateFileDefinition(FileDefinitionDao fileDef) {
		List<Exception> errors = new ArrayList<Exception>();

		errors.addAll(dataVal.validateDataFileName(fileDef.getFileName()));

		try {
			dataVal.validateUint32(fileDef.getFileSize());
		} catch (IllegalArgumentException e) {
			errors.add(e);
		}

		errors.addAll(validateIntegrityDefinition(fileDef.getFileIntegrityDefinition()));

		return errors;
	}

	public List<Exception> validateIntegrityDefinition(IntegrityDefinitionDao integDef) {
		List<Exception> errors = new ArrayList<Exception>();

		try {
			dataVal.validateIntegrityType(integDef.getIntegrityType());
		} catch (IllegalArgumentException e) {
			errors.add(e);
		}

		try {
			dataVal.validateIntegrityValue(integDef.getIntegrityValue());
		} catch (IllegalArgumentException e) {
			errors.add(e);
		}

		return errors;
	}

}
