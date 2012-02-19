package edu.cmu.sv.arinc838.ui;

import edu.cmu.sv.arinc838.ui.item.MenuItem;

public interface Menu {
	public MenuItem[] getItems ();
	public String getHeader ();
}
