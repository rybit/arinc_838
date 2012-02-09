package edu.cmu.sv.arinc838.xml;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.PSource;

import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.specification.TargetHardwareDefinition;

public class XmlTargetHardwareDefinition implements TargetHardwareDefinition {

	private String id;
	private List<String> positions = new ArrayList<String>();

	public XmlTargetHardwareDefinition(ThwDefinition jaxbDef) {
		this.id = jaxbDef.getThwId();
		
		for(String position : jaxbDef.getThwPosition()){
			positions.add(position);
		}
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
		return positions;
	}

}
