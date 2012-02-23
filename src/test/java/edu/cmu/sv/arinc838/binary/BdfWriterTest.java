package edu.cmu.sv.arinc838.binary;

import static org.testng.Assert.*;

import java.io.File;

import org.testng.annotations.Test;


public class BdfWriterTest {
	
	@Test
	public void writeUint32Test() throws Exception {
		BdfFile f = new BdfFile (File.createTempFile("tmpFile", ".bdf"));
		
		//Grab on more than the max value if we get a negative we know we go boom
		long uInt32 = (long)Integer.MAX_VALUE;
		uInt32++;
		
		f.writeUint32(uInt32);
		assertEquals(f.length(),BdfFile.UINT32_LENGTH);
		f.seek(0);
		
		long actualUint32 = BdfFile.asUint32(f.readInt());
				
		assertEquals(actualUint32, uInt32, "Expected  uInt32 = " + uInt32 + " received --> " + actualUint32);
	}
	
}
