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
import java.io.File;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.arinc.arinc838.SdfFile;

import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;
import edu.cmu.sv.arinc838.ui.Menu;
import edu.cmu.sv.arinc838.ui.XmlFileMenu;

public class XmlReadItem  extends AbstractMenuItem {	
	public XmlReadItem(String prompt) {
		super(prompt);
	}
	
	@Override
	public Menu execute(SoftwareDefinitionFileBuilder builder) throws Exception {		
		System.out.print ("Which file? ");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String ret = br.readLine();
				
		File file = new File(ret);
				
		JAXBContext jaxbContext = JAXBContext.newInstance(SdfFile.class);		
		Unmarshaller jaxbMarshaller = jaxbContext.createUnmarshaller();
		
		SdfFile jaxbFile = (SdfFile) jaxbMarshaller.unmarshal(file);
				
		builder.initialize(jaxbFile);
		
		return new XmlFileMenu(builder.getFileFormatVersion()+"");
	}
}
