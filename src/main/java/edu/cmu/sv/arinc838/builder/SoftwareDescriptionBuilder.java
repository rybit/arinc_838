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

import edu.cmu.sv.arinc838.specification.SoftwareDescription;

public class SoftwareDescriptionBuilder implements SoftwareDescription {

	private String softwarePartNumber;
	private String softwareTypeDescription;
	private long softwareTypeId;

	public SoftwareDescriptionBuilder(
			com.arinc.arinc838.SoftwareDescription softwareDescription) {
		this.softwarePartNumber = softwareDescription.getSoftwarePartnumber();
		this.softwareTypeDescription = softwareDescription
				.getSoftwareTypeDescription();
		this.softwareTypeId = softwareDescription.getSoftwareTypeId();
	}

	public SoftwareDescriptionBuilder() {

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

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof SoftwareDescription) {
			SoftwareDescription other = (SoftwareDescription) obj;

			return this.getSoftwareTypeDescription().equals(
					other.getSoftwareTypeDescription())
					&& this.getSoftwarePartNumber().equals(
							other.getSoftwarePartNumber())
					&& this.getSoftwareTypeId() == other.getSoftwareTypeId();

		}

		return false;
	}

	@Override
	public int hashCode() {
		return (int) (getSoftwarePartNumber().hashCode()
				^ getSoftwareTypeDescription().hashCode() ^ getSoftwareTypeId());
	}

	public com.arinc.arinc838.SoftwareDescription build() {
		com.arinc.arinc838.SoftwareDescription desc = new com.arinc.arinc838.SoftwareDescription();
		
		desc.setSoftwarePartnumber(this.getSoftwarePartNumber());
		desc.setSoftwareTypeDescription(this.getSoftwareTypeDescription());
		desc.setSoftwareTypeId(this.getSoftwareTypeId());
		
		return desc;
	}
}
