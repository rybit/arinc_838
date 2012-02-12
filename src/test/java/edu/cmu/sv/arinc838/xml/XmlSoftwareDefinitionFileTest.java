package edu.cmu.sv.arinc838.xml;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.*;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import com.arinc.arinc838.SdfFile;
import com.arinc.arinc838.SdfSections;

import edu.cmu.sv.arinc838.builder.SoftwareDefinitionSectionsBuilder;
import edu.cmu.sv.arinc838.specification.SoftwareDefinitionSections;
import edu.cmu.sv.arinc838.specification.SoftwareDescription;

public class XmlSoftwareDefinitionFileTest {
	private SdfFile swDefFile;
	private SdfSections swDefSects;
	private XmlSoftwareDefinitionFile xmlSwDefFile;

	@BeforeMethod
	public void beforeMethod() {
		swDefFile = new SdfFile();
		swDefSects = new SdfSections();
		swDefFile.setFileFormatVersion("VersionTest");
		swDefFile.setSdfSections(swDefSects);
		xmlSwDefFile = new XmlSoftwareDefinitionFile(swDefFile);

	}

	@Test
	public void getFileFormatVersion() {
		assertEquals(xmlSwDefFile.getFileFormatVersion(),
				swDefFile.getFileFormatVersion());
	}

	@Test
	public void getSoftwareDefinitionSections() {
		assertEquals(xmlSwDefFile.getSoftwareDefinitionSections(),
				swDefFile.getSdfSections());
	}

	@Test
	public void setFileFormatVersion() {
		assertEquals(xmlSwDefFile.getFileFormatVersion(),
				swDefFile.getFileFormatVersion(),
				"Expected file format versions to be equal");

		String newString = swDefFile.getFileFormatVersion()
				+ "_some_new_string";
		xmlSwDefFile.setFileFormatVersion(newString);

		assertEquals(xmlSwDefFile.getFileFormatVersion(), newString);

		assertNotEquals(xmlSwDefFile.getFileFormatVersion(),
				swDefFile.getFileFormatVersion());
	}

	@Test
	public void setSoftwareDefinitionSections() {
		
		assertEquals(xmlSwDefFile.getSoftwareDefinitionSections().getSoftwareDescription().getSoftwarePartNumber(),
				swDefFile.getSdfSections().getSoftwareDescription().getSoftwarePartnumber(), "Exepected sofware part numbers to be equal");
		
		assertEquals(xmlSwDefFile.getSoftwareDefinitionSections().getSoftwareDescription().getSoftwareTypeId(),
				swDefFile.getSdfSections().getSoftwareDescription().getSoftwareTypeId(), "Expecteed software type IDs to be equal");
		
		SoftwareDefinitionSections tmp = new SoftwareDefinitionSectionsBuilder();
		SoftwareDescription swDestmp = new XmlSoftwareDescription();
		swDestmp.setSoftwarePartNumber(xmlSwDefFile.getSoftwareDefinitionSections().getSoftwareDescription().getSoftwarePartNumber() + "_some_new_string");
		tmp.setSoftwareDescription(swDestmp);
		
		
		
		xmlSwDefFile.setSoftwareDefinitionSections(tmp);
		
		assertEquals(xmlSwDefFile.getSoftwareDefinitionSections(),tmp, "Expected the set to value to equal the returned value");
		
		assertNotEquals(xmlSwDefFile.getSoftwareDefinitionSections().getSoftwareDescription().getSoftwarePartNumber(),
				swDefFile.getSdfSections().getSoftwareDescription().getSoftwarePartnumber(), "Exepected sofware part numbers to not be equal");
		

	}
}
