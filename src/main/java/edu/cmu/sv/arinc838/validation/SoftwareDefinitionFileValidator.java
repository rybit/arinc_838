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

import java.util.List;

import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.dao.FileDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.dao.SoftwareDescriptionDao;
import edu.cmu.sv.arinc838.dao.TargetHardwareDefinitionDao;

public class SoftwareDefinitionFileValidator {

	public SoftwareDefinitionFileValidator(DataValidator dataVal) {
		// TODO Auto-generated constructor stub
	}

	public void validateSdfFile(SoftwareDefinitionFileDao sdfDao) {
		validateSoftwareDescription(sdfDao.getSoftwareDescription());
		validateTargetHardwareDefinitions(sdfDao.getTargetHardwareDefinitions());
		validateFileDefinitions(sdfDao.getFileDefinitions());
		validateSdfIntegrityDefinition(sdfDao.getSdfIntegrityDefinition());
		validateLspIntegrityDefinition(sdfDao.getLspIntegrityDefinition());
	}
	
	
	public List<Exception> validateSoftwareDescription(SoftwareDescriptionDao softwareDesc)
	{
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
