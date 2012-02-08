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

	private com.arinc.arinc838.SoftwareDescription softwareDescription;

	public XmlSoftwareDescription(com.arinc.arinc838.SoftwareDescription softwareDescription) {
		this.softwareDescription = softwareDescription;
	}
	
	@Override
	public String getSoftwarePartnumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSoftwarePartnumber(String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSoftwareTypeDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSoftwareTypeDescription(String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getSoftwareTypeId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSoftwareTypeId(long value) {
		// TODO Auto-generated method stub

	}

}
