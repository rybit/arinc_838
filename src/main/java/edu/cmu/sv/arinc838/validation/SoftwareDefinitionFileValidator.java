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

import java.util.ArrayList;
import java.util.List;

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

	public List<Exception> validateSdfFile(SoftwareDefinitionFileDao sdfDao) {
		List<Exception> errors = new ArrayList<Exception>();

		try {
			dataVal.validateFileFormatVersion(sdfDao.getFileFormatVersion());
		} catch (IllegalArgumentException e) {
			errors.add(e);
		}

		errors.addAll(validateSoftwareDescription(sdfDao
				.getSoftwareDescription()));
		errors.addAll(validateTargetHardwareDefinitions(sdfDao
				.getTargetHardwareDefinitions()));
		errors.addAll(validateFileDefinitions(sdfDao.getFileDefinitions()));
		errors.addAll(validateSdfIntegrityDefinition(sdfDao
				.getSdfIntegrityDefinition()));
		errors.addAll(validateLspIntegrityDefinition(sdfDao
				.getLspIntegrityDefinition()));

		return errors;
	}

	public List<Exception> validateSoftwareDescription(
			SoftwareDescriptionDao softwareDesc) {
		return null;
	}

	public List<Exception> validateTargetHardwareDefinitions(
			List<TargetHardwareDefinitionDao> thwDefs) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Exception> validateFileDefinitions(
			List<FileDefinitionDao> fileDefs) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Exception> validateSdfIntegrityDefinition(
			IntegrityDefinitionDao sdfInteg) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Exception> validateLspIntegrityDefinition(
			IntegrityDefinitionDao lspInteg) {
		// TODO Auto-generated method stub
		return null;
	}

}
