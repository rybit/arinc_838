/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 7, 2012
 */
package edu.cmu.sv.arinc838.xml;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.fail;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.arinc.arinc838.FileDefinition;
import com.arinc.arinc838.IntegrityDefinition;

public class XmlFileDefinitionTest {

	private FileDefinition fileDef;
	private IntegrityDefinition fileInteg;
	private XmlFileDefinition xmlFileDef;

	@BeforeMethod
	public void setUp() {
		fileDef = new FileDefinition();
		fileInteg = new IntegrityDefinition();
		fileInteg.setIntegrityType(3);
		fileInteg.setIntegrityValue("bleh");

		fileDef.setFileIntegrityDefinition(fileInteg);
		fileDef.setFileLoadable(true);
		fileDef.setFileName("testfile");
		fileDef.setFileSize(12345);

		xmlFileDef = new XmlFileDefinition(fileDef);
	}

	@Test
	public void testGetFileName() {
		assertEquals(xmlFileDef.getFileName(), fileDef.getFileName());
	}

	@Test
	public void testGetFileSize() {
		assertEquals(xmlFileDef.getFileSize(), fileDef.getFileSize());
	}

	@Test
	public void testGetFileIntegrityDefinition() {

		assertEquals(
				xmlFileDef.getFileIntegrityDefinition().getIntegrityType(),
				fileDef.getFileIntegrityDefinition().getIntegrityType());
		assertEquals(xmlFileDef.getFileIntegrityDefinition()
				.getIntegrityValue(), fileDef.getFileIntegrityDefinition()
				.getIntegrityValue());
	}

	@Test
	public void testSetFileName() {
		assertEquals(xmlFileDef.getFileName(), fileDef.getFileName());
		String newString = fileDef.getFileName() + "some_new_string";
		xmlFileDef.setFileName(newString);

		assertEquals(xmlFileDef.getFileName(), newString);
		assertNotEquals(xmlFileDef.getFileName(), fileDef.getFileName());
	}

	@Test
	public void testSetFileSize() {
		assertEquals(xmlFileDef.getFileSize(), fileDef.getFileSize());
		long newSize = xmlFileDef.getFileSize() + 100;
		xmlFileDef.setFileSize(newSize);

		assertEquals(xmlFileDef.getFileSize(), newSize);
		assertNotEquals(xmlFileDef.getFileSize(), fileDef.getFileSize());
	}

	@Test
	public void testIsFileLoadable() {
		assertEquals(xmlFileDef.isFileLoadable(), fileDef.isFileLoadable());
		xmlFileDef.setFileLoadable(!xmlFileDef.isFileLoadable());

		assertNotEquals(xmlFileDef.isFileLoadable(), fileDef.isFileLoadable());
	}

	@Test
	public void testSetFileIntegrityDefinition() {
		assertEquals(
				xmlFileDef.getFileIntegrityDefinition().getIntegrityType(),
				fileInteg.getIntegrityType());
		assertEquals(xmlFileDef.getFileIntegrityDefinition()
				.getIntegrityValue(), fileInteg.getIntegrityValue());

		XmlIntegrityDefinition newInteg = new XmlIntegrityDefinition();
		newInteg.setIntegrityType(fileInteg.getIntegrityType() - 1);
		newInteg.setIntegrityValue(fileInteg.getIntegrityValue() + "123");
		xmlFileDef.setFileIntegrityDefinition(newInteg);

		assertEquals(xmlFileDef.getFileIntegrityDefinition(), newInteg);
		assertNotEquals(xmlFileDef.getFileIntegrityDefinition()
				.getIntegrityType(), fileInteg.getIntegrityType());
		assertNotEquals(xmlFileDef.getFileIntegrityDefinition()
				.getIntegrityValue(), fileInteg.getIntegrityValue());
	}

	@Test
	public void testEquals() {
		assertNotEquals(xmlFileDef, new Object());
		XmlFileDefinition match = new XmlFileDefinition(fileDef);
		assertEquals(match, xmlFileDef);

		XmlFileDefinition noMatch1 = new XmlFileDefinition();
		noMatch1.setFileIntegrityDefinition(xmlFileDef
				.getFileIntegrityDefinition());
		noMatch1.setFileLoadable(xmlFileDef.isFileLoadable());
		noMatch1.setFileName(xmlFileDef.getFileName());
		noMatch1.setFileSize(xmlFileDef.getFileSize() - 1);
		assertNotEquals(noMatch1, xmlFileDef,
				"Objects should not be equal with different file sizes");

		noMatch1 = new XmlFileDefinition();
		noMatch1.setFileIntegrityDefinition(xmlFileDef
				.getFileIntegrityDefinition());
		noMatch1.setFileLoadable(xmlFileDef.isFileLoadable());
		noMatch1.setFileName(xmlFileDef.getFileName() + "asdf");
		noMatch1.setFileSize(xmlFileDef.getFileSize());
		assertNotEquals(noMatch1, xmlFileDef,
				"Objects should not be equal with different file names");

		noMatch1 = new XmlFileDefinition();
		noMatch1.setFileIntegrityDefinition(xmlFileDef
				.getFileIntegrityDefinition());
		noMatch1.setFileLoadable(!xmlFileDef.isFileLoadable());
		noMatch1.setFileName(xmlFileDef.getFileName());
		noMatch1.setFileSize(xmlFileDef.getFileSize());
		assertNotEquals(noMatch1, xmlFileDef,
				"Objects should not be equal with different loadable values");

		noMatch1 = new XmlFileDefinition();
		edu.cmu.sv.arinc838.specification.IntegrityDefinition newIntegDef = new XmlIntegrityDefinition();
		newIntegDef.setIntegrityType(fileInteg.getIntegrityType() - 1);
		newIntegDef.setIntegrityValue(fileInteg.getIntegrityValue() + "9");
		noMatch1.setFileIntegrityDefinition(newIntegDef);
		noMatch1.setFileLoadable(xmlFileDef.isFileLoadable());
		noMatch1.setFileName(xmlFileDef.getFileName());
		noMatch1.setFileSize(xmlFileDef.getFileSize());
		assertNotEquals(noMatch1, xmlFileDef,
				"Objects should not be equal with different integrity definitions");
	}

	@Test
	public void testHashcode() {
		XmlIntegrityDefinition integ1 = new XmlIntegrityDefinition();
		integ1.setIntegrityType(2);
		integ1.setIntegrityValue("012345678");

		XmlIntegrityDefinition integ2 = new XmlIntegrityDefinition();
		integ2.setIntegrityType(integ1.getIntegrityType());
		integ2.setIntegrityValue(integ1.getIntegrityValue());

		XmlIntegrityDefinition integ3 = new XmlIntegrityDefinition();
		integ3.setIntegrityType(3);
		integ3.setIntegrityValue("0123456789");

		XmlFileDefinition a = new XmlFileDefinition();
		a.setFileIntegrityDefinition(integ1);
		a.setFileLoadable(true);
		a.setFileName("file1");
		a.setFileSize(1234);

		XmlFileDefinition b = new XmlFileDefinition();
		b.setFileIntegrityDefinition(a.getFileIntegrityDefinition());
		b.setFileLoadable(a.isFileLoadable());
		b.setFileName(a.getFileName());
		b.setFileSize(a.getFileSize());

		assertEquals(a.hashCode(), b.hashCode());

		XmlFileDefinition diff = new XmlFileDefinition();
		diff.setFileIntegrityDefinition(integ3);
		diff.setFileLoadable(a.isFileLoadable());
		diff.setFileName(a.getFileName());
		diff.setFileSize(a.getFileSize());

		assertNotEquals(diff.hashCode(), a.hashCode(),
				"Object hash should not match for different file integrity definitions");
		
		diff.setFileIntegrityDefinition(integ1);
		diff.setFileLoadable(!a.isFileLoadable());
		diff.setFileName(a.getFileName());
		diff.setFileSize(a.getFileSize());

		assertNotEquals(diff.hashCode(), a.hashCode(),
				"Object hash should not match for different file loadable values");
		
		diff.setFileIntegrityDefinition(integ1);
		diff.setFileLoadable(a.isFileLoadable());
		diff.setFileName(a.getFileName() + "asdf");
		diff.setFileSize(a.getFileSize());

		assertNotEquals(diff.hashCode(), a.hashCode(),
				"Object hash should not match for different file names");
		
		diff.setFileIntegrityDefinition(integ1);
		diff.setFileLoadable(a.isFileLoadable());
		diff.setFileName(a.getFileName());
		diff.setFileSize(a.getFileSize() - 1);

		assertNotEquals(diff.hashCode(), a.hashCode(),
				"Object hash should not match for different file sizes");

	}
}
