package edu.cmu.sv.arinc838.builder;

import edu.cmu.sv.arinc838.dao.SoftwareDescriptionDao;


public class BuilderFactoryImpl implements BuilderFactory {

	@SuppressWarnings("unchecked")
	@Override
	public <DaoType, JaxbType> Builder<DaoType, JaxbType> getBuilder(
			Class<DaoType> Dao, Class<JaxbType> JaxB) {

		if (Dao == SoftwareDescriptionDao.class){
			return (Builder<DaoType, JaxbType>) new SoftwareDescriptionBuilder();
		}
		
		return null;
	}

}
