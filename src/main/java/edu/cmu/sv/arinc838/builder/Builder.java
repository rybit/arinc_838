/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php) 
 * 
 * Created on Feb 11, 2012
 */
package edu.cmu.sv.arinc838.builder;

import java.io.IOException;

import edu.cmu.sv.arinc838.binary.BdfFile;

public interface Builder<T>{
	
	public T buildXml();
	
	public int buildBinary(BdfFile file) throws IOException;

}
