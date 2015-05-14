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

@SuppressWarnings("restriction")
public class QtPropertyBuilder extends Application
{  
  public static void main(String ... args)
  {
      launch();
  }
  
  @Override
  public void start(Stage stage) throws Exception
  {
    VBox root = new VBox();
    
    
    final Label lower = new Label("Use " + QtPropertyBuilderCore.LOWER + " for lowerCase (method)");
    final Label staticc = new Label("Use " + QtPropertyBuilderCore.STATIC + " for STATIC_CASE");
    final Label value = new Label("Use " + QtPropertyBuilderCore.VALUE + " for ObjectCase");
    final Label raw = new Label("Use " + QtPropertyBuilderCore.RAW + " for 'Raw Value'");

    final TextArea pre = new TextArea();
    final TextArea data = new TextArea();
    final TextArea out = new TextArea();
    
    StringBuilder setup = new StringBuilder();
    setup.append("\tQ_PROPERTY Q_PROPERTY(QString " + lower + " READ " + lower + " WRITE set" + QtPropertyBuilderCore.VALUE + " NOTIFY " + lower + "Changed)");
    setup.append("\tQString " + QtPropertyBuilderCore.LOWER + "()\t\n{return m_"+lower+";\n\t}");
    setup.append("\tQString set" + QtPropertyBuilderCore.VALUE + "(const QString &v)\t\n{m_"+lower+" = v;\n\t}");
    
    pre.setText(setup.toString());
    //pre.setText("public void " + LOWER + "(String "+ LOWER+ ")\n{\n\tthis." + LOWER +" = " + LOWER + ";\n}\\\\" + STATIC +" " + RAW + ";");
    pre.setPromptText("Code before element");
    data.setPromptText("TSV elements");
    

    Button process =new Button("Process");
    process.onActionProperty().set(new EventHandler<ActionEvent>(){

      @Override
      public void handle(ActionEvent arg0)
      {
        out.setText(QtPropertyBuilderCore.process(data.getText(), "REPLACE_ME").getOne());
      }});
        
    Button getFiles = new Button("From *.gen");
    getFiles.onActionProperty().set(new EventHandler<ActionEvent>(){

      @Override
      public void handle(ActionEvent arg0)
      {
        QtPropertyBuilderCore.processFiles(".");
      }});
    
    root.getChildren().addAll(
        lower,
        staticc,
        value,
        raw,
//        pre,
        data, 
        process,
        getFiles,
        out);
    
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    stage.setTitle("TSV Code Builder");
  }

  
}
