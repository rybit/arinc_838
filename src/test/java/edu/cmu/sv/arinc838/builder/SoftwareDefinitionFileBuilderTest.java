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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mockito.InOrder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.arinc.arinc838.FileDefinition;
import com.arinc.arinc838.IntegrityDefinition;
import com.arinc.arinc838.SdfFile;
import com.arinc.arinc838.SoftwareDescription;
import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.crc.Crc16Generator;
import edu.cmu.sv.arinc838.crc.Crc32Generator;
import edu.cmu.sv.arinc838.crc.Crc64Generator;
import edu.cmu.sv.arinc838.crc.CrcGeneratorFactory;
import edu.cmu.sv.arinc838.dao.FileDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao.IntegrityType;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.dao.SoftwareDescriptionDao;
import edu.cmu.sv.arinc838.dao.TargetHardwareDefinitionDao;
import edu.cmu.sv.arinc838.util.Converter;
import edu.cmu.sv.arinc838.validation.ReferenceData;

public class SoftwareDefinitionFileBuilderTest {
	private SdfFile swDefFile;
	private SoftwareDefinitionFileBuilder swDefFileBuilder;
	private com.arinc.arinc838.IntegrityDefinition integrity;
	private SoftwareDescription description;
	private SoftwareDescriptionBuilder swDescBuilder;
	private com.arinc.arinc838.FileDefinition fileDef;
	private com.arinc.arinc838.ThwDefinition hardwareDef;
	private SoftwareDefinitionFileDao sdfDao;
	private BuilderFactory bFactory;
	private TargetHardwareDefinitionBuilder thdBuilder;
	private FileDefinitionBuilder fdBuilder;
	private IntegrityDefinitionBuilder integDefBuilder;
	private CrcGeneratorFactory crcFactory;
	private Crc16Generator crc16Generator;
	private final long crcValue = 65535;

	@BeforeMethod
	public void beforeMethod() throws Exception {
		crcFactory = mock(CrcGeneratorFactory.class);
		crc16Generator = mock(Crc16Generator.class);
		when(crcFactory.getCrc16Generator()).thenReturn(crc16Generator);

		bFactory = mock(BuilderFactory.class);
		swDescBuilder = mock(SoftwareDescriptionBuilder.class);

		when(
				bFactory.getBuilder(SoftwareDescriptionDao.class,
						SoftwareDescription.class)).thenReturn(swDescBuilder);

		swDefFileBuilder = new SoftwareDefinitionFileBuilder(bFactory,
				crcFactory);

		thdBuilder = mock(TargetHardwareDefinitionBuilder.class);
		when(
				bFactory.getBuilder(TargetHardwareDefinitionDao.class,
						ThwDefinition.class)).thenReturn(thdBuilder);

		fdBuilder = mock(FileDefinitionBuilder.class);
		when(bFactory.getBuilder(FileDefinitionDao.class, FileDefinition.class))
				.thenReturn(fdBuilder);

		integDefBuilder = mock(IntegrityDefinitionBuilder.class);
		when(
				bFactory.getBuilder(IntegrityDefinitionDao.class,
						IntegrityDefinition.class)).thenReturn(integDefBuilder);

		integrity = new com.arinc.arinc838.IntegrityDefinition();
		integrity.setIntegrityType(IntegrityType.CRC16.getType());
		integrity.setIntegrityValue(Converter.hexToBytes("FFFF"));

		when(crc16Generator.calculateCrc(any(byte[].class))).thenReturn(
				crcValue);

		description = new SoftwareDescription();
		description
				.setSoftwarePartnumber(ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		description.setSoftwareTypeDescription("desc");
		description.setSoftwareTypeId(Converter.hexToBytes("FFFF"));

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
				.setFileFormatVersion(SoftwareDefinitionFileDao.DEFAULT_FILE_FORMAT_VERSION);
		swDefFile.setSdfIntegrityDefinition(integrity);
		swDefFile.setLspIntegrityDefinition(integrity);
		swDefFile.setSoftwareDescription(description);

		swDefFile.getFileDefinitions().add(fileDef);
		swDefFile.getFileDefinitions().add(fileDef);

		swDefFile.getThwDefinitions().add(hardwareDef);
		swDefFile.getThwDefinitions().add(hardwareDef);

		sdfDao = new SoftwareDefinitionFileDao(swDefFile);
	}

	@Test
	public void testBuildAddsFileFormatVersion() {
		SdfFile file = swDefFileBuilder.buildXml(sdfDao);

		assertEquals(swDefFile.getFileFormatVersion(),
				file.getFileFormatVersion());
	}

	@Test
	public void testDefaultConstructor() {
		SoftwareDefinitionFileDao builder = new SoftwareDefinitionFileDao();

		assertEquals(builder.getFileFormatVersion(),
				SoftwareDefinitionFileDao.DEFAULT_FILE_FORMAT_VERSION);
	}

	public void testBuildBinaryWritesHeader() throws FileNotFoundException,
			IOException {
		swDefFileBuilder = new SoftwareDefinitionFileBuilder(
				new BuilderFactory(), new CrcGeneratorFactory());
		BdfFile file = new BdfFile(File.createTempFile("tmp", "bin"));
		int bytesWritten = swDefFileBuilder.buildBinary(sdfDao, file);

		assertEquals(bytesWritten, 163);

		file.seek(0);
		assertEquals(file.readUint32(), file.length());
		byte[] fileFormatVersion = new byte[4];
		file.read(fileFormatVersion);
		assertEquals(fileFormatVersion,
				SoftwareDefinitionFileDao.DEFAULT_FILE_FORMAT_VERSION);

		assertEquals(file.readUint32(), 28); // software description pointer
		assertEquals(file.readUint32(), 28 + 25); // target hardware definition
													// pointer
		assertEquals(file.readUint32(), 28 + 25 + 40);// file definition pointer
		assertEquals(file.readUint32(), 28 + 25 + 40 + 50);// SDF integrity
															// definition
															// pointer
		assertEquals(file.readUint32(), 28 + 25 + 40 + 50 + 10);// LSP integrity
																// definition
																// pointer
	}

	@Test
	public void testBuildBinaryWritesSoftwareDefinition() throws IOException {
		BdfFile file = mock(BdfFile.class);
		SoftwareDescriptionDao swDescription = mock(SoftwareDescriptionDao.class);
		TargetHardwareDefinitionDao thdDao = mock(TargetHardwareDefinitionDao.class);
		TargetHardwareDefinitionDao thdDAOLast = mock(TargetHardwareDefinitionDao.class);
		FileDefinitionDao fdDao = mock(FileDefinitionDao.class);
		FileDefinitionDao fdDaoLast = mock(FileDefinitionDao.class);
		IntegrityDefinitionDao sdfIntegDao = mock(IntegrityDefinitionDao.class);
		IntegrityDefinitionDao lspIntegDao = mock(IntegrityDefinitionDao.class);

		sdfDao.setSoftwareDescription(swDescription);
		sdfDao.getTargetHardwareDefinitions().clear();
		sdfDao.getTargetHardwareDefinitions().add(thdDao);
		sdfDao.getTargetHardwareDefinitions().add(thdDAOLast);
		sdfDao.getFileDefinitions().clear();
		sdfDao.getFileDefinitions().add(fdDao);
		sdfDao.getFileDefinitions().add(fdDao);
		sdfDao.getFileDefinitions().add(fdDaoLast);

		sdfDao.setSdfIntegrityDefinition(sdfIntegDao);
		sdfDao.setLspIntegrityDefinition(lspIntegDao);

		InOrder order = inOrder(file, swDescBuilder, thdDao, integDefBuilder,
				thdDAOLast, thdBuilder, fdBuilder, fdDao, fdDaoLast,
				sdfIntegDao, lspIntegDao, crc16Generator);

		when(file.length()).thenReturn(0L, 14L);
		when(sdfIntegDao.getIntegrityType()).thenReturn(IntegrityType.CRC16.getType());
		when(file.readAll()).thenReturn(new byte[] { (byte) 222,
				(byte) 173, (byte) 190, (byte) 239 });
		
		int bytesWritten = swDefFileBuilder.buildBinary(sdfDao, file);

		assertEquals(bytesWritten, 14L);

		order.verify(file).seek(0);
		order.verify(file).writePlaceholder();
		order.verify(file).writeHexbin32(sdfDao.getFileFormatVersion());
		order.verify(file, times(5)).writePlaceholder();

		order.verify(swDescBuilder).buildBinary(swDescription, file);

		order.verify(file).writeTargetDefinitionsPointer();
		order.verify(file).writeUint32(2);
		order.verify(thdDAOLast).setIsLast(true);
		order.verify(thdBuilder).buildBinary(thdDao, file);
		order.verify(thdBuilder).buildBinary(thdDAOLast, file);

		order.verify(file).writeFileDefinitionsPointer();
		order.verify(file).writeUint32(3);
		order.verify(fdDaoLast).setIsLast(true);
		order.verify(fdBuilder, times(2)).buildBinary(fdDao, file);
		order.verify(fdBuilder).buildBinary(fdDaoLast, file);

		
		
		order.verify(crc16Generator).calculateCrc(file.readAll());

		order.verify(file).writeSdfIntegrityDefinitionPointer();
		order.verify(sdfIntegDao).setIntegrityValue(
				Converter.longToBytes(crcValue));
		order.verify(integDefBuilder).buildBinary(sdfIntegDao, file);

		order.verify(file).writeLspIntegrityDefinitionPointer();
		// TODO actually calculate the CRC
		order.verify(lspIntegDao).setIntegrityValue(
				Converter.hexToBytes("FFFF"));

		order.verify(integDefBuilder).buildBinary(lspIntegDao, file);
		order.verify(file).seek(0);
		order.verify(file).writeUint32(file.length());
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testBinaryFileIsEmptyPriorToBuild()
			throws FileNotFoundException, IOException {
		BdfFile bdf = new BdfFile(File.createTempFile("tmp", "BDF"));
		bdf.write(3);

		SoftwareDefinitionFileBuilder builder = new SoftwareDefinitionFileBuilder(
				null, null);

		builder.buildBinary(null, bdf);
	}
	
	@Test
	public void testThatCorrectSdfCrcIsWrittenBasedOnType32() throws Exception {
		crcFactory = mock(CrcGeneratorFactory.class);
		Crc32Generator crc32Generator = mock(Crc32Generator.class);
		when(crcFactory.getCrc32Generator()).thenReturn(crc32Generator);
		
		
		swDefFileBuilder = new SoftwareDefinitionFileBuilder(
				new BuilderFactory(), crcFactory);
		BdfFile file = new BdfFile(File.createTempFile("tmp", "bin"));
		sdfDao.getSdfIntegrityDefinition().setIntegrityType(IntegrityType.CRC32.getType());
		swDefFileBuilder.buildBinary(sdfDao, file);	
		verify(crc32Generator).calculateCrc(any(byte[].class));
	}
	
	@Test
	public void testThatCorrectSdfCrcIsWrittenBasedOnType64() throws Exception {
		crcFactory = mock(CrcGeneratorFactory.class);
		Crc64Generator crc64Generator = mock(Crc64Generator.class);
		when(crcFactory.getCrc64Generator()).thenReturn(crc64Generator);
		
		
		swDefFileBuilder = new SoftwareDefinitionFileBuilder(
				new BuilderFactory(), crcFactory);
		BdfFile file = new BdfFile(File.createTempFile("tmp", "bin"));
		sdfDao.getSdfIntegrityDefinition().setIntegrityType(IntegrityType.CRC64.getType());
		swDefFileBuilder.buildBinary(sdfDao, file);	
		verify(crc64Generator).calculateCrc(any(byte[].class));
	}
	
}
