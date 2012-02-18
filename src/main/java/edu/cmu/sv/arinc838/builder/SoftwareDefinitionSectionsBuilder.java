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

import edu.cmu.sv.arinc838.validation.DataValidator;

public class SoftwareDefinitionSectionsBuilder implements Builder<SdfSections> {

	private List<FileDefinitionBuilder> fileDefinitions = new ArrayList<FileDefinitionBuilder>();
	private List<TargetHardwareDefinitionBuilder> thwDefinitions = new ArrayList<TargetHardwareDefinitionBuilder>();
	private SoftwareDescriptionBuilder softwareDescription;
	private IntegrityDefinitionBuilder lspIntegrityDefinition;
	private IntegrityDefinitionBuilder sdfIntegrityDefinition;

	public SoftwareDefinitionSectionsBuilder() {
	}

	@SuppressWarnings("unchecked")
	public SoftwareDefinitionSectionsBuilder(SdfSections sdfSections) {

		List<FileDefinition> fileDefs = (List<FileDefinition>) DataValidator
				.validateList1(sdfSections.getFileDefinitions());

		for (FileDefinition fileDef : fileDefs) {
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

	@SuppressWarnings("unchecked")
	@Override
	public SdfSections build() {
		SdfSections sdfSections = new SdfSections();

		// we have to re-validate this as a LIST1 since it can be modified
		// without a set method to verify its validity prior to building
		List<FileDefinitionBuilder> fileDefsValidated = (List<FileDefinitionBuilder>) DataValidator
				.validateList1(fileDefinitions);
		for (FileDefinitionBuilder fileDef : fileDefsValidated) {
			sdfSections.getFileDefinitions().add(fileDef.build());
		}

		for (TargetHardwareDefinitionBuilder thwDef : thwDefinitions) {
			sdfSections.getThwDefinitions().add(thwDef.build());
		}

		sdfSections.setLspIntegrityDefinition(lspIntegrityDefinition.build());
		sdfSections.setSdfIntegrityDefinition(sdfIntegrityDefinition.build());
		sdfSections.setSoftwareDescription(softwareDescription.build());

		return sdfSections;
	}
}
