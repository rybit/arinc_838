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

import nu.xom.Builder;
import nu.xom.Document;

import org.apache.xerces.dom.NotationImpl;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.arinc.arinc838.SdfFile;

import edu.cmu.sv.arinc838.builder.SoftwareDefinitionSectionsBuilder;
import edu.cmu.sv.arinc838.xml.XdfWriter;

public class XdfWriterTest {

	@Test
	public void xdfWritesFileFormatVersionTest() throws Exception {
		SdfFile file = getTestFile();
		String filename = "test_xdf_writer.xml";
		
		new XdfWriter(file).write(filename);
		
		File writtenXmlFile = new File(filename);
		
		SdfFile jaxbFile = readJaxb(writtenXmlFile);
		
		
		assertEquals(file, jaxbFile);
		
		writtenXmlFile.delete();
	}
	
	private SdfFile readJaxb(File writtenXmlFile) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(SdfFile.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return (SdfFile) jaxbUnmarshaller.unmarshal(writtenXmlFile);		
	}

	private static SdfFile getTestFile() {
		SdfFile file = new SdfFile();
		file.setFileFormatVersion("version");
		return file;
	}
}
