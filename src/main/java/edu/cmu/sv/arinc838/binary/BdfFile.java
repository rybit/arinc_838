/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 20, 2012
 */
package edu.cmu.sv.arinc838.binary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BdfFile extends RandomAccessFile {

	public static final int UINT32_LENGTH = 4;
	public static final int BOOLEAN_LENGTH = 1;
	public static final int FILE_FORMAT_VERSION_LOCATION = 4;
	public static final int SOFTWARE_DESCRIPTION_POINTER_LOCATION = 8;
	public static final int TARGET_DEFINITIONS_POINTER_LOCATION = 12;
	public static final int FILE_DEFINITIONS_POINTER_LOCATION = 16;
	public static final int SDF_INTEGRITY_POINTER_LOCATION = 20;
	public static final int LSP_INTEGRITY_POINTER_LOCATION = 24;
	public static final long PLACEHOLDER = 0;

	public BdfFile(File file) throws FileNotFoundException {
		super(file, "rw");
	}

	public void writeUint32(long value) throws IOException {
		super.writeInt((int)value);
	}

	public static long asUint32(int value) {
		return value & 0xffffffffL;
	}

	public void writeStr64k(String value) throws IOException {
		super.writeUTF(value);
	}
	
	/**
	 * Simple wrapper around {@link #readUTF()} to keep the API of this class symmetric.
	 * @return
	 * @throws IOException
	 */
	public String readStr64k() throws IOException {
		return readUTF();
	}

	public long readUint32() throws IOException {
		return asUint32(super.readInt());
	}

	public void writeSoftwareDescriptionPointer() throws IOException {
		writePointer(SOFTWARE_DESCRIPTION_POINTER_LOCATION);
	}

	public void writeTargetDefinitionsPointer() throws IOException {
		writePointer(TARGET_DEFINITIONS_POINTER_LOCATION);
	}

	public void writeFileDefinitionsPointer() throws IOException {
		writePointer(FILE_DEFINITIONS_POINTER_LOCATION);
	}

	public void writeSdfIntegrityDefinitionPointer() throws IOException {
		writePointer(SDF_INTEGRITY_POINTER_LOCATION);
	}

	public void writeLspIntegrityDefinitionPointer() throws IOException {
		writePointer(LSP_INTEGRITY_POINTER_LOCATION);
	}

	private void writePointer(int pointerLocation) throws IOException {
		long currentLocation = super.getFilePointer();
		super.seek(pointerLocation);
		writeUint32(currentLocation);
		super.seek(currentLocation);
	}

	public void writeHexbin32(byte[] hexBin) throws IOException {
		byte[] hexBinToWrite = hexBin;

		write(hexBinToWrite);
	}

	public void writeHexbin64k(byte[] hexBin) throws IOException {
		byte[] hexBinToWrite = hexBin;

		writeShort(hexBinToWrite.length);
		write(hexBinToWrite);
	}

	public void writePlaceholder() throws IOException {
		writeUint32(0);
	}

	public byte[] readHexbin32() throws IOException {
		byte[] hexbin = new byte[4];

		this.read(hexbin);

		return hexbin;
	}

	public byte[] readHexbin64k() throws IOException {
		short length = readShort();
		byte[] data = new byte[length];
		read(data);
		return data;
	}
	
	public long readSoftwareDescriptionPointer() throws IOException {
		return readPointer(SOFTWARE_DESCRIPTION_POINTER_LOCATION);
	}

	public long readTargetDefinitionsPointer() throws IOException {
		return readPointer(TARGET_DEFINITIONS_POINTER_LOCATION);
	}

	public long readFileDefinitionsPointer() throws IOException {
		return readPointer(FILE_DEFINITIONS_POINTER_LOCATION);
	}

	public long readSdfIntegrityDefinitionPointer() throws IOException {
		return readPointer(SDF_INTEGRITY_POINTER_LOCATION);
	}

	public long readLspIntegrityDefinitionPointer() throws IOException {
		return readPointer(LSP_INTEGRITY_POINTER_LOCATION);
	}

	private long readPointer(int pointerLocation) throws IOException {
		seek(pointerLocation);
		return readUint32();
	}

	public byte[] readAll() throws IOException {
		byte[] buffer = new byte[(int) this.length()];
		
		long location = this.getFilePointer();
		
		this.seek(0);
		
		this.readFully(buffer);
		
		this.seek(location);
		
		return buffer;
	}
}
