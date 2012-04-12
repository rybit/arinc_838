package edu.cmu.sv.arinc838.crc;

public class CrcGeneratorFactory {

	public Crc16Generator getCrc16Generator() {
		return new Crc16Generator();
	}

	public Crc32Generator getCrc32Generator() {
		return new Crc32Generator();
	}

	public Crc64Generator getCrc64Generator() {
		return new Crc64Generator();
	}

}
