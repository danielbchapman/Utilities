package com.danielbchapman.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import com.danielbchapman.text.Text;
import com.danielbchapman.utility.Utility;

public class StripDuplicates
{
  public static void main(String ... args)
  {
    StringBuffer buf = Utility.readFile("dups.txt");
    String[] data = buf.toString().split("\n");
    
    HashSet<String> unique = new HashSet<String>();
    for(String x : data)
      unique.add(x);
    
    ArrayList<String> list = new ArrayList<String>();
    for(String x : unique)
      if(!Text.isEmpty(x))
        list.add(x);
    
    Collections.sort(list);
    for(String x : list)
      System.out.println(x);
  }
}
