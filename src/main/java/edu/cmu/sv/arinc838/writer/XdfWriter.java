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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.arinc.arinc838.SdfFile;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import edu.cmu.sv.arinc838.builder.BuilderFactory;
import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;
import edu.cmu.sv.arinc838.crc.CrcGeneratorFactory;
import edu.cmu.sv.arinc838.crc.LspCrcCalculator;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;

public class XdfWriter implements SdfWriter {
	@Override
	public void write(String path, SoftwareDefinitionFileDao sdfDao)
			throws Exception {
		File file = new File(path + sdfDao.getXmlFileName());
		SoftwareDefinitionFileBuilder builder = new SoftwareDefinitionFileBuilder(
				new BuilderFactory(), new CrcGeneratorFactory(),
				new LspCrcCalculator());

		SdfFile sdfFile = builder.buildXml(sdfDao);
		write(file, sdfFile);
	}

	public void write(File file, SdfFile sdfFile) throws Exception {
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
		jaxbMarshaller.marshal(sdfFile, file);
	}

	@Override
	public String getFilename(SoftwareDefinitionFileDao sdfDao) {
		return sdfDao.getXmlFileName();
	}
}
