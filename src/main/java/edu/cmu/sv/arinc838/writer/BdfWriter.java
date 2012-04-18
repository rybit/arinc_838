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

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.builder.BuilderFactory;
import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;

public class BdfWriter implements SdfWriter {
	@Override
	public void write(String path, SoftwareDefinitionFileDao sdfDao)
			throws Exception {
		File fileOnDisk = new File(path + sdfDao.getBinaryFileName());
		BdfFile file = new BdfFile(fileOnDisk);
		// This file must be empty
		file.setLength(0);
		SoftwareDefinitionFileBuilder builder = new SoftwareDefinitionFileBuilder(
				new BuilderFactory());
		write(file, builder, sdfDao);

	}

	public void write(BdfFile file, SoftwareDefinitionFileBuilder builder,
			SoftwareDefinitionFileDao sdfDao) throws Exception {
		builder.buildBinary(sdfDao, file);
		file.close();
	}

	@Override
	public String getFilename(SoftwareDefinitionFileDao sdfDao) {
		return sdfDao.getBinaryFileName();
	}
}
