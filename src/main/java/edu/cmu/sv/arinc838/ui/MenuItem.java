package edu.cmu.sv.arinc838.ui;

import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;

public interface MenuItem {
	public Menu execute (SoftwareDefinitionFileBuilder builder) throws Exception;
	public String getPrompt ();
}
