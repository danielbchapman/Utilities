package com.danielbchapman.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.danielbchapman.text.Text;

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
	public static Logger log = Logger.getLogger("UTILITY");

	/**
	 * Returns the value of the second argument if the 
	 * first argument is null.
	 * 
	 * @param obj the value
	 * @param def the default value
	 * @return <Return Description>  
	 * 
	 */
	public static <T> T ifNull(T obj, T def)
	{
		if(obj == null)
			return def;
		else
			return obj;
	}

	/**
	 * A specialized version of the ifNull method for
	 * strings that returns the default if the string is
	 * null or empty.
	 * @param str
	 * @param def
	 * @return str or def  
	 * 
	 */
	public static String ifEmpty(String str, String def)
	{
		if(Text.isEmpty(str))
			return def;
		else
			return str;
	}

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
	 * @return -1 if less, 0 if equal, 1 if greater 
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

	/**
	 * Compare one to two using equals but in a 
	 * method that will not cause a null pointer exception.
	 * @param one one
	 * @param two two
	 * @return
	 */
	public static boolean equalsNullSafe(Object one, Object two)
	{
		if(one == null && two == null)
			return true;

		if(one == null || two == null)
			return false;

		return one.equals(two);
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
		{
		  if(file.getParentFile() != null && file.getParentFile().exists())
		    file.getParentFile().mkdirs();
		}
		
		if(file.isDirectory())
		{
		  file.mkdirs();
		}
		else
		{
		  try
	    {
	      file.createNewFile();
	    }
	    catch (IOException e)
	    {
	      throw new RuntimeException(e.getMessage(), e);
	    }  
		}

		
	}

	public static <T> ArrayList<T> list(T[] t)
	{
		if(t == null)
			return null;
		ArrayList<T> list = new ArrayList<T>();

		for(int i = 0; i < t.length; i++)
			list.add(t[i]);

		return list;
	}

	public static <T> ArrayList<T> list(AbstractCollection<T> collection)
	{
		if(collection == null)
			return null;

		ArrayList<T> list = new ArrayList<T>();

		for(T t : collection)
			list.add(t);

		return list;
	}

	public static void writeFile(String path, String string)
	{
		File file = new File(path);
		if(!file.exists())
		  file.mkdirs();
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

	public static String replaceAll(String value, String pattern, String newPattern)
	{
		return value;
	}

	public static String getStackTrace(Throwable t)
	{
		if(t == null)
			return "Throwable is: null";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintWriter ps = new PrintWriter(out);
		if(t != null)
			t.printStackTrace(ps);

		ps.flush();
		ps.close();
		return out.toString(); 
	}

	public static Date findMonday(Date inWeek, TimeZone timeZone)
	{
		if(inWeek == null)
			return null;

		Calendar monday = Calendar.getInstance(timeZone);
		Calendar date = Calendar.getInstance(timeZone);

		monday.setTime(inWeek);
		date.setTime(inWeek);

		monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		monday.set(Calendar.HOUR_OF_DAY, 0);
		monday.set(Calendar.MINUTE, 0);
		monday.set(Calendar.SECOND, 0);
		monday.set(Calendar.MILLISECOND, 0);

		int comp = Utility.compareToNullSafe(monday.getTime(), date.getTime());
		if(comp > 0)
		{
			monday.add(Calendar.WEEK_OF_YEAR, -1);
			return monday.getTime();
		}
		else
			return monday.getTime();
	}

	public static int decodeBytes(byte a, byte b, byte c, byte d)
	{
		return (a & 0xFF) << 24 | (b & 0xFF) << 16 | (c & 0xFF) << 8 | (d & 0xFF);
	}
	public static int decodeBytes(byte[] len4)
	{
		return(len4[0] & 0xFF) << 24 | (len4[1] & 0xFF) << 16 | (len4[2] & 0xFF) << 8 | (len4[3] & 0xFF); 
	}

	public static byte[] encodeBytes(int integer)
	{
		return new byte[]{ (byte) (integer >>> 24), (byte) (integer >>> 16), (byte) (integer >>> 8), (byte) (integer >>> 0) };
	}

	public enum BindingType
	{
		COLOR, CURRENCY, DATE, INTEGER, REAL, TEXT;
	}

	public static byte[] encode(int i)
	{
		return new byte[]{ (byte) (i >>> 24), (byte) (i >>> 16), (byte) (i >>> 8), (byte) (i >>> 0) };
	}

	public static int decode(byte a, byte b, byte c, byte d)
	{
		return  (a & 0xFF) << 24 | (b & 0xFF) << 16 | (c & 0xFF) << 8 | (d & 0xFF);
	}
}

