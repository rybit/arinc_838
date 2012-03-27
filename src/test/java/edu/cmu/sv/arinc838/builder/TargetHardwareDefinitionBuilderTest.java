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

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.arinc.arinc838.ThwDefinition;

import edu.cmu.sv.arinc838.builder.TargetHardwareDefinitionBuilder;

public class TargetHardwareDefinitionBuilderTest {
	private TargetHardwareDefinitionBuilder first;
	private TargetHardwareDefinitionBuilder second;

	@BeforeMethod
	public void setup() {
		first = new TargetHardwareDefinitionBuilder();
		first.setThwId("first");
		first.getPositions().add("one");
		first.getPositions().add("two");
		
		second = new TargetHardwareDefinitionBuilder();
		second.setThwId("first");
		second.getPositions().add("one");
		second.getPositions().add("two");		
	}

	@Test
	public void testBuildReturnsProperJaxbObject() {
		ThwDefinition def = first.buildXml();
		
		assertEquals(def.getThwId(), first.getThwId());
		
		for(int i = 0; i < def.getThwPosition().size(); i++){
			assertEquals(def.getThwPosition().get(i), first.getPositions().get(i));
		}
	}
}
