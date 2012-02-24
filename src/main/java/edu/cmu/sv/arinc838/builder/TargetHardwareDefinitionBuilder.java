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

import java.util.ArrayList;
import java.util.List;


import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.validation.DataValidator;

public class TargetHardwareDefinitionBuilder implements Builder<ThwDefinition> {

	private String id;
	private List<String> positions = new ArrayList<String>();

	public TargetHardwareDefinitionBuilder(ThwDefinition jaxbDef) {
		setId(jaxbDef.getThwId());
		
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
		this.id = DataValidator.validateStr64kXml(value);
	}

	public List<String> getPositions() {
		return positions;
	}

	@Override
	public ThwDefinition buildXml() {
		ThwDefinition def = new ThwDefinition();
		
		def.setThwId(this.getId());
		for(String position : this.getPositions()){
			def.getThwPosition().add(position);
		}
		
		return def;
	}
	
	@Override
	public void buildBinary(BdfFile file) {
		// TODO Auto-generated method stub
		
	}
}
