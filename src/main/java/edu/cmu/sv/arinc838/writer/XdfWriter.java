/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 6, 2012
 */
package edu.cmu.sv.arinc838.writer;

import java.io.File;
import java.io.RandomAccessFile;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.arinc.arinc838.SdfFile;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import edu.cmu.sv.arinc838.builder.BuilderFactory;
import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;

public class XdfWriter implements SdfWriter {
	@Override
	public void write(String path, SoftwareDefinitionFileDao sdfDao)
			throws Exception {
		File file = new File(path + sdfDao.getXmlFileName());
		SoftwareDefinitionFileBuilder builder = new SoftwareDefinitionFileBuilder(
				new BuilderFactory());

		SdfFile sdfFile = builder.buildXml(sdfDao);
		write(file, sdfFile);
	}

	public void write(File file, SdfFile sdfFile) throws Exception {
		JAXBContext jaxbContext = JAXBContext.newInstance(SdfFile.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE); // omit xml tag, we write ourselves below to omit the "standalone" attribute that JAXB adds
		jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.arinc.com");

		NamespacePrefixMapper mapper = new NamespacePrefixMapper() {
			public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
            	if (namespaceUri.equals(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI))
                    return "xsi";
                if (namespaceUri.equals(XMLConstants.W3C_XML_SCHEMA_NS_URI))
                    return "xs";
				if (namespaceUri.contains("arinc.com")) {
					return "sdf";
				}

                return suggestion;
			}
		};

		jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
				mapper);
		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(sdfFile, file);
		
		// prepend the XML tag
		RandomAccessFile randAccessFile = new RandomAccessFile(file, "rw");
		byte[] contents = new byte[(int) randAccessFile.length()];
		randAccessFile.readFully(contents);
		randAccessFile.seek(0);
		randAccessFile.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n".getBytes());
		randAccessFile.write(contents);
		randAccessFile.close();
	}

	@Override
	public String getFilename(SoftwareDefinitionFileDao sdfDao) {
		return sdfDao.getXmlFileName();
	}
}
