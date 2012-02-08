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

import edu.cmu.sv.arinc838.specification.IntegrityDefinition;

public class XmlFileDefinition implements
		edu.cmu.sv.arinc838.specification.FileDefinition {

	private XmlIntegrityDefinition fileIntegrityDef = new XmlIntegrityDefinition();
	private boolean loadable;
	private String fileName;
	private long fileSize;

	public XmlFileDefinition(com.arinc.arinc838.FileDefinition fileDef) {
		fileIntegrityDef = new XmlIntegrityDefinition(
				fileDef.getFileIntegrityDefinition());

		loadable = fileDef.isFileLoadable();
		fileName = fileDef.getFileName();
		fileSize = fileDef.getFileSize();
	}

	public XmlFileDefinition() {
	}

	@Override
	public void setFileIntegrityDefinition(IntegrityDefinition value) {
		fileIntegrityDef.setIntegrityType(value.getIntegrityType());
		fileIntegrityDef.setIntegrityValue(value.getIntegrityValue());
	}

	@Override
	public boolean isFileLoadable() {
		return loadable;
	}

	@Override
	public void setFileLoadable(boolean loadable) {
		this.loadable = loadable;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public long getFileSize() {
		return fileSize;
	}

	@Override
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	@Override
	public IntegrityDefinition getFileIntegrityDefinition() {
		return fileIntegrityDef;
	}

	@Override
	public boolean equals(Object o) {
		boolean isEqual = false;
		if (o instanceof XmlFileDefinition) {
			XmlFileDefinition other = (XmlFileDefinition) o;

			isEqual = (other.isFileLoadable() && isFileLoadable())
					&& (other.getFileIntegrityDefinition()
							.equals(getFileIntegrityDefinition()))
					&& (other.getFileName().equals(getFileName()))
					&& (other.getFileSize() == getFileSize());
		}
		return isEqual;
	}

	@Override
	public int hashCode() {
		int hash = fileIntegrityDef.hashCode();
		hash = hash * 31 + new Boolean(loadable).hashCode();
		hash = hash * 31 + fileName.hashCode();
		hash = hash * 31 + new Long(fileSize).hashCode();

		return hash;
	}

}
