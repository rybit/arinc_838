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
import edu.cmu.sv.arinc838.validation.DataValidator;

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
		this.softwarePartNumber = DataValidator
				.validateSoftwarePartNumber(value);
	}

	public String getSoftwareTypeDescription() {
		return this.softwareTypeDescription;
	}

	public void setSoftwareTypeDescription(String value) {
		this.softwareTypeDescription = DataValidator.validateStr64kXml(value);
	}

	public byte[] getSoftwareTypeId() {
		return this.softwareTypeId;
	}

	public void setSoftwareTypeId(byte[] value) {
		this.softwareTypeId = DataValidator.validateHexbin32(value);
	}
//
//	@Override
//	public SoftwareDescription buildXml() {
//		SoftwareDescription desc = new SoftwareDescription();
//
//		desc.setSoftwarePartnumber(this.getSoftwarePartnumber());
//		desc.setSoftwareTypeDescription(this.getSoftwareTypeDescription());
//		desc.setSoftwareTypeId(this.getSoftwareTypeId());
//
//		return desc;
//	}
//
//	@Override
//	public int buildBinary(BdfFile file) throws IOException {
//		int initialPosition = (int) file.getFilePointer();
//
//		file.writeSoftwareDescriptionPointer();
//		file.writeStr64k(this.getSoftwarePartnumber());
//		file.writeStr64k(this.getSoftwareTypeDescription());
//		file.writeHexbin32(this.getSoftwareTypeId());
//
//		return (int) (file.getFilePointer() - initialPosition);
//	}

	@Override
	public boolean equals(Object obj) {
		return obj != null &&
				this == obj ||
				(obj instanceof SoftwareDescriptionDao &&
				equals((SoftwareDescriptionDao)obj));
	}
	
	public boolean equals(SoftwareDescriptionDao obj){
		return obj != null &&
				this == obj ||
				(this.getSoftwarePartnumber().equals(obj.getSoftwarePartnumber()) &&
				this.getSoftwareTypeDescription().equals(obj.getSoftwareTypeDescription()) &&
				Arrays.equals(this.getSoftwareTypeId(), obj.getSoftwareTypeId()));
	}
	
	@Override
	public int hashCode() {
		if (this.getSoftwarePartnumber() != null) {
			return this.getSoftwarePartnumber().hashCode();
		}
		return 0;
	}
}
