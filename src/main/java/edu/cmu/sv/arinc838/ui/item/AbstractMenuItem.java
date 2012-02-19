package edu.cmu.sv.arinc838.ui.item;

public abstract class AbstractMenuItem implements MenuItem{
	private String prompt;
	AbstractMenuItem (String prompt) {
		this.prompt = prompt;
	}
	
	@Override
	public String getPrompt () {
		return prompt;
	}
}
