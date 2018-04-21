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


public class QuizMain extends Application {

  private Text question, status;
  private ChoiceQuestionBuilder quizSet;
  private Iterator<ChoiceQuestion> questionIterator;
  private ChoiceQuestion currentQuestion;
  private HBox answerBtns;

  public QuizMain() {
    URL u = QuizParser.class.getResource("/basic.txt");
    QuizParser p = new QuizParser(u);
    try {
      quizSet = p.parse();
    } catch (IOException e) {}

    question = new Text();
    status = new Text();
  }

  @Override
  public void start(Stage primaryStage) {
    question.setWrappingWidth(400);

    BorderPane primaryPane = new BorderPane();

    VBox p = new VBox();
    p.setPadding(new Insets(30,31,32,33));
    p.getChildren().add(question);

    answerBtns = new HBox();
    p.getChildren().add(answerBtns);


    status.setText("Question Set");
    status.setTextAlignment(TextAlignment.CENTER);
    status.setWrappingWidth(500);

    primaryPane.setCenter(p);
    primaryPane.setTop(status);

    Scene quizScene = new Scene(primaryPane, 500, 400);

    initQuestion();


    primaryStage.setTitle("Space Quiz");
    primaryStage.setScene(quizScene);
    primaryStage.show();
  }


  private void initQuestion() {
    List<ChoiceQuestion> cqSet = quizSet.getQuestions(10);
    questionIterator = cqSet.iterator();
    showQuestion();
  }

  private void selectAnswer(String choice) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Information Dialog");
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

      System.out.println(currentQuestion);

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
    }

  }

  public static void main(String[] args) {
    launch(args);
  }
}
