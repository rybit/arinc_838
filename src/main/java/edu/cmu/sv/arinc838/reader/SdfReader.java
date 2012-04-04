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

import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;

public interface SdfReader {

	public abstract SoftwareDefinitionFileDao read(String sdfFile) throws Exception;

}