package edu.cmu.sv.arinc838.crc;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Crc16GeneratorTest {
	private static void assertCrc(String fileName, int expectedCrc)
			throws FileNotFoundException, IOException {
		File file = new File("src/test/resources/crc_test_files/" + fileName);
		FileInputStream fin = new FileInputStream(file);
		byte fileContent[] = new byte[(int) file.length()];
		fin.read(fileContent);

		int crc = Crc16Generator.calculateCrc(fileContent);
		assertEquals(crc, expectedCrc);
	}

	@Test
	public void calculateCrcTest() throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document dom = db.parse("src/test/resources/crc_test_files/CRC_Expected_Values.xml");
		NodeList nl = dom.getElementsByTagName("test_file");
		for(int i=0; i<nl.getLength(); i++) {
			Element el = (Element) nl.item(i);
			String crc = el.getAttribute("crc16").substring(2);
			assertCrc(el.getAttribute("name"), Integer.parseInt(crc, 16));
		}
		
	}
}
