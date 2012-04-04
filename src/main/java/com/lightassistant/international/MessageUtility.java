package com.lightassistant.international;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.WeakHashMap;

import com.lightassistant.utility.Utility;

public class MessageUtility
{
  public static final String DEFAULT_INTERNATIONALIZATION_FILES = "i1n8";
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

  public static class Instance
  {
    private String baseName;
    private String directory;
    private String filePath;
    private Locale locale;
    private ResourceBundle resourceBundle;

    public Instance(Class<?> clazz, Locale locale)
    {
      this(DEFAULT_INTERNATIONALIZATION_FILES, clazz.getName(), locale);
    }

    public Instance(String directory, String baseName, Locale locale)
    {
      this.baseName = baseName;
      this.locale = locale;
      this.directory = directory;
      init(directory, baseName, locale);
    }

    public String get(String key)
    {
      try
      {
        return resourceBundle.getString(key);
      }
      catch (Exception e)
      {
        return logMissingKey(key);
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
      catch (Exception e)
      {
        return logMissingKey(key, params);
      }
    }

    public void setLocale(Locale locale)
    {
      this.locale = locale;
      init(directory, baseName, locale);
    }

    protected void init(String directoryPath, String baseName, Locale locale)
    {
      System.err.println(getClass().getName() + " -> " + "Localization not implemented, this will use a default english set");
      System.out.println("LOCAL NOT SUPPORTD");
      File directory = new File(directoryPath);
      if (!directory.exists())
        directory.mkdirs();

      File loadedFile = new File(directory + File.separator + baseName + ".properties");
      filePath = loadedFile.getAbsolutePath();

      if (!loadedFile.exists())
        Utility.touchFile(new File(filePath));

      StringBuffer buffer = Utility.readFile(filePath);
      StringReader reader = new StringReader(buffer.toString());
      PropertyResourceBundle props;
      try
      {
        props = new PropertyResourceBundle(reader);
      }
      catch (IOException e)
      {
        /* It's a string reader...this should be fine unless you run out of memory... */
        throw new RuntimeException(e.getMessage(), e);
      }

      resourceBundle = props;
    }

    protected String logMissingKey(String key)
    {
      return logMissingKey(key, new Object[] {});
    }

    protected String logMissingKey(String key, Object... params)
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
      init(directory, baseName, locale);
      return ret;
    }

    private void writePair(String key, String value)
    {
      Utility.appendFile(filePath, key + " = " + value);
    }
  }
}
