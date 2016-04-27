package com.danielbchapman.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.danielbchapman.text.Text;

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

  public static String text(Node node, String xpath)
  {
    XPathExpression exp = compileXPath(xpath);
    try
    {
      return (String) exp.evaluate(node);
    }
    catch (XPathExpressionException e)
    {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * This method writes to the stream specified
   * @param doc the document to write
   * @param stream the stream to write to
   * @throws TransformerException 
   * @throws IOException 
   */
  public static void writeToStream(Document doc, OutputStream stream) throws TransformerException, IOException
  {
      TransformerFactory tFactory = TransformerFactory.newInstance();
      Transformer transformer = tFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(stream);
      transformer.transform(source, result);
  }

  /**
   * Write an XML document out to the file specified. Exceptions 
   * will be swallowed and placed on the error stream.
   * @param doc
   * @param file
   */
  public static void writeToFile(Document doc, String file)
  {
    try(FileOutputStream out = new FileOutputStream(new File(file)))
    {
      writeToStream(doc, out);
      out.close();
    }
    catch (IOException | TransformerException e)
    {
      e.printStackTrace();
    }
  }

  public static String printXml(Document doc)
  {
    if (doc == null)
      return "null";

    Node first = doc.getFirstChild();
    if (first == null)
      return "First node is null";

    return printXml(first);
  }

  public static String printXml(Node node)
  {
    if (node == null)
      return "null";

    StringBuilder builder = new StringBuilder();
    printXmlHelper(node, builder, 0);

    return builder.toString();
  }

  /**
   * @param node the node to read
   * @return the value of this node's inner text node 
   * <tt>
   * example: 
   *   &lt;Node&gt;Has Some Text&lt;/Node&gt;
   *   &lt;Node2&gt;Has Some Text&lt;/Node2&gt;
   *   
   *   called on the node "Node" would result in 
   *   "Has Some Text"
   *   
   *   called on the node "Node" would result in 
   *   null (as this is empty)
   * </tt>
   */
  public static String getNodeText(Node node)
  {
    if (node.getNodeType() == Node.ELEMENT_NODE)
    {
      Node text = node.getFirstChild();
      if (text != null && text.getNodeType() == Node.TEXT_NODE)
      {
        if (isNullOrWhitespace(node))
          return null;
        else
          return node.getTextContent();
      }
      else
        return null;
    }
    else
      return null;
  }

  public static boolean isNullOrWhitespace(Node node)
  {
    if (node.getNodeType() == Node.TEXT_NODE)
      return Text.isEmpty(node.getTextContent());

    return false;
  }

  private static void printXmlHelper(Node node, StringBuilder builder, int indent)
  {
    if (node == null)
      return;

    if (node.getNodeType() == Node.ELEMENT_NODE)
      appendLine(node.getNodeName(), indent, builder);
    else
      if (node.getNodeType() == Node.TEXT_NODE && !Text.isEmpty(node.getNodeValue()))
        appendLine(node.getNodeName() + ": " + node.getNodeValue(), indent, builder);

    if (node.hasChildNodes())
    {
      NodeList list = node.getChildNodes();
      if (list != null)
        for (int i = 0; i < list.getLength(); i++)
          printXmlHelper(list.item(i), builder, indent + 1);
    }
  }

  private static void appendLine(String line, int sets, StringBuilder builder)
  {
    for (int i = 0; i < sets; i++)
      builder.append("  ");
    builder.append(line);
    builder.append("\n");
  }
}
