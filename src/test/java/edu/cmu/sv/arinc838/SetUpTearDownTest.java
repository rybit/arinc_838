/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Jan 21, 2012
 */
package edu.cmu.sv.arinc838;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SetUpTearDownTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private boolean keepGoing1;
	private boolean keepGoing2;

	private Thread t1;
	private Thread t2;

	@Before
	public void setUp() throws Exception {
		keepGoing1 = true;
		keepGoing2 = true;

		t1 = new Thread(new Runnable() {

			public void run() {
				while (keepGoing1)
					Thread.yield();

			}
		});

		t2 = new Thread(new Runnable() {

			public void run() {
				while (keepGoing2)
					Thread.yield();

			}
		});
	}

	@After
	public void tearDown() throws Exception {


		try {
			t1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// all done, now close my thread
		keepGoing2 = false;
		try {
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testThatDoesNotTearDownCorrectly() {

		System.out.println("This test will hang due to a "
				+ "failure that leaves the thread open");

		t1.start();

		String s = "Hello W0Rld!";

		assertEquals("HELLO WORLD!", s.toUpperCase());

		// all done, now close my thread
		keepGoing1 = false;
	}

	@Test
	public void testThatDoesTearDownCorrectly() {
		System.out.println("This test completes despite the thread left open");

		t2.start();

		String s = "hEllo wOrLD!";

		assertEquals("HELLO WORLD!", s.toUpperCase());
	}
}
