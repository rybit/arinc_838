/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 7, 2012
 */
package edu.cmu.sv.arinc838.xml;

import java.util.ArrayList;
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
	private List<TargetHardwareDefinition> hardwareDefinitions = new ArrayList<TargetHardwareDefinition>();
	private SoftwareDescription softwareDescription;
	private IntegrityDefinition lspIntegrityDefinition;
	private IntegrityDefinition sdfIntegrityDefinition;

	public XmlSoftwareDefinitionSections() {

	}

	public XmlSoftwareDefinitionSections(SdfSections sdfSections) {
		for (com.arinc.arinc838.FileDefinition fileDefinition : sdfSections
				.getFileDefinitions()) {
			fileDefinitions.add(new XmlFileDefinition(fileDefinition));
		}

		for (com.arinc.arinc838.ThwDefinition hardwareDefinition : sdfSections
				.getThwDefinitions()) {
			hardwareDefinitions.add(new XmlTargetHardwareDefinition(
					hardwareDefinition));
		}

		softwareDescription = new XmlSoftwareDescription(
				sdfSections.getSoftwareDescription());
		lspIntegrityDefinition = new XmlIntegrityDefinition(
				sdfSections.getLspIntegrityDefinition());
		sdfIntegrityDefinition = new XmlIntegrityDefinition(
				sdfSections.getSdfIntegrityDefinition());
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
		return hardwareDefinitions;
	}

	@Override
	public List<FileDefinition> getFileDefinitions() {
		return fileDefinitions;
	}

	@Override
	public IntegrityDefinition getSdfIntegrityDefinition() {
		return this.sdfIntegrityDefinition;
	}

	@Override
	public void setSdfIntegrityDefinition(IntegrityDefinition value) {
		this.sdfIntegrityDefinition = value;
	}

	@Override
	public IntegrityDefinition getLspIntegrityDefinition() {
		return this.lspIntegrityDefinition;
	}

	@Override
	public void setLspIntegrityDefinition(IntegrityDefinition value) {
		this.lspIntegrityDefinition = value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof SoftwareDefinitionSections) {
			SoftwareDefinitionSections other = (SoftwareDefinitionSections) obj;

			return this.getSoftwareDescription().equals(
					other.getSoftwareDescription())
					&& this.getLspIntegrityDefinition().equals(
							other.getLspIntegrityDefinition())
					&& this.getSdfIntegrityDefinition().equals(
							other.getSdfIntegrityDefinition())
					&& this.getFileDefinitions().equals(
							other.getFileDefinitions())
					&& this.getTargetHardwareDefinitions().equals(
							other.getTargetHardwareDefinitions());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.getSoftwareDescription().hashCode()
				^ this.getLspIntegrityDefinition().hashCode()
				^ this.getSdfIntegrityDefinition().hashCode()
				^ this.getFileDefinitions().hashCode()
				^ this.getTargetHardwareDefinitions().hashCode();
	}
}
