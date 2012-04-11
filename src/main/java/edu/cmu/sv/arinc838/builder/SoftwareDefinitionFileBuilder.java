package edu.cmu.sv.arinc838.builder;

import java.io.IOException;
import java.util.List;

import com.arinc.arinc838.FileDefinition;
import com.arinc.arinc838.IntegrityDefinition;
import com.arinc.arinc838.SdfFile;
import com.arinc.arinc838.SoftwareDescription;
import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.dao.FileDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.dao.SoftwareDescriptionDao;
import edu.cmu.sv.arinc838.dao.TargetHardwareDefinitionDao;
import edu.cmu.sv.arinc838.util.Converter;

public class SoftwareDefinitionFileBuilder implements
		Builder<SoftwareDefinitionFileDao, SdfFile> {

	private BuilderFactory builderFactory;

	public SoftwareDefinitionFileBuilder(BuilderFactory builderFact) {
		this.builderFactory = builderFact;
	}

	@Override
	public SdfFile buildXml(SoftwareDefinitionFileDao softwareDefinitionFileDao) {
		SdfFile file = new SdfFile();
		file.setFileFormatVersion(softwareDefinitionFileDao
				.getFileFormatVersion());

		// we have to re-validate this as a LIST1 since it can be modified
		// without a set method to verify its validity prior to building
		List<FileDefinitionDao> fileDefsValidated = softwareDefinitionFileDao
				.getFileDefinitions();
		Builder<FileDefinitionDao, FileDefinition> fileDefBuilder = builderFactory.getBuilder(FileDefinitionDao.class, FileDefinition.class);
		for (FileDefinitionDao fileDef : fileDefsValidated) {
			file.getFileDefinitions().add(fileDefBuilder.buildXml(fileDef));
		}

		Builder<TargetHardwareDefinitionDao, ThwDefinition> thwDefBuilder = builderFactory.getBuilder(TargetHardwareDefinitionDao.class, ThwDefinition.class);
		for (TargetHardwareDefinitionDao thwDef : softwareDefinitionFileDao
				.getTargetHardwareDefinitions()) {
			file.getThwDefinitions().add(thwDefBuilder.buildXml(thwDef));
		}

		Builder<IntegrityDefinitionDao, IntegrityDefinition> integDefBuilder = builderFactory.getBuilder(IntegrityDefinitionDao.class, IntegrityDefinition.class);
		file.setLspIntegrityDefinition(integDefBuilder
				.buildXml(softwareDefinitionFileDao.getLspIntegrityDefinition()));
		file.setSdfIntegrityDefinition(integDefBuilder
				.buildXml(softwareDefinitionFileDao.getSdfIntegrityDefinition()));

		file.setSoftwareDescription(builderFactory.getBuilder(SoftwareDescriptionDao.class, SoftwareDescription.class)
				.buildXml(softwareDefinitionFileDao.getSoftwareDescription()));

		return file;
	}

	@Override
	public int buildBinary(SoftwareDefinitionFileDao softwareDefinitionFileDao, BdfFile file) throws IOException, IllegalArgumentException{
		
		if (file.length() != 0){
			throw new IllegalArgumentException();
		}
		
		file.seek(0);
		// write the header
		file.writePlaceholder(); // file size
		file.writeHexbin32(softwareDefinitionFileDao.getFileFormatVersion());
		file.writePlaceholder(); // software description pointer
		file.writePlaceholder(); // target hardware definition pointer
		file.writePlaceholder(); // file definition pointer
		file.writePlaceholder(); // SDF integrity definition pointer
		file.writePlaceholder(); // LSP integrity definition pointer
		Builder<SoftwareDescriptionDao, SoftwareDescription> swDescBuilder = builderFactory
				.getBuilder(SoftwareDescriptionDao.class,
						SoftwareDescription.class);
		
		swDescBuilder.buildBinary(
				softwareDefinitionFileDao.getSoftwareDescription(), file);

		// Write the target hardware definitions
		int size = softwareDefinitionFileDao.getTargetHardwareDefinitions()
				.size();
		file.writeTargetDefinitionsPointer();
		file.writeUint32(size);
		Builder<TargetHardwareDefinitionDao, ThwDefinition> targetHardwareDefinitionBuilder = builderFactory.getBuilder(TargetHardwareDefinitionDao.class, ThwDefinition.class);
		if (size > 0) {
			softwareDefinitionFileDao.getTargetHardwareDefinitions()
					.get(size - 1).setIsLast(true);
			for (int i = 0; i < size; i++) {
				targetHardwareDefinitionBuilder.buildBinary(
						softwareDefinitionFileDao
								.getTargetHardwareDefinitions().get(i), file);
			}
		}

		// write the file definitions
		size = softwareDefinitionFileDao.getFileDefinitions().size();
		file.writeFileDefinitionsPointer();
		file.writeUint32(size);
		softwareDefinitionFileDao.getFileDefinitions().get(size - 1)
				.setIsLast(true);
		
		Builder<FileDefinitionDao, FileDefinition> fileDefBuilder = builderFactory.getBuilder(FileDefinitionDao.class, FileDefinition.class);
		for (int i = 0; i < size; i++) {
			fileDefBuilder.buildBinary(softwareDefinitionFileDao
					.getFileDefinitions().get(i), file);
		}

		
		//calculate CRC up to this point
		

		Builder<IntegrityDefinitionDao, IntegrityDefinition> integDefBuilder = builderFactory.getBuilder(IntegrityDefinitionDao.class, IntegrityDefinition.class);

		// write the SDF integrity def
		file.writeSdfIntegrityDefinitionPointer();
		softwareDefinitionFileDao.getSdfIntegrityDefinition()
				.setIntegrityValue(Converter.hexToBytes("0000000A"));
		integDefBuilder.buildBinary(
				softwareDefinitionFileDao.getSdfIntegrityDefinition(), file);

		// write the LSP integrity def
		file.writeLspIntegrityDefinitionPointer();
		softwareDefinitionFileDao.getLspIntegrityDefinition()
				.setIntegrityValue(Converter.hexToBytes("0000000A"));
		integDefBuilder.buildBinary(
				softwareDefinitionFileDao.getLspIntegrityDefinition(), file);

		// write the file size
		file.seek(0);
		file.writeUint32(file.length());
		file.seek(file.length());

		return (int) file.length();
	}

}
