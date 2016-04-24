package com.danielbchapman.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class UtilityXml
{
	public static Document readDocument(File file)
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try
		{
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);
			return doc;
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}
	
	public static XPathExpression compileXPath(String expression)
	{
		try
		{
			XPathExpression xpath = XPathFactory.newInstance().newXPath().compile(expression);
			return xpath;
		}
		catch (XPathExpressionException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
   * @param xpath
   * @param document
   * @return null on exception (exception will be logged)
   */
  public static Node node(Document document, String xpath)
  {
    XPathExpression path = compileXPath(xpath);
    try
    {
      return (Node) path.evaluate(document, XPathConstants.NODE);
    }
    catch (XPathExpressionException e)
    {
      e.printStackTrace();
      return null;
    }
  }
  
	/**
	 * @param xpath
	 * @param document
	 * @return null on exception (exception will be logged)
	 */
	public static NodeList nodeList(Document document, String xpath)
	{
		XPathExpression path = compileXPath(xpath);
		try
    {
      return (NodeList) path.evaluate(document, XPathConstants.NODESET);
    }
    catch (XPathExpressionException e)
    {
      e.printStackTrace();
      return null;
    }
	}
	
	public static String printXml(Document doc)
	{
		if(doc == null)
			return "null";
		
		Node first = doc.getFirstChild();
		if(first == null)
			return "First node is null";
		
		return printXml(first);
	}
	
	public static String printXml(Node node)
	{
		if(node == null)
			return "null";
		
		StringBuilder builder = new StringBuilder();
		printXmlHelper(node, builder, 0);
		
		return builder.toString();
	}
	
	private static void printXmlHelper(Node node, StringBuilder builder, int indent)
	{
		if(node == null)
			return;
		
		if(node.getNodeType() == Node.ELEMENT_NODE)
			appendLine(node.getNodeName(), indent, builder);
		else if (node.getNodeType() == Node.TEXT_NODE && !UtilityText.isEmpty(node.getNodeValue()))
			appendLine(node.getNodeName() + ": " + node.getNodeValue(), indent, builder);
		
		if(node.hasChildNodes())
		{
			NodeList list = node.getChildNodes();
			if(list != null)
				for(int i = 0; i < list.getLength(); i++)
					printXmlHelper(list.item(i), builder, indent + 1);
		}
	}
	
	private static void appendLine(String line, int sets, StringBuilder builder)
	{
		for(int i = 0; i < sets; i++)
			builder.append("  ");
		builder.append(line);
		builder.append("\n");
	}
}
