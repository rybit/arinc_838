package edu.cmu.sv.arinc838.specification;

import com.arinc.arinc838.ThwPositions;

public interface TargetHardwareDefinition {

	public String getId();

	public void setId(String value);

	public ThwPositions getPositions();

	public void setPositions(ThwPositions value);

}