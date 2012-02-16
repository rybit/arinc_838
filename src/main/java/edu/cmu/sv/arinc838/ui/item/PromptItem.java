package edu.cmu.sv.arinc838.ui.item;

import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;
import edu.cmu.sv.arinc838.ui.Menu;
import edu.cmu.sv.arinc838.ui.MenuItem;

public class PromptItem implements MenuItem {
	private String prompt;
	private Menu subMenu;
	
	public PromptItem (String prompt, Menu subMenu) {
		this.prompt = prompt;
		this.subMenu = subMenu;
	}
	

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public Menu getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(Menu subMenu) {
		this.subMenu = subMenu;
	}

	@Override
	public String getPrompt() {
		return prompt;
	}

	@Override
	public Menu execute(SoftwareDefinitionFileBuilder builder) {
		return subMenu;
	}
}
