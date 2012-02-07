/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php) 
 * 
 * Created on Feb 6, 2012
 */
package edu.cmu.sv.arinc838.specification;

import java.util.Collection;


public interface SoftwareDefinitionSections {

	public SoftwareDescription getSoftwareDescription();

	public void setSoftwareDescription(SoftwareDescription sd);

	public Collection<TargetHardwareDefinition> getThwDefinitions();

	public void setTargetHardwareDefinitions(Collection<TargetHardwareDefinition> thwDefs);

	public Collection<FileDefinition> getFileDefinitions();

	public void setFileDefinitions(Collection<FileDefinition> fileDefs);

	public IntegrityDefinition getSdfIntegrityDefinition();

	public void setSdfIntegrityDefinition(IntegrityDefinition sdfIntegDefs);

	public IntegrityDefinition getLspIntegrityDefinition();

	public void setLspIntegrityDefinition(IntegrityDefinition lspIntegDefs);

}