/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 18, 2012
 */
package edu.cmu.sv.arinc838.ui;

import edu.cmu.sv.arinc838.ui.item.ExitItem;
import edu.cmu.sv.arinc838.ui.item.MenuItem;
import edu.cmu.sv.arinc838.ui.item.SaveItem;
import edu.cmu.sv.arinc838.writer.XdfWriter;

public class XmlFileMenu implements Menu {

	private final String fileName;

	public XmlFileMenu(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public MenuItem[] getItems() {
		return new MenuItem[] { new SaveItem("Save current file.", new XdfWriter ()), new ExitItem() };
	}

	@Override
	public String getHeader() {
		return "File loaded: " + fileName;
	}

}
