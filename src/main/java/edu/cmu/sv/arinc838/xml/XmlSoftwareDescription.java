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

import edu.cmu.sv.arinc838.specification.SoftwareDescription;

public class XmlSoftwareDescription implements SoftwareDescription {

	private String softwarePartNumber;
	private String softwareTypeDescription;
	private long softwareTypeId;
	
	public XmlSoftwareDescription(com.arinc.arinc838.SoftwareDescription softwareDescription) {
		this.softwarePartNumber = softwareDescription.getSoftwarePartnumber();
		this.softwareTypeDescription = softwareDescription.getSoftwareTypeDescription();
		this.softwareTypeId = softwareDescription.getSoftwareTypeId();
	}
	
	public XmlSoftwareDescription() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getSoftwarePartNumber() {
		return this.softwarePartNumber;
	}

	@Override
	public void setSoftwarePartNumber(String value) {
		this.softwarePartNumber = value;
	}

	@Override
	public String getSoftwareTypeDescription() {
		return this.softwareTypeDescription;
	}

	@Override
	public void setSoftwareTypeDescription(String value) {
		this.softwareTypeDescription = value;
	}

	@Override
	public long getSoftwareTypeId() {
		return this.softwareTypeId;
	}

	@Override
	public void setSoftwareTypeId(long value) {
		this.softwareTypeId = value;
	}

}
