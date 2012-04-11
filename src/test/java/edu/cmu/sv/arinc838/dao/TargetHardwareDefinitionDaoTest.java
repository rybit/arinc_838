/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 7, 2012
 */
package edu.cmu.sv.arinc838.dao;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.arinc.arinc838.ThwDefinition;

public class TargetHardwareDefinitionDaoTest {
	private TargetHardwareDefinitionDao first;
	private TargetHardwareDefinitionDao second;

	@Test
	public void testIdAccessors () {
		ThwDefinition jaxbDef = new com.arinc.arinc838.ThwDefinition();
		jaxbDef.setThwId("test");

		TargetHardwareDefinitionDao thDao = new TargetHardwareDefinitionDao();
		thDao.setThwId(jaxbDef.getThwId());
		
		assertEquals(thDao.getThwId(), jaxbDef.getThwId());
		
		thDao.setThwId("Not that value");
		assertNotEquals(thDao, jaxbDef.getThwId());
	}

	@Test
	public void testPositionsAccessors () {
		ThwDefinition jaxbDef = new ThwDefinition();
		
		jaxbDef.setThwId("id");
		jaxbDef.getThwPosition().add("one");
		jaxbDef.getThwPosition().add("two");

		TargetHardwareDefinitionDao thDao = new TargetHardwareDefinitionDao();

		assertEquals(thDao.getPositions().size(), 0);

		for (String pos : jaxbDef.getThwPosition()) {
			thDao.addPosition(pos);
		}
			
		assertEquals(thDao.getPositions().size(), jaxbDef.getThwPosition().size());
		assertEquals(thDao.getPositions().get(0), "one");
		assertEquals(thDao.getPositions().get(1), "two");
	}

	@Test
	public void equalsFailsIfPositionsAreNotSame() {
		first = new TargetHardwareDefinitionDao();
		first.setThwId("first");
		first.getPositions().add("one");
		first.getPositions().add("two");

		second = new TargetHardwareDefinitionDao();
		second.setThwId(first.getThwId());
		second.addPosition(first.getPositions().get(1));
		second.addPosition(first.getPositions().get(0));

		assertNotEquals(first, second);
	}

	@Test
	public void testHashCode() {
		assertEquals(first.hashCode(), first.getThwId().hashCode());
	}

	@Test
	public void testHashCodeWithNoId() {
		assertEquals(new TargetHardwareDefinitionDao().hashCode(), 0);
	}

	@Test
	public void testEquals() {
		first = new TargetHardwareDefinitionDao();
		first.setThwId("first");
		first.getPositions().add("one");
		first.getPositions().add("two");

		second = new TargetHardwareDefinitionDao();
		second.setThwId(first.getThwId());
		second.addPosition(first.getPositions().get(0));
		second.addPosition(first.getPositions().get(1));
		
		assertTrue (first.equals(second));
		assertTrue (second.equals(first));
		assertFalse (first.equals(null));
		assertFalse (first.equals(new Object()));
	}
}
