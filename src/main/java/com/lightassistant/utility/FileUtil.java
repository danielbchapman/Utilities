package com.lightassistant.utility;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class FileUtil
{
  public static void copyDir(String from, String to)
  {
    try
    {

      DirCopy copy = new DirCopy(Paths.get(from), Paths.get(to));
      Files.walkFileTree(Paths.get(from), copy);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  public static void rmDir(String target)
  {
    try
    {
      Files.walkFileTree(Paths.get(target), new DirRemove());
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }

  }

  public static void mkDir(String newPath, Path dir)
  {
    try
    {
      if(!Files.exists(dir))
        Files.createDirectories(dir);
      
      if(!Files.isDirectory(dir))
        throw new RuntimeException("Can not create path inside non-directory " + dir);
        
      Path toMake = Paths.get(dir.toString(), newPath);
      Files.createDirectories(toMake);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  public static Path[] list(String from) throws IOException
  {
    try
    {
      DirList list = new DirList();
      Files.walkFileTree(Paths.get(from), list);
      System.out.println(list.toString());

      return list.getPaths();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }

  }

  public static void delete(String name)
  {
    Path x = Paths.get(name);
    if(!Files.exists(x))
      return;
    
    if(Files.isDirectory(x))
      return;
    
    try
    {
      Files.delete(x);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
  
  public static String print(String from)
  {
    try
    {
      DirList list = new DirList();
      Files.walkFileTree(Paths.get(from), list);
      return list.toString();
    }
    catch (IOException e)
    {
      return e.getMessage();
    }
  }

  public static class DirList extends SimpleFileVisitor<Path>
  {
    ArrayList<Path> paths = new ArrayList<>();

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr) throws IOException
    {
      paths.add(dir);
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException
    {
      paths.add(file);
      return FileVisitResult.CONTINUE;
    }

    public synchronized Path[] getPaths()
    {
      Path[] result = new Path[paths.size()];

      for (int i = 0; i < paths.size(); i++)
        result[i] = paths.get(i);

      return result;
    }

    public String toString()
    {
      StringBuilder build = new StringBuilder();

      for (Path p : paths)
      {
        if (Files.isDirectory(p))
          build.append(p.toAbsolutePath());
        else
        {
          build.append("\t");
          build.append(p.toAbsolutePath());
        }

        build.append("\n");
      }

      return build.toString();
    }
  }

  public static class DirRemove extends SimpleFileVisitor<Path>
  {
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException
    {
      if (e != null)
        throw e;

      Files.delete(dir);
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException
    {
      Files.delete(file);
      return FileVisitResult.CONTINUE;
    }
  }

  public static class DirCopy extends SimpleFileVisitor<Path>
  {
    private Path from;
    private Path to;

    public DirCopy(Path from, Path to)
    {
      this.from = from;
      this.to = to;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr) throws IOException
    {
      Path target = to.resolve(from.relativize(dir));
      if (!Files.exists(target))
        Files.createDirectories(target);

      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException
    {
      Files.copy(file, to.resolve(from.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
      return FileVisitResult.CONTINUE;
    }
  }
}