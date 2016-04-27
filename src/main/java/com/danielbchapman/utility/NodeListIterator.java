package com.danielbchapman.utility;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * A simple node list iterator that is index based and
 * can remove objects.
 */
public class NodeListIterator implements Iterator<Node>, Iterable<Node>
{
  Node current;
  Node next;
  boolean isEmpty;
  
  public NodeListIterator(NodeList list)
  {
    if(list.getLength() < 1)
    {
      isEmpty = true;
    }
    
    current = null;
    next = list.item(0); 
  }
  @Override
  public boolean hasNext()
  {
    return next != null;
  }
  @Override
  public Node next()
  {
    if(isEmpty)
      return null;

    if(next == null)
      throw new IndexOutOfBoundsException("There are no more nodes in the list");
    
    Node move = next.getNextSibling();
    current = next;
    next = move;
    return current;
  }
  
  public void remove()
  {
    if(isEmpty)
      throw new UnsupportedOperationException("The node list is empty");
    
    Node parent = current.getParentNode();
    if(parent == null) //this should probably throw an error
    {
      throw new UnsupportedOperationException("This node has no parent");
    }
    parent.removeChild(current);
  }
  
  @Override
  public Iterator<Node> iterator()
  {
    return this;
  }
  
  /**
   * @return a non-parallel stream for iteratating over this node.  
   * 
   */
  public Stream<Node> stream()
  {
    return StreamSupport.stream(this.spliterator(), false);
  }
}
