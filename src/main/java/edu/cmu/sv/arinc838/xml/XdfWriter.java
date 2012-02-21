/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 6, 2012
 */
package edu.cmu.sv.arinc838.xml;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.arinc.arinc838.SdfFile;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class XdfWriter {

	private final SdfFile softwareDefinition;

	public XdfWriter(SdfFile softwareDefinition) {
		this.softwareDefinition = softwareDefinition;
	}

	public void write(String fileName) throws JAXBException,
			FileNotFoundException {
		File file = new File(fileName);

		JAXBContext jaxbContext = JAXBContext.newInstance(SdfFile.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		NamespacePrefixMapper mapper = new NamespacePrefixMapper() {
			public String getPreferredPrefix(String namespaceUri,
					String suggestion, boolean requirePrefix) {
				if (namespaceUri.contains("arinc.com")) {
					return "sdf";
				} else {
					return "";
				}
			}
		};
		jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
				mapper);
		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(softwareDefinition, file);
	}
}
