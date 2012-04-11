package edu.cmu.sv.arinc838.builder;

import java.io.IOException;

import com.arinc.arinc838.IntegrityDefinition;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao;

public class IntegrityDefinitionBuilder implements Builder<IntegrityDefinitionDao, IntegrityDefinition> {

	@Override
	public IntegrityDefinition buildXml(IntegrityDefinitionDao integDao) {
		IntegrityDefinition retDef = new IntegrityDefinition();

		retDef.setIntegrityType(integDao.getIntegrityType());
		retDef.setIntegrityValue(integDao.getIntegrityValue());

		return retDef;
	}

	@Override
	public int buildBinary(IntegrityDefinitionDao integDao, BdfFile bdfFile) throws IOException {
		int initialPosition = (int) bdfFile.getFilePointer();

		bdfFile.writeUint32(integDao.getIntegrityType());
		bdfFile.writeHexbin64k(integDao.getIntegrityValue());

		int finalPosition = (int) bdfFile.getFilePointer();

		return (int) (finalPosition - initialPosition);
	}
}
