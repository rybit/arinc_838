package edu.cmu.sv.arinc838.ui;

import edu.cmu.sv.arinc838.reader.BdfReader;
import edu.cmu.sv.arinc838.reader.XdfReader;
import edu.cmu.sv.arinc838.ui.item.ExitItem;
import edu.cmu.sv.arinc838.ui.item.MenuItem;
import edu.cmu.sv.arinc838.ui.item.SaveItem;
import edu.cmu.sv.arinc838.ui.item.SdfReadItem;
import edu.cmu.sv.arinc838.writer.BdfWriter;
import edu.cmu.sv.arinc838.writer.XdfWriter;

public class InitialMenu implements Menu {
	private static final MenuItem[] items = new MenuItem[] { 
		new SdfReadItem ("Read in Xml File", new XdfReader()),
		new SdfReadItem("Read in Binary File", new BdfReader()),
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
