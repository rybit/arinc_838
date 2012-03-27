package edu.cmu.sv.arinc838.writer;

import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;

public interface SdfWriter {
	public void write (String path, SoftwareDefinitionFileDao sdfDao) throws Exception;
	public String getFilename (SoftwareDefinitionFileDao sdfDao);
}
