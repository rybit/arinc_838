package edu.cmu.sv.arinc838.ui;

import edu.cmu.sv.arinc838.ui.item.ExitItem;
import edu.cmu.sv.arinc838.ui.item.MenuItem;
import edu.cmu.sv.arinc838.ui.item.XmlSaveItem;

public class XmlFileMenu implements Menu {

	private final String fileName;

	public XmlFileMenu(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public MenuItem[] getItems() {
		return new MenuItem[] { new XmlSaveItem("Save current file."), new ExitItem() };
	}

	@Override
	public String getHeader() {
		return "File loaded: " + fileName;
	}

}
