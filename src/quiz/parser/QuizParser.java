package quiz.parser;
import java.net.URL;
import java.util.Scanner;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import quiz.builder.QuestionBuilder;
import quiz.builder.QuestionTemplate;

import quiz.Question;
import quiz.ChoiceQuestion;
import quiz.SimpleQuestion;
import quiz.MultipleChoiceQuestion;
import quiz.TrueFalseQuestion;
import quiz.ConfirmationQuestion;
import quiz.QuestionType;

public class QuizParser {
  // Test code for parsing a file
  public static void main(String[] args) {
    URL u = QuizParser.class.getResource("/quizzes/main.txt");
    System.out.println(u);
    QuizParser p = new QuizParser(u);
    try {
      QuestionBuilder b = p.parse();
      System.out.println(b.getMetadata());
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



  public Map<String, String> parseMetadata() throws IOException {
    Scanner s = new Scanner(url.openStream());
    Map<String, String> builder = new HashMap<String, String>();
    while (s.hasNext()) {
      String line = s.nextLine().trim();
      if (line.startsWith("@")) {
        line = line.substring(1);
        int metadataTag = line.indexOf('=');
        if (metadataTag >= 0) {
          String key = line.substring(0, metadataTag);
          String value = line.substring(metadataTag + 1);
          if (key.length() > 0 && value.length() > 0) {
            builder.put(key, value);
          }
        }
      }
    }
    return builder;
  }


  public QuestionBuilder parse() throws IOException {
    Scanner s = new Scanner(url.openStream());
    QuestionBuilder builder = new QuestionBuilder();
    QuestionTemplate<Question> template = new QuestionTemplate<Question>();

    // Question type usually represented by two letters
    String currentQuestionType = null;
    // Question as a string
    String currentQuestion = null;
    // A list of answers
    List<String> currentAnswers = new ArrayList<String>();
    // Index of the current correct answer
    int currentAnswer = -1;

    // Keep a count of number of answers
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
        // If the stream ends, then implicitly add @END tag
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

      // If a line starts with @, then it either be metadata or a question tag.
      // A metadata tag must contain the "=" symbol.
      if (line.startsWith("@")) {
        qType = line.substring(1).toLowerCase();
        if (qType.length() <= 0) {
          newVariant = true;
          // System.out.println("New Variant");
        } else {
          // Check if it's a metadata tag, and set it if it is.
          int metadataTag = qType.indexOf('=');
          if (metadataTag >= 0) {
            String key = qType.substring(0, metadataTag);
            String value = qType.substring(metadataTag + 1);
            if (key.length() > 0 && value.length() > 0) {
              builder.getMetadata().put(key, value);
            }
            continue;
          }
          // Otherwise, treat it as a question tag
          newQuestion = true;
          // System.out.println("New Question");
        }
      } else if (currentQuestionType == null) {
        // Ignore everything before the first @
        continue;
      }

      // Process a new question / new variant if necessary
      if ((newQuestion || newVariant) && currentQuestionType != null) {
        Question cq;
        if (currentQuestionType.equals("sp")) {
          if (currentAnswers.size() <= 0) {
            throw new ParseException("Not enough answers given for Simple Question.", lineCount);
          }
          String answer = currentAnswers.get(0);
          cq = new SimpleQuestion(currentQuestion, answer);
        }
        else if (currentQuestionType.equals("tf")) {
          // True/False question only accepts exactly one answer,
          // true or false. More answers are ignored.
          if (currentAnswers.size() <= 0) {
            throw new ParseException("Not enough answers given for True/False Question.", lineCount);
          }
          String answer = currentAnswers.get(0);
          cq = new TrueFalseQuestion(currentQuestion, Boolean.valueOf(answer));
        } else {
          // CO or SC or MC
          // Simple choice question and multiple choice are basically the same
          // Copy the answer to an array
          String[] answers = new String[currentAnswers.size()];
          for (int i = 0; i < currentAnswers.size(); i++) {
            answers[i] = currentAnswers.get(i);
          }

          // Confirmation question does not need to check answer count
          if (currentQuestionType.equals("co")) {
            if (currentAnswers.size() <= 0) {
              cq = new ConfirmationQuestion(currentQuestion);
            } else {
              cq = new ConfirmationQuestion(currentQuestion, answers);
            }
          } else {
            if (currentAnswers.size() <= 0) {
              throw new ParseException("Not enough answers given for Choice Question.", lineCount);
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
        }

        // System.out.println("↓↓↓ *** Question *** ↓↓↓");
        // System.out.println(cq);
        // System.out.println("↑↑↑ * End Question * ↑↑↑");
        template.addQuestion(cq);
        currentAnswers.clear();
        currentQuestion = null;
        answerCount = 0;
      }

      // Process a new question. The template must be already completed by now
      if (newQuestion) {
        if (currentQuestionType != null) {
          builder.add(template);
          template = new QuestionTemplate<Question>();
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
      // System.out.println(line);

      if (currentQuestionType.equals("end")) {
        break;
      }
    }

    return builder;
  }
}
