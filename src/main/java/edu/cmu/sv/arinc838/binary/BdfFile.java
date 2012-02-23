package edu.cmu.sv.arinc838.binary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import edu.cmu.sv.arinc838.validation.DataValidator;

public class BdfFile extends RandomAccessFile {

	public static final int UINT32_LENGTH = 4;
	public static final int BOOLEAN_LENGTH = 1;

	public BdfFile(File file) throws FileNotFoundException {
		super(file, "rw");
		
	}

	public void writeUint32(long value) throws IOException {				
		super.writeInt((int) DataValidator.validateUint32(value));
	}	
	
	public static long asUint32(int value){
		return value & 0xffffffffL;		
	}

	public void writeStr64k(String value) throws IOException {
		super.writeUTF(DataValidator.validateStr64kBinary(value));
	}
}
