package com.danielbchapman.code;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Data;

import com.lightassistant.utility.UtilityText;


public class QtPropertyBuilder extends Application
{

  public final static String LOWER = "#lower";
  public final static String STATIC = "#static";
  public final static String VALUE = "#value";
  public final static String RAW = "#raw";
  public static void main(String ... args)
  {
    launch();
  }
  @Override
  public void start(Stage stage) throws Exception
  {
    VBox root = new VBox();
    
    
    final Label lower = new Label("Use " + LOWER + " for lowerCase (method)");
    final Label staticc = new Label("Use " + STATIC + " for STATIC_CASE");
    final Label value = new Label("Use " + VALUE + " for ObjectCase");
    final Label raw = new Label("Use " + RAW + " for 'Raw Value'");

    final TextArea pre = new TextArea();
    final TextArea data = new TextArea();
    final TextArea out = new TextArea();
    
    StringBuilder setup = new StringBuilder();
    setup.append("\tQ_PROPERTY Q_PROPERTY(QString " + lower + " READ " + lower + " WRITE set" + VALUE + " NOTIFY " + lower + "Changed)");
    setup.append("\tQString " + LOWER + "()\t\n{return m_"+lower+";\n\t}");
    setup.append("\tQString set" + VALUE + "(const QString &v)\t\n{m_"+lower+" = v;\n\t}");
    
    pre.setText(setup.toString());
    //pre.setText("public void " + LOWER + "(String "+ LOWER+ ")\n{\n\tthis." + LOWER +" = " + LOWER + ";\n}\\\\" + STATIC +" " + RAW + ";");
    pre.setPromptText("Code before element");
    data.setPromptText("TSV elements");
    

    Button process =new Button("Process");
    process.onActionProperty().set(new EventHandler<ActionEvent>(){

      @Override
      public void handle(ActionEvent arg0)
      {
        out.setText(process(data.getText()));
      }});
        
    root.getChildren().addAll(
        lower,
        staticc,
        value,
        raw,
//        pre,
        data, 
        process,
        out);
    
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    stage.setTitle("TSV Code Builder");
  }

  public static String process(String data)
  {
    StringBuilder builder = new StringBuilder();
    String[] raw = data.split("\\n");
    ArrayList<Parts> parts = new ArrayList<Parts>();
    for(String s : raw)
    { 
      String clean = UtilityText.Code.clean(s);
      String lowerCase = UtilityText.lowerCaseFirst(clean);
      String staticCase = UtilityText.Code.staticCase(lowerCase);
      
      Parts part = new Parts();
      
      part.props = 
          "Q_PROPERTY(QString " + LOWER + " READ " + LOWER + " WRITE set" + VALUE + " NOTIFY " + LOWER + "Changed)";
      part.props = part.props
          .replaceAll(RAW, s)
          .replaceAll(LOWER, lowerCase)
          .replaceAll(STATIC, staticCase)
          .replaceAll(VALUE, clean);
      
      part.pub = 
          "QString " + LOWER + "()\n\t{\n\t\treturn m_" + LOWER + ";\n\t}\n"
          + "\tvoid set" + VALUE + "(const QString &v)\n\t{\n\t\tif(v == m_" + LOWER + ")\n\t\t\treturn;\n\n\t\tm_" + LOWER + " = v;\n\t\temit "+LOWER+"Changed();\n\t}";
      part.pub = part.pub    
          .replaceAll(RAW, s)
          .replaceAll(LOWER, lowerCase)
          .replaceAll(STATIC, staticCase)
          .replaceAll(VALUE, clean);
      
      part.signals = 
          "\tvoid " + LOWER + "Changed();";
      part.signals = part.signals
          .replaceAll(RAW, s)
          .replaceAll(LOWER, lowerCase)
          .replaceAll(STATIC, staticCase)
          .replaceAll(VALUE, clean);
      
      part.priv = "m_" + s + ";";
      
      parts.add(part);
    }
    
    for(Parts p : parts)
    {
      builder.append("\t");
      builder.append(p.props);
      builder.append("\n");
    }
    
    builder.append("public:\n");
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
    
    builder.append("private:\n");
    for(Parts p : parts)
    {
      builder.append("\tQString ");
      builder.append(p.priv);
      builder.append("\n");
    }
    
    return builder.toString();
  }
  
  @Data
  public static class Parts
  {
    String props;
    String pub;
    String signals; 
    String priv;
  }
}
