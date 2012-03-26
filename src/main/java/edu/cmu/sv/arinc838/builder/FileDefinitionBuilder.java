package edu.cmu.sv.arinc838.builder;

import java.io.IOException;

import com.arinc.arinc838.FileDefinition;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.dao.FileDefinitionDao;

public class FileDefinitionBuilder implements Builder<FileDefinitionDao, FileDefinition> {
	@Override
	public FileDefinition buildXml(FileDefinitionDao fileDefinitionDao) {
		FileDefinition retDef = new FileDefinition();

		retDef.setFileLoadable(fileDefinitionDao.isFileLoadable());
		retDef.setFileName(fileDefinitionDao.getFileName());
		retDef.setFileSize(fileDefinitionDao.getFileSize());

		retDef.setFileIntegrityDefinition(new IntegrityDefinitionBuilder().buildXml(fileDefinitionDao.getFileIntegrityDefinition()));

		return retDef;
	}

	@Override
	public int buildBinary(FileDefinitionDao fileDefinitionDao, BdfFile bdfFile) throws IOException {
		int initialPosition = (int) bdfFile.getFilePointer();

		bdfFile.writeUint32(0); // Place holder for APTPTR to the next file
								// definition
		bdfFile.writeBoolean(fileDefinitionDao.isFileLoadable());
		bdfFile.writeStr64k(fileDefinitionDao.getFileName());
		bdfFile.writeUint32(fileDefinitionDao.getFileSize());
		new IntegrityDefinitionBuilder().buildBinary(fileDefinitionDao.getFileIntegrityDefinition(), bdfFile);

		int finalPosition = (int) bdfFile.getFilePointer();

		// If not last file def then fill in the pointer to the next file def
		if (!fileDefinitionDao.isLast()) {
			bdfFile.seek(initialPosition);
			bdfFile.writeUint32(finalPosition);
			bdfFile.seek(finalPosition);
		}

		return (int) (finalPosition - initialPosition);
	}

}
