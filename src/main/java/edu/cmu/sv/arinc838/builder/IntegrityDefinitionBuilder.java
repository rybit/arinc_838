package edu.cmu.sv.arinc838.builder;

import com.arinc.arinc838.IntegrityDefinition;

import edu.cmu.sv.arinc838.validation.DataValidator;

public class IntegrityDefinitionBuilder implements Builder<IntegrityDefinition> {

	private long integType;
	private String integValue;

	public IntegrityDefinitionBuilder() {
	}

	public IntegrityDefinitionBuilder(IntegrityDefinition integDef) {
		setIntegrityType(integDef.getIntegrityType());
		setIntegrityValue(integDef.getIntegrityValue());
	}

	public void setIntegrityType(long value) {
		integType = DataValidator.validateUint32(value);
	}

	public long getIntegrityType() {
		return integType;
	}

	public void setIntegrityValue(String value) {
		integValue = DataValidator.validateStr64k(value);
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
