import javafx.application.Application;
import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.geometry.Pos;
import javafx.geometry.Insets;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.util.List;
import java.util.Iterator;
import java.net.URL;
import java.io.IOException;

import quiz.Question;
import quiz.ChoiceQuestion;
import quiz.MultipleChoiceQuestion;
import quiz.TrueFalseQuestion;

import quiz.builder.ChoiceQuestionBuilder;
import quiz.builder.QuestionTemplate;

import quiz.parser.QuizParser;
import quiz.parser.ParseException;


public class QuestionScene implements SceneContainer {
  private Scene myScene;
  BorderPane primaryPane;
  VBox box;

  private Text question, status;
  private ChoiceQuestionBuilder quizSet;
  private Iterator<ChoiceQuestion> questionIterator;
  private ChoiceQuestion currentQuestion;
  private HBox answerBtns;

  Button quitButton;


  public QuestionScene(URL qSetUrl, int w, int h) {
    QuizParser p = new QuizParser(qSetUrl);
    try {
      quizSet = p.parse();
    } catch (IOException e) {}

    question = new Text();
    question.setWrappingWidth(400);
    status = new Text();

    primaryPane = new BorderPane();
    box = new VBox();

    box.setSpacing(10);
    box.setPadding(new Insets(30,31,32,33));
    box.getChildren().add(question);

    answerBtns = new HBox();
    box.getChildren().add(answerBtns);

    status.setText("Question Set");
    status.setTextAlignment(TextAlignment.CENTER);
    status.setWrappingWidth(500);

    primaryPane.setCenter(box);
    primaryPane.setTop(status);

    quitButton = new Button("Quit");
    StackPane bottomPane = new StackPane();
    bottomPane.getChildren().add(quitButton);
    primaryPane.setBottom(bottomPane);

    myScene = new Scene(primaryPane, w, h);

    initQuestion();
  }


  public Scene getScene() {
    return myScene;
  }

  public Button getQuitButton() {
    return quitButton;
  }


  private void initQuestion() {
    List<ChoiceQuestion> cqSet = quizSet.getQuestions(10);
    questionIterator = cqSet.iterator();
    showQuestion();
  }

  private void selectAnswer(String choice) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Result Dialog");
    alert.setHeaderText(null);
    if (currentQuestion.checkAnswer(choice)) {
      alert.setContentText("Correct!");
    } else {
      alert.setContentText("Incorrect!");
    }

    alert.showAndWait();
    showQuestion();
  }

  private void showQuestion() {
    if (questionIterator.hasNext()) {
      answerBtns.getChildren().clear();

      currentQuestion = questionIterator.next();
      question.setText(currentQuestion.getQuestion());

      for (String s : currentQuestion.getAnswers()) {
        Button ans = new Button(s);
        answerBtns.getChildren().add(ans);
        ans.setOnAction(new EventHandler<ActionEvent>() {
          public void handle(ActionEvent e) {
            Button source = (Button)(e.getSource());
            selectAnswer(source.getText());
          }
        });
      }
    } else {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Quiz Completion");
      alert.setHeaderText(null);
      alert.setContentText("You have completed the quiz!");
      alert.showAndWait();
    }

  }
}
