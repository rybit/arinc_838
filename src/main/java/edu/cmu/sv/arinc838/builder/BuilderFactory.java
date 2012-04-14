package edu.cmu.sv.arinc838.builder;

import edu.cmu.sv.arinc838.crc.CrcGeneratorFactory;
import edu.cmu.sv.arinc838.crc.LspCrcCalculator;
import edu.cmu.sv.arinc838.dao.FileDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.dao.SoftwareDescriptionDao;
import edu.cmu.sv.arinc838.dao.TargetHardwareDefinitionDao;

public class BuilderFactory {

	@SuppressWarnings("unchecked")
	public <DaoType, JaxbType> Builder<DaoType, JaxbType> getBuilder(
			Class<DaoType> Dao, Class<JaxbType> JaxB) {

		if (Dao == SoftwareDescriptionDao.class) {
			return (Builder<DaoType, JaxbType>) new SoftwareDescriptionBuilder();
		}

		if (Dao == FileDefinitionDao.class) {
			return (Builder<DaoType, JaxbType>) new FileDefinitionBuilder(this);
		}

		if (Dao == IntegrityDefinitionDao.class) {
			return (Builder<DaoType, JaxbType>) new IntegrityDefinitionBuilder();
		}

		if (Dao == SoftwareDefinitionFileDao.class) {
			return (Builder<DaoType, JaxbType>) new SoftwareDefinitionFileBuilder(
					this, new CrcGeneratorFactory(), new LspCrcCalculator());
		}

		if (Dao == TargetHardwareDefinitionDao.class) {
			return (Builder<DaoType, JaxbType>) new TargetHardwareDefinitionBuilder();
		}

		throw new IllegalArgumentException();
	}

}
