/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 7, 2012
 */
package edu.cmu.sv.arinc838.dao;

import java.io.IOException;

import com.arinc.arinc838.FileDefinition;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.validation.DataValidator;

/**
 * The builder paradigm is that all the fields will be extracted and stored in
 * their native data types and then stuffed back in at build time. This means
 * that all the fields will map. The reason to extract the fields is so that
 * modifications to the builder after the construction does NOT effect the
 * original xml container.
 *  
 *   This allows for the code to be de-coupled like so:
 *  	FileDefinition someDef = new ..
 *  	someDef.set...
 *  	// more setters
 *  	FileDefinitionBuilder someBuilder = new FileDefinitionBuilder (someDef)
 *                                                                      ^
 *  	someBuilder.set... <-- Does not effect the values in someDef    |
 *  
 * Complex data objects (e.g. IntegrityDefinition) are stored as references (according to Java)
 * and then at build time a NEW version is created. This means that the 'get' of these 
 * will be equal (same object) and but the equals will not match in the built version
 * 
 * For example:
 * 	FileDefinition someDef = new ..
 * 	IntegrityDefinition someInteg = someDef.get...
 *  
 * 	FileDefinitionBuilder someBuilder = new FileDefinitionBuilder (someDef)
 *  // someBuilder.getIntegrity... WILL be the same object as someInteg  
 * 
 * 	FileDefinition builtDef = someBuilder.build ();
 *  // the builtDef.getIntegrityDefinition () WILL NOT be the same object as someDef.getIntegrityDefinition ()
 * 
 * @author ryan
 * 
 */
public class FileDefinitionDao {
	private IntegrityDefinitionDao integDefBuilder;
	private boolean loadable;
	private String fileName;
	private long fileSize;
	private boolean isLast;

	public FileDefinitionDao() {
		;
	}

	public FileDefinitionDao(FileDefinition fileDef) {
		setFileLoadable (fileDef.isFileLoadable());
		setFileName(fileDef.getFileName());
		setFileSize (fileDef.getFileSize());

		setFileIntegrityDefinition (new IntegrityDefinitionDao(fileDef.getFileIntegrityDefinition()));
	}

	public FileDefinitionDao(BdfFile bdfFile) throws IOException {
		setFileLoadable(bdfFile.readBoolean());
		setFileName(bdfFile.readStr64k());
		setFileSize(bdfFile.readUint32());
		setFileIntegrityDefinition(new IntegrityDefinitionDao(bdfFile));
	}

	public IntegrityDefinitionDao getFileIntegrityDefinition() {
		return integDefBuilder;
	}

	public void setFileIntegrityDefinition(IntegrityDefinitionDao value) {
		this.integDefBuilder = value;
	}

	public boolean isFileLoadable() {
		return loadable;
	}

	public void setFileLoadable(boolean loadable) {
		this.loadable = loadable;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = DataValidator.validateStr64kXml(fileName);
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = DataValidator.validateUint32(fileSize);
	}

	public boolean isLast() {
		return isLast;
	}

	public void setIsLast(boolean value) {
		isLast = value;	
	}
	
	@Override
	public int hashCode() {
		if(this.getFileIntegrityDefinition() != null){
			return this.getFileIntegrityDefinition().hashCode();
		}
		
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj !=  null &&
				this == obj ||
				(obj instanceof FileDefinitionDao &&
				equals((FileDefinitionDao)obj));		
	}
	
	public boolean equals(FileDefinitionDao obj){
		return obj != null &&
				this == obj ||
				(this.getFileIntegrityDefinition().equals(obj.getFileIntegrityDefinition()) &&
				((this.isFileLoadable() && obj.isFileLoadable() || (!this.isFileLoadable() && !obj.isFileLoadable()))) &&
				this.getFileName().equals(obj.getFileName()) &&
				(this.getFileSize() == obj.getFileSize()));
	}
}
