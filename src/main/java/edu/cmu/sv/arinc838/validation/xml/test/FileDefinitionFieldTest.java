package edu.cmu.sv.arinc838.validation.xml.test;

import java.util.ArrayList;
import java.util.List;

import com.arinc.arinc838.FileDefinition;

import edu.cmu.sv.arinc838.dao.FileDefinitionDao;
import edu.cmu.sv.arinc838.validation.VerficationTest;

public class FileDefinitionFieldTest implements VerficationTest<List<FileDefinition>> {
	@Override
	public List<Exception> execute(List<FileDefinition> defs) {
		List<Exception> retExceptions = new ArrayList<Exception>();

		if (defs == null || defs.isEmpty()) {
			retExceptions.add(new IllegalArgumentException("There are no File Definitions defined"));
		} else {
			for (FileDefinition def : defs) {
//				TestUtils.checkPrimitives (def, FileDefinitionDao.class, retExceptions);
			}
		}
		
		return retExceptions;
	}

}
