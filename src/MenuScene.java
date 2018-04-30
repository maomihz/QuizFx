import javafx.application.Application;
import javafx.stage.Stage;

import javafx.scene.Scene;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import javafx.geometry.Pos;
import javafx.geometry.Insets;

import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


import java.util.List;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.net.URL;
import java.io.IOException;
import java.io.File;
import java.net.MalformedURLException;

import quiz.parser.QuizParser;


public class MenuScene implements SceneContainer, ChangeListener<String> {
  private Scene myScene;

  ListView<String> quizList;
  LinkedHashMap<String, URL> fileMap;
  Text title, content, difficulty;
  Button startButton;
  String selectedFile;


  public MenuScene(int w, int h) {
    GridPane primaryPane = new GridPane();
    primaryPane.setAlignment(Pos.CENTER);
    primaryPane.setPadding(new Insets(10,11,12,13));
    primaryPane.setVgap(14);
    primaryPane.setHgap(15);

    ColumnConstraints col1 = new ColumnConstraints();
    ColumnConstraints col2 = new ColumnConstraints();
    col2.setHgrow(Priority.ALWAYS);
    col2.setFillWidth(true);
    primaryPane.getColumnConstraints().addAll(col1, col2);

    refresh();
    quizList.setMaxWidth(200);
    quizList.setMinWidth(200);
    primaryPane.add(quizList, 0, 0, 1, 3);

    // Set the texts
    title = new Text("");
    title.setFont(Font.font("Arial", 30));
    content = new Text("Select a quiz from the left list");
    difficulty = new Text();
    TextFlow titleFlow = new TextFlow(title);
    TextFlow contentFlow = new TextFlow(content);
    TextFlow difficultyFlow = new TextFlow(difficulty);

    primaryPane.add(titleFlow, 1, 0);
    primaryPane.add(contentFlow, 1, 1);
    primaryPane.add(difficultyFlow, 1, 2);

    startButton = new Button("Start!");
    primaryPane.add(startButton, 1, 3);

    myScene = new Scene(primaryPane, w, h);
  }


  public Button getStartButton() {
    return startButton;
  }

  public URL getSelectedFile() {
    return fileMap.get(selectedFile);
  }



  private void refresh() {
    // Add the quizzes into the listview
    fileMap = new LinkedHashMap<String, URL>(DefaultQuizzes.quizzes);
    quizList = new ListView<String>();
    for (Map.Entry<String, URL> entry : fileMap.entrySet()) {
      String key = entry.getKey();
      quizList.getItems().add(key);
    }
    // Scan external directory
    File d = new File("quizzes");
    if (d.isDirectory()) {
      File[] fList = d.listFiles();
      for (File file : fList) {
        if (file.isFile() && file.getPath().endsWith(".txt")) {
          try {
            fileMap.put(file.getName(), file.toURL());
            quizList.getItems().add(file.getName());
          } catch (MalformedURLException e) {

          }
        }
      }
    }
    quizList.getSelectionModel().selectedItemProperty().addListener(this);
  }

  @Override
  public Scene getScene() {
    return myScene;
  }

  @Override
  public void changed(ObservableValue<? extends String> observable, String oldV, String newV) {
    selectedFile = newV;
    URL u = fileMap.get(newV);
    QuizParser parser = new QuizParser(u);
    try {
      Map<String, String> data = parser.parseMetadata();
      title.setText(data.get("title"));
      content.setText(data.get("description"));
      String difficultyStr = "Difficulty: ";
      try {
        for (int i = 0; i < Integer.parseInt(data.get("difficulty")); i++) {
          difficultyStr += '★';
        }
        difficulty.setText(difficultyStr);
      } catch (NumberFormatException e) {
        difficulty.setText("");
      }
    } catch (IOException e) {}
  }

}
