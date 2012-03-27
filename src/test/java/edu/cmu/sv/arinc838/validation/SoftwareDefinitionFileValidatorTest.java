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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.cmu.sv.arinc838.dao.FileDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.dao.SoftwareDescriptionDao;
import edu.cmu.sv.arinc838.dao.TargetHardwareDefinitionDao;

public class SoftwareDefinitionFileValidatorTest {

	private SoftwareDefinitionFileValidator sdfVal;
	private DataValidator dataVal;

	@BeforeMethod
	public void setUp() {
		dataVal = mock(DataValidator.class);
	}

	@Test
	public void testValidateSdfFile() {

		SoftwareDefinitionFileDao sdfDao = mock(SoftwareDefinitionFileDao.class);
		when(sdfDao.getSoftwareDescription()).thenReturn(
				mock(SoftwareDescriptionDao.class));
		when(sdfDao.getTargetHardwareDefinitions()).thenReturn(mock(List.class));
		when(sdfDao.getFileDefinitions()).thenReturn(mock(List.class));
		when(sdfDao.getSdfIntegrityDefinition()).thenReturn(mock(IntegrityDefinitionDao.class));
		when(sdfDao.getLspIntegrityDefinition()).thenReturn(mock(IntegrityDefinitionDao.class));

		sdfVal = new SoftwareDefinitionFileValidator(dataVal) {

			@Override
			public List<Exception> validateSoftwareDescription(
					SoftwareDescriptionDao softwareDesc) {
				softwareDesc.getSoftwarePartnumber();
				return null;
			}

			@Override
			public List<Exception> validateTargetHardwareDefinitions(
					List<TargetHardwareDefinitionDao> thwDefs) {
				thwDefs.isEmpty();
				return null;
			}
			
			@Override
			public List<Exception> validateFileDefinitions(List<FileDefinitionDao> fileDefs)
			{
				fileDefs.isEmpty();
				return null;
			}
			
			@Override
			public List<Exception> validateSdfIntegrityDefinition(IntegrityDefinitionDao sdfInteg)
			{
				sdfInteg.getIntegrityType();
				return null;
			}
			
			@Override
			public List<Exception> validateLspIntegrityDefinition(IntegrityDefinitionDao lspInteg)
			{
				lspInteg.getIntegrityType();
				return null;
			}
		};

		sdfVal.validateSdfFile(sdfDao);

		verify(sdfDao.getSoftwareDescription()).getSoftwarePartnumber();
		verify(sdfDao.getTargetHardwareDefinitions()).isEmpty();
		verify(sdfDao.getFileDefinitions()).isEmpty();
		verify(sdfDao.getSdfIntegrityDefinition()).getIntegrityType();
		verify(sdfDao.getLspIntegrityDefinition()).getIntegrityType();
	}
}
