/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 12, 2012
 */
package edu.cmu.sv.arinc838.builder;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.mockito.InOrder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.arinc.arinc838.FileDefinition;
import com.arinc.arinc838.SdfFile;
import com.arinc.arinc838.SoftwareDescription;
import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.builder.IntegrityDefinitionBuilder.IntegrityType;
import edu.cmu.sv.arinc838.util.Converter;
import edu.cmu.sv.arinc838.validation.DataValidator;
import edu.cmu.sv.arinc838.validation.ReferenceData;
import edu.cmu.sv.arinc838.writer.BdfWriter;

public class SoftwareDefinitionFileBuilderTest {
	private SdfFile swDefFile;
	private SoftwareDefinitionFileBuilder swDefFileBuilder;
	private com.arinc.arinc838.IntegrityDefinition integrity;
	private SoftwareDescription description;
	private com.arinc.arinc838.FileDefinition fileDef;
	private com.arinc.arinc838.ThwDefinition hardwareDef;
	private BdfFile binaryFile;
	private SoftwareDefinitionFileBuilder readBinaryFile;

	@BeforeMethod
	public void beforeMethod() throws Exception {

		integrity = new com.arinc.arinc838.IntegrityDefinition();
		integrity.setIntegrityType(IntegrityType.CRC16.getType());
		integrity.setIntegrityValue(Converter.hexToBytes("0000000A"));

		description = new SoftwareDescription();
		description
				.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		description.setSoftwareTypeDescription("desc");
		description.setSoftwareTypeId(Converter.hexToBytes("0000000A"));

		fileDef = new com.arinc.arinc838.FileDefinition();
		fileDef.setFileName("file");
		fileDef.setFileIntegrityDefinition(integrity);
		fileDef.setFileSize(1234);
		List<FileDefinition> fileDefs = new ArrayList<FileDefinition>();
		fileDefs.add(fileDef);

		hardwareDef = new ThwDefinition();
		hardwareDef.setThwId("hardware");

		swDefFile = new SdfFile();

		swDefFile
				.setFileFormatVersion(SoftwareDefinitionFileBuilder.DEFAULT_FILE_FORMAT_VERSION);
		swDefFile.setSdfIntegrityDefinition(integrity);
		swDefFile.setLspIntegrityDefinition(integrity);
		swDefFile.setSoftwareDescription(description);

		swDefFile.getFileDefinitions().add(fileDef);
		swDefFile.getFileDefinitions().add(fileDef);

		swDefFile.getThwDefinitions().add(hardwareDef);
		swDefFile.getThwDefinitions().add(hardwareDef);

		swDefFileBuilder = new SoftwareDefinitionFileBuilder(swDefFile);

		binaryFile = new BdfFile(File.createTempFile("tmp", "bin"));
		swDefFileBuilder.buildBinary(binaryFile);
		readBinaryFile = new SoftwareDefinitionFileBuilder(binaryFile);
	}

	@Test
	public void getFileFormatVersion() {
		assertEquals(swDefFileBuilder.getFileFormatVersion(),
				swDefFile.getFileFormatVersion());
	}

	@Test
	public void getSoftwareDefinitionSections() {
		assertEquals(swDefFileBuilder.getSoftwareDescription()
				.getSoftwarePartNumber(), swDefFile.getSoftwareDescription()
				.getSoftwarePartnumber());
	}

	@Test
	public void testBuildAddsFileFormatVersion() {
		SdfFile file = swDefFileBuilder.buildXml();

		assertEquals(swDefFile.getFileFormatVersion(),
				file.getFileFormatVersion());
	}

	@Test
	public void testDefaultConstructor() {
		SoftwareDefinitionFileBuilder builder = new SoftwareDefinitionFileBuilder();

		assertEquals(builder.getFileFormatVersion(),
				SoftwareDefinitionFileBuilder.DEFAULT_FILE_FORMAT_VERSION);

	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testFileFormatVersionIsCorrect() {
		SdfFile file = swDefFileBuilder.buildXml();
		file.setFileFormatVersion(Converter.hexToBytes("0000000A"));

		new SoftwareDefinitionFileBuilder(file);

	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testFileDefinitionsEmpty() {
		SdfFile newSdfFile = swDefFileBuilder.buildXml();
		newSdfFile.getFileDefinitions().clear();

		new SoftwareDefinitionFileBuilder(newSdfFile);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testFileDefinitionsEmptyAtBuild() {
		swDefFileBuilder.getFileDefinitions().clear();

		swDefFileBuilder.buildXml();
	}

	@Test
	public void getFileDefinitions() {
		assertEquals(swDefFileBuilder.getFileDefinitions().size(), 2);

		assertEquals(
				swDefFileBuilder.getFileDefinitions().get(0).getFileName(),
				"file");
		assertEquals(
				swDefFileBuilder.getFileDefinitions().get(1).getFileName(),
				"file");
	}

	@Test
	public void addFileDefinition() {
		swDefFileBuilder.getFileDefinitions().clear();

		FileDefinitionBuilder expected = mock(FileDefinitionBuilder.class);

		swDefFileBuilder.getFileDefinitions().add(expected);
		FileDefinitionBuilder actualFileDefinition = swDefFileBuilder
				.getFileDefinitions().get(0);
		assertEquals(actualFileDefinition, expected);
	}

	@Test
	public void getLspIntegrityDefinition() {
		assertEquals(swDefFileBuilder.getLspIntegrityDefinition()
				.getIntegrityType(), integrity.getIntegrityType());
		assertEquals(swDefFileBuilder.getLspIntegrityDefinition()
				.getIntegrityValue(), integrity.getIntegrityValue());
	}

	@Test
	public void setLspIntegrityDefinition() {

		IntegrityDefinitionBuilder newDef = mock(IntegrityDefinitionBuilder.class);
		when(newDef.getIntegrityType()).thenReturn(10l);
		when(newDef.getIntegrityValue()).thenReturn(new byte[] { 1, 2, 3, 4 });

		swDefFileBuilder.setLspIntegrityDefinition(newDef);

		assertEquals(swDefFileBuilder.getLspIntegrityDefinition()
				.getIntegrityType(), newDef.getIntegrityType());
		assertEquals(swDefFileBuilder.getLspIntegrityDefinition()
				.getIntegrityValue(), newDef.getIntegrityValue());
	}

	@Test
	public void getSdfIntegrityDefinition() {
		assertEquals(swDefFileBuilder.getSdfIntegrityDefinition()
				.getIntegrityType(), integrity.getIntegrityType());
		assertEquals(swDefFileBuilder.getSdfIntegrityDefinition()
				.getIntegrityValue(), integrity.getIntegrityValue());
	}

	@Test
	public void setSdfIntegrityDefinition() {

		IntegrityDefinitionBuilder newDef = mock(IntegrityDefinitionBuilder.class);
		when(newDef.getIntegrityType()).thenReturn(10l);
		when(newDef.getIntegrityValue()).thenReturn(new byte[] { 1, 2, 3, 4 });

		swDefFileBuilder.setSdfIntegrityDefinition(newDef);

		assertEquals(swDefFileBuilder.getSdfIntegrityDefinition()
				.getIntegrityType(), newDef.getIntegrityType());
		assertEquals(swDefFileBuilder.getSdfIntegrityDefinition()
				.getIntegrityValue(), newDef.getIntegrityValue());
	}

	@Test
	public void getSoftwareDescription() {
		assertEquals(swDefFileBuilder.getSoftwareDescription()
				.getSoftwarePartNumber(), description.getSoftwarePartnumber());
		assertEquals(swDefFileBuilder.getSoftwareDescription()
				.getSoftwareTypeDescription(),
				description.getSoftwareTypeDescription());
		assertEquals(swDefFileBuilder.getSoftwareDescription()
				.getSoftwareTypeId(), description.getSoftwareTypeId());
	}

	@Test
	public void setSoftwareDescription() {
		SoftwareDescriptionBuilder newDesc = new SoftwareDescriptionBuilder();
		newDesc.setSoftwarePartNumber(DataValidator
				.generateSoftwarePartNumber("YZT??-ABCD-EFGH"));

		newDesc.setSoftwareTypeDescription("new desc");
		newDesc.setSoftwareTypeId(Converter.hexToBytes("0000000A"));

		swDefFileBuilder.setSoftwareDescription(newDesc);

		assertEquals(swDefFileBuilder.getSoftwareDescription()
				.getSoftwarePartNumber(), newDesc.getSoftwarePartNumber());
		assertEquals(swDefFileBuilder.getSoftwareDescription()
				.getSoftwareTypeDescription(),
				newDesc.getSoftwareTypeDescription());
		assertEquals(swDefFileBuilder.getSoftwareDescription()
				.getSoftwareTypeId(), newDesc.getSoftwareTypeId());
	}

	@Test
	public void getTargetHardwareDefinitions() {

		assertEquals(swDefFileBuilder.getTargetHardwareDefinitions().size(), 2);

		assertEquals(swDefFileBuilder.getTargetHardwareDefinitions().get(0)
				.getId(), "hardware");
		assertEquals(swDefFileBuilder.getTargetHardwareDefinitions().get(1)
				.getId(), "hardware");
	}

	@Test
	public void addTargetHardwareDefinitions() {
		swDefFileBuilder.getTargetHardwareDefinitions().clear();

		TargetHardwareDefinitionBuilder expectedHardwareDefinition = new TargetHardwareDefinitionBuilder();

		swDefFileBuilder.getTargetHardwareDefinitions().add(
				expectedHardwareDefinition);
		TargetHardwareDefinitionBuilder actualHardwareDefinition = swDefFileBuilder
				.getTargetHardwareDefinitions().get(0);
		assertEquals(actualHardwareDefinition, expectedHardwareDefinition);
	}

	@Test
	public void testBuildBinaryWritesHeader() throws FileNotFoundException,
			IOException {
		BdfFile file = new BdfFile(File.createTempFile("tmp", "bin"));
		int bytesWritten = swDefFileBuilder.buildBinary(file);

		assertEquals(bytesWritten, 169);

		file.seek(0);
		assertEquals(file.readUint32(), file.length());
		byte[] fileFormatVersion = new byte[4];
		file.read(fileFormatVersion);
		assertEquals(fileFormatVersion,
				SoftwareDefinitionFileBuilder.DEFAULT_FILE_FORMAT_VERSION);

		assertEquals(file.readUint32(), 28); // software description pointer
		assertEquals(file.readUint32(), 28 + 27); // target hardware definition
													// pointer
		assertEquals(file.readUint32(), 28 + 27 + 40);// file definition pointer
		assertEquals(file.readUint32(), 28 + 27 + 40 + 54);// SDF integrity
															// definition
															// pointer
		assertEquals(file.readUint32(), 28 + 27 + 40 + 54 + 10);// LSP integrity
																// definition
																// pointer
	}

	@Test
	public void testBuildBinaryWritesSoftwareDefinition() throws IOException {
		BdfFile file = mock(BdfFile.class);
		SoftwareDescriptionBuilder swDescription = mock(SoftwareDescriptionBuilder.class);
		TargetHardwareDefinitionBuilder thdBuilder = mock(TargetHardwareDefinitionBuilder.class);
		TargetHardwareDefinitionBuilder thdBuilderLast = mock(TargetHardwareDefinitionBuilder.class);
		FileDefinitionBuilder fdBuilder = mock(FileDefinitionBuilder.class);
		FileDefinitionBuilder fdBuilderLast = mock(FileDefinitionBuilder.class);
		IntegrityDefinitionBuilder sdfInteg = mock(IntegrityDefinitionBuilder.class);
		IntegrityDefinitionBuilder lspInteg = mock(IntegrityDefinitionBuilder.class);

		swDefFileBuilder.setSoftwareDescription(swDescription);
		swDefFileBuilder.getTargetHardwareDefinitions().clear();
		swDefFileBuilder.getTargetHardwareDefinitions().add(thdBuilder);
		swDefFileBuilder.getTargetHardwareDefinitions().add(thdBuilderLast);
		swDefFileBuilder.getFileDefinitions().clear();
		swDefFileBuilder.getFileDefinitions().add(fdBuilder);
		swDefFileBuilder.getFileDefinitions().add(fdBuilder);
		swDefFileBuilder.getFileDefinitions().add(fdBuilderLast);

		swDefFileBuilder.setSdfIntegrityDefinition(sdfInteg);
		swDefFileBuilder.setLspIntegrityDefinition(lspInteg);

		InOrder order = inOrder(file, swDescription, thdBuilder,
				thdBuilderLast, fdBuilder, fdBuilderLast, sdfInteg, lspInteg);

		when(file.length()).thenReturn(0L, 14L);
		int bytesWritten = swDefFileBuilder.buildBinary(file);
		assertEquals(bytesWritten, 14L);

		order.verify(file).seek(0);
		order.verify(file).writePlaceholder();
		order.verify(file).writeHexbin32(
				swDefFileBuilder.getFileFormatVersion());
		order.verify(file, times(5)).writePlaceholder();

		order.verify(swDescription).buildBinary(file);

		order.verify(file).writeTargetDefinitionsPointer();
		order.verify(file).writeUint32(2);
		order.verify(thdBuilderLast).setIsLast(true);
		order.verify(thdBuilder).buildBinary(file);
		order.verify(thdBuilderLast).buildBinary(file);

		order.verify(file).writeFileDefinitionsPointer();
		order.verify(file).writeUint32(3);
		order.verify(fdBuilderLast).setIsLast(true);
		order.verify(fdBuilder, times(2)).buildBinary(file);
		order.verify(fdBuilderLast).buildBinary(file);

		order.verify(file)
		order.verify(file).writeSdfIntegrityDefinitionPointer();
		
		order.verify(sdfInteg).setIntegrityValue(
				Converter.hexToBytes("0000000A"));
		order.verify(sdfInteg).buildBinary(file);

		order.verify(file).writeLspIntegrityDefinitionPointer();
		// TODO actually calculate the CRC
		order.verify(lspInteg).setIntegrityValue(
				Converter.hexToBytes("0000000A"));

		order.verify(lspInteg).buildBinary(file);
		order.verify(file).seek(0);
		order.verify(file).writeUint32(file.length());
	}

	@Test
	public void testHasBinaryFileName() {
		assertEquals(swDefFileBuilder.getBinaryFileName(),
				swDefFileBuilder.getSoftwareDescription()
						.getSoftwarePartNumber().replace("-", "")
						+ ".BDF");
	}

	@Test
	public void testHasXmlFileName() {
		assertEquals(swDefFileBuilder.getXmlFileName(),
				swDefFileBuilder.getSoftwareDescription()
						.getSoftwarePartNumber().replace("-", "")
						+ ".XDF");
	}

	@Test
	public void testReadBinary() throws Exception {
		SoftwareDefinitionFileBuilder actual = new SoftwareDefinitionFileBuilder(binaryFile);		
		assertEquals(actual,  swDefFileBuilder);
	}
	
	@Test
	public void testReadBinaryActualFilesOnDisk() throws Exception {
		BdfWriter writer = new BdfWriter();

		String path = System.getProperty("java.io.tmpdir");

		String firstFileName = writer.write(path, swDefFileBuilder);
		File firstOnDisk = new File(firstFileName);

		BdfFile file = new BdfFile(firstOnDisk);

		SoftwareDefinitionFileBuilder actual = new SoftwareDefinitionFileBuilder(
				file);

		String secondFileName = writer.write(path, actual);

		RandomAccessFile first = new RandomAccessFile(firstOnDisk, "r");
		byte[] firstBytes = new byte[(int) first.length()];
		first.readFully(firstBytes);

		File secondOnDisk = new File(secondFileName);
		RandomAccessFile second = new RandomAccessFile(secondOnDisk, "r");
		byte[] secondBytes = new byte[(int) second.length()];
		second.readFully(secondBytes);

		assertEquals(firstBytes, secondBytes);

		firstOnDisk.delete();
		secondOnDisk.delete();
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testReadBinaryWithWrongFileFormatThrowsException()
			throws IOException {
		BdfFile file = new BdfFile(File.createTempFile("prefix", "suffix"));
		file.writeUint32(14); // write bogus length of file
		file.write(Converter.hexToBytes("00008111")); // write invalid file
														// format version

		new SoftwareDefinitionFileBuilder(file);
	}

	@Test
	public void testBinaryConstructorReadsDescription() {
		assertEquals(readBinaryFile.getSoftwareDescription()
				.getSoftwareTypeDescription(), swDefFileBuilder
				.getSoftwareDescription().getSoftwareTypeDescription());
	}

	@Test
	public void testSoftwareDefinitionFileBuilderBdfFile()
			throws FileNotFoundException, IOException {
		assertEquals(readBinaryFile.getLspIntegrityDefinition()
				.getIntegrityValue(), swDefFileBuilder
				.getLspIntegrityDefinition().getIntegrityValue());
		assertEquals(readBinaryFile.getSdfIntegrityDefinition()
				.getIntegrityValue(), swDefFileBuilder
				.getSdfIntegrityDefinition().getIntegrityValue());
	}
	
	@Test
	public void testEquals(){
		SoftwareDefinitionFileBuilder copy = new SoftwareDefinitionFileBuilder(swDefFile);
		
		assertEquals(swDefFileBuilder, copy);		
	}
	
	@Test
	public void testHashcode(){
		assertEquals(swDefFileBuilder.hashCode(), swDefFileBuilder.getXmlFileName().hashCode());
	}
	
	@Test
	public void testHashcodeWithNoDescriptionSet(){
		SoftwareDefinitionFileBuilder builder = new SoftwareDefinitionFileBuilder();
		
		assertEquals(builder.hashCode(), 0);
	}
	
	@Test
	public void testInitializeBinaryClearsFileDefinitions() throws IOException{
		SoftwareDefinitionFileBuilder builder = new SoftwareDefinitionFileBuilder();
		builder.getFileDefinitions().add(new FileDefinitionBuilder());
		
		builder.initialize(binaryFile);
		
		assertEquals(builder.getFileDefinitions().size(), swDefFile.getFileDefinitions().size());
	}
	
	@Test
	public void testInitializeBinaryClearsTargetHardwareDefinitions() throws IOException{
		SoftwareDefinitionFileBuilder builder = new SoftwareDefinitionFileBuilder();
		builder.getTargetHardwareDefinitions().add(new TargetHardwareDefinitionBuilder());
		
		builder.initialize(binaryFile);
		
		assertEquals(builder.getTargetHardwareDefinitions().size(), swDefFile.getThwDefinitions().size());		
	}
	
	@Test
	public void testInitializeXmlClearsFileDefinitions(){
		SoftwareDefinitionFileBuilder builder = new SoftwareDefinitionFileBuilder();
		builder.getFileDefinitions().add(new FileDefinitionBuilder());
		
		builder.initialize(swDefFile);
		
		assertEquals(builder.getFileDefinitions().size(), swDefFile.getFileDefinitions().size());
	}
	
	@Test
	public void testInitializeXmlClearsTargetHardwareDefinitions(){
		SoftwareDefinitionFileBuilder builder = new SoftwareDefinitionFileBuilder();
		builder.getTargetHardwareDefinitions().add(new TargetHardwareDefinitionBuilder());
		
		builder.initialize(swDefFile);
		
		assertEquals(builder.getTargetHardwareDefinitions().size(), swDefFile.getThwDefinitions().size());		
	}
	
	
	@Test (expectedExceptions = IllegalArgumentException.class)
	public void testBinaryFileIsEmptyPriorToBuild() throws FileNotFoundException, IOException{
		BdfFile bdf = new BdfFile(File.createTempFile("tmp", "BDF"));
		bdf.write(3);
		
		SoftwareDefinitionFileBuilder builder = new SoftwareDefinitionFileBuilder();
		
		builder.buildBinary(bdf);
		
	}
	
}
