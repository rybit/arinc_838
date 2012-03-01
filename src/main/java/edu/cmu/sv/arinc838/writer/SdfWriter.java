package edu.cmu.sv.arinc838.writer;

import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;

public interface SdfWriter {
	public void write (String path, SoftwareDefinitionFileBuilder builder) throws Exception;
	public String getFileNameAndPath(String path, SoftwareDefinitionFileBuilder builder);
}
