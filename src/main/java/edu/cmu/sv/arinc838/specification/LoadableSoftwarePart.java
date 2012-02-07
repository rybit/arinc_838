package edu.cmu.sv.arinc838.specification;

import com.arinc.arinc838.FileDefinitions;
import com.arinc.arinc838.LspIntegrityDefinition;
import com.arinc.arinc838.SdfIntegrityDefinition;
import com.arinc.arinc838.SoftwareDescription;
import com.arinc.arinc838.ThwDefinitions;

public interface LoadableSoftwarePart {

    public String getSoftwarePartnumber();

    public void setSoftwarePartnumber(String value) ;

    public String getSoftwareTypeDescription() ;

    public void setSoftwareTypeDescription(String value) ;

    public long getSoftwareTypeId() ;

    public void setSoftwareTypeId(long value) ;

	public void setSoftwareDescription(SoftwareDescription value);

	public ThwDefinitions getThwDefinitions();

	public void setThwDefinitions(ThwDefinitions value);

	public FileDefinitions getFileDefinitions();

	public void setFileDefinitions(FileDefinitions value);

	public SdfIntegrityDefinition getSdfIntegrityDefinition();

	public void setSdfIntegrityDefinition(SdfIntegrityDefinition value);

	public LspIntegrityDefinition getLspIntegrityDefinition();

	public void setLspIntegrityDefinition(LspIntegrityDefinition value);

}