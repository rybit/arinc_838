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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.arinc.arinc838.SdfFile;

import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;

public class XdfReader implements SdfReader {

	@Override
	public SoftwareDefinitionFileDao read(String filename) throws Exception {
		File file = new File(filename);
		
		JAXBContext jaxbContext = JAXBContext.newInstance(SdfFile.class);		
		Unmarshaller jaxbMarshaller = jaxbContext.createUnmarshaller();
		
		SdfFile jaxbFile = (SdfFile) jaxbMarshaller.unmarshal(file);
				
		SoftwareDefinitionFileDao sdfDao = new SoftwareDefinitionFileDao(jaxbFile);
		
		
		return sdfDao;
	}

}
