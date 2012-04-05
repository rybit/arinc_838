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

import java.util.ArrayList;

import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.reader.BdfReader;

public class BinaryReadItem extends AbstractMenuItem {

	public BinaryReadItem(String prompt) {
		super(prompt);
	}

	@Override
	public MenuItem[] execute(SoftwareDefinitionFileDao sdfDao)
			throws Exception {
		String filename = promptForResponse("Which file?");

		BdfReader reader = new BdfReader();
		ArrayList<Exception> errorList = new ArrayList<Exception>();
		SoftwareDefinitionFileDao read = reader.read(filename, errorList);

		if (errorList.isEmpty()) {
			sdfDao.initialize(read);
			System.out.println("Successfully read in " + filename);
		} else {
			// TODO need to print out any errors, or write to log
			System.out.println("Failed to read in file '" + filename
					+ "'. Encountered " + errorList.size() + " errors.");
		}

		return super.getEmptyItems();
	}

}
