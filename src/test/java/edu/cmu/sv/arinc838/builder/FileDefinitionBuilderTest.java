/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 7, 2012
 */
package edu.cmu.sv.arinc838.builder;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.arinc.arinc838.FileDefinition;

import edu.cmu.sv.arinc838.dao.FileDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao.IntegrityType;
import edu.cmu.sv.arinc838.util.Converter;

public class FileDefinitionBuilderTest {
	FileDefinition xmlFileDef;

	// Our builder
	private FileDefinitionDao fileBuilder;

	@BeforeMethod
	public void setUp() {
		IntegrityDefinitionDao integDao = new IntegrityDefinitionDao();
		integDao.setIntegrityType(IntegrityType.CRC16.getType());
		integDao.setIntegrityValue(Converter.hexToBytes("0000000A"));
		
		xmlFileDef = new FileDefinition();
		xmlFileDef.setFileLoadable(false);
		xmlFileDef.setFileName("testFile");
		xmlFileDef.setFileSize(1234);
		xmlFileDef.setFileIntegrityDefinition( new IntegrityDefinitionBuilder ().buildXml(integDao));

		fileBuilder = new FileDefinitionDao();
		fileBuilder.setFileLoadable(xmlFileDef.isFileLoadable());
		fileBuilder.setFileName(xmlFileDef.getFileName());
		fileBuilder.setFileSize(xmlFileDef.getFileSize());		
		fileBuilder.setFileIntegrityDefinition(integDao);
	}

	@Test
	public void testBuilder() {
		FileDefinitionDao fdDao = new FileDefinitionDao(xmlFileDef);
		FileDefinition built = new FileDefinitionBuilder(new BuilderFactory()).buildXml(fdDao);

		assertNotEquals(null, built);
		assertNotEquals(built, xmlFileDef,
				"Should be different, a NEW instance should be built");

		assertNotEquals(null, built.getFileIntegrityDefinition());
		assertNotEquals(built.getFileIntegrityDefinition(),
				xmlFileDef.getFileIntegrityDefinition(),
				"Should have built a new integrity definition");

		assertEquals(built.getFileName(), xmlFileDef.getFileName());
		assertEquals(built.getFileSize(), xmlFileDef.getFileSize());
		assertEquals(built.isFileLoadable(), xmlFileDef.isFileLoadable());
	}
}
