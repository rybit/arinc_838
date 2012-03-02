package edu.cmu.sv.arinc838.binary;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.cmu.sv.arinc838.validation.DataValidator;

public class BdfFileTest {

	private BdfFile f;

	@BeforeMethod
	public void setup() throws FileNotFoundException, IOException {
		f = new BdfFile(File.createTempFile("tmpFile", ".bdf"));
	}

	@Test
	public void writeUint32Test() throws Exception {
		// Grab on more than the max value if we get a negative we know we go
		// boom
		long uInt32 = (long) Integer.MAX_VALUE;
		uInt32++;

		f.writeUint32(uInt32);
		assertEquals(f.length(), BdfFile.UINT32_LENGTH);
		f.seek(0);

		long actualUint32 = BdfFile.asUint32(f.readInt());

		assertEquals(actualUint32, uInt32);
	}

	@Test
	public void writePlaceholderTest() throws Exception {
		// Grab one more than the max value if we get a negative we know we go
		// boom

		f.writePlaceholder();

		assertEquals(f.length(), BdfFile.UINT32_LENGTH);
		f.seek(0);

		long actualUint32 = BdfFile.asUint32(f.readInt());

		assertEquals(actualUint32, 0);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void writeUint32UsesValidationTest() throws Exception {
		long badUInt32 = Long.MAX_VALUE;

		f.writeUint32(badUInt32);
	}

	/**
	 * We have this test to ensure that the RandomAccessFile class we are
	 * extending behaves as our API requires.
	 * 
	 * There is no validation test because you can't have a bad boolean.
	 * 
	 * @throws Exception
	 */
	@Test
	public void writeAndReadBoolean() throws Exception {
		boolean expected = true;

		f.writeBoolean(expected);
		assertEquals(f.length(), BdfFile.BOOLEAN_LENGTH);
		f.seek(0);

		boolean actual = f.readBoolean();

		assertEquals(actual, expected);
	}	

	@Test
	public void writeAndReadStr64k() throws Exception {
		String ipsum = "lorum ipsum";

		f.writeStr64k(ipsum);
		assertEquals(f.length(), ipsum.toCharArray().length + 2);
		f.seek(0);

		String actual = f.readStr64k();

		assertEquals(actual, ipsum);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void writeStr64kUsesValidation() throws Exception {

		StringBuilder value = new StringBuilder();

		// build a string that is too big
		for (int i = 0; i < DataValidator.STR64K_MAX_LENGTH + 1; i++) {
			value.append('c');
		}

		f.writeStr64k(value.toString());
	}

	@Test
	public void writeHexbin32() throws IOException {
		byte[] hexBin = new byte[4];
		Arrays.fill(hexBin, (byte) 99);

		f.writeHexbin32(hexBin);
		// offset 2 bytes for length
		f.seek(0);
		
		byte[] hexBin2 = f.readHexbin32();
				
		assertEquals(hexBin2, hexBin);
	}

	@Test
	public void writeHexbin64k() throws IOException {
		byte[] hexBin = new byte[10];
		Arrays.fill(hexBin, (byte) 99);

		f.writeHexbin64k(hexBin);
		// offset 2 bytes for length
		f.seek(2);
		byte[] hexBin2 = new byte[hexBin.length];

		assertEquals(f.read(hexBin2), hexBin.length);
		assertEquals(hexBin2, hexBin);
	}

	@Test
	public void writeHexbin64kMax() throws IOException {
		byte[] hexBin = new byte[DataValidator.HEXBIN64K_MAX_LENGTH];
		Arrays.fill(hexBin, (byte) 104);

		f.writeHexbin64k(hexBin);
		// offset 2 bytes for length
		f.seek(2);
		byte[] hexBin2 = new byte[DataValidator.HEXBIN64K_MAX_LENGTH];

		assertEquals(f.read(hexBin2), DataValidator.HEXBIN64K_MAX_LENGTH);
		assertEquals(hexBin2, hexBin);
	}

	@Test
	public void readUint32() throws Exception {
		long uInt32 = (long) Integer.MAX_VALUE;
		uInt32++;

		f.writeUint32(uInt32);

		f.seek(0);

		assertEquals(f.readUint32(), uInt32);
	}

	@Test
	public void readHexbin64k() throws IOException {
		byte[] hexbin64k = new byte[] { 1, 2, 3, 4, 5, 6 };
		f.writeHexbin64k(hexbin64k);
		f.seek(0);
		assertEquals(f.readHexbin64k(), hexbin64k);
	}

	@Test
	public void testWriteSoftwareDescriptionPointer() throws Exception {
		long expected = 42;
		f.seek(expected);

		f.writeSoftwareDescriptionPointer();

		f.seek(BdfFile.SOFTWARE_DESCRIPTION_POINTER_LOCATION);
		assertEquals(f.readUint32(), expected);
	}

	@Test
	public void testWriteTargetDefinitionsPointer() throws Exception {
		long expected = 42;
		f.seek(expected);

		f.writeTargetDefinitionsPointer();

		f.seek(BdfFile.TARGET_DEFINITIONS_POINTER_LOCATION);
		assertEquals(f.readUint32(), expected);
	}

	@Test
	public void testWriteFileDefinitionsPointer() throws Exception {
		long expected = 42;
		f.seek(expected);

		f.writeFileDefinitionsPointer();

		f.seek(BdfFile.FILE_DEFINITIONS_POINTER_LOCATION);
		assertEquals(f.readUint32(), expected);
	}

	@Test
	public void testWriteSdfIntegrityDefinitionPointer() throws Exception {
		long expected = 42;
		f.seek(expected);

		f.writeSdfIntegrityDefinitionPointer();

		f.seek(BdfFile.SDF_INTEGRITY_POINTER_LOCATION);
		assertEquals(f.readUint32(), expected);
	}

	@Test
	public void testSeekAndRestoreFilePointer() throws Exception {
		long expected = 42;
		f.seek(expected);

		f.writeSdfIntegrityDefinitionPointer();
		assertEquals(f.getFilePointer(), expected);
	}

	@Test
	public void testWriteLspIntegrityDefinitionPointer() throws Exception {
		long expected = 42;
		f.seek(expected);

		f.writeLspIntegrityDefinitionPointer();

		f.seek(BdfFile.LSP_INTEGRITY_POINTER_LOCATION);
		assertEquals(f.readUint32(), expected);
	}
	
	@Test
	public void testReadSoftwareDescriptionPointer() throws IOException {
		f.seek(100); // pretend the software-description starts at byte 100
		f.writeSoftwareDescriptionPointer();
		assertEquals(f.readSoftwareDescriptionPointer(), 100);
	}

	@Test
	public void testReadTargetDefinitionsPointer() throws IOException {
		f.seek(100); // pretend the target-definitions starts at byte 100
		f.writeTargetDefinitionsPointer();
		assertEquals(f.readTargetDefinitionsPointer(), 100);
	}

	@Test
	public void testReadFileDefinitionsPointer() throws IOException {
		f.seek(100); // pretend the file-definitions starts at byte 100
		f.writeFileDefinitionsPointer();
		assertEquals(f.readFileDefinitionsPointer(), 100);
	}

	@Test
	public void testReadSdfIntegrityDefinitionPointer() throws IOException {
		f.seek(100); // pretend the sdf-integrity-definition starts at byte 100
		f.writeSdfIntegrityDefinitionPointer();
		assertEquals(f.readSdfIntegrityDefinitionPointer(), 100);
	}

	@Test
	public void testReadLspIntegrityDefinitionPointer() throws IOException {
		f.seek(100); // pretend the lsp-integrity-definition starts at byte 100
		f.writeLspIntegrityDefinitionPointer();
		assertEquals(f.readLspIntegrityDefinitionPointer(), 100);
	}

	@Test
	public void testReadHexbin32k() throws Exception{
		byte[] expected = new byte[4];
		expected[0] = 1;
		expected[1] = 2;
		expected[2] = 3;
		expected[3] = 4;
		
		f.write(expected);
		
		f.seek(0);
		
		assertEquals(f.readHexbin32(), expected);		
	}
	
	@Test
	public void testReadHexbin64k() throws Exception{
		byte[] expected = new byte[8];
		expected[0] = 1;
		expected[1] = 2;
		expected[2] = 3;
		expected[3] = 4;
		expected[4] = 5;
		expected[5] = 6;
		expected[6] = 7;
		expected[7] = 8;
		
		f.writeHexbin64k(expected);
		
		f.seek(0);
		
		assertEquals(f.readHexbin64k(), expected);		
	}
}
