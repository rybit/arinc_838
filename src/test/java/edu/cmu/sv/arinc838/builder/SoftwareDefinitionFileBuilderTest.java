package edu.cmu.sv.arinc838.builder;

import static org.testng.Assert.*;
import static org.mockito.Mockito.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.arinc.arinc838.IntegrityDefinition;
import com.arinc.arinc838.SdfFile;
import com.arinc.arinc838.SdfSections;
import com.arinc.arinc838.SoftwareDescription;

public class SoftwareDefinitionFileBuilderTest {
	private SdfFile swDefFile;
	private SdfSections swDefSects;
	private SoftwareDefinitionFileBuilder swDefFileBuilder;

	@BeforeMethod
	public void beforeMethod() {
		swDefFile = new SdfFile();
		swDefSects = new SdfSections();
		IntegrityDefinition lspInteg = new IntegrityDefinition();
		lspInteg.setIntegrityType(2);
		lspInteg.setIntegrityValue("DEADBEEF");
		IntegrityDefinition sdfInteg = new IntegrityDefinition();
		sdfInteg.setIntegrityType(2);
		sdfInteg.setIntegrityValue("DEADBEEF");
		
		swDefSects.setLspIntegrityDefinition(lspInteg);
		swDefSects.setSdfIntegrityDefinition(sdfInteg);

		SoftwareDescription swDesc = new SoftwareDescription();
		swDesc.setSoftwarePartnumber("1234");
		swDesc.setSoftwareTypeDescription("type");
		swDesc.setSoftwareTypeId(2);
		
		swDefSects.setSoftwareDescription(swDesc);
		
		swDefFile.setFileFormatVersion("VersionTest");
		swDefFile.setSdfSections(swDefSects);
		swDefFileBuilder = new SoftwareDefinitionFileBuilder(swDefFile);
	}

	@Test
	public void getFileFormatVersion() {
		assertEquals(swDefFileBuilder.getFileFormatVersion(),
				swDefFile.getFileFormatVersion());
	}

	@Test
	public void getSoftwareDefinitionSections() {
		assertEquals(swDefFileBuilder.getSoftwareDefinitionSections().getSoftwareDescription().getSoftwarePartnumber(),
				swDefFile.getSdfSections().getSoftwareDescription().getSoftwarePartnumber());
	}

	@Test
	public void setFileFormatVersion() {
		String newString = swDefFile.getFileFormatVersion()
				+ "_some_new_string";
		swDefFileBuilder.setFileFormatVersion(newString);

		assertEquals(swDefFileBuilder.getFileFormatVersion(), newString);

		assertNotEquals(swDefFileBuilder.getFileFormatVersion(),
				swDefFile.getFileFormatVersion());
	}

	@Test
	public void setSoftwareDefinitionSections() {

		assertEquals(swDefFileBuilder.getSoftwareDefinitionSections()
				.getSoftwareDescription().getSoftwarePartnumber(), swDefFile
				.getSdfSections().getSoftwareDescription()
				.getSoftwarePartnumber(),
				"Exepected sofware part numbers to be equal");

		assertEquals(swDefFileBuilder.getSoftwareDefinitionSections()
				.getSoftwareDescription().getSoftwareTypeId(), swDefFile
				.getSdfSections().getSoftwareDescription().getSoftwareTypeId(),
				"Expecteed software type IDs to be equal");

		SdfSections tmp = new SdfSections();
		SoftwareDescription swDestmp = new SoftwareDescription();
		swDestmp.setSoftwarePartnumber(swDefFileBuilder
				.getSoftwareDefinitionSections().getSoftwareDescription()
				.getSoftwarePartnumber()
				+ "_some_new_string");
		tmp.setSoftwareDescription(swDestmp);

		swDefFileBuilder.setSoftwareDefinitionSections(tmp);

		assertEquals(swDefFileBuilder.getSoftwareDefinitionSections(), tmp,
				"Expected the set to value to equal the returned value");

		assertNotEquals(swDefFileBuilder.getSoftwareDefinitionSections()
				.getSoftwareDescription().getSoftwarePartnumber(), swDefFile
				.getSdfSections().getSoftwareDescription()
				.getSoftwarePartnumber(),
				"Exepected sofware part numbers to not be equal");
	}
	
	@Test
	public void testBuildAddsFileFormatVersion(){
		SdfFile file =	swDefFileBuilder.build();
		
		assertEquals(swDefFile.getFileFormatVersion(), file.getFileFormatVersion());
	}
	
	@Test
	public void testBuildAddsSection(){
		SdfFile file = swDefFileBuilder.build();
		
		assertEquals(file.getSdfSections().getSoftwareDescription().getSoftwarePartnumber(), swDefSects.getSoftwareDescription().getSoftwarePartnumber());
	}
}
