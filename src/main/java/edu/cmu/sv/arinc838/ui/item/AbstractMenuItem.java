/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 18, 2012
 */
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
