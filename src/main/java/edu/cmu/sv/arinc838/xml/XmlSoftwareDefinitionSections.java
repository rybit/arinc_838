package edu.cmu.sv.arinc838.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.arinc.arinc838.SdfSections;

import edu.cmu.sv.arinc838.specification.FileDefinition;
import edu.cmu.sv.arinc838.specification.IntegrityDefinition;
import edu.cmu.sv.arinc838.specification.SoftwareDefinitionSections;
import edu.cmu.sv.arinc838.specification.SoftwareDescription;
import edu.cmu.sv.arinc838.specification.TargetHardwareDefinition;

public class XmlSoftwareDefinitionSections implements
		SoftwareDefinitionSections {

	private List<FileDefinition> fileDefinitions = new ArrayList<FileDefinition>();
	private SoftwareDescription softwareDescription;

	public XmlSoftwareDefinitionSections(SdfSections sdfSections) {
		for (com.arinc.arinc838.FileDefinition fileDefinition : sdfSections
				.getFileDefinitions()) {
			fileDefinitions.add(new XmlFileDefinition(fileDefinition));
		}

		softwareDescription = new XmlSoftwareDescription(sdfSections.getSoftwareDescription());
	}

	@Override
	public SoftwareDescription getSoftwareDescription() {
		return softwareDescription;
	}

	@Override
	public void setSoftwareDescription(SoftwareDescription sd) {
		softwareDescription = sd;
	}

	@Override
	public List<TargetHardwareDefinition> getTargetHardwareDefinitions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FileDefinition> getFileDefinitions() {
		return fileDefinitions;
	}

	@Override
	public IntegrityDefinition getSdfIntegrityDefinition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSdfIntegrityDefinition(IntegrityDefinition sdfIntegDefs) {
		// TODO Auto-generated method stub

	}

	@Override
	public IntegrityDefinition getLspIntegrityDefinition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLspIntegrityDefinition(IntegrityDefinition lspIntegDefs) {
		// TODO Auto-generated method stub

	}

}
