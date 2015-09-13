package com.danielbchapman.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log
{
  public static Logger LOG;
  public static boolean IS_DEBUG = true;
  
  static 
  {
    LOG = Logger.getAnonymousLogger();
    LOG.setLevel(Level.ALL);
  }
  
  public static void severe(String msg)
  {
    LOG.severe(msg);
  }
  
  public static void warn(String msg)
  {
    LOG.warning(msg);
  }
  
  public static void info(String msg)
  {
    LOG.info(msg);
  }
  
  public static void debug(String msg)
  {
    System.out.println(msg);
    LOG.fine(msg);
  }
  
  public static String format(String msg)
  {
    SimpleDateFormat fmt = new SimpleDateFormat("YYYYMMdd HH:mm:ss");
    return "[" + fmt.format(new Date()) + "]\t" + msg;
  }

  public static void exception(Exception e)
  {
    LOG.log(Level.SEVERE, format(e.getMessage()), e);
  }
}
