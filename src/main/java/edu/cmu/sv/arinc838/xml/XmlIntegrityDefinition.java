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

import edu.cmu.sv.arinc838.specification.IntegrityDefinition;

public class XmlIntegrityDefinition implements IntegrityDefinition {

	private String integrityValue;
	private long integrityType;

	public XmlIntegrityDefinition(
			com.arinc.arinc838.IntegrityDefinition fileIntegrityDefinition) {
		if (fileIntegrityDefinition != null) {
			integrityType = fileIntegrityDefinition.getIntegrityType();
			integrityValue = fileIntegrityDefinition.getIntegrityValue();
		}
	}

	public XmlIntegrityDefinition(){
		
	}
	
	@Override
	public long getIntegrityType() {
		return integrityType;
	}

	@Override
	public void setIntegrityType(long type) {
		integrityType = type;
	}

	@Override
	public String getIntegrityValue() {
		return integrityValue;
	}

	@Override
	public void setIntegrityValue(String value) {
		integrityValue = value;

	}

	@Override
	public boolean equals(Object o) {
		boolean isEqual = false;
		if (o != null && o instanceof XmlIntegrityDefinition) {
			XmlIntegrityDefinition other = (XmlIntegrityDefinition) o;
			isEqual = (other.getIntegrityType() == getIntegrityType())
					&& (other.getIntegrityValue() == getIntegrityValue() || other
							.getIntegrityValue().equals(getIntegrityValue()));
		}

		return isEqual;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 31
				+ (integrityValue == null ? 0 : integrityValue.hashCode());
		hash = hash * 31 + new Long(integrityType).hashCode();

		return hash;
	}

}
