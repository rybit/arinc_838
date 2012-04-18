package edu.cmu.sv.arinc838.crc;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.dao.FileDefinitionDao;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;

public class LspCrcCalculator {
	public long calculateCrc(BdfFile bdf, SoftwareDefinitionFileDao sdf, CrcGenerator crc) throws IOException{
		
		int bdfLength = (int) bdf.readLspIntegrityDefinitionPointer() + 6;
		byte[] bdfData = new byte[bdfLength];
		bdf.seek(0);
		bdf.read(bdfData);
		
		int length = bdfData.length;
		ArrayList<byte[]> fileDefData = new  ArrayList<byte[]>();
		for(FileDefinitionDao fileDef : sdf.getFileDefinitions()) {
			byte[] data = readFile(new File(sdf.getPath(), fileDef.getFileName()));
			length += data.length;
			fileDefData.add(data);
		}
		
		// TODO this algorithm is limited to files < 2GB
		byte[] fullData = new byte[length];
		int offset = 0;
		for(byte[] data : fileDefData) {
			System.arraycopy(data, 0, fullData, offset, data.length);
			offset += data.length;
		}

		System.arraycopy(bdfData, 0, fullData, offset, bdfData.length);
		return crc.calculateCrc(fullData);
		
	}
	
	public byte[] readFile(File file) throws IOException {
		byte[] data = new byte[(int) file.length()];
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		dis.read(data);
		dis.close();

		return data;
	}
}