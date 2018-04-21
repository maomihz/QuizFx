package quiz.parser;
import java.net.URL;
import java.util.Scanner;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import quiz.builder.ChoiceQuestionBuilder;
import quiz.builder.QuestionTemplate;

import quiz.ChoiceQuestion;
import quiz.MultipleChoiceQuestion;
import quiz.TrueFalseQuestion;
import quiz.QuestionType;

public class QuizParser {
  // Test code for parsing a file
  public static void main(String[] args) {
    URL u = QuizParser.class.getResource("/main.txt");
    System.out.println(u);
    QuizParser p = new QuizParser(u);
    try {
      p.parse();
    } catch (IOException e) {

    }
  }

  private URL url;

  public QuizParser(URL url) {
    if (url == null) {
      throw new IllegalArgumentException("Don't give me an null url!");
    }
    this.url = url;
  }



  public ChoiceQuestionBuilder parse() throws IOException {
    Scanner s = new Scanner(url.openStream());
    ChoiceQuestionBuilder builder = new ChoiceQuestionBuilder();
    QuestionTemplate<ChoiceQuestion> template = new QuestionTemplate<ChoiceQuestion>();

    // Question type usually represented by two letters
    String currentQuestionType = null;
    // Question as a string
    String currentQuestion = null;
    // A list of answers
    List<String> currentAnswers = new ArrayList<String>();
    // Index of the current correct answer
    int currentAnswer = -1;

    int answerCount = 0;
    int lineCount = 0;

    while (true) {
      // Detect if a new question is starting.
      // Add the previous question if there is a new question or if
      // end of file is reached.
      boolean newQuestion = false;
      boolean newVariant = false;
      String qType = null; // Temporary variable, need to use it later when
                           // when the last question is processed
      String line = null;

      if (!s.hasNext()) {
        newQuestion = true;
        line = "@END";
      } else {
        // Read a line of test, skip if empty line
        line = s.nextLine().trim();
        lineCount++;
        if (line.length() <= 0) {
          continue;
        }
      }

      if (line.startsWith("@")) {
        qType = line.substring(1).toLowerCase();
        if (qType.length() <= 0) {
          newVariant = true;
          System.out.println("New Variant");
        } else {
          newQuestion = true;
          System.out.println("New Question");
        }
      } else if (currentQuestionType == null) {
        // Ignore everything before the first @
        continue;
      }

      // Process a new question / new variant if necessary
      if ((newQuestion || newVariant) && currentQuestionType != null) {
        // Throw error if answer is not given enough
        if (currentAnswers.size() <= 0) {
          throw new ParseException("Not enough answers given.", lineCount);
        }
        ChoiceQuestion cq;

        // True/False question only accepts exactly one answer,
        // true or false. More answers are ignored.
        if (currentQuestionType.equals("tf")) {
          String answer = currentAnswers.get(0);
          cq = new TrueFalseQuestion(currentQuestion, Boolean.valueOf(answer));
        // SC or MC
        // Simple choice question and multiple choice are basically the same
        } else {
          // Copy the answer to an array
          String[] answers = new String[currentAnswers.size()];
          for (int i = 0; i < currentAnswers.size(); i++) {
            answers[i] = currentAnswers.get(i);
          }

          // Create the question object
          if (currentQuestionType.equals("sc")) {
            cq = new ChoiceQuestion(currentQuestion, answers, currentAnswer);
          } else if (currentQuestionType.equals("mc")) {
            cq = new MultipleChoiceQuestion(currentQuestion, answers, currentAnswer);
          } else { // currently only support tf, sc, mc
            throw new ParseException("Invalid question type: " + currentQuestionType.toUpperCase(), lineCount);
          }
        }

        System.out.println("↓↓↓ *** Question *** ↓↓↓");
        System.out.println(cq);
        System.out.println("↑↑↑ * End Question * ↑↑↑");
        template.addQuestion(cq);
        currentAnswers.clear();
        currentQuestion = null;
        answerCount = 0;
      }

      // Process a new question. The template must be already completed by now
      if (newQuestion) {
        if (currentQuestionType != null) {
          builder.add(template);
          template = new QuestionTemplate<ChoiceQuestion>();
        }
        currentQuestionType = qType;
      }


      // If not the start of a question then must be an answer
      if (!newQuestion && !newVariant) {
        // This line is a question
        if (currentQuestion == null) {
          currentQuestion = line;
        } else {
          String answer = line;
          if (line.startsWith("*")) {
            answer = line.substring(1);
            currentAnswer = answerCount;
          }
          currentAnswers.add(answer);
          answerCount++;
        }
      }
      System.out.println(line);

      if (currentQuestionType.equals("end")) {
        break;
      }
    }

    return builder;
  }
}
