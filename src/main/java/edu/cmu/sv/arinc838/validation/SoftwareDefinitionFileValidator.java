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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.crc.CrcCalculator;
import edu.cmu.sv.arinc838.dao.FileDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao.IntegrityType;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.dao.SoftwareDescriptionDao;
import edu.cmu.sv.arinc838.dao.TargetHardwareDefinitionDao;
import edu.cmu.sv.arinc838.util.Converter;

public class SoftwareDefinitionFileValidator {

	private DataValidator dataVal;

	public SoftwareDefinitionFileValidator(DataValidator dataVal) {
		this.dataVal = dataVal;
	}

	/**
	 * Validates that the header conforms to the spec. This includes the
	 * attributes (e.g. schemaLocation) the namespace (e.g. xsi, sdf) and the
	 * encoding/version (utf-8, 1.0).
	 * 
	 * @param xmlFile
	 * @return
	 */
	public List<Exception> validateXmlFileHeader(File xmlFile) {
		XMLStreamReader xsr;
		List<Exception> errors = new ArrayList<Exception>();

		try {
			xsr = XMLInputFactory.newInstance().createXMLStreamReader(
					new FileInputStream(xmlFile));
			xsr.nextTag(); // move to the root
		} catch (Exception e) {
			errors.add(e);
			return errors; // can't work if the xsr can't be created
		}

		if (!DataValidator.XML_ENCODING.equalsIgnoreCase(xsr
				.getCharacterEncodingScheme())) {
			Exception e = new IllegalArgumentException(
					"The XML Encoding is wrong." + "Expected: "
							+ DataValidator.XML_ENCODING + " found: "
							+ xsr.getCharacterEncodingScheme());
			errors.add(e);
		}
		if (!DataValidator.XML_VERSION.equalsIgnoreCase(xsr.getVersion())) {
			Exception e = new IllegalArgumentException(
					"The XML version is wrong." + "Expected: "
							+ DataValidator.XML_VERSION + " found: "
							+ xsr.getVersion());
			errors.add(e);
		}

		// attribute check
		errors.addAll(dataVal.validateXmlHeaderAttributes(xsr));

		// namespace check
		errors.addAll(dataVal.validateXmlHeaderNamespaces(xsr));

		return errors;
	}

	public List<Exception> validateSdfFile(SoftwareDefinitionFileDao sdfDao,
			String sourceFile, BdfFile bdfFile) {
		List<Exception> errors = new ArrayList<Exception>();

		try {
			dataVal.validateFileFormatVersion(sdfDao.getFileFormatVersion());
		} catch (IllegalArgumentException e) {
			errors.add(e);
		}

		errors.addAll(validateSoftwareDescription(
				sdfDao.getSoftwareDescription(), sourceFile));
		errors.addAll(validateTargetHardwareDefinitions(
				sdfDao.getTargetHardwareDefinitions(), sourceFile));
		errors.addAll(validateFileDefinitions(sdfDao.getFileDefinitions()));
		errors.addAll(validateSdfIntegrityDefinition(sdfDao, bdfFile));
		errors.addAll(validateLspIntegrityDefinition(sdfDao, bdfFile));

		return errors;
	}

	public List<Exception> validateSoftwareDescription(
			SoftwareDescriptionDao softwareDesc, String sourceFile) {
		List<Exception> errors = new ArrayList<Exception>();

		try {
			dataVal.validateSoftwarePartNumber(softwareDesc
					.getSoftwarePartnumber());
		} catch (IllegalArgumentException e) {
			errors.add(e);
		}
		String partNumberAsFile = softwareDesc.getSoftwarePartnumber().replace(
				"-", "");
		String partNumberAsXDF = partNumberAsFile + ".XDF";
		String partNumberAsBDF = partNumberAsFile + ".BDF";
		if (!partNumberAsXDF.equals(sourceFile)
				&& !partNumberAsBDF.equals(sourceFile)) {
			errors.add(new IllegalArgumentException(
					"Source file name did not match software part number. File name was '"
							+ sourceFile + "', expected '" + partNumberAsXDF
							+ "' or '" + partNumberAsBDF + "'."));
		}

		errors.addAll(validateStr64k(softwareDesc.getSoftwareTypeDescription(),
				sourceFile));
		try {
			dataVal.validateHexbin32(softwareDesc.getSoftwareTypeId());
		} catch (IllegalArgumentException e) {
			errors.add(e);
		}

		return errors;
	}

	private List<Exception> validateStr64k(String value, String sourceFile) {
		String extension = sourceFile.split("\\.")[1];

		if (extension.equals("XDF")) {
			return dataVal.validateStr64kXml(value);
		} else {
			return dataVal.validateStr64kBinary(value);
		}
	}

	public List<Exception> validateTargetHardwareDefinitions(
			List<TargetHardwareDefinitionDao> thwDefs, String sourceFile) {
		List<Exception> errors = new ArrayList<Exception>();

		for (TargetHardwareDefinitionDao thwDef : thwDefs) {
			if (thwDef.getPositions() == null) {
				continue;
			}
			for (String position : thwDef.getPositions()) {
				errors.addAll(validateStr64k(position, sourceFile));
			}
		}

		return errors;
	}

	public List<Exception> validateFileDefinitions(
			List<FileDefinitionDao> fileDefs) {
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

	public List<Exception> validateSdfIntegrityDefinition(
			SoftwareDefinitionFileDao sdf, BdfFile bdf) {
		List<Exception> errors = new ArrayList<Exception>();

		errors.addAll(validateIntegrityDefStructure(sdf
				.getSdfIntegrityDefinition()));

		try {
			CrcValidator.validateSdfCrc(sdf, bdf);
		} catch (Exception e) {
			errors.add(e);
		}

		return errors;
	}

	public List<Exception> validateLspIntegrityDefinition(
			SoftwareDefinitionFileDao sdf, BdfFile bdf) {
		List<Exception> errors = new ArrayList<Exception>();

		errors.addAll(validateIntegrityDefStructure(sdf
				.getLspIntegrityDefinition()));

		try {
			CrcValidator.validateLspCrc(sdf, bdf);
		} catch (Exception e) {
			errors.add(e);
		}

		return errors;
	}

	public List<Exception> validateFileDefinition(FileDefinitionDao fileDef) {
		List<Exception> errors = new ArrayList<Exception>();

		errors.addAll(dataVal.validateDataFileName(fileDef.getFileName()));

		try {
			dataVal.validateUint32(fileDef.getFileSize());
		} catch (IllegalArgumentException e) {
			errors.add(e);
		}

		byte[] data = null;

		// TODO we need to handle paths correctly since all we have is the file
		// name
		try {
			data = CrcCalculator.readFile(new File(fileDef.getFileName()));

		} catch (IOException e) {
			errors.add(e);
			data = null; // don't validate CRC if there was an error reading the
							// file
		}

		errors.addAll(validateIntegrityDefinition(
				fileDef.getFileIntegrityDefinition(), data));

		return errors;
	}

	public List<Exception> validateIntegrityDefinition(
			IntegrityDefinitionDao integDef, byte[] data) {
		List<Exception> errors = new ArrayList<Exception>();

		errors.addAll(validateIntegrityDefStructure(integDef));

		if (data != null) {
			try {
				IntegrityType type = IntegrityType.fromLong(integDef
						.getIntegrityType());

				long value = Converter.checksumBytesToLong(integDef);

				switch (type) {
				case CRC16:
					CrcValidator.validateCrc16((int) value, data);
					break;
				case CRC32:
					CrcValidator.validateCrc32(value, data);
					break;
				case CRC64:
					CrcValidator.validateCrc64(value, data);
					break;
				default:
					errors.add(new IllegalArgumentException(
							"Invalid integrity type "
									+ integDef.getIntegrityType()));

				}

			} catch (Exception e) {
				errors.add(e);
			}
		}

		return errors;
	}

	protected List<Exception> validateIntegrityDefStructure(
			IntegrityDefinitionDao integDef) {
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
