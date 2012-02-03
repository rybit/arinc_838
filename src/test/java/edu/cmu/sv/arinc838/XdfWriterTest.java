package edu.cmu.sv.arinc838;

import nu.xom.Builder;
import nu.xom.Document;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.arinc.arinc838.SdfFile;
import edu.cmu.sv.arinc838.writer.XdfWriter;

public class XdfWriterTest {

	@Test
	public void xdfWriteTest() throws Exception {
		File sampleXdfFile = new File("src/test/resources/sample_file.xml");
		JAXBContext jaxbContext = JAXBContext
				.newInstance(SdfFile.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		SdfFile sdfFile = (SdfFile) jaxbUnmarshaller
				.unmarshal(sampleXdfFile);
		XdfWriter xdfWriter = new XdfWriter(sdfFile);
		xdfWriter.write("test_xdf_writer.xml");

		Builder builder = new Builder();
		Document inputDoc = builder.build(sampleXdfFile);
		File outputFile = new File("test_xdf_writer.xml");
		Document outputDoc = builder.build(outputFile);
		outputFile.delete();
		assertEquals(outputDoc.toString(), inputDoc.toString());
	}
}
