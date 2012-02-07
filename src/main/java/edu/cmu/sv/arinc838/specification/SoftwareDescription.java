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


public interface SoftwareDescription {

	public String getSoftwarePartnumber();

	public void setSoftwarePartnumber(String value);

	public String getSoftwareTypeDescription();

	public void setSoftwareTypeDescription(String value);

	public long getSoftwareTypeId();

	public void setSoftwareTypeId(long value);

}