/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 7, 2012
 */
package edu.cmu.sv.arinc838.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.validation.DataValidator;

public class TargetHardwareDefinitionDao {

	private String id;
	private List<String> positions = new ArrayList<String>();
	private boolean isLast;

	public TargetHardwareDefinitionDao(ThwDefinition jaxbDef) {
		setThwId(jaxbDef.getThwId());

		for (String position : jaxbDef.getThwPosition()) {
			positions.add(position);
		}
	}

	public TargetHardwareDefinitionDao() {
	}

	public TargetHardwareDefinitionDao(BdfFile bdfFile) throws IOException {		
		setThwId(bdfFile.readStr64k());
		long positionsLength = bdfFile.readUint32();
		for(int i=0; i<positionsLength; i++) {
			bdfFile.readUint32(); // Read out the pointer to the next thw-position. We don't use it.
			getPositions().add(bdfFile.readStr64k());
		}
	}

	public String getThwId() {
		return id;
	}

	public void setThwId(String value) {
		this.id = DataValidator.validateStr64kXml(value);
	}

	public List<String> getPositions() {
		return positions;
	}

//	@Override
//	public ThwDefinition buildXml() {
//		ThwDefinition def = new ThwDefinition();
//
//		def.setThwId(this.getThwId());
//		for (String position : this.getPositions()) {
//			def.getThwPosition().add(position);
//		}
//
//		return def;
//	}
//
//	@Override
//	public int buildBinary(BdfFile bdfFile) throws IOException {
//		int initialPosition = (int) bdfFile.getFilePointer();
//
//		bdfFile.writePlaceholder();
//		bdfFile.writeStr64k(getThwId());
//		bdfFile.writeUint32(getPositions().size());
//		for (int i = 0; i < getPositions().size(); i++) {
//			String position = getPositions().get(i);
//			// next pointer is current position + 4 pointer to next thw-position
//			// + 2 position string length + position length
//			long nextThwPositionPointer = bdfFile.getFilePointer() + 4 + 2
//					+ position.length();
//
//			if (i == getPositions().size() - 1) {
//				bdfFile.writeUint32(0);
//			} else {
//				bdfFile.writeUint32(nextThwPositionPointer);
//			}
//
//			bdfFile.writeStr64k(position);
//		}
//
//		int finalPosition = (int) bdfFile.getFilePointer();
//		if (!isLast()) {
//			bdfFile.seek(initialPosition);
//			bdfFile.writeUint32(finalPosition);
//			bdfFile.seek(finalPosition);
//		}
//
//		return (int) (finalPosition - initialPosition);
//	}

	public boolean isLast() {
		return isLast;
	}

	public void setIsLast(boolean value) {
		isLast = value;
	}
	
	@Override
	public int hashCode() {
		if(this.getThwId() != null){
			return this.getThwId().hashCode();
		}
		
		return 0;
	}
	
//	@Override
//	public boolean equals(Object obj) {
//		return obj !=  null &&
//				this == obj ||
//				(obj instancTargetHardwareDefinitionBuildernDao &&
//				equaTargetHardwareDefinitionBuildernDao)obj));		
//	}
//	
//	public boolean equTargetHardwareDefinitionBuildernDao obj){
//		return obj != null &&
//				this == obj ||
//				(this.getThwId().equals(obj.getThwId()) &&
//				this.getPositions().equals(obj.getPositions()));
//	}
}
