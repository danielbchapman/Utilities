package com.danielbchapman.utilites.tests;

import java.io.File;

import javax.xml.xpath.XPathExpression;

import org.junit.Test;
import org.w3c.dom.Document;

import com.danielbchapman.utility.Xml;

public class TestXmlUtility
{
	@Test
	public void TestDocumentRead()
	{
		Document xml = Xml.readDocument(new File("tests/xml/vectorworks.xml"));
		System.out.println(Xml.printXml(xml));
	}
	
	@Test
	public void TestXPathCompile()
	{
		XPathExpression xpath = Xml.compileXPath("*");
		System.out.println(xpath);
	}
}
