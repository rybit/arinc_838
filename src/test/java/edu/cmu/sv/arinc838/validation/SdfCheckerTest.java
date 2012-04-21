package edu.cmu.sv.arinc838.validation;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.cmu.sv.arinc838.dao.FileDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.dao.SoftwareDescriptionDao;
import edu.cmu.sv.arinc838.dao.TargetHardwareDefinitionDao;
import edu.cmu.sv.arinc838.validation.SdfChecker.CompareRef;

/**
 * Each of the tests for the compare methods will mock out the sub layers -
 * those are tested otherwise. Each will verify the right # of calls but check
 * the primitives.
 * 
 * @author ryan
 */
public class SdfCheckerTest {
	private SdfChecker checker;
	
	private String type = "MyType";
	private List<String> empty = Collections.<String>emptyList ();
	private List<String> results;
	
	private Class<IntegrityDefinitionDao> integClass = IntegrityDefinitionDao.class;
	private Class<TargetHardwareDefinitionDao> thClass = TargetHardwareDefinitionDao.class;
	private Class<FileDefinitionDao> fDefClass = FileDefinitionDao.class;
	private Class<SoftwareDescriptionDao> descClass = SoftwareDescriptionDao.class;
	
	@BeforeMethod
	public void beforeMethod() {
		checker = new SdfChecker();
	}

	@Test
	public void testSoftwareDefinitionFileDaoComparison() {
		SdfChecker mockChecker = spy (checker);
		
		when (mockChecker.compare (any (integClass), any(integClass))).thenReturn(empty);
		when (mockChecker.compare(any (fDefClass), any(fDefClass))).thenReturn (empty);
		when (mockChecker.compare(any (thClass), any(thClass))).thenReturn (empty);
		when (mockChecker.compare(any (descClass), any (descClass))).thenReturn (empty);
		
		byte[] byteArray = new byte[] {0x11, 0x23};
		SoftwareDefinitionFileDao mine = new SoftwareDefinitionFileDao();
		mine.setFileFormatVersion(byteArray);
		
		SoftwareDefinitionFileDao theirs = new SoftwareDefinitionFileDao();
		theirs.setFileFormatVersion(byteArray);
		
		List<String> results = mockChecker.compare(mine, theirs);
		assertNotNull (results);
		assertEquals(results.size(), 0);
	}
	
	@Test
	public void testSoftwareDefinitionFileDaoFailedComparison() {
		SdfChecker mockChecker = spy (checker);
		
		when (mockChecker.compare (any (integClass), any(integClass))).thenReturn(empty);
		when (mockChecker.compare(any (fDefClass), any(fDefClass))).thenReturn (empty);
		when (mockChecker.compare(any (thClass), any(thClass))).thenReturn (empty);
		when (mockChecker.compare(any (descClass), any (descClass))).thenReturn (empty);
		
		byte[] byteArray = new byte[] {0x11, 0x23};
		SoftwareDefinitionFileDao mine = new SoftwareDefinitionFileDao();
		mine.setFileFormatVersion(byteArray);
		
		byteArray = new byte[] {0x12};
		SoftwareDefinitionFileDao theirs = new SoftwareDefinitionFileDao();
		theirs.setFileFormatVersion(byteArray);
		
		results = mockChecker.compare(mine, theirs);
		assertNotNull (results);
		assertEquals (results.size(), 1);
	}
	
	@Test 
	public void testSoftwareDescriptionComparisonGood () {
		SoftwareDescriptionDao mine = new SoftwareDescriptionDao();
		SoftwareDescriptionDao theirs = new SoftwareDescriptionDao();
		
		String partNumber = "partnumber";
		String description = "description";
		byte[] byteArray = new byte[] {0x11, 0x23};
		
		mine.setSoftwarePartnumber(partNumber);
		mine.setSoftwareTypeDescription(description);
		mine.setSoftwareTypeId(byteArray);
		theirs.setSoftwarePartnumber(partNumber);
		theirs.setSoftwareTypeDescription(description);
		theirs.setSoftwareTypeId(byteArray);
	
		results = checker.compare(mine, theirs);
		assertNotNull (results);
		assertEquals(results.size(), 0);

		results = checker.compare(theirs, mine);
		assertNotNull (results);
		assertEquals(results.size(), 0);
	}
	
	@Test 
	public void testSoftwareDescriptionComparisonBad () {
		SoftwareDescriptionDao mine = new SoftwareDescriptionDao();
		SoftwareDescriptionDao theirs = new SoftwareDescriptionDao();
		
		String partNumber = "partnumber";
		String description = "description";
		byte[] byteArray = new byte[] {0x11, 0x23};
		
		mine.setSoftwarePartnumber(partNumber);
		mine.setSoftwareTypeDescription(description);
		mine.setSoftwareTypeId(byteArray);
		theirs.setSoftwarePartnumber(partNumber + "garbage");
		theirs.setSoftwareTypeDescription(description + "garbage");
		theirs.setSoftwareTypeId(byteArray);
	
		results = checker.compare(mine, theirs);
		assertNotNull (results);
		assertEquals (results.size(), 2);
		
		results = checker.compare(mine, theirs);
		assertNotNull (results);
		assertEquals (results.size(), 2);
	}

	@Test
	public void testIntegDefGood () {
		IntegrityDefinitionDao mine = new IntegrityDefinitionDao ();
		IntegrityDefinitionDao theirs = new IntegrityDefinitionDao ();
		
		long integType = 4;
		byte[] value = new byte[] {0x11, 0x23};

		mine.setIntegrityType(integType);
		mine.setIntegrityValue(value);
		theirs.setIntegrityType(integType);
		theirs.setIntegrityValue(value);

		results = checker.compare(theirs, mine);
		assertNotNull (results);
		assertEquals (results.size(), 0);
		
		results = checker.compare(mine, theirs);
		assertNotNull (results);
		assertEquals (results.size(), 0);
	}
	
	@Test
	public void testIntegDefBad () {
		IntegrityDefinitionDao mine = new IntegrityDefinitionDao ();
		IntegrityDefinitionDao theirs = new IntegrityDefinitionDao ();
		
		long integType = 4;
		byte[] value = new byte[] {0x11, 0x23};

		mine.setIntegrityType(integType);
		mine.setIntegrityValue(value);
		
		theirs.setIntegrityType(integType + 1);
		theirs.setIntegrityValue(new byte[] {0x34});

		results = checker.compare(theirs, mine);
		assertNotNull (results);
		assertEquals (results.size(), 2);
		
		results = checker.compare(mine, theirs);
		assertNotNull (results);
		assertEquals (results.size(), 2);
	}

	@Test 
	public void testFileDefinitionGood () {
		SdfChecker mockChecker = spy (checker);
		when (mockChecker.compare(any (integClass), any (integClass))).thenReturn(empty);
		
		FileDefinitionDao mine = new FileDefinitionDao();
		FileDefinitionDao theirs = new FileDefinitionDao();
		
		String fname = "fname";
		long fsize = 123;
		
		mine.setFileLoadable(true);
		mine.setIsLast(true);
		mine.setFileName(fname);
		mine.setFileSize(fsize);

		theirs.setFileLoadable(true);
		theirs.setIsLast(true);
		theirs.setFileName(fname);
		theirs.setFileSize(fsize);
		
		results = checker.compare(mine, theirs);
		assertNotNull (results);
		assertEquals(results.size (), 0);
		
		results = checker.compare(theirs, mine);
		assertNotNull (results);
		assertEquals(results.size (), 0);
	}
	
	@Test 
	public void testFileDefinitionBad () {
		SdfChecker mockChecker = spy (checker);
		when (mockChecker.compare(any (integClass), any (integClass))).thenReturn(empty);
		
		FileDefinitionDao mine = new FileDefinitionDao();
		FileDefinitionDao theirs = new FileDefinitionDao();
		
		String fname = "fname";
		long fsize = 123;
		
		mine.setFileLoadable(true);
		mine.setIsLast(true);
		mine.setFileName(fname);
		mine.setFileSize(fsize);

		theirs.setFileLoadable(true);
		theirs.setIsLast(true);
		theirs.setFileName(fname + "garbage");
		theirs.setFileSize(fsize + 32);
		
		results = checker.compare(mine, theirs);
		assertNotNull (results);
		assertEquals(results.size (), 2);
		
		results = checker.compare(theirs, mine);
		assertNotNull (results);
		assertEquals(results.size (), 2);
	}
	
	@Test
	public void testTargetHardwareDefGood () {
		TargetHardwareDefinitionDao mine = new TargetHardwareDefinitionDao();
		TargetHardwareDefinitionDao theirs = new TargetHardwareDefinitionDao();
		
		String hwId = "test";
		
		mine.setIsLast(true);
		mine.setThwId(hwId);
		theirs.setIsLast(true);
		theirs.setThwId(hwId);
		
		results = checker.compare(mine, theirs);
		assertNotNull (results);
		assertEquals(results.size (), 0);
		
		results = checker.compare(theirs, mine);
		assertNotNull (results);
		assertEquals(results.size (), 0);
	}
	
	@Test
	public void testTargetHardwareDefBad () {
		TargetHardwareDefinitionDao mine = new TargetHardwareDefinitionDao();
		TargetHardwareDefinitionDao theirs = new TargetHardwareDefinitionDao();
		
		String hwId = "test";
		
		mine.setIsLast(true);
		mine.setThwId(hwId);
		theirs.setIsLast(true);
		theirs.setThwId(hwId + "garbage");
		
		results = checker.compare(mine, theirs);
		assertNotNull (results);
		assertEquals(results.size (), 1);
		
		results = checker.compare(theirs, mine);
		assertNotNull (results);
		assertEquals(results.size (), 1);
	}	
	
	@Test
	public void testNullCheck (){
		assertTrue (checker.nullCheck(null, null, results));
		assertTrue (checker.nullCheck(null, new Object (), results));
		assertEquals (results.size(), 1);
		assertTrue (checker.nullCheck(new Object (), null, results));
		assertEquals (results.size(), 2);
		assertFalse (checker.nullCheck(new Object (), new Object (), results));
		assertEquals (results.size(), 2);
	}
	
	@Test
	public void testCheckListNullNull() {
		@SuppressWarnings("unchecked")
		CompareRef<Object> comp = mock(CompareRef.class);
		List<Object> mine = null, theirs = null;

		results = checker.check(mine, theirs, type, comp);
		assertTrue(checker.check(mine, theirs, type, results));
		assertEquals(results.size(), 0);
	}

	@Test
	public void testCheckListNotNullNull() {
		@SuppressWarnings("unchecked")
		CompareRef<Object> comp = mock(CompareRef.class);
		List<Object> mine = null, theirs = null;
		mine = new ArrayList<Object>();

		results = checker.check(mine, theirs, type, comp);
		assertEquals(results.size(), 1);
		assertTrue(results.get(0).contains(type));
	}

	@Test
	public void testCheckListNullNotNull() {
		@SuppressWarnings("unchecked")
		CompareRef<Object> comp = mock(CompareRef.class);
		List<Object> mine = null, theirs = null;
		theirs = new ArrayList<Object>();

		results = checker.check(mine, theirs, type, comp);
		assertEquals(results.size(), 1);
		assertTrue(results.get(0).contains(type));
	}

	@Test
	public void testCheckListSameObject() {
		@SuppressWarnings("unchecked")
		CompareRef<Object> comp = mock(CompareRef.class);
		List<Object> mine = null, theirs = null;

		theirs = new ArrayList<Object>();
		mine = theirs;

		results = checker.check(mine, theirs, type, comp);
		assertTrue(checker.check(mine, theirs, type, results));
		assertEquals(results.size(), 0);
	}

	@Test
	public void testCheckListDifferentListSameObject() {
		@SuppressWarnings("unchecked")
		CompareRef<Object> comp = mock(CompareRef.class);
		List<Object> mine = null, theirs = null;
		Object same = new Object();

		theirs = new ArrayList<Object>();
		mine = new ArrayList<Object>();
		mine.add(same);
		theirs.add(same);

		results = checker.check(mine, theirs, type, comp);
		assertTrue(checker.check(mine, theirs, type, results));
		assertEquals(results.size(), 0);
	}

	@Test
	public void testCheckListDifferentListWrongNumObject() {
		@SuppressWarnings("unchecked")
		CompareRef<Object> comp = mock(CompareRef.class);
		List<Object> mine = null, theirs = null;
		Object same = new Object();

		theirs = new ArrayList<Object>();
		theirs.add(same);
		mine = new ArrayList<Object>();
		mine.add(same);
		mine.add(new Object());

		results = checker.check(mine, theirs, type, comp);
		assertEquals(results.size(), 1);
		assertTrue(results.get(0).contains(type));
	}

	@Test
	public void testSimpleCheckNullNull() {
		results = new ArrayList<String>();
		Object mine = null, theirs = null;

		assertTrue(checker.check(mine, theirs, type, results));
		assertEquals(results.size(), 0);
	}

	@Test
	public void testSimpleCheckNotNullNull() {
		results = new ArrayList<String>();
		Object mine = new Object(), theirs = null;

		assertFalse(checker.check(mine, theirs, type, results));
		assertEquals(results.size(), 1);
		assertTrue(results.get(0).contains(type));
	}

	@Test
	public void testSimpleCheckNullNotNull() {
		results = new ArrayList<String>();
		Object mine = null, theirs = new Object();

		assertFalse(checker.check(mine, theirs, type, results));
		assertEquals(results.size(), 1);
		assertTrue(results.get(0).contains(type));
	}

	@Test
	public void testSimpleCheckSameObj() {
		results = new ArrayList<String>();
		Object mine = new Object(), theirs = mine;

		// same objects
		theirs = mine;
		assertTrue(checker.check(mine, theirs, type, results));
		assertEquals(results.size(), 0);
	}

	@Test
	public void testSimpleCheckDifferentObjects() {
		results = new ArrayList<String>();
		Object mine = new Object(), theirs = new Object();

		assertFalse(checker.check(mine, theirs, type, results));
		assertEquals(results.size(), 1);
		assertTrue(results.get(0).contains(type));
	}
}
