package edu.cmu.sv.arinc838.crc;

public interface CrcGenerator {
	public long calculateCrc(byte[] bytes);
}