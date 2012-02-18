package edu.cmu.sv.arinc838.ui.item;

import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;
import edu.cmu.sv.arinc838.ui.Menu;

public interface MenuItem {
	public Menu execute (SoftwareDefinitionFileBuilder builder) throws Exception;
	public String getPrompt ();
}
