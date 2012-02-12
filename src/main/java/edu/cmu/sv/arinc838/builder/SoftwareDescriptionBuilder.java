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

import com.arinc.arinc838.SoftwareDescription;

public class SoftwareDescriptionBuilder implements Builder<SoftwareDescription> {

	private String softwarePartNumber;
	private String softwareTypeDescription;
	private long softwareTypeId;

	public SoftwareDescriptionBuilder(SoftwareDescription softwareDescription) {
		this.softwarePartNumber = softwareDescription.getSoftwarePartnumber();
		this.softwareTypeDescription = softwareDescription
				.getSoftwareTypeDescription();
		this.softwareTypeId = softwareDescription.getSoftwareTypeId();
	}

	public SoftwareDescriptionBuilder() {

	}

	public String getSoftwarePartNumber() {
		return this.softwarePartNumber;
	}

	public void setSoftwarePartNumber(String value) {
		this.softwarePartNumber = value;
	}

	public String getSoftwareTypeDescription() {
		return this.softwareTypeDescription;
	}

	public void setSoftwareTypeDescription(String value) {
		this.softwareTypeDescription = value;
	}

	public long getSoftwareTypeId() {
		return this.softwareTypeId;
	}

	public void setSoftwareTypeId(long value) {
		this.softwareTypeId = value;
	}

	@Override
	public SoftwareDescription build() {
		SoftwareDescription desc = new SoftwareDescription();

		desc.setSoftwarePartnumber(this.getSoftwarePartNumber());
		desc.setSoftwareTypeDescription(this.getSoftwareTypeDescription());
		desc.setSoftwareTypeId(this.getSoftwareTypeId());

		return desc;
	}
}
