package edu.cmu.sv.arinc838.xml;

import java.util.List;

import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.specification.TargetHardwareDefinition;

public class XmlTargetHardwareDefinition implements TargetHardwareDefinition {

	private String id;

	public XmlTargetHardwareDefinition(ThwDefinition jaxbDef) {
		this.id = jaxbDef.getThwId();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String value) {
		this.id = value;
	}

	@Override
	public List<String> getPositions() {
		// TODO Auto-generated method stub
		return null;
	}

}
