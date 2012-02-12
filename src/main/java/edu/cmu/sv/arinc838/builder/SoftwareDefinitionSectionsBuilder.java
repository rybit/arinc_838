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
import com.arinc.arinc838.IntegrityDefinition;
import com.arinc.arinc838.SdfSections;
import com.arinc.arinc838.SoftwareDescription;
import com.arinc.arinc838.ThwDefinition;

public class SoftwareDefinitionSectionsBuilder {

	private List<FileDefinition> fileDefinitions = new ArrayList<FileDefinition>();
	private List<ThwDefinition> thwDefinitions = new ArrayList<ThwDefinition>();
	private SoftwareDescription softwareDescription;
	private IntegrityDefinition lspIntegrityDefinition;
	private IntegrityDefinition sdfIntegrityDefinition;

	public SoftwareDefinitionSectionsBuilder() {
	}

	public SoftwareDefinitionSectionsBuilder(SdfSections sdfSections) {
		copyFileDefitions(sdfSections.getFileDefinitions(), fileDefinitions);
		copyThwDefitions(sdfSections.getThwDefinitions(), thwDefinitions);

		softwareDescription = new SoftwareDescriptionBuilder(
				sdfSections.getSoftwareDescription()).build();
		lspIntegrityDefinition = new IntegrityDefinitionBuilder(
				sdfSections.getLspIntegrityDefinition()).build();
		sdfIntegrityDefinition = new IntegrityDefinitionBuilder(
				sdfSections.getSdfIntegrityDefinition()).build();
	}

	private void copyFileDefitions(List<FileDefinition> srcList,
			List<FileDefinition> destList) {

		if (srcList != null && destList != null) {
			destList.clear();
			for (FileDefinition fileDef : srcList) {
				destList.add(new FileDefinitionBuilder(fileDef).build());
			}
		}
	}

	private void copyThwDefitions(List<ThwDefinition> srcList,
			List<ThwDefinition> destList) {

		if (srcList != null && destList != null) {
			destList.clear();
			for (ThwDefinition thwDef : srcList) {
				destList.add(new ThwDefinitionBuilder(thwDef).build());
			}
		}
	}

	public SoftwareDescription getSoftwareDescription() {
		return softwareDescription;
	}

	public void setSoftwareDescription(SoftwareDescription sd) {
		softwareDescription = sd;
	}

	public List<ThwDefinition> getTargetHardwareDefinitions() {
		return thwDefinitions;
	}

	public List<FileDefinition> getFileDefinitions() {
		return fileDefinitions;
	}

	public IntegrityDefinition getSdfIntegrityDefinition() {
		return this.sdfIntegrityDefinition;
	}

	public void setSdfIntegrityDefinition(IntegrityDefinition value) {
		this.sdfIntegrityDefinition = value;
	}

	public IntegrityDefinition getLspIntegrityDefinition() {
		return this.lspIntegrityDefinition;
	}

	public void setLspIntegrityDefinition(IntegrityDefinition value) {
		this.lspIntegrityDefinition = value;
	}

	@Override
	public SdfSections build() {
		SdfSections sdfSections = new SdfSections();
		copyFileDefitions(fileDefinitions, sdfSections.getFileDefinitions());
		copyThwDefitions(thwDefinitions, sdfSections.getThwDefinitions());
		sdfSections.setLspIntegrityDefinition(new IntegrityDefinitionBuilder(
				lspIntegrityDefinition).build());
		sdfSections.setSdfIntegrityDefinition(new IntegrityDefinitionBuilder(
				sdfIntegrityDefinition).build());
		sdfSections.setSoftwareDescription(new SoftwareDescriptionBuilder(
				softwareDescription).build());

		return sdfSections;
	}
}
