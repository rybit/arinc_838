package edu.cmu.sv.arinc838.writer;

import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;

public interface SdfWriter {
	public void write (String filename, SoftwareDefinitionFileBuilder builder) throws Exception;
}
