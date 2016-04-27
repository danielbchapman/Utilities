package com.danielbchapman.utilites.tests;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.danielbchapman.utility.NodeListIterator;
import com.danielbchapman.utility.UtilityXml;

public class NodeIteratorTest
{
  @Test
  public void TestNodeIteratorStream()
  {
    Document doc = UtilityXml.readDocument(new File("test/turco-test.xml"));
    new NodeListIterator(doc.getChildNodes().item(0).getChildNodes()).stream().forEach(x -> System.out.println("!" + x));
  }
  
  @Test
  public void TestNodeIterator()
  {
    Document doc = UtilityXml.readDocument(new File("test/turco-test.xml"));
    for(Node n : new NodeListIterator(doc.getChildNodes().item(0).getChildNodes()))
      System.out.println(n + " Children:[" + n.getChildNodes().getLength() + "]");
  }
  
  @Test
  public void TestXmlDoc()
  {
    Document doc = UtilityXml.readDocument(new File("test/turco-test.xml"));
    Assert.assertTrue(doc.getChildNodes().getLength() > 0);
  }
  
  @Test
  public void TestNodeIteratorRemove()
  {
    Document doc = UtilityXml.readDocument(new File("test/turco-test.xml"));
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
