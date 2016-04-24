package com.danielbchapman.code;

/**
 * A simple exception class that adds a warning to the compilation
 * process and is used to mark unfinished methods. 
 * 
 * @author danielbchapman
 *
 */
@Deprecated
public class NotImplementedException extends RuntimeException
{
  private static final long serialVersionUID = 1L;

  public NotImplementedException()
  {
    this("Not implemented...");
  }
  
  public NotImplementedException(String message)
  {
    super(message);
  }
}
