/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 18, 2012
 */
package edu.cmu.sv.arinc838.ui.item;

import java.io.File;

import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.writer.SdfWriter;

public class SaveItem extends AbstractMenuItem {
	private SdfWriter writer;

	public SaveItem(String prompt, SdfWriter writer) {
		super(prompt);
		this.writer = writer;
	}

	@Override
	public MenuItem[] execute(SoftwareDefinitionFileDao sdfDao) throws Exception {
		String path = promptForResponse("Save where? (path only, filename will be appended)");
		if (!path.endsWith(File.separator)) {
			path += File.separator;
		}

		File pathDir = new File (path);
		if (!pathDir.isDirectory() || !pathDir.canWrite()) {
			System.out.println ("Invalid path: " + pathDir.getAbsolutePath());
			return super.getEmptyItems();
		}
		
		String filename = this.writer.getFilename(sdfDao);
		File f = new File (path + filename);
		if (f.exists()) { 
			String overwrite = promptForResponse("The file: " + (path + filename) + " exists - do you want to over write it? [y/n]");
			if ("n".equalsIgnoreCase(overwrite)) {
				System.out.println ("Aborting write");
				return super.getEmptyItems();
			}
		}
		
		this.writer.write(path, sdfDao);
		System.out.println("Wrote file to " + path + filename);

		return super.getEmptyItems();
	}
}
