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

import java.io.IOException;

import com.arinc.arinc838.SoftwareDescription;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.validation.DataValidator;

public class SoftwareDescriptionBuilder implements Builder<SoftwareDescription> {

	public static final String SOFTWARE_PART_NUMBER_FORMAT = "MMMCC-SSSS-SSSS";

	private String softwarePartNumber;
	private String softwareTypeDescription;
	private byte[] softwareTypeId;

	public SoftwareDescriptionBuilder(SoftwareDescription softwareDescription) {
		setSoftwarePartNumber(softwareDescription.getSoftwarePartnumber());
		setSoftwareTypeDescription(softwareDescription
				.getSoftwareTypeDescription());
		setSoftwareTypeId(softwareDescription.getSoftwareTypeId());
	}

	public SoftwareDescriptionBuilder() {
	}

	public SoftwareDescriptionBuilder(BdfFile file) throws IOException {
		setSoftwarePartNumber(file.readStr64k());
		setSoftwareTypeDescription(file.readStr64k());
		setSoftwareTypeId(file.readHexbin32());
	}

	public String getSoftwarePartNumber() {
		return this.softwarePartNumber;
	}

	public void setSoftwarePartNumber(String value) {
		this.softwarePartNumber = DataValidator.validateSoftwarePartNumber(value);
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

	@Override
	public SoftwareDescription buildXml() {
		SoftwareDescription desc = new SoftwareDescription();

		desc.setSoftwarePartnumber(this.getSoftwarePartNumber());
		desc.setSoftwareTypeDescription(this.getSoftwareTypeDescription());
		desc.setSoftwareTypeId(this.getSoftwareTypeId());

		return desc;
	}
	
	@Override
	public int buildBinary(BdfFile file) throws IOException {
		int initialPosition = (int) file.getFilePointer();

		file.writeSoftwareDescriptionPointer();
		file.writeStr64k(this.getSoftwarePartNumber());		
		file.writeStr64k(this.getSoftwareTypeDescription());
		file.write(this.getSoftwareTypeId());
		
		return (int) (file.getFilePointer() - initialPosition);
	}
}
