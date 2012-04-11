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

		String filename = this.writer.getFilename(sdfDao);
		this.writer.write(path, sdfDao);

		System.out.println("Wrote file to " + filename);

		return super.getEmptyItems();
	}
}
