package com.danielbchapman.utilites.tests;

import java.io.File;

import javax.xml.xpath.XPathExpression;

import org.junit.Test;
import org.w3c.dom.Document;

import com.danielbchapman.utility.UtilityXml;

public class TestXmlUtility
{
	@Test
	public void TestDocumentRead()
	{
		Document xml = UtilityXml.readDocument(new File("tests/xml/vectorworks.xml"));
		System.out.println(UtilityXml.printXml(xml));
	}
	
	@Test
	public void TestXPathCompile()
	{
		XPathExpression xpath = UtilityXml.compileXPath("*");
	}
}
