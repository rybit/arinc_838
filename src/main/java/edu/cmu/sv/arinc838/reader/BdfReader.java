/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Apr 3, 2012
 */
package edu.cmu.sv.arinc838.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.validation.DataValidator;
import edu.cmu.sv.arinc838.validation.SoftwareDefinitionFileValidator;

public class BdfReader implements SdfReader {

	private SoftwareDefinitionFileValidator validator;

	public BdfReader() {
		validator = new SoftwareDefinitionFileValidator(new DataValidator());
	}

	@Override
	public SoftwareDefinitionFileDao read(String filename,
			List<Exception> errorList) {
		BdfFile bdf = null;
		SoftwareDefinitionFileDao sdfDao = null;
		try {
			File file = new File(filename);
			bdf = new BdfFile(file);
			sdfDao = new SoftwareDefinitionFileDao(bdf);
			if (errorList != null) {
				errorList.addAll(validator.validateSdfFile(sdfDao,
						file.getName()));
			}
		} catch (Exception e) {
			if (errorList != null) {
				errorList.add(e);
			}
		}

		return sdfDao;
	}
}
