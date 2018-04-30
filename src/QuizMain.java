import javafx.application.Application;
import javafx.stage.Stage;

import javafx.scene.Scene;
import java.net.URL;


public class QuizMain extends Application {
  MenuScene menuScene;

  public QuizMain() {
  }

  @Override
  public void start(Stage primaryStage) {
    // QuestionScene quizScene = new QuestionScene(QuizMain.class.getResource("/basic.txt"), 500, 400);
    menuScene = new MenuScene(500, 400);
    menuScene.getStartButton().setOnAction((e) -> {
      URL selected = menuScene.getSelectedFile();
      if (selected != null) {
        QuestionScene quizScene = new QuestionScene(selected, 500, 400);
        quizScene.getQuitButton().setOnAction((e1) -> {
          primaryStage.setScene(menuScene.getScene());
        });
        primaryStage.setScene(quizScene.getScene());
      }
    });

    primaryStage.setTitle("Quiz");
    primaryStage.setScene(menuScene.getScene());
    primaryStage.setResizable(false);
    primaryStage.show();
  }


  public static void main(String[] args) {
    launch(args);
  }
}
