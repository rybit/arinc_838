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

public interface SoftwareDefinitionFile {
	
    public long getFileFormatVersion();

    public void setFileFormatVersion(long version);
	
	public SoftwareDefinitionSections getSoftwareDefinitionSections();
	
	public void setSoftwareDefinitionSections(SoftwareDefinitionSections sdf);

}