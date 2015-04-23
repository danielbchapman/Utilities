package com.danielbchapman.code.jdk;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ReloadingClassLoader extends ClassLoader
{
  public ReloadingClassLoader(ClassLoader parent, File classPath)
  {
    super(parent);
    this.classPath = classPath;
  }

  private File classPath;

  public Class loadClass(String name) throws ClassNotFoundException
  {
    if (name == null || !name.contains("LambdaBrushDynamic"))
    {
      return super.loadClass(name);
    }

    try
    {
      String path = name.replaceAll("\\.", "/");
      String base = classPath.getAbsolutePath() + "/" + path + ".class";
      File redirect = new File(base);
      URL url = redirect.toURI().toURL();
      URLConnection connection = url.openConnection();
      InputStream input = connection.getInputStream();
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      int data = input.read();

      while (data != -1)
      {
        buffer.write(data);
        data = input.read();
      }

      input.close();

      byte[] classData = buffer.toByteArray();

      return defineClass(name, classData, 0, classData.length);

    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    return null;
  }

}
