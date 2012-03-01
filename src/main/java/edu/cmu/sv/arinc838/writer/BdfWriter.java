/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 28, 2012
 */
package edu.cmu.sv.arinc838.writer;

import java.io.File;
import java.io.IOException;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;

public class BdfWriter implements SdfWriter {

	@Override
	public String write(String path, SoftwareDefinitionFileBuilder builder)
			throws Exception {		
		
		File fileOnDisk =new File(path+builder.getBinaryFileName());
		
		BdfFile file = new BdfFile(fileOnDisk);
		
		write(file, builder);
		
		return fileOnDisk.getCanonicalPath();
	}
	
	public void write(BdfFile file, SoftwareDefinitionFileBuilder builder) throws IOException{
		builder.buildBinary(file);
		file.close();
	}
}
