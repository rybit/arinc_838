package edu.cmu.sv.arinc838.specification;

import java.math.BigInteger;
import java.util.Collection;

import com.arinc.arinc838.SdfSections;

public interface SoftwareDefinition {

	public long getFileFormatVersion();

	public void setFileFormatVersion(long value);

	public Collection<LoadableSoftwarePart> getSoftwareDefinitionSections();

	public void setSoftwareDefinitionSections(Collection<LoadableSoftwarePart> value);

}