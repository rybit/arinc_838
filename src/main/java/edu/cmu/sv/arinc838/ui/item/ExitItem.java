package edu.cmu.sv.arinc838.ui.item;

import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;
import edu.cmu.sv.arinc838.ui.Menu;

public class ExitItem implements MenuItem {

	@Override
	public Menu execute(SoftwareDefinitionFileBuilder builder) throws Exception {
		return null;
	}

	@Override
	public String getPrompt() {
		return "Exit";
	}

}
