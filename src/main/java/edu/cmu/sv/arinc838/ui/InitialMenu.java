package edu.cmu.sv.arinc838.ui;

import edu.cmu.sv.arinc838.ui.item.*;
import edu.cmu.sv.arinc838.writer.BdfWriter;
import edu.cmu.sv.arinc838.writer.XdfWriter;

public class InitialMenu implements Menu {
	private static final MenuItem[] items = new MenuItem[] { 
		new XmlReadItem ("Read in Xml File"),
		new BinaryReadItem("Read in Binary File"),
		new SaveItem ("Save the file as XML", new XdfWriter ()),
		new SaveItem ("Save the file as Binary", new BdfWriter ()),
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
