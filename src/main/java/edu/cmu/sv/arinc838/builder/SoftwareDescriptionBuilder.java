package edu.cmu.sv.arinc838.builder;

import java.io.IOException;

import com.arinc.arinc838.SoftwareDescription;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.dao.SoftwareDescriptionDao;

public class SoftwareDescriptionBuilder implements
		Builder<SoftwareDescriptionDao, SoftwareDescription> {
	@Override
	public SoftwareDescription buildXml(SoftwareDescriptionDao softwareDescription) {
		SoftwareDescription desc = new SoftwareDescription();

		desc.setSoftwarePartnumber(softwareDescription.getSoftwarePartnumber());
		desc.setSoftwareTypeDescription(softwareDescription.getSoftwareTypeDescription());
		desc.setSoftwareTypeId(softwareDescription.getSoftwareTypeId());

		return desc;
	}

	@Override
	public int buildBinary(SoftwareDescriptionDao softwareDescription, BdfFile file) throws IOException {
		int initialPosition = (int) file.getFilePointer();

		file.writeSoftwareDescriptionPointer();
		file.writeStr64k(softwareDescription.getSoftwarePartnumber());
		file.writeStr64k(softwareDescription.getSoftwareTypeDescription());
		file.writeHexbin32(softwareDescription.getSoftwareTypeId());

		return (int) (file.getFilePointer() - initialPosition);
	}

}
