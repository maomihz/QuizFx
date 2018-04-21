package quiz;

import java.util.Random;

// A question type that offers multiple choices
// It consists of a question and many possible answers.
public class ChoiceQuestion extends Question {
  protected String[] myChoices;
  protected int myAnswer;


  // Must provide the text and at least one answer. By default the first
  // answer is considered correct
  public ChoiceQuestion(String question, String[] answers) {
    this(question, answers, 0);
  }

  public ChoiceQuestion(String question, String[] answers, int answer) {
    myQuestion = question;
    myChoices = answers;
    setAnswer(answer);
  }

  // set the correct answer!
  protected void setAnswer(int choice) {
    if (choice < 0 || choice >= myChoices.length) {
      throw new IllegalArgumentException(choice + ": Please give a good choice");
    }
    myAnswer = choice;
  }



  // Return a list of possible answers
  public String[] getAnswers() {
    return myChoices;
  }


  // Shuffle the answers
  public void shuffle() {
    Random r = new Random();

    // Shuffle algorithm
    for (int i = 0; i < myChoices.length - 1; i++) {
      int select = r.nextInt(myChoices.length - i) + i;

      if (select != i) {
        // Swap
        String tmp = myChoices[select];
        myChoices[select] = myChoices[i];
        myChoices[i] = tmp;

        // Swap correct answer
        if (select == myAnswer) {
          myAnswer = i;
        } else if (i == myAnswer) {
          myAnswer = select;
        }
      }
    }
  }

  @Override
  public String getQuestion() {
    return myQuestion;
  }

  @Override
  public String getAnswer() {
    return myChoices[myAnswer];
  }

  @Override()
  public boolean checkAnswer(String choice) {
    return myChoices[myAnswer].equals(choice);
  }

  @Override
  public QuestionType getType() {
    return QuestionType.CHOICE;
  }


  @Override
  public String toString() {
    StringBuilder question = new StringBuilder(myQuestion);
    question.append("\n\n");
    for (int i = 0; i < myChoices.length; i++) {
      question.append(myChoices[i] + "\n");
    }
    return question.toString();
  }

}
