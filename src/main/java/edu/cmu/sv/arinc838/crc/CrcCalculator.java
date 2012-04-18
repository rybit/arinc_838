/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Apr 14, 2012
 */
package edu.cmu.sv.arinc838.crc;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.dao.FileDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao.IntegrityType;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;

public class CrcCalculator {

	public static long calculateLspCrc(SoftwareDefinitionFileDao sdf,
			BdfFile bdf) throws IOException {
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
		
		return calculateCrc(sdf.getSdfIntegrityDefinition(), fullData);
	}

	public static long calculateSdfCrc(SoftwareDefinitionFileDao sdf,
			BdfFile bdf) throws IOException {
		byte[] data = new byte[(int) bdf.readSdfIntegrityDefinitionPointer() + 6];
		bdf.seek(0);
		bdf.read(data);

		return calculateCrc(sdf.getSdfIntegrityDefinition(), data);

	}

	public static byte[] readFile(File file) throws IOException {
		byte[] data = new byte[(int) file.length()];
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		dis.read(data);
		dis.close();

		return data;
	}

	public static long calculateCrc(IntegrityDefinitionDao integ, byte[] data) {
		IntegrityType type = IntegrityType.fromLong(integ.getIntegrityType());
		long crc = -1;
		CrcGeneratorFactory factory = new CrcGeneratorFactory();
		switch (type) {
		case CRC16:
			crc = factory.getCrc16Generator().calculateCrc(data) & 0xFFFFL;
			break;
		case CRC32:
			crc = factory.getCrc32Generator().calculateCrc(data) & 0xFFFFFFFFL;
			break;
		case CRC64:
			crc = factory.getCrc64Generator().calculateCrc(data);
			break;
		default:
			break;
		}

		return crc;
	}

}
