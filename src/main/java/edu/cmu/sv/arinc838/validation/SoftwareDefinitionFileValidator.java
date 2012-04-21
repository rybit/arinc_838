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
import edu.cmu.sv.arinc838.validation.CrcValidator.ChecksumValidationException;

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
	 * @throws Exception
	 */
	public List<Exception> validateXmlFileHeader(File xmlFile) throws Exception {
		XMLStreamReader xsr = null;
		FileInputStream fileInputStream = null;
		List<Exception> errors = new ArrayList<Exception>();

		try {
			fileInputStream = new FileInputStream(xmlFile);
			xsr = XMLInputFactory.newInstance().createXMLStreamReader(
					fileInputStream);
			xsr.nextTag(); // move to the root
		} catch (Exception e) {
			if (xsr != null) {
				xsr.close();
			}
			if (fileInputStream != null) {
				fileInputStream.close();
			}
			errors.add(e);
			return errors; // can't work if the xsr can't be created
		}

		// attribute check
		errors.addAll(dataVal.validateXmlHeaderAttributes(xsr));

		// namespace check
		errors.addAll(dataVal.validateXmlHeaderNamespaces(xsr));

		xsr.close();
		fileInputStream.close();
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

		boolean isXdfFile = sourceFile.split("\\.")[1].equals("XDF");

		errors.addAll(validateSoftwareDescription(
				sdfDao.getSoftwareDescription(), sourceFile));
		errors.addAll(validateTargetHardwareDefinitions(
				sdfDao.getTargetHardwareDefinitions(), sourceFile));
		errors.addAll(validateFileDefinitions(sdfDao.getFileDefinitions(),
				sdfDao, isXdfFile));
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
			List<FileDefinitionDao> fileDefs, SoftwareDefinitionFileDao sdfDao,
			boolean isXdf) {
		List<Exception> errors = new ArrayList<Exception>();

		try {
			dataVal.validateList1(fileDefs);
		} catch (IllegalArgumentException e) {
			errors.add(e);
		}

		errors.addAll(dataVal.validateDataFileNamesAreUnique(fileDefs));

		for (FileDefinitionDao fileDef : fileDefs) {
			errors.addAll(validateFileDefinition(fileDef, sdfDao, isXdf));
		}

		return errors;
	}

	public List<Exception> validateSdfIntegrityDefinition(
			SoftwareDefinitionFileDao sdf, BdfFile bdf) {
		List<Exception> errors = new ArrayList<Exception>();

		errors.addAll(validateIntegrityDefStructure(sdf
				.getSdfIntegrityDefinition()));

		if (bdf != null) {
			try {
				CrcValidator.validateSdfCrc(sdf, bdf);
			} catch (Exception e) {
				errors.add(e);
			}
		}

		return errors;
	}

	public List<Exception> validateLspIntegrityDefinition(
			SoftwareDefinitionFileDao sdf, BdfFile bdf) {
		List<Exception> errors = new ArrayList<Exception>();

		errors.addAll(validateIntegrityDefStructure(sdf
				.getLspIntegrityDefinition()));

		if (bdf != null) {
			try {
				CrcValidator.validateLspCrc(sdf, bdf);
			} catch (Exception e) {
				errors.add(e);
			}
		}

		return errors;
	}

	public List<Exception> validateFileDefinition(FileDefinitionDao fileDef,
			SoftwareDefinitionFileDao sdfDao, boolean isXdf) {
		List<Exception> errors = new ArrayList<Exception>();

		errors.addAll(dataVal.validateDataFileName(fileDef.getFileName()));

		try {
			dataVal.validateUint32(fileDef.getFileSize());
		} catch (IllegalArgumentException e) {
			IllegalArgumentException exceptionToAdd = new IllegalArgumentException(
					"Checksum for file " + fileDef.getFileName()
							+ " was invalid! Error was '" + e.getMessage()
							+ "'.");
			errors.add(exceptionToAdd);
		}

		byte[] data = null;

		if (!isXdf) {
			try {
				data = CrcCalculator.readFile(new File(sdfDao.getPath(),
						fileDef.getFileName()));

			} catch (IOException e) {
				IOException exceptionToAdd = new IOException(
						"Error reading file " + fileDef.getFileName()
								+ "! Error was '" + e.getMessage() + "'.");
				errors.add(exceptionToAdd);
				data = null; // don't validate CRC if there was an error reading
								// the
								// file
			}
		}

		List<Exception> integDefErrors = validateIntegrityDefinition(
				fileDef.getFileIntegrityDefinition(), data);
		if (integDefErrors != null) {

			for (Exception e : integDefErrors) {
				Exception exceptionToAdd = e;
				if (e.getClass() == ChecksumValidationException.class) {
					exceptionToAdd = new ChecksumValidationException(
							"Checksum for file " + fileDef.getFileName()
									+ " was invalid! Error was '"
									+ e.getMessage() + "'.");
				} else if (e.getClass() == IllegalArgumentException.class) {
					exceptionToAdd = new IllegalArgumentException(
							"Checksum for file " + fileDef.getFileName()
									+ " was invalid! Error was '"
									+ e.getMessage() + "'.");
				}

				errors.add(exceptionToAdd);
			}
		}

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
