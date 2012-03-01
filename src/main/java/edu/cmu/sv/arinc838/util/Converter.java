package edu.cmu.sv.arinc838.util;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class Converter {
	public static byte[] hexToBytes(String hexString) {
	     return new HexBinaryAdapter().unmarshal(hexString);
	}	
}
