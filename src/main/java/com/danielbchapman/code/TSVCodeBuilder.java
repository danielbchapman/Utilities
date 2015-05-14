package com.danielbchapman.code;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.danielbchapman.utility.UtilityText;

@SuppressWarnings("restriction")
public class TSVCodeBuilder extends Application
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
    
    pre.setText("public void " + LOWER + "(String "+ LOWER+ ")\n{\n\tthis." + LOWER +" = " + LOWER + ";\n}\\\\" + STATIC +" " + RAW + ";");
    pre.setPromptText("Code before element");
    data.setPromptText("TSV elements");
    

    Button process =new Button("Process");
    process.onActionProperty().set(new EventHandler<ActionEvent>(){

      @Override
      public void handle(ActionEvent arg0)
      {
        out.setText(process(pre.getText(), data.getText()));
      }});
        
    root.getChildren().addAll(
        lower,
        staticc,
        value,
        raw,
        pre,
        data, 
        process,
        out);
    
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    stage.setTitle("TSV Code Builder");
  }

  public static String process(String template, String data)
  {
    StringBuilder builder = new StringBuilder();
    String[] raw = data.split("\\t");
    
    for(String s : raw)
    { 
      String clean = UtilityText.Code.clean(s);
      String lowerCase = UtilityText.lowerCaseFirst(clean);
      String staticCase = UtilityText.Code.staticCase(lowerCase);
      
      String local = template;
      local = local
          .replaceAll(RAW, s)
          .replaceAll(LOWER, lowerCase)
          .replaceAll(STATIC, staticCase)
          .replaceAll(VALUE, clean);
      
      builder.append(local);
      builder.append("\n");
    }
    
    return builder.toString();
  }
}
