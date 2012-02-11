package edu.cmu.sv.arinc838.builder;

import com.arinc.arinc838.IntegrityDefinition;

public class IntegrityDefinitionBuilder {
	
	private long integType;
	private String integValue;

	public IntegrityDefinitionBuilder () { ; }
	public IntegrityDefinitionBuilder(IntegrityDefinition integDef) {
		integType = integDef.getIntegrityType();
		integValue = integDef.getIntegrityValue();
	}

	public void setIntegrityType (long value) {
		integType = value;
	}
	public long getIntegrityType() {
		return integType;
	}
	
	public void setIntegrityValue (String value) { 
		integValue = value;
	}
	public String getIntegrityValue() {
		return integValue;
	}

	public IntegrityDefinition build() {
		IntegrityDefinition retDef = new IntegrityDefinition();
		
		retDef.setIntegrityType(integType);
		retDef.setIntegrityValue(integValue);
		
		return retDef;
	}
}
