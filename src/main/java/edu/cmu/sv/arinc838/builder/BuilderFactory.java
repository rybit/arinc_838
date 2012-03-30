package edu.cmu.sv.arinc838.builder;

public interface BuilderFactory {

	
	<DaoType, JaxbType> Builder<DaoType, JaxbType> getBuilder(Class<DaoType> Dao, Class<JaxbType> JaxB);
	
}
