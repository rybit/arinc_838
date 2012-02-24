package edu.cmu.sv.arinc838.validation;

import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;

public class XmlFormatterTest {
	@Test
	public void testUnEscapeSpecialXmlChars()
	{
		assertEquals("PB&J", XmlFormatter.unescapeXmlSpecialChars("PB&ampJ"));
		assertEquals("A > B", XmlFormatter.unescapeXmlSpecialChars("A &gt B"));
		assertEquals("A < B", XmlFormatter.unescapeXmlSpecialChars("A &lt B"));
		assertEquals("A < B && B > C", XmlFormatter.unescapeXmlSpecialChars("A &lt B &amp&amp B &gt C"));
	}
	
	@Test
	public void testEscapeSpecialXmlChars()
	{
		assertEquals("PB&ampJ", XmlFormatter.escapeXmlSpecialChars("PB&J"));
		assertEquals("A &gt B", XmlFormatter.escapeXmlSpecialChars("A > B"));
		assertEquals("A &lt B", XmlFormatter.escapeXmlSpecialChars("A < B"));
		assertEquals("A &lt B &amp&amp B &gt C", XmlFormatter.escapeXmlSpecialChars("A < B && B > C"));	}
}
