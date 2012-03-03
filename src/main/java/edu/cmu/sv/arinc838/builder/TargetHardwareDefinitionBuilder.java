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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.validation.DataValidator;

public class TargetHardwareDefinitionBuilder implements Builder<ThwDefinition> {

	private String id;
	private List<String> positions = new ArrayList<String>();
	private boolean isLast;

	public TargetHardwareDefinitionBuilder(ThwDefinition jaxbDef) {
		setId(jaxbDef.getThwId());

		for (String position : jaxbDef.getThwPosition()) {
			positions.add(position);
		}
	}

	public TargetHardwareDefinitionBuilder() {
	}

	public TargetHardwareDefinitionBuilder(BdfFile bdfFile) throws IOException {		
		setId(bdfFile.readStr64k());
		long positionsLength = bdfFile.readUint32();
		for(int i=0; i<positionsLength; i++) {
			bdfFile.readUint32(); // Read out the pointer to the next thw-position. We don't use it.
			getPositions().add(bdfFile.readStr64k());
		}
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
		for (String position : this.getPositions()) {
			def.getThwPosition().add(position);
		}

		return def;
	}

	@Override
	public int buildBinary(BdfFile bdfFile) throws IOException {
		int initialPosition = (int) bdfFile.getFilePointer();

		bdfFile.writePlaceholder();
		bdfFile.writeStr64k(getId());
		bdfFile.writeUint32(getPositions().size());
		for (int i = 0; i < getPositions().size(); i++) {
			String position = getPositions().get(i);
			// next pointer is current position + 4 pointer to next thw-position
			// + 2 position string length + position length
			long nextThwPositionPointer = bdfFile.getFilePointer() + 4 + 2
					+ position.length();

			if (i == getPositions().size() - 1) {
				bdfFile.writeUint32(0);
			} else {
				bdfFile.writeUint32(nextThwPositionPointer);
			}

			bdfFile.writeStr64k(position);
		}

		int finalPosition = (int) bdfFile.getFilePointer();
		if (!isLast()) {
			bdfFile.seek(initialPosition);
			bdfFile.writeUint32(finalPosition);
			bdfFile.seek(finalPosition);
		}

		return (int) (finalPosition - initialPosition);
	}

	public boolean isLast() {
		return isLast;
	}

	public void setIsLast(boolean value) {
		isLast = value;
	}
	
	@Override
	public int hashCode() {
		if(this.getId() != null){
			return this.getId().hashCode();
		}
		
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj !=  null &&
				this == obj ||
				(obj instanceof TargetHardwareDefinitionBuilder &&
				equals((TargetHardwareDefinitionBuilder)obj));		
	}
	
	public boolean equals(TargetHardwareDefinitionBuilder obj){
		return obj != null &&
				this == obj ||
				(this.getId().equals(obj.getId()) &&
				this.getPositions().equals(obj.getPositions()));
	}
}
