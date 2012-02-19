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

import java.io.BufferedReader;
import java.io.InputStreamReader;

import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;
import edu.cmu.sv.arinc838.ui.Menu;
import edu.cmu.sv.arinc838.xml.XdfWriter;

public class XmlSaveItem extends AbstractMenuItem {	
	public XmlSaveItem(String prompt) {
		super(prompt);
	}

	@Override
	public Menu execute(SoftwareDefinitionFileBuilder builder) throws Exception {
		System.out.print ("Save where? ");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String ret = br.readLine();
		
		XdfWriter writer = new XdfWriter(builder.build());
		
		writer.write(ret);
		
		System.out.println("File saved to "+ret);
		
		return null;
	}
}
