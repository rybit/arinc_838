package edu.cmu.sv.arinc838.binary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import edu.cmu.sv.arinc838.validation.DataValidator;

public class BdfFile extends RandomAccessFile {

	public static final int UINT32_LENGTH = 4;
	public static final int BOOLEAN_LENGTH = 1;
	public static final int BINARY_FILE_FORMAT_VERSION_LOCATION = 4;
	public static final int SOFTWARE_DESCRIPTION_POINTER_LOCATION = 8;
	public static final int TARGET_DEFINITIONS_POINTER_LOCATION = 12;
	public static final int FILE_DEFINITIONS_POINTER_LOCATION = 16;
	public static final int SDF_INTEGRITY_POINTER_LOCATION = 20;
	public static final int LSP_INTEGRITY_POINTER_LOCATION = 24;

	public BdfFile(File file) throws FileNotFoundException {
		super(file, "rw");

	}

	public void writeUint32(long value) throws IOException {
		super.writeInt((int) DataValidator.validateUint32(value));
	}

	public static long asUint32(int value) {
		return value & 0xffffffffL;
	}

	public void writeStr64k(String value) throws IOException {
		super.writeUTF(DataValidator.validateStr64kBinary(value));
	}

	public long readUint32() throws IOException {
		return asUint32(super.readInt());
	}

	public void writeFileFormatVersion(long version) throws IOException {
		writeUint32(version, BINARY_FILE_FORMAT_VERSION_LOCATION);
	}

	public void writeSoftwareDescriptionPointer(long expected) throws IOException {
		writeUint32(expected,SOFTWARE_DESCRIPTION_POINTER_LOCATION);
	}

	public void writeTargetDefinitionsPointer(long expected) throws IOException {
		writeUint32(expected, TARGET_DEFINITIONS_POINTER_LOCATION);
	}

	public void writeFileDefinitionsPointer(long expected) throws IOException {
		writeUint32(expected, FILE_DEFINITIONS_POINTER_LOCATION);
	}

	public void writeSdfIntegrityDefinitionPointer(long expected) throws IOException {
		writeUint32(expected, SDF_INTEGRITY_POINTER_LOCATION);
	}

	public void writeLspIntegrityDefinitionPointer(long expected) throws IOException {
		writeUint32(expected, LSP_INTEGRITY_POINTER_LOCATION);
		
	}

	private void writeUint32(long value, int location) throws IOException {
		long tmp = super.getFilePointer();
		super.seek(location);
		writeUint32(value);
		super.seek(tmp);
	}
}
