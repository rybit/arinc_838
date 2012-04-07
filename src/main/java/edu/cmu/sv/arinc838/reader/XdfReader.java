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
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.arinc.arinc838.SdfFile;

import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.validation.DataValidator;
import edu.cmu.sv.arinc838.validation.SoftwareDefinitionFileValidator;

public class XdfReader implements SdfReader {

	private SoftwareDefinitionFileValidator validator;
	
	public XdfReader() {
		validator = new SoftwareDefinitionFileValidator(new DataValidator());
	}
	
	@Override
	public SoftwareDefinitionFileDao read(String filename,
			List<Exception> errorList) {
		File file = new File(filename);

		SoftwareDefinitionFileDao sdfDao = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(SdfFile.class);

			Unmarshaller jaxbMarshaller = jaxbContext.createUnmarshaller();

			SdfFile jaxbFile = (SdfFile) jaxbMarshaller.unmarshal(file);

			sdfDao = new SoftwareDefinitionFileDao(jaxbFile);
			
			if(errorList != null) {
				errorList.addAll(validator.validateSdfFile(sdfDao, file.getName()));
			}
			
		} catch (Exception e) {
			if (errorList != null) {
				errorList.add(e);
			}
		}

		return sdfDao;
	}

}
