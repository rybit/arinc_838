/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 11, 2012
 */
package edu.cmu.sv.arinc838.builder;

import com.arinc.arinc838.IntegrityDefinition;

public class IntegrityDefinitionBuilder implements Builder<IntegrityDefinition> {

	private long integType;
	private String integValue;

	public IntegrityDefinitionBuilder() {
	}

	public IntegrityDefinitionBuilder(IntegrityDefinition integDef) {
		integType = integDef.getIntegrityType();
		integValue = integDef.getIntegrityValue();
	}

	public void setIntegrityType(long value) {
		integType = value;
	}

	public long getIntegrityType() {
		return integType;
	}

	public void setIntegrityValue(String value) {
		integValue = value;
	}

	public String getIntegrityValue() {
		return integValue;
	}

	@Override
	public IntegrityDefinition build() {
		IntegrityDefinition retDef = new IntegrityDefinition();

		retDef.setIntegrityType(integType);
		retDef.setIntegrityValue(integValue);

		return retDef;
	}
}
