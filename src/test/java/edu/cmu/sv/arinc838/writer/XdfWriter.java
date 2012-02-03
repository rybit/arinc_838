package edu.cmu.sv.arinc838.writer;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.arinc.arinc838.SdfFile;

import edu.cmu.sv.arinc838.specification.SoftwareDefinition;

public class XdfWriter {

	private final SdfFile softwareDefinition;

	public XdfWriter(SdfFile softwareDefinition) {
		this.softwareDefinition = softwareDefinition;
	}

	public void write(String fileName) throws JAXBException {
		File file = new File(fileName);
		JAXBContext jaxbContext = JAXBContext.newInstance(SdfFile.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
 
		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
 		jaxbMarshaller.marshal(softwareDefinition, file);
	}
}
