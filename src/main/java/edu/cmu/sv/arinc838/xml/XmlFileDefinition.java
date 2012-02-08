/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php) 
 * 
 * Created on Feb 7, 2012
 */
package edu.cmu.sv.arinc838.xml;

import com.arinc.arinc838.FileDefinition;

import edu.cmu.sv.arinc838.specification.IntegrityDefinition;

public class XmlFileDefinition implements
		edu.cmu.sv.arinc838.specification.FileDefinition {
	
	private FileDefinition fileDef;

	public XmlFileDefinition(com.arinc.arinc838.FileDefinition fileDef)
	{
		this.fileDef = fileDef;
	}

	@Override
	public void setFileIntegrityDefinition(IntegrityDefinition value) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isFileLoadable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFileLoadable(boolean loadable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFileName(String fileName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getFileSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setFileSize(long size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IntegrityDefinition getFileIntegrityDefinition() {
		// TODO Auto-generated method stub
		return null;
	}

}
