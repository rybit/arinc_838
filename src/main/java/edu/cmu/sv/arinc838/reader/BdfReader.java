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

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;

public class BdfReader implements SdfReader {

	@Override
	public SoftwareDefinitionFileDao read(String sdfFile) throws Exception {
		BdfFile bdf = new BdfFile(new File(sdfFile));
		
		SoftwareDefinitionFileDao sdfDao = new SoftwareDefinitionFileDao(bdf);
		
		return sdfDao;
	}

}
