/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 7, 2012
 */
package edu.cmu.sv.arinc838.builder;

import java.util.ArrayList;
import java.util.List;

import com.arinc.arinc838.FileDefinition;
import com.arinc.arinc838.SdfSections;
import com.arinc.arinc838.ThwDefinition;

public class SoftwareDefinitionSectionsBuilder implements Builder<SdfSections> {

	private List<FileDefinitionBuilder> fileDefinitions = new ArrayList<FileDefinitionBuilder>();
	private List<TargetHardwareDefinitionBuilder> thwDefinitions = new ArrayList<TargetHardwareDefinitionBuilder>();
	private SoftwareDescriptionBuilder softwareDescription;
	private IntegrityDefinitionBuilder lspIntegrityDefinition;
	private IntegrityDefinitionBuilder sdfIntegrityDefinition;

	public SoftwareDefinitionSectionsBuilder() {
	}

	public SoftwareDefinitionSectionsBuilder(SdfSections sdfSections) {
		
		for (FileDefinition fileDef : sdfSections.getFileDefinitions()) {
			fileDefinitions.add(new FileDefinitionBuilder(fileDef));
		}
		
		for (ThwDefinition thwDef : sdfSections.getThwDefinitions()) {
			thwDefinitions.add(new TargetHardwareDefinitionBuilder(thwDef));
		}

		softwareDescription = new SoftwareDescriptionBuilder(
				sdfSections.getSoftwareDescription());
		lspIntegrityDefinition = new IntegrityDefinitionBuilder(
				sdfSections.getLspIntegrityDefinition());
		sdfIntegrityDefinition = new IntegrityDefinitionBuilder(
				sdfSections.getSdfIntegrityDefinition());
	}
	
	public SoftwareDescriptionBuilder getSoftwareDescription() {
		return softwareDescription;
	}

	public void setSoftwareDescription(SoftwareDescriptionBuilder sd) {
		softwareDescription = sd;
	}

	public List<TargetHardwareDefinitionBuilder> getTargetHardwareDefinitions() {
		return thwDefinitions;
	}

	public List<FileDefinitionBuilder> getFileDefinitions() {
		return fileDefinitions;
	}

	public IntegrityDefinitionBuilder getSdfIntegrityDefinition() {
		return this.sdfIntegrityDefinition;
	}

	public void setSdfIntegrityDefinition(IntegrityDefinitionBuilder value) {
		this.sdfIntegrityDefinition = value;
	}

	public IntegrityDefinitionBuilder getLspIntegrityDefinition() {
		return this.lspIntegrityDefinition;
	}

	public void setLspIntegrityDefinition(IntegrityDefinitionBuilder value) {
		this.lspIntegrityDefinition = value;
	}

	@Override
	public SdfSections build() {
		SdfSections sdfSections = new SdfSections();
		
		for (FileDefinitionBuilder fileDef : 			fileDefinitions) {
			sdfSections.getFileDefinitions().add(fileDef.build());
		}
		
		for (TargetHardwareDefinitionBuilder thwDef : thwDefinitions) {
			sdfSections.getThwDefinitions().add(thwDef.build());
		}
		
		sdfSections.setLspIntegrityDefinition(				lspIntegrityDefinition.build());
		sdfSections.setSdfIntegrityDefinition(sdfIntegrityDefinition.build());
		sdfSections.setSoftwareDescription(softwareDescription.build());

		return sdfSections;
	}
}
