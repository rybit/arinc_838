package edu.cmu.sv.arinc838.specification;


import com.arinc.arinc838.FileIntegrityDefinition;

public interface FileDefinition {

	public boolean isFileLoadable();

	public void setFileLoadable(boolean value);

	public String getFileName();

	public void setFileName(String value);

	public long getFileSize();

	public void setFileSize(long value);

	public FileIntegrityDefinition getFileIntegrityDefinition();

	public void setFileIntegrityDefinition(FileIntegrityDefinition value);

}