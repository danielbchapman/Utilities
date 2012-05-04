package com.lightassistant.utility;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class UtilityGraphics
{
  public static boolean writeImageToFile(String path, BufferedImage image, String encoding)
  {
    File out = new File(path);
    if(out.exists())
      out.delete();
    else
      out.mkdirs();
    try
    {
      out.createNewFile();
      ImageIO.write(image, encoding, out);
      return true;
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
    return false;
  }
}
