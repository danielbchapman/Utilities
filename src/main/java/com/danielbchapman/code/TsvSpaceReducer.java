package com.danielbchapman.code;
import com.lightassistant.utility.UtilityText;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TsvSpaceReducer extends Application
{

  public static void main(String... args)
  {
    launch();
  }

  @Override
  public void start(Stage stage) throws Exception
  {
    VBox root = new VBox();

    final TextArea text = new TextArea();
    text.promptTextProperty().set("Enter a TSV with split words");
    final Button process = new Button("Process");
    final TextArea out = new TextArea();
    process.onActionProperty().set(new EventHandler<ActionEvent>()
    {

      @Override
      public void handle(ActionEvent arg0)
      {
        out.setText(stripWhiteSpace(text.getText(), "\t"));
      }
    });

    root.getChildren().addAll(text, process, out);
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  public static String stripWhiteSpace(String in, String separator)
  {
    String[] split = in.split(separator);
    for (int i = 0; i < split.length; i++)
      split[i] = UtilityText.Code.clean(split[i]);

    StringBuilder out = new StringBuilder();
    for (String s : split)
    {
      out.append(s);
      out.append(" ");
    }

    return out.toString();
  }
}
