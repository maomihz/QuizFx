package quiz;

import java.util.Random;


// MultipleChoiceQuestion uses "A, B, C ..." to represent answers. The actual
// answer text will be included as part of the question, and the choices are
// just simple letters.

public class MultipleChoiceQuestion extends ChoiceQuestion {
  private String[] actualAnswers;

  public MultipleChoiceQuestion(String question, String[] answers) {
    this(question, answers, 0);
  }

  public MultipleChoiceQuestion(String question, String[] answers, int answer) {
    super(question, new String[answers.length], answer);
    actualAnswers = new String[answers.length];

    for (int i = 0; i < answers.length; i++) {
      myChoices[i] = Character.toString((char)('A' + (i % 26)));
      actualAnswers[i] = answers[i];
    }
  }

  // The question is a combination of the actual question and choices
  @Override
  public String getQuestion() {
    StringBuilder question = new StringBuilder(myQuestion);
    question.append("\n\n");
    for (int i = 0; i < actualAnswers.length; i++) {
      question.append(myChoices[i] + ". " + actualAnswers[i] + "\n");
    }
    return question.toString();
  }


  @Override
  public QuestionType getType() {
    return QuestionType.MULTIPLE_CHOICE;
  }


  @Override
  public void shuffle() {
    // It doesn't shuffle the actual answer, but shuffles the question
    Random r = new Random();

    // Shuffle algorithm
    for (int i = 0; i < actualAnswers.length - 1; i++) {
      int select = r.nextInt(actualAnswers.length - i) + i;

      if (select != i) {
        // Swap
        String tmp = actualAnswers[select];
        actualAnswers[select] = actualAnswers[i];
        actualAnswers[i] = tmp;

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
  public String toString() {
    return getQuestion();
  }

}
