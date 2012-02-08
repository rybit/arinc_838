/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php) 
 * 
 * Created on Feb 6, 2012
 */
package edu.cmu.sv.arinc838.specification;

public interface FileDefinition {

	public boolean isFileLoadable();

	public void setFileLoadable(boolean loadable);

	public String getFileName();

	public void setFileName(String fileName);

	public long getFileSize();

	public void setFileSize(long fileSize);

	public IntegrityDefinition getFileIntegrityDefinition();

	public void setFileIntegrityDefinition(IntegrityDefinition value);

}