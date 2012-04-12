/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Mar 26, 2012
 */
package edu.cmu.sv.arinc838.validation;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mockito.InOrder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.cmu.sv.arinc838.crc.Crc16Generator;
import edu.cmu.sv.arinc838.dao.FileDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao.IntegrityType;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.dao.SoftwareDescriptionDao;
import edu.cmu.sv.arinc838.dao.TargetHardwareDefinitionDao;
import edu.cmu.sv.arinc838.util.Converter;

public class SoftwareDefinitionFileValidatorTest {

	private DataValidator dataVal;

	@BeforeMethod
	public void setUp() {
		dataVal = mock(DataValidator.class);
	}

	private List<Exception> errorList(String message) {
		List<Exception> errors = new ArrayList<Exception>();
		errors.add(new Exception(message));
		return errors;
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testValidateSdfFile() {

		SoftwareDefinitionFileDao sdfDao = mock(SoftwareDefinitionFileDao.class);

		when(dataVal.validateFileFormatVersion(any(byte[].class))).thenThrow(
				new IllegalArgumentException("0"));

		when(sdfDao.getFileFormatVersion()).thenReturn(
				SoftwareDefinitionFileDao.DEFAULT_FILE_FORMAT_VERSION);
		when(sdfDao.getSoftwareDescription()).thenReturn(
				mock(SoftwareDescriptionDao.class));
		when(sdfDao.getTargetHardwareDefinitions())
				.thenReturn(mock(List.class));
		when(sdfDao.getFileDefinitions()).thenReturn(mock(List.class));
		when(sdfDao.getSdfIntegrityDefinition()).thenReturn(
				mock(IntegrityDefinitionDao.class));
		when(sdfDao.getLspIntegrityDefinition()).thenReturn(
				mock(IntegrityDefinitionDao.class));

		SoftwareDefinitionFileValidator sdfVal = new SoftwareDefinitionFileValidator(
				dataVal) {

			@Override
			public List<Exception> validateSoftwareDescription(
					SoftwareDescriptionDao softwareDesc, String sourceFile) {
				softwareDesc.getSoftwarePartnumber();
				return errorList("1");
			}

			@Override
			public List<Exception> validateTargetHardwareDefinitions(
					List<TargetHardwareDefinitionDao> thwDefs, String sourceFile) {
				thwDefs.isEmpty();
				return errorList("2");
			}

			@Override
			public List<Exception> validateFileDefinitions(
					List<FileDefinitionDao> fileDefs) {
				fileDefs.isEmpty();
				return errorList("3");
			}

			@Override
			public List<Exception> validateSdfIntegrityDefinition(
					IntegrityDefinitionDao sdfInteg) {
				sdfInteg.getIntegrityType();
				return errorList("4");
			}

			@Override
			public List<Exception> validateLspIntegrityDefinition(
					IntegrityDefinitionDao lspInteg) {
				lspInteg.getIntegrityType();
				return errorList("5");
			}
		};

		List<Exception> errors = sdfVal.validateSdfFile(sdfDao, "abc");
		assertEquals(errors.size(), 6);
		for (int i = 0; i < errors.size(); i++) {
			assertEquals(errors.get(i).getMessage(), i + "");
		}

		verify(sdfDao.getSoftwareDescription()).getSoftwarePartnumber();
		verify(sdfDao.getTargetHardwareDefinitions()).isEmpty();
		verify(sdfDao.getFileDefinitions()).isEmpty();
		verify(sdfDao.getSdfIntegrityDefinition()).getIntegrityType();
		verify(sdfDao.getLspIntegrityDefinition()).getIntegrityType();
		verify(dataVal)
				.validateFileFormatVersion(sdfDao.getFileFormatVersion());
	}

	@Test
	public void testValidateSdfFileXmlvsBinary() {

		SoftwareDefinitionFileDao sdfDao = ReferenceData.SDF_TEST_FILE;

		DataValidator dataVal = new DataValidator() {
			@Override
			public List<Exception> validateStr64kXml(String value) {
				ArrayList<Exception> errors = new ArrayList<Exception>();
				errors.add(new Exception("xml"));

				return errors;
			}

			@Override
			public List<Exception> validateStr64kBinary(String value) {
				ArrayList<Exception> errors = new ArrayList<Exception>();
				errors.add(new Exception("binary"));

				return errors;
			}
		};
		SoftwareDefinitionFileValidator sdfVal = new SoftwareDefinitionFileValidator(
				dataVal) {
			
			@Override
			public List<Exception> validateFileDefinition(
					FileDefinitionDao fileDef) {
				return new ArrayList<Exception>();
			}

			@Override
			public List<Exception> validateIntegrityDefinition(
					IntegrityDefinitionDao integDef, byte[] data) {
				return new ArrayList<Exception>();
			}
		};

		List<Exception> errors = sdfVal.validateSdfFile(sdfDao,
				sdfDao.getXmlFileName());
		assertEquals(errors.size(), 3);
		for (Exception e : errors) {
			assertEquals(e.getMessage(), "xml");
		}
		errors = sdfVal.validateSdfFile(sdfDao, sdfDao.getBinaryFileName());
		assertEquals(errors.size(), 3);
		for (Exception e : errors) {
			assertEquals(e.getMessage(), "binary");
		}

	}

	@Test
	public void testValidateSoftwareDescriptionSoftwarePartnumber() {
		SoftwareDescriptionDao softDesc = mock(SoftwareDescriptionDao.class);
		when(softDesc.getSoftwarePartnumber()).thenReturn("ACM47-1234-5677");
		when(
				dataVal.validateSoftwarePartNumber(softDesc
						.getSoftwarePartnumber())).thenThrow(
				new IllegalArgumentException("0"));

		SoftwareDefinitionFileValidator sdfVal = new SoftwareDefinitionFileValidator(
				dataVal);

		List<Exception> errors = sdfVal.validateSoftwareDescription(softDesc,
				"ACM4712345678.XDF");
		assertEquals(errors.size(), 2);
		assertEquals(errors.get(0).getMessage(), "0");
	}

	@Test
	public void testValidateSoftwareDescriptionSoftwareTypeDescription() {
		SoftwareDescriptionDao softDesc = mock(SoftwareDescriptionDao.class);
		when(softDesc.getSoftwarePartnumber()).thenReturn(
				ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);
		when(softDesc.getSoftwareTypeDescription()).thenReturn("123");
		when(dataVal.validateStr64kXml(softDesc.getSoftwareTypeDescription()))
				.thenReturn(errorList("0"));

		SoftwareDefinitionFileValidator sdfVal = new SoftwareDefinitionFileValidator(
				dataVal);

		List<Exception> errors = sdfVal.validateSoftwareDescription(
				softDesc,
				ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE
						.replaceAll("-", "") + ".XDF");
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getMessage(), "0");
	}

	@Test
	public void testValidateSoftwareDescriptionSoftwareTypeId() {
		SoftwareDescriptionDao softDesc = mock(SoftwareDescriptionDao.class);
		when(softDesc.getSoftwarePartnumber()).thenReturn(
				ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE);

		when(softDesc.getSoftwareTypeId()).thenReturn(new byte[] {});
		when(dataVal.validateHexbin32(softDesc.getSoftwareTypeId())).thenThrow(
				new IllegalArgumentException("0"));

		SoftwareDefinitionFileValidator sdfVal = new SoftwareDefinitionFileValidator(
				dataVal);

		List<Exception> errors = sdfVal.validateSoftwareDescription(
				softDesc,
				ReferenceData.SOFTWARE_PART_NUMBER_REFERENCE
						.replaceAll("-", "") + ".XDF");
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getMessage(), "0");
	}

	@Test
	public void testValidateTargetHardwareDefinitions() {
		when(dataVal.validateStr64kXml("0")).thenReturn(errorList("0"));
		when(dataVal.validateStr64kXml("1")).thenReturn(errorList("1"));
		when(dataVal.validateStr64kXml("2")).thenReturn(errorList("2"));

		TargetHardwareDefinitionDao thwDef1 = mock(TargetHardwareDefinitionDao.class);
		when(thwDef1.getPositions()).thenReturn(
				Arrays.asList(new String[] { "0" }));

		TargetHardwareDefinitionDao thwDef2 = mock(TargetHardwareDefinitionDao.class);
		when(thwDef2.getPositions()).thenReturn(
				Arrays.asList(new String[] { "1", "2" }));

		TargetHardwareDefinitionDao thwDef3 = mock(TargetHardwareDefinitionDao.class);
		when(thwDef3.getPositions()).thenReturn(null);

		List<TargetHardwareDefinitionDao> thwDefs = Arrays
				.asList(new TargetHardwareDefinitionDao[] { thwDef1, thwDef2,
						thwDef3 });

		SoftwareDefinitionFileValidator sdfVal = new SoftwareDefinitionFileValidator(
				dataVal);

		List<Exception> errors = sdfVal.validateTargetHardwareDefinitions(
				thwDefs, "file.XDF");
		assertEquals(errors.size(), 3);
		for (int i = 0; i < errors.size(); i++) {
			assertEquals(errors.get(i).getMessage(), i + "");
		}
	}

	@Test
	public void testValidateFileDefinitions() {
		SoftwareDefinitionFileValidator sdfVal = new SoftwareDefinitionFileValidator(
				dataVal) {

			@Override
			public List<Exception> validateFileDefinition(
					FileDefinitionDao fileDef) {
				fileDef.getFileName();
				return errorList(fileDef.getFileName());
			}
		};

		FileDefinitionDao fileDef1 = mock(FileDefinitionDao.class);
		when(fileDef1.getFileName()).thenReturn("a");

		FileDefinitionDao fileDef2 = mock(FileDefinitionDao.class);
		when(fileDef2.getFileName()).thenReturn("b");

		FileDefinitionDao fileDef3 = mock(FileDefinitionDao.class);
		when(fileDef3.getFileName()).thenReturn("b");

		List<FileDefinitionDao> fileDefs = Arrays
				.asList(new FileDefinitionDao[] { fileDef1, fileDef2 });

		InOrder order = inOrder(dataVal);
		when(dataVal.validateList1(fileDefs)).thenThrow(
				new IllegalArgumentException("0"));

		List<Exception> errors = sdfVal.validateFileDefinitions(fileDefs);

		order.verify(dataVal).validateList1(fileDefs);
		order.verify(dataVal).validateDataFileNamesAreUnique(fileDefs);

		assertEquals(errors.size(), 3);
		assertEquals(errors.get(0).getMessage(), "0");
		assertEquals(errors.get(1).getMessage(), fileDef1.getFileName());
		assertEquals(errors.get(2).getMessage(), fileDef2.getFileName());
	}

	@Test
	public void testValidateFileDefinitionFileName() {
		FileDefinitionDao fileDef = mock(FileDefinitionDao.class);
		when(fileDef.getFileName()).thenReturn("123");
		when(dataVal.validateDataFileName(fileDef.getFileName())).thenReturn(
				errorList("0"));

		SoftwareDefinitionFileValidator sdfVal = new SoftwareDefinitionFileValidator(
				dataVal) {
			@Override
			public List<Exception> validateIntegrityDefinition(
					IntegrityDefinitionDao integDef, byte[] data) {
				return new ArrayList<Exception>();
			}
		};

		List<Exception> errors = sdfVal.validateFileDefinition(fileDef);
		assertEquals(errors.size(), 2);
		assertEquals(errors.get(0).getMessage(), "0");
	}

	@Test
	public void testValidateFileDefinitionFileSize() {
		FileDefinitionDao fileDef = mock(FileDefinitionDao.class);
		when(fileDef.getFileSize()).thenReturn(1234L);
		when(fileDef.getFileName()).thenReturn("someFile");
		when(dataVal.validateUint32(fileDef.getFileSize())).thenThrow(
				new IllegalArgumentException("0"));

		SoftwareDefinitionFileValidator sdfVal = new SoftwareDefinitionFileValidator(
				dataVal) {
			@Override
			public List<Exception> validateIntegrityDefinition(
					IntegrityDefinitionDao integDef, byte[] data) {
				return new ArrayList<Exception>();
			}
		};

		List<Exception> errors = sdfVal.validateFileDefinition(fileDef);
		assertEquals(errors.size(), 2);
		assertEquals(errors.get(0).getMessage(), "0");
	}

	@Test
	public void testValidateFileDefinitionIntegrityDefinition() {
		SoftwareDefinitionFileValidator sdfVal = new SoftwareDefinitionFileValidator(
				dataVal) {

			@Override
			public List<Exception> validateIntegrityDefinition(
					IntegrityDefinitionDao integDef, byte[] data) {
				return errorList("0");
			}
		};

		FileDefinitionDao fileDef = mock(FileDefinitionDao.class);
		when(fileDef.getFileIntegrityDefinition()).thenReturn(
				new IntegrityDefinitionDao());
		when(fileDef.getFileName()).thenReturn("someFile");

		List<Exception> errors = sdfVal.validateFileDefinition(fileDef);

		assertEquals(errors.size(), 2);
		assertEquals(errors.get(1).getMessage(), "0");
	}

	@Test
	public void testValidateFileDefinitionIntegrityDefinitionCrcCheckPass() {
		SoftwareDefinitionFileValidator sdfVal = new SoftwareDefinitionFileValidator(
				dataVal);

		FileDefinitionDao fileDef = new FileDefinitionDao();
		fileDef.setFileName("src/test/resources/crc_test_files/CRC_T05A.rom");
		fileDef.setFileSize(3976);
		IntegrityDefinitionDao integ = new IntegrityDefinitionDao();
		integ.setIntegrityType(IntegrityType.CRC32.getType());
		integ.setIntegrityValue(Converter.hexToBytes("0096142DCA"));
		fileDef.setFileIntegrityDefinition(integ);

		List<Exception> errors = sdfVal.validateFileDefinition(fileDef);
		assertEquals(errors.size(), 0);
	}

	@Test
	public void testValidateIntegrityDefinitionIntegrityType() {
		IntegrityDefinitionDao integDef = mock(IntegrityDefinitionDao.class);
		when(integDef.getIntegrityType()).thenReturn(
				IntegrityType.CRC16.getType());
		when(dataVal.validateIntegrityType(integDef.getIntegrityType()))
				.thenThrow(new IllegalArgumentException("0"));

		SoftwareDefinitionFileValidator sdfVal = new SoftwareDefinitionFileValidator(
				dataVal);

		List<Exception> errors = sdfVal.validateIntegrityDefinition(integDef,
				null);
		assertEquals(errors.size(), 2);
		assertEquals(errors.get(0).getMessage(), "0");
	}

	@Test
	public void testValidateIntegrityDefinitionIntegrityValue() {
		IntegrityDefinitionDao integDef = mock(IntegrityDefinitionDao.class);
		when(integDef.getIntegrityValue())
				.thenReturn(new byte[] { 1, 2, 3, 4 });
		when(dataVal.validateIntegrityValue(integDef.getIntegrityValue()))
				.thenThrow(new IllegalArgumentException("0"));

		SoftwareDefinitionFileValidator sdfVal = new SoftwareDefinitionFileValidator(
				dataVal);

		List<Exception> errors = sdfVal.validateIntegrityDefinition(integDef,
				null);
		assertEquals(errors.size(), 2);
		assertEquals(errors.get(0).getMessage(), "0");
	}

	@Test
	public void testValidateIntegrityDefinitionCrc16() {
		IntegrityDefinitionDao integDef = mock(IntegrityDefinitionDao.class);
		when(integDef.getIntegrityValue())
				.thenReturn(new byte[] { 0, 0, 0, 0 });
		when(integDef.getIntegrityType()).thenReturn(
				IntegrityType.CRC16.getType());

		SoftwareDefinitionFileValidator sdfVal = new SoftwareDefinitionFileValidator(
				dataVal);
		byte[] data = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
		List<Exception> errors = sdfVal.validateIntegrityDefinition(integDef,
				data);
		assertEquals(errors.size(), 1);
	}

	@Test
	public void testValidateIntegrityDefinitionCrc32() {
		IntegrityDefinitionDao integDef = mock(IntegrityDefinitionDao.class);
		when(integDef.getIntegrityValue())
				.thenReturn(new byte[] { 0, 0, 0, 0 });
		when(integDef.getIntegrityType()).thenReturn(
				IntegrityType.CRC32.getType());

		SoftwareDefinitionFileValidator sdfVal = new SoftwareDefinitionFileValidator(
				dataVal);
		byte[] data = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
		List<Exception> errors = sdfVal.validateIntegrityDefinition(integDef,
				data);
		assertEquals(errors.size(), 1);
	}

	@Test
	public void testValidateIntegrityDefinitionCrc64() {
		IntegrityDefinitionDao integDef = mock(IntegrityDefinitionDao.class);
		when(integDef.getIntegrityValue())
				.thenReturn(new byte[] { 0, 0, 0, 0 });
		when(integDef.getIntegrityType()).thenReturn(
				IntegrityType.CRC64.getType());

		SoftwareDefinitionFileValidator sdfVal = new SoftwareDefinitionFileValidator(
				dataVal);
		byte[] data = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
		List<Exception> errors = sdfVal.validateIntegrityDefinition(integDef,
				data);
		assertEquals(errors.size(), 1);
	}

	@Test
	public void testValidateSdfIntegrityDefinition() {
		SoftwareDefinitionFileValidator sdfVal = new SoftwareDefinitionFileValidator(
				dataVal) {

			@Override
			public List<Exception> validateIntegrityDefinition(
					IntegrityDefinitionDao integDef, byte[] data) {
				return errorList("0");
			}

		};

		List<Exception> errors = sdfVal
				.validateSdfIntegrityDefinition(new IntegrityDefinitionDao());

		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getMessage(), "0");
	}

	@Test
	public void testValidateLspIntegrityDefinition() {
		SoftwareDefinitionFileValidator sdfVal = new SoftwareDefinitionFileValidator(
				dataVal) {

			@Override
			public List<Exception> validateIntegrityDefinition(
					IntegrityDefinitionDao integDef, byte[] data) {
				return errorList("0");
			}

		};

		List<Exception> errors = sdfVal
				.validateLspIntegrityDefinition(new IntegrityDefinitionDao());

		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getMessage(), "0");
	}

}
