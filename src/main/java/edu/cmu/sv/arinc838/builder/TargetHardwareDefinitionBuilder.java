package edu.cmu.sv.arinc838.builder;

import java.util.ArrayList;
import java.util.List;


import com.arinc.arinc838.ThwDefinition;

public class TargetHardwareDefinitionBuilder implements Builder<ThwDefinition> {

	private String id;
	private List<String> positions = new ArrayList<String>();

	public TargetHardwareDefinitionBuilder(ThwDefinition jaxbDef) {
		this.id = jaxbDef.getThwId();
		
		for(String position : jaxbDef.getThwPosition()){
			positions.add(position);
		}
	}

	public TargetHardwareDefinitionBuilder() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String value) {
		this.id = value;
	}

	public List<String> getPositions() {
		return positions;
	}

	@Override
	public ThwDefinition build() {
		ThwDefinition def = new ThwDefinition();
		
		def.setThwId(this.getId());
		for(String position : this.getPositions()){
			def.getThwPosition().add(position);
		}
		
		return def;
	}
}
