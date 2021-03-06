package com.danielbchapman.code;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import lombok.Data;

import com.danielbchapman.text.Text;
import com.danielbchapman.utility.Utility;

public class QtPropertyBuilderCore
{
  
  public final static String TYPE = "#type";
  public final static String LOWER = "#lower";
  public final static String STATIC = "#static";
  public final static String VALUE = "#value";
  public final static String RAW = "#raw";
  
  public static void main(String ... args)
  {
    if(args != null)
      for(String s : args)
        System.out.println(s);
    if(args != null && args.length > 0 && "gen".equals(args[0]))
    {   
      processFiles(args[0]);
    }
    else
      processFiles(".");
  }
  
  public static void processFiles(String root)
  {
    Path masterPath = Paths.get(root);
    File master = masterPath.toFile();
    
    if(!master.exists() && !master.isDirectory())
      throw new RuntimeException("Unable to procees non-directory " + root);
    
    File[] children = master.listFiles();
    
    for(File f : children)
      if(f.getName().endsWith(".gen"))
      {
        String rootName = f.getName().replaceAll(".gen", "");
        String header = rootName + ".h";
        String ifDef = rootName.toUpperCase() + "_H";
        String className = Text.capitalizeFirst(rootName).trim();
        Pair<String, ArrayList<Parts>> parts = process(Utility.readFile(f.getAbsolutePath()).toString(), className);
          
        if(parts == null)
        {
          System.out.println("Unable to process blank file " + f.getName());
          continue;
        }
          
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try(ClassWriter out = new ClassWriter(new PrintWriter(bytes)))
        {
          out.print("#ifndef ");
          out.println(ifDef);
          out.print("#define ");
          out.println(ifDef);
          
          out.println();
          out.println("#include <QtCore>");
          out.println();
          out.println("#include \"../Groups/Item.h\"");
          out.println("#include \"../Groups/Json.h\"");
          
          out.println("//AUTOGENERATED AT " + new Date().toString());          
          out.println("namespace la");
          out.println("{");
          out.print("namespace ");
          out.print(className.trim().toLowerCase());
          out.println("{");
          
          out.tab();
          out.println("namespace keys");
          out.println("{");
          for(Parts p : parts.getTwo())
            out.println(p.staticKey);
          out.println("}");
          out.tabBack();
          out.println();
          
          out.print("class ");
          out.print(className);
          out.println(" : public QObject\n{");
          out.println("\tQ_OBJECT");
          
          out.println();
          
          out.println(parts.getOne());
          
          out.println("};");
          
          out.println("}//namespace " + className.toLowerCase());
          out.println("}//namespace la");
          
          out.print("#endif //");
          out.println(ifDef);
          
        }catch (Exception e)
        {
          throw new RuntimeException(e.getMessage(), e);
        };
        
        Utility.writeFile(f.getParent() + "/" + header.toLowerCase(), bytes.toString());
        System.out.println("Processed file " + f.getName());
      }
  }
  
  @SuppressWarnings("unused")
  public static Pair<String, ArrayList<Parts>> process(String data, String className)
  {
    if(Text.isEmpty(data))
      return null;
    
    StringBuilder builder = new StringBuilder();
    String[] lines = data.split("\\n");
    ArrayList<Parts> parts = new ArrayList<Parts>();
    for(String element : lines)
    { 
      element = element.replaceAll("\\s+", "\t");
      String[] rowData = element.split("\t");
      String type = null;
      String raw = null;
      
      if(rowData.length == 1)
      {
        raw = rowData[0];
        type = "QString";
      }
      else
      {
        type = rowData[0];
        raw = rowData[1];
      }
      String clean = Text.clean(raw);
      String lowerCase = Text.lowerCaseFirst(raw);
      String staticCase = Text.staticCase(lowerCase);
      
      Parts part = new Parts();
      
      part.setStaticCase(staticCase);
      part.setLowerCase(lowerCase);
      
      part.staticKey = "\tconst static char * " + STATIC + " = \"" + LOWER + "\";";
      part.staticKey = part.staticKey
        .replaceAll(TYPE, type)
        .replaceAll(RAW, raw)
        .replaceAll(LOWER, lowerCase)
        .replaceAll(STATIC, staticCase)
        .replaceAll(VALUE, clean);
      
      part.props = 
          "Q_PROPERTY(#type " + LOWER + " READ " + LOWER + " WRITE set" + VALUE + " NOTIFY " + LOWER + "Changed)";
      part.props = part.props
          .replaceAll(TYPE, type)
          .replaceAll(RAW, raw)
          .replaceAll(LOWER, lowerCase)
          .replaceAll(STATIC, staticCase)
          .replaceAll(VALUE, clean);
      
      part.pub = 
          "#type " + LOWER + "()\n\t{\n\t\treturn m_" + LOWER + ";\n\t}\n"
          + "\tvoid set" + VALUE + "(const #type &v)\n\t{\n\t\tif(v == m_" + LOWER + ")\n\t\t\treturn;\n\n\t\tm_" + LOWER + " = v;\n\t\temit "+LOWER+"Changed();\n\t}";
      part.pub = part.pub
          .replaceAll(TYPE, type)
          .replaceAll(RAW, raw)
          .replaceAll(LOWER, lowerCase)
          .replaceAll(STATIC, staticCase)
          .replaceAll(VALUE, clean);
      
      
      part.signals = 
          "void " + LOWER + "Changed();";
      part.signals = part.signals
          .replaceAll(TYPE, type)
          .replaceAll(RAW, raw)
          .replaceAll(LOWER, lowerCase)
          .replaceAll(STATIC, staticCase)
          .replaceAll(VALUE, clean);
      
      part.priv = "m_" + raw;
      
      part.type = type;
      parts.add(part);
    }
    
    for(Parts p : parts)
    {
      builder.append("\t");
      builder.append(p.props);
      builder.append("\n");
    }
    
    builder.append("public:\n");
    
    builder.append("\texplicit ");
    builder.append(className);
    builder.append("(QObject * parent = 0) : QObject(parent){}\n");
    
    builder.append("\tvirtual ~");
    builder.append(className);
    builder.append("(){}\n");
    for(Parts p : parts)
    {
      builder.append("\t");
      builder.append(p.pub);
      builder.append("\n");
    }
    
    builder.append("signals:\n");
    for(Parts p : parts)
    {
      builder.append("\t");
      builder.append(p.signals);
      builder.append("\n");
    }
    
    builder.append("public slots:\n");
    
    builder.append("/*\n");
    //To Item
    String namespaceKeys = "la::" + className.toLowerCase() + "::keys::";
    String namespaceClass = "la::" + className.toLowerCase() + "::";
    builder.append("\tGroups::Item toItem()\n");
    builder.append("\t{\n");
    
    builder.append("\t\tGroups::Item result;\n");
    for(Parts p : parts)
    {
      builder.append("\t\tresult.add(");
      builder.append(namespaceKeys);
      builder.append(p.staticCase);
      builder.append(", ");
      builder.append(p.priv);
      builder.append(");\n");
    }

    builder.append("\t\treturn result;\n");
    builder.append("\t}\n\n");
    //From Item
    builder.append("\t");
    builder.append(className);
    builder.append(" fromItem(Groups::Item & item)\n");
    builder.append("\t{\n");
    
    builder.append("\t\t");
    builder.append(className);
    builder.append(" result;\n");
    
    for(Parts p : parts)
    {
      builder.append("\t\t");
      builder.append(guessType(p.priv, p.type, namespaceKeys, p.staticCase));
      builder.append("\n");
    }

    builder.append("\t\treturn result;\n");
    builder.append("\t}\n");    
    
    builder.append("*/\n\n");
    builder.append("private:\n");
    for(Parts p : parts)
    {
      builder.append("\t");
      builder.append(p.type);
      builder.append(" ");
      builder.append(p.priv);
      builder.append(";\n");
    }
        
    return new Pair<>(builder.toString(), parts);
  }
  
  public static String guessType(String priv, String type, String namespace, String key)
  {
    if("QString".equals(type))
      return "result." + priv + " = item.get(" + namespace + key + ").asQString();";
    else if ("int".equals(type))
      return "result." + priv + " = item.get(" + namespace + key + ").asInt();";
    else if ("bool".equals(type))
      return "result." + priv + " = item.get(" + namespace + key + ").asBoolean();";
    else if ("double".equals(type))
      return "result." + priv + " = item.get(" + namespace + key + ").asDouble();";
    else
      return "//unknown type " + type + "//result." + priv + " = item.get(" + namespace + key + ").asUNKNOWN();"; 
  }
  
  @Data
  public static class Parts
  {
    String staticKey;
    String props;
    String pub;
    String signals; 
    String priv;
    String type;
    String staticCase;
    String lowerCase;
  }  
}
