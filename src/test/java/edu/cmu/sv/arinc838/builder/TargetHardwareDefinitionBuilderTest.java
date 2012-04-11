/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 7, 2012
 */
package edu.cmu.sv.arinc838.builder;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.dao.TargetHardwareDefinitionDao;

public class TargetHardwareDefinitionBuilderTest {
	private TargetHardwareDefinitionDao first;

	@BeforeMethod
	public void setup() {
		first = new TargetHardwareDefinitionDao();
		first.setThwId("first");
		first.getPositions().add("one");
		first.getPositions().add("two");
	}

	@Test
	public void testBuildReturnsProperJaxbObject() {
		ThwDefinition def = new TargetHardwareDefinitionBuilder().buildXml(first);

		for (int i = 0; i < def.getThwPosition().size(); i++) {
			assertEquals(def.getThwPosition().get(i), first.getPositions().get(i));
		}
	}
}
