package com.danielbchapman.utilites.tests;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.danielbchapman.utility.NodeListIterator;
import com.danielbchapman.utility.UtilityXml;

public class NodeIteratorTest
{
  @Test
  public void TestNodeIteratorStream()
  {
    Document doc = UtilityXml.readDocument(new File("test/turco-test.xml"));
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
    Document doc = UtilityXml.readDocument(new File("test/turco-test.xml"));
    NodeList children = doc.getChildNodes().item(0).getChildNodes();
    
    List<Node> nodes = new NodeListIterator(children).stream().filter(n -> n.getNodeType() != Node.TEXT_NODE).collect(Collectors.toList());
    nodes.stream().forEach(System.out::println);
      
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
