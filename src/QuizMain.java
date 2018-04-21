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

import javafx.geometry.Pos;
import javafx.geometry.Insets;

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

  Text question, status;
  ChoiceQuestionBuilder quizSet;
  Iterator<ChoiceQuestion> currentQuestions;
  HBox answerBtns;

  public QuizMain() {
    URL u = QuizParser.class.getResource("/main.txt");
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


  public void initQuestion() {
    List<ChoiceQuestion> cqSet = quizSet.getQuestions(10);
    currentQuestions = cqSet.iterator();
    showQuestion();
  }

  public void showQuestion() {
    if (currentQuestions.hasNext()) {
      answerBtns.getChildren().clear();

      ChoiceQuestion q = currentQuestions.next();
      question.setText(q.getQuestion());
      for (String s : q.getAnswers()) {
        Button ans = new Button(s);
        answerBtns.getChildren().add(ans);
      }
    }

  }

  public static void main(String[] args) {
    launch(args);
  }
}
