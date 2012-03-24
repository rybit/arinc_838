package edu.cmu.sv.arinc838.builder;

import java.io.IOException;

import com.arinc.arinc838.IntegrityDefinition;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao;

public class IntegrityDefinitionBuilder implements Builder<IntegrityDefinition> {

	public IntegrityDefinitionBuilder(IntegrityDefinitionDao integDao) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IntegrityDefinition buildXml() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int buildBinary(BdfFile bdfFile) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}
}
