package com.danielbchapman.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * A simple set of static methods for dealing with strings and text.
 * 
 * @author Daniel B. Chapman
 * @since Mar 5, 2012
 * @copyright Copyright 2012 Daniel B. Chapman/Harrison Fortier. All rights
 *            reserved. PROPRIETARY/CONFIDENTAL. Use is subject to license
 *            terms.
 */
public class Text
{

  public static boolean areEqual(String one, String two)
  {
    return compare(one, two) == 0;
  }

  /**
   * @param s the string to capitalize (the first character)
   * @return the string with the first character capitalized. 
   */
  public static String capitalize(String s)
  {
    if (s.length() > 2)
    {
      return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
    else
      if (s.length() == 1)
        return Character.toUpperCase(s.charAt(0)) + "";
      else
        return s;
  }

  /**
   * @param one
   *            the first string to compare
   * @param two
   *            the second string to compare
   * @see @link {@link java.lang.Comparable}
   * @return an int conforming to the specification (1 is "greater")
   */
  public static int compare(String one, String two)
  {
    return Safe.compare(one, two);
  }

  /**
   * A simple static method that does a contains ignoring case.
   * 
   * @param one the element to search
   * @param two the string to search for.
   * @throws IllegalAccessException when the first argument is null, a second argument of null will return false
   * @return true if one contains two ignoring case...
   * 
   */
  public static boolean containsIgnoreCase(String one, String two)
  {
    if (one == null)
      throw new IllegalArgumentException("Value " + one + " can not be null");
    if (two == null)
      return false;

    if (one.toLowerCase().contains(two.toLowerCase()))
      return true;

    return false;
  }

  public static boolean containsIgnoreCase(String one, String... strings)
  {
    if (strings == null)
      throw new IllegalArgumentException("vargs 'strings' can not be null");

    for (String s : strings)
      if (containsIgnoreCase(one, s))
        return true;

    return false;
  }

  /**
   * @param string
   * @return <b>true</b> if the string is null or empty <b>false</b> if it
   *         contains data
   */
  public static boolean isEmpty(String string)
  {
    if (string == null)
      return true;
    if (string.trim().length() == 0)
      return true;
    return false;
  }

  public static boolean maximum(String string, int length)
  {
    if (!Text.isEmpty(string))
      if (string.length() >= length)
        return true;
      else
        return false;

    return false;
  }

  /**
   * @param string
   *            the string to check
   * @param length
   *            the length to compare against
   * @return true if this has a minimum length of [length]
   */
  public static boolean minimum(String string, int length)
  {
    if (!Text.isEmpty(string))
      if (string.length() < length)
        return false;
      else
        return true;

    return false;
  }

  /**
   * Read an input stream into a text file.
   * @param stream
   * @return the contents of the file
   * @throws IOException <Return Description>
   * 
   */
  // FIXME Java Doc Needed
  public static String readStream(InputStream stream) throws IOException
  {
    if (stream == null)
      throw new IllegalArgumentException("The stream is null");

    String line = null;
    StringBuilder builder = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream)))
    {
      while ((line = reader.readLine()) != null)
      {
        builder.append(line);
        builder.append("\r\n");
      }
    }
    return builder.toString();
  }

  /**
   * Return true if the string is empty or null
   * 
   *
   */
  public static boolean isEmptyOrNull(String x)
  {
    if (x == null)
      return true;

    if (x.trim().isEmpty())
      return true;

    return false;
  }

  public static String staticCase(String text)
  {
    char[] chars = text.toCharArray();
    char[] buffer = new char[chars.length * 2];

    int add = 0;
    for (int i = 0; i < chars.length; i++)
    {
      if (Character.isUpperCase(chars[i]))
      {
        buffer[add + i] = '_';
        add++;
        buffer[add + i] = Character.toUpperCase(chars[i]);
      }
      else
        buffer[add + i] = Character.toUpperCase(chars[i]);
    }

    return new String(Arrays.copyOf(buffer, chars.length + add));
  }

  public static String clean(String string)
  {
    return string.replaceAll("\\-", "").replaceAll("\\s+", "");
  }

  public static String capitalizeFirst(String text)
  {
    char[] ret = text.toCharArray();
    ret[0] = Character.toUpperCase(ret[0]);
    return new String(ret);
  }

  public static String lowerCaseFirst(String text)
  {
    if (Text.isEmpty(text))
      return "";
    char[] ret = text.toCharArray();
    ret[0] = Character.toLowerCase(ret[0]);
    return new String(ret);
  }
}
