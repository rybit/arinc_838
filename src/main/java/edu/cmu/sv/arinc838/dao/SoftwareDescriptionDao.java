/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 7, 2012
 */
package edu.cmu.sv.arinc838.dao;

import java.io.IOException;
import java.util.Arrays;

import com.arinc.arinc838.SoftwareDescription;

import edu.cmu.sv.arinc838.binary.BdfFile;

public class SoftwareDescriptionDao {

	public static final String SOFTWARE_PART_NUMBER_FORMAT = "MMMCC-SSSS-SSSS";

	private String softwarePartNumber;
	private String softwareTypeDescription;
	private byte[] softwareTypeId;

	public SoftwareDescriptionDao(SoftwareDescription softwareDescription) {
		setSoftwarePartnumber(softwareDescription.getSoftwarePartnumber());
		setSoftwareTypeDescription(softwareDescription
				.getSoftwareTypeDescription());
		setSoftwareTypeId(softwareDescription.getSoftwareTypeId());
	}

	public SoftwareDescriptionDao() {
	}

	public SoftwareDescriptionDao(BdfFile file) throws IOException {
		setSoftwarePartnumber(file.readStr64k());
		setSoftwareTypeDescription(file.readStr64k());
		setSoftwareTypeId(file.readHexbin32());
	}

	public String getSoftwarePartnumber() {
		return this.softwarePartNumber;
	}

	public void setSoftwarePartnumber(String value) {
		this.softwarePartNumber = value;
	}

	public String getSoftwareTypeDescription() {
		return this.softwareTypeDescription;
	}

	public void setSoftwareTypeDescription(String value) {
		this.softwareTypeDescription = value;
	}

	public byte[] getSoftwareTypeId() {
		return this.softwareTypeId;
	}

	public void setSoftwareTypeId(byte[] value) {
		this.softwareTypeId = value;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null &&
				this == obj ||
				(obj instanceof SoftwareDescriptionDao &&
				equals((SoftwareDescriptionDao)obj));
	}
	
	public boolean equals(SoftwareDescriptionDao obj){
		return obj != null &&
				(this == obj ||
				(this.getSoftwarePartnumber().equals(obj.getSoftwarePartnumber()) &&
				this.getSoftwareTypeDescription().equals(obj.getSoftwareTypeDescription()) &&
				Arrays.equals(this.getSoftwareTypeId(), obj.getSoftwareTypeId())));
	}
	
	@Override
	public int hashCode() {
		if (this.getSoftwarePartnumber() != null) {
			return this.getSoftwarePartnumber().hashCode();
		}
		return 0;
	}
}
