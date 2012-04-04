package com.lightassistant.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple set of static helper methods to do mundane tasks and reduce the
 * complexity of drawing/coding etc...
 *
 ***************************************************************************
 * @author Daniel B. Chapman 
 * <br /><i><b>Light Assistant</b></i> copyright Daniel B. Chapman
 * @since Nov 3, 2011
 * @version 2 Development
 * @link http://www.lightassistant.com
 ***************************************************************************
 */
public class Utility
{
  public static Logger log = Logger.getLogger("LA_UTILITY");

  public static void appendFile(String path, String string)
  {
    StringBuffer buffer = readFile(path);
    StringWriter value = new StringWriter();
    PrintWriter writer = new PrintWriter(value);
    /* newline included */
    writer.print(buffer.toString());
    writer.println(string);

    writeFile(path, value.getBuffer().toString());
  }

  public static void close(Closeable closeable)
  {
    if (closeable == null)
      return;

    try
    {
      closeable.close();
    }
    catch (IOException e)
    {
      if (!e.getMessage().toLowerCase().contains("stream closed"))
        logWarningException("Failed to close stream << " + e.getMessage(), e);
    }
  }

  /**
   * A null safe compare that shoves nulls down the list.
   * @param <T>
   * @param one
   * @param two
   * @return <Return Description>  
   * 
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static int compareToNullSafe(Comparable one, Comparable two)
  {
    if (one == null && two != null)
      return 1;

    if (two == null && one != null)
      return -1;

    if (one == null && two == null)
      return 0;

    return one.compareTo(two);
  }

  public static String international(String key)
  {
    return "*" + key + "*";
  }

  public static String international(String key, Object... parameters)
  {
    return "*" + key + "*";
  }

  public static void logWarningException(String message, Throwable t)
  {
    log.log(Level.WARNING, message, t);
  }

  public static StringBuffer readFile(String path)
  {
    File file = new File(path);
    StringBuffer builder = new StringBuffer();
    FileReader reader = null;
    try
    {
      reader = new FileReader(file);
    }
    catch (FileNotFoundException e)
    {
      try
      {
        touchFile(new File(path));
        reader = new FileReader(file);
      }
      catch (FileNotFoundException e2)
      {
        Utility.close(reader);
        throw new RuntimeException(e.getMessage(), e);
      }
    }

    BufferedReader buffer = new BufferedReader(reader);
    try
    {
      String line = null;
      for (; (line = buffer.readLine()) != null;)
      {
        builder.append(line);
        builder.append('\n');
      }

      close(reader);
      close(buffer);
    }
    catch (FileNotFoundException e)
    {
      /* This should be dealt with above, however it could happen */
      close(buffer);
      close(reader);
      throw new RuntimeException(e.getMessage(), e);
    }
    catch (IOException e)
    {
      /* This should be dealt with above, however it could happen. Permissions etc... */
      close(buffer);
      close(reader);
      throw new RuntimeException(e.getMessage(), e);
    }

    return builder;
  }

  public static void touchFile(File file)
  {
    if (!file.exists())
      file.getParentFile().mkdirs();

    try
    {
      file.createNewFile();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  public static void writeFile(String path, String string)
  {
    File file = new File(path);
    BufferedWriter buffer = null;
    FileWriter writer = null;

    try
    {
      writer = new FileWriter(file);
      buffer = new BufferedWriter(writer);
      buffer.write(string);
      buffer.flush();
      buffer.close();
      writer.close();
    }
    catch (IOException e)
    {
      close(writer);
      close(buffer);
      logWarningException("Failed to write file '" + file.getName() + "'. " + e.getMessage(), e);
    }
  }

  public String replaceAll(String value, String pattern, String newPattern)
  {
    return value;
  }

  public enum BindingType
  {
    COLOR, CURRENCY, DATE, INTEGER, REAL, TEXT;
  }
}
