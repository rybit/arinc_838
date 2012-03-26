package edu.cmu.sv.arinc838.builder;

import java.io.IOException;

import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.dao.TargetHardwareDefinitionDao;

public class TargetHardwareDefinitionBuilder implements
		Builder<TargetHardwareDefinitionDao, ThwDefinition> {
	@Override
	public ThwDefinition buildXml(TargetHardwareDefinitionDao targetHardwareDefinition) {
		ThwDefinition def = new ThwDefinition();

		def.setThwId(targetHardwareDefinition.getThwId());
		for (String position : targetHardwareDefinition.getPositions()) {
			def.getThwPosition().add(position);
		}

		return def;
	}

	@Override
	public int buildBinary(TargetHardwareDefinitionDao targetHardwareDefinition, BdfFile bdfFile) throws IOException {
		int initialPosition = (int) bdfFile.getFilePointer();

		bdfFile.writePlaceholder();
		bdfFile.writeStr64k(targetHardwareDefinition.getThwId());
		bdfFile.writeUint32(targetHardwareDefinition.getPositions().size());
		for (int i = 0; i < targetHardwareDefinition.getPositions().size(); i++) {
			String position = targetHardwareDefinition.getPositions().get(i);
			// next pointer is current position + 4 pointer to next thw-position
			// + 2 position string length + position length
			long nextThwPositionPointer = bdfFile.getFilePointer() + 4 + 2
					+ position.length();

			if (i == targetHardwareDefinition.getPositions().size() - 1) {
				bdfFile.writeUint32(0);
			} else {
				bdfFile.writeUint32(nextThwPositionPointer);
			}

			bdfFile.writeStr64k(position);
		}

		int finalPosition = (int) bdfFile.getFilePointer();
		if (!targetHardwareDefinition.isLast()) {
			bdfFile.seek(initialPosition);
			bdfFile.writeUint32(finalPosition);
			bdfFile.seek(finalPosition);
		}

		return (int) (finalPosition - initialPosition);
	}

}
