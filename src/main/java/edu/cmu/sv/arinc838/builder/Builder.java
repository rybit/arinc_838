/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 11, 2012
 */
package edu.cmu.sv.arinc838.builder;

import java.io.IOException;

import edu.cmu.sv.arinc838.binary.BdfFile;

public interface Builder<DaoType, JaxbType> {

	/**
	 * Builds and returns a JAXB object based on the builder's current values.
	 * 
	 * @return A JAXB object that reflects the builder's current state.
	 */
	public JaxbType buildXml(DaoType dao);

	/**
	 * Writes all the binary data represented by the builder to the
	 * {@link BdfFile}.
	 * 
	 * @param bdfFile
	 *            The {@link BdfFile} used to writing binary data.
	 * @return The number of bytes written to the file
	 * @throws IOException
	 */
	public int buildBinary(DaoType dao, BdfFile bdfFile) throws IOException;
}
