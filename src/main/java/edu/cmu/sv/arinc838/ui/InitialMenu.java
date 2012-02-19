package edu.cmu.sv.arinc838.ui;

import edu.cmu.sv.arinc838.ui.item.ExitItem;
import edu.cmu.sv.arinc838.ui.item.MenuItem;
import edu.cmu.sv.arinc838.ui.item.XmlReadItem;

public class InitialMenu implements Menu {
	private static final MenuItem[] items = new MenuItem[] { 
		new XmlReadItem ("Read in Xml File"),
		new ExitItem()
	};
	
	@Override
	public MenuItem[]getItems() {
		return items;
	}

	@Override
	public String getHeader() {
		return null;
	}
}
