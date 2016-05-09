package com.danielbchapman.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
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

public class Xml
{
  private static void appendLine(String line, int sets, StringBuilder builder)
  {
    for (int i = 0; i < sets; i++)
      builder.append("  ");
    builder.append(line);
    builder.append("\n");
  }

  /**
   * Appends a child node to this node and returns it.
   * @param parent
   * @param tag
   * @return the child node
   * 
   */
  public static Node appendChild(Node parent, String tag)
  {
    Document doc = parent.getOwnerDocument();
    if(doc == null && parent instanceof Document)
      doc = (Document) parent;
    Node child = doc.createElement(tag);
    parent.appendChild(child);
    return child;
  }
  
  /**
   * Appends a child node to this parent with the string content specified
   * @param parent
   * @param tag
   * @param content
   * @return the child node  
   */
  public static Node appendChild(Node parent, String tag, String content)
  {
    Node child = appendChild(parent, tag);
    child.setTextContent(content);
    return child;
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
   * Creates a new DOM document.
   * @return <Return Description>  
   */
  public static Document createDomDocument()
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    try
    {
      builder = factory.newDocumentBuilder();
      Document doc = builder.newDocument();
      return doc;
    }
    catch (ParserConfigurationException e)
    {
      e.printStackTrace();
    }

    return null;
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
  
  /**
   * 
   * @param list this NodeList as an iterator
   * @return an Iterable version of the node-list from  
   * 
   */
  public static NodeListIterator iterator(NodeList list)
  {
    return new NodeListIterator(list);
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

  /**
   * Returns a stream from this node list
   * @param list
   * @return a stream from this node list
   * 
   */
  public static Stream<Node> stream(NodeList list)
  {
    return new NodeListIterator(list).stream();
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
}
