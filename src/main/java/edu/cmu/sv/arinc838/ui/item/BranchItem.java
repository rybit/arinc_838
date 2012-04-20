package edu.cmu.sv.arinc838.ui.item;

import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.ui.Menu;

public class BranchItem extends AbstractMenuItem{
	private Menu menu;

	public BranchItem (String prompt, Menu menu) {
		super (prompt);
		this.menu = menu;
	}
	
	@Override
	public MenuItem[] execute(SoftwareDefinitionFileDao sdfDao) throws Exception {
		return menu.getItems();
	}
}
