/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 17, 2012
 */
package edu.cmu.sv.arinc838.validation;

import edu.cmu.sv.arinc838.dao.FileDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.dao.SoftwareDescriptionDao;
import edu.cmu.sv.arinc838.dao.TargetHardwareDefinitionDao;
import edu.cmu.sv.arinc838.util.Converter;

/**
 * Simple data class for holding reference data or other test values that can be
 * used when initializing objects.
 * 
 * @author Mike Deats
 * 
 * 
 */
public class ReferenceData {

	public static final String SOFTWARE_PART_NUMBER_REFERENCE = "ACM47-1234-5678";

	public static final SoftwareDefinitionFileDao SDF_TEST_FILE = new SoftwareDefinitionFileDao();

	static {
		SDF_TEST_FILE.setFileFormatVersion(SoftwareDefinitionFileDao.DEFAULT_FILE_FORMAT_VERSION);
		
		SoftwareDescriptionDao desc = new SoftwareDescriptionDao();
		desc.setSoftwarePartnumber(SOFTWARE_PART_NUMBER_REFERENCE);
		desc.setSoftwareTypeDescription("STD");
		desc.setSoftwareTypeId(Converter.hexToBytes("00000042"));
		SDF_TEST_FILE.setSoftwareDescription(desc);

		TargetHardwareDefinitionDao thw1 = new TargetHardwareDefinitionDao();
		thw1.setThwId("ID1");
		TargetHardwareDefinitionDao thw2 = new TargetHardwareDefinitionDao();
		thw2.setThwId("ID2");
		TargetHardwareDefinitionDao thw3 = new TargetHardwareDefinitionDao();
		thw3.setThwId("ID3");
		thw3.addPosition("L");
		thw3.addPosition("R");
		thw3.setIsLast(false);
		SDF_TEST_FILE.getTargetHardwareDefinitions().add(thw1);
		SDF_TEST_FILE.getTargetHardwareDefinitions().add(thw2);
		SDF_TEST_FILE.getTargetHardwareDefinitions().add(thw3);

		FileDefinitionDao fileDef1 = new FileDefinitionDao();
		fileDef1.setFileLoadable(true);
		fileDef1.setFileName("CRC_T05A.rom");
		fileDef1.setFileSize(3976);
		IntegrityDefinitionDao integ1 = new IntegrityDefinitionDao();
		integ1.setIntegrityType((long) 3);
		integ1.setIntegrityValue(Converter.hexToBytes("96142DCA"));
		fileDef1.setFileIntegrityDefinition(integ1);

		FileDefinitionDao fileDef2 = new FileDefinitionDao();
		fileDef2.setFileLoadable(true);
		fileDef2.setFileName("CRC_T06A.rom");
		fileDef2.setFileSize(18152);
		IntegrityDefinitionDao integ2 = new IntegrityDefinitionDao();
		integ2.setIntegrityType((long) 3);
		integ2.setIntegrityValue(Converter.hexToBytes("AE34897C"));
		fileDef2.setFileIntegrityDefinition(integ2);
		SDF_TEST_FILE.getFileDefinitions().add(fileDef1);
		SDF_TEST_FILE.getFileDefinitions().add(fileDef2);
		
		IntegrityDefinitionDao sdfInteg = new IntegrityDefinitionDao();
		sdfInteg.setIntegrityType((long)3);
		sdfInteg.setIntegrityValue(Converter.hexToBytes("0000000A"));
		SDF_TEST_FILE.setSdfIntegrityDefinition(sdfInteg);
		
		IntegrityDefinitionDao lspInteg = new IntegrityDefinitionDao();
		lspInteg.setIntegrityType((long)3);
		lspInteg.setIntegrityValue(Converter.hexToBytes("0000000A"));		
		SDF_TEST_FILE.setLspIntegrityDefinition(lspInteg);
	}
}
