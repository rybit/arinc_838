package edu.cmu.sv.arinc838.writer;

import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileDao;

public interface SdfWriter {

	public String write (String path, SoftwareDefinitionFileDao builder) throws Exception;	
}
