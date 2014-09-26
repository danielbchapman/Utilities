package com.danielbchapman.international;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.WeakHashMap;

import com.danielbchapman.utility.Utility;

public class MessageUtility
{
  public static final String DEFAULT_INTERNATIONALIZATION_FILES = "language";
  public static final String DEFAULT_FALLBACK_INTERNATIONALIZATION_FILES = "i1n8";
  private static WeakHashMap<Class<?>, Instance> instanceMap = new WeakHashMap<Class<?>, Instance>();
  private static Locale locale = Locale.getDefault();

  public static Instance getInstance(Class<?> clazz)
  {
    Instance instance = instanceMap.get(clazz);

    if (instance == null)
      instance = new Instance(clazz, locale);

    instanceMap.put(clazz, instance);
    return instance;
  }

  public static void setLocale(Locale locale)
  {
    instanceMap.clear();
  }

  
  /**
   * <p>
   * This class provides a "two" file based instance for properties. The first file
   * checked ({@link #DEFAUL_INTERNATIONALIZATION_FILES}) will be read and if no 
   * key is found it will fallback to {@link #DEFAULT_FALLBACK_INTERNATIONALIZATION_FILES} which
   * will then fallback to writing to the fallback.
   * </p>
   * <p> 
   * In general, this is to keep garbage from populating the fields that have been finished. It 
   * also keeps the finished files clean allowing missed keys to "roll over" into the other
   * directory so they can be easily identified.
   *</p>
   ***************************************************************************
   * @author Daniel B. Chapman 
   * <br /><i><b>Light Assistant</b></i> copyright Daniel B. Chapman
   * @since Sep 18, 2012
   * @version 2 Development
   * @link http://www.lightassistant.com
   ***************************************************************************
   */
  public static class Instance
  {
    private String baseName;
    private String directory;
    private String directoryPath;
    private String fallback;
    private String fallbackPath;
    private Locale locale;
    private ResourceBundle resourceBundle;
    private ResourceBundle resourceBundleFallback;

    public Instance(Class<?> clazz, Locale locale)
    {
      this(DEFAULT_INTERNATIONALIZATION_FILES, DEFAULT_FALLBACK_INTERNATIONALIZATION_FILES, clazz.getName(), locale);
    }

    public Instance(String directory, String fallback, String baseName, Locale locale)
    {
      this.baseName = baseName;
      this.locale = locale;
      this.directory = directory;
      this.fallback = fallback;
      init();
    }

    public String get(String key)
    {
      try
      {
        return resourceBundle.getString(key);
      }
      catch (Exception no)
      {
        try
        {
          return resourceBundleFallback.getString(key);
        }
        catch(Exception e)
        {
          return logMissingKey(key);          
        }

      }
    }

    public String get(String key, Object... params)
    {      
      try
      {
        MessageFormat formatter = new MessageFormat("");
        formatter.setLocale(locale);

        formatter.applyPattern(resourceBundle.getString(key));
        String output = formatter.format(params);
        return output;
      }
      catch (Exception no)
      {
        try
        {
          MessageFormat formatter = new MessageFormat("");
          formatter.setLocale(locale);
          formatter.applyPattern(resourceBundleFallback.getString(key));
          String output = formatter.format(params);
          return output;          
        }
        catch(Exception e)
        {
          return logMissingKey(key, params);  
        }
      }
    }

    public void setLocale(Locale locale)
    {
      this.locale = locale;
      init();
    }

    protected void init()
    {      
      Object[] main = createBundle(directory, baseName, locale);;
      Object[] secondary = createBundle(fallback, baseName, locale);
      
      resourceBundle = (ResourceBundle) main[0]; 
      directoryPath = (String) main[1];
      
      resourceBundleFallback = (ResourceBundle) secondary[0];
      fallbackPath = (String) secondary[1];
    }

    protected String logMissingKey(String key)
    {
      return logMissingKey(key, new Object[] {});
    }

    protected synchronized String logMissingKey(String key, Object... params)
    {
      StringBuilder missing = new StringBuilder();
      missing.append("*");
      missing.append(key);
      int i = 0;
      for (Object o : params)
      {
        missing.append(" PARAM(");
        missing.append(i);
        missing.append(")");
        missing.append(" {");
        missing.append(i);
        missing.append(", ");

        if (o instanceof Number)
        {
          missing.append("number");

          if (o instanceof Integer || o instanceof Long)
            ;
          missing.append(", integer");
        }
        else
          if (o instanceof Date)
          {
            missing.append("date");
          }
          else
            if (o != null)
              missing.append(o.getClass().getSimpleName());
            else
              missing.append("null");

        missing.append("}");

        if (o != null)
          missing.append(o.toString());
        else
          missing.append("null");
        i++;
      }

      String ret = missing.toString();
      writePair(key, ret);

      /* Store this new key for subsequent calls */
      init();
      return ret;
    }

    private void writePair(String key, String value)
    {
      Utility.appendFile(fallbackPath, key + " = " + value);
    }
    
    private Object[] createBundle(String directoryPath, String baseName, Locale locale)
    {
      File directory = new File(directoryPath);
      if (!directory.exists())
        directory.mkdirs();

      File loadedFile = new File(directory + File.separator + baseName + ".properties");
      String path = loadedFile.getAbsolutePath();

      if (!loadedFile.exists())
        Utility.touchFile(new File(path));

      StringBuffer buffer = Utility.readFile(path);
      StringReader reader = new StringReader(buffer.toString());
      PropertyResourceBundle props = null;
      
      try
      {
        props = new PropertyResourceBundle(reader);
      }
      catch (IOException e)
      {
        /* It's a string reader...this should be fine unless you run out of memory... */
        throw new RuntimeException(e.getMessage(), e);
      }
      
      return new Object[]{props, path};
    }
  }
}
