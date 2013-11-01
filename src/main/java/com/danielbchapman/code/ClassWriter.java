package com.danielbchapman.code;

import java.io.PrintWriter;
import java.io.Writer;
import java.text.MessageFormat;

public class ClassWriter extends PrintWriter
  {

    private int tabs = 0;
    public ClassWriter(Writer out)
    {
      super(out);
    }
    
    @Override
    public void println()
    {
      for(int i = 0; i < tabs; i++)
        super.print("\t");
      super.println();
    }
    
    @Override
    public void println(String s)
    {
      for(int i = 0; i < tabs; i++)
        super.print("\t");
      
      super.println(s);
    }
    
    /**
     * Write a line using a standard message formatted string.
     * @param s
     * @param args <Return Description>  
     * 
     */
    public void println(String s, String ... args)
    {
      println(MessageFormat.format(s, (Object[]) args));
    }
    
    public void setTabs(int x)
    {
      if(x < 0)
        tabs = 0;
      tabs = x;
    }
    
    public void tab()
    {
      setTabs(++tabs);
    }
    
    public void tabBack()
    {
      setTabs(--tabs);
    }
  }