package com.danielbchapman.utilites.tests;

import java.io.File;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.danielbchapman.utility.NodeListIterator;
import com.danielbchapman.utility.Xml;

public class NodeIteratorTest
{
  @Test
  public void TestNodeIteratorStream()
  {
    Document doc = Xml.readDocument(new File("test/turco-test.xml"));
    NodeList children = doc.getChildNodes().item(0).getChildNodes();
    
    long count = children.getLength();
    long streamCount =  new NodeListIterator(children).stream().count();
    final ArrayList<Integer> bucket = new ArrayList<>();
    new NodeListIterator(children).stream().forEach(x -> bucket.add(0));
    Assert.assertTrue(String.format("Count %d != Stream count %d", count, streamCount),  count == streamCount);
    Assert.assertTrue(count == bucket.size());
  }
  
  @Test
  public void TestNodeIteratorStreamRemove()
  {
    Document doc = Xml.readDocument(new File("test/turco-test.xml"));
    NodeList children = doc.getChildNodes().item(0).getChildNodes();
    
    //Before
    System.out.println("------------------------------BEFORE");
    new NodeListIterator(children).stream().forEach(System.out::println);
    //Clear whitespace
    new NodeListIterator(children).stream()
        .filter(n -> n.getNodeType() == Node.TEXT_NODE)
        .forEach(n -> n.getParentNode().removeChild(n));
    
    new NodeListIterator(children).stream().forEach(System.out::println);
    
    boolean valid = new NodeListIterator(children).stream()
      .anyMatch(n -> n.getNodeType() == Node.TEXT_NODE);
      
    System.out.println("------------------------------AFTER");
    new NodeListIterator(children)
      .stream()
      .forEach(System.out::println);
    
    Assert.assertTrue("There are Text Nodes present!!", valid);
  }
  
  @Test
  public void TestNodeIterator()
  {
    Document doc = Xml.readDocument(new File("test/turco-test.xml"));
    for(Node n : new NodeListIterator(doc.getChildNodes().item(0).getChildNodes()))
      System.out.println(n + " Children:[" + n.getChildNodes().getLength() + "]");
  }
  
  @Test
  public void TestXmlDoc()
  {
    Document doc = Xml.readDocument(new File("test/turco-test.xml"));
    Assert.assertTrue(doc.getChildNodes().getLength() > 0);
  }
  
  @Test
  public void TestNodeIteratorRemove()
  {
    Document doc = Xml.readDocument(new File("test/turco-test.xml"));
    int count = doc.getChildNodes().item(0).getChildNodes().getLength();
    System.out.println("------------------BEFORE " + count);
    for(Node n : new NodeListIterator(doc.getChildNodes().item(0).getChildNodes()))
      System.out.println(n + " Children:[" + n.getChildNodes().getLength() + "]");
    
    
    int i = 1;
    NodeListIterator it = new NodeListIterator(doc.getChildNodes().item(0).getChildNodes());
    
    Node node = null;
    boolean check = it.hasNext();
    while(check)
    {
      node = it.next();
      if(i % 2 == 0)
      {
        System.out.println("Removing node: " + node);
        it.remove();
      }
      else
        System.out.println("i % 2 =" + i % 2);
        
      i++;
      check = it.hasNext();
      System.out.println("Next? " + check);
    }
    
    int countAfter = doc.getChildNodes().item(0).getChildNodes().getLength();
    System.out.println("------------------AFTER " + countAfter);
    for(Node n : new NodeListIterator(doc.getChildNodes().item(0).getChildNodes()))
      System.out.println(n + " Children:[" + n.getChildNodes().getLength() + "]");
  }
}
