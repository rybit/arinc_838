package edu.cmu.sv.arinc838.crc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CrcCalculatorTestCommon {
	private static byte[] getBytes(String fileName) throws IOException {
		File file = new File("src/test/resources/crc_test_files/" + fileName);
		FileInputStream fin = new FileInputStream(file);
		byte fileContent[] = new byte[(int) file.length()];
		fin.read(fileContent);
		return fileContent;
	}

	public static Map<BigInteger, byte[]> getExpectedCrcs(String crcType)
			throws Exception {
		Map<BigInteger, byte[]> result = new HashMap<BigInteger, byte[]>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document dom = db
				.parse("src/test/resources/crc_test_files/CRC_Expected_Values.xml");
		NodeList nl = dom.getElementsByTagName("test_file");
		for (int i = 0; i < nl.getLength(); i++) {
			Element el = (Element) nl.item(i);
			String crc = el.getAttribute(crcType).substring(2);
			result.put(new BigInteger(crc, 16),
					getBytes(el.getAttribute("name")));
		}
		return result;
	}
}
