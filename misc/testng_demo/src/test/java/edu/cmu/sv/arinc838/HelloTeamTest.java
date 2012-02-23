/*
 * Created on Jan 21, 2012
 */
package edu.cmu.sv.arinc838;

import static org.testng.Assert.*;

import org.testng.annotations.*;

import edu.cmu.sv.arinc838.HelloTeam;

/**
 * @author Mike Deats
 * 
 * 
 */
public class HelloTeamTest {

	private static Integer staticVar1;
	private static Integer staticVar2;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// This method runs before any tests. All references must be static
		staticVar1 = 7;
		staticVar2 = 9;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// This method runs after all tests
	}

	private HelloTeam team1;
	private HelloTeam team2;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeMethod
	public void setUp() throws Exception {
		// This method runs before each tests
		team1 = new HelloTeam(1);
		team2 = new HelloTeam(-3);

		// you can assert within non-test methods too:
		assertEquals(-3, team2.getMyNumber(), 0.1);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterMethod
	public void tearDown() throws Exception {
		// This method runs after each test
	}

	@Test
	public void aSimpleTest() {
		int a = 1;
		int b = a;
		assertTrue(a == b, "That shouldn't have happened");
	}

	@Test
	public void testNoAssertions() {
		// This test will still pass with no assertions
	}

	@Test
	public void assertionsWithComparisons() {
		// Comparison assertions follow the pattern:
		// <failure message>, <expected> <actual>
		// You can assert equality with primatives and objects
		assertEquals(7, staticVar1.intValue(), "This should pass");
		assertEquals(new Integer(7), staticVar1, "This too shall pass");
	}

	@Test
	public void assertionsWithNull() {
		// You can assert that something is null or not null

		assertNull("This should not fail", null);
		assertNotNull(new Object(), "This should pass");
		assertNotNull(null, "This should fail because null is not 'not null'");
	}

	@Test
	public void assertionsWithBooleans() {
		// These are pretty self-explanatory
		assertTrue(true, "Should pass");
		assertFalse(false, "Should pass");
	}

	@Test
	public void testFail() {
		// This test will always fail, even if you have assertions that pass
		assertTrue(true);
		fail("YOU! SHALL NOT! PASS!");

		// When a test fails, the method ends at the failure. This is why you
		// should
		// never release resources within a test method
		System.out.println("You shouldn't see me");
	}

	public void notATestMethod() {
		// This is not a test, and will not run
		System.out.println("If you see this message, you're already dead.");
	}

	@Test(expectedExceptions = NullPointerException.class)
	public void testThrowsException() {
		// This test will pass if it catches a NullPointerException
		throw new NullPointerException("I'm null!");
	}

	@Test(expectedExceptions = NullPointerException.class)
	public void testThrowsUnexpectedException() {
		// This test will fail with an error because it has the wrong exception
		throw new IllegalArgumentException("I am the wrong exception");
	}

	@Test(expectedExceptions = NullPointerException.class)
	public void testShouldThrowException() {
		// This test will fail because the exception was not thrown, even though
		// there is a passing assertion

		assertTrue(true);
	}

	@Test
	public void assertionsWithDoubles1() {
		// Doubles and floats require a delta parameter. This defines
		// the absolute value by which the numbers can differ. This
		// parameter must be positive or the method will always fail

		// The 0.001 means the numbers can differ by up to 0.001. Since these
		// numbers differ by 0.0005, this is fine
		assertEquals(1.2345, 1.234, 0.001, "These should be equal");

		// Same thing here with 0.0005 and 0.0006
		assertEquals(1.2345, 1.234, 0.0005, "These should be equal");
		assertEquals(1.2345, 1.234, 0.0006, "These should be equal");

		// In this case, the increased precision will cause this to fail
		assertEquals(1.2345, 1.234, 0.0001, "These should be equal");
	}

	@Test
	public void assertionsWithDoubles2() {
		// The precision factor works with larger values as well. This passes
		// because the numbers differ by 1
		assertEquals(100, 99, 1.0, "These should be equal");

		// Note that this also passes
		assertEquals(99, 100, 1.0, "These should be equal");

		// This will fail because the difference is > 1
		assertEquals(100, 98, 1.0, "These are not equal, but this passes?");
	}

	@Test
	public void testSetMyNumber() {
		assertEquals(1, team1.getMyNumber(), 0.01, "My number was incorrect");
		team1.setMyNumber(99);
		assertEquals(99, team1.getMyNumber(), 0.01, "My number was incorrect");
	}

	@Test
	public void testGetMyNumber() {
		assertEquals(1, team1.getMyNumber(), 0.01, "My number was incorrect");
	}

}
