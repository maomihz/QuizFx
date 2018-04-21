package quiz;

import java.util.Random;

public class TrueFalseQuestion extends ChoiceQuestion {
  public static final String TRUE_TEXT = "True";
  public static final String FALSE_TEXT = "False";

  private boolean myBooleanAnswer;

  public TrueFalseQuestion(String question, boolean answer) {
    this(question, answer, TRUE_TEXT);
  }
  public TrueFalseQuestion(String question, boolean answer, String trueText) {
    this(question, answer, trueText, FALSE_TEXT);
  }

  public TrueFalseQuestion(String question, boolean answer, String trueText, String falseText) {
    super(question, new String[] {trueText, falseText});
    setAnswer(answer ? 0 : 1);
    myBooleanAnswer = answer;
  }

  // Check & Get answer in boolean form
  public boolean checkAnswer(boolean choice) {
    return myBooleanAnswer == choice;
  }

  public boolean getBooleanAnswer() {
    return myBooleanAnswer;
  }

  @Override
  public QuestionType getType() {
    return QuestionType.TRUE_FALSE;
  }

  @Override
  public void shuffle() {
    // Do nothing! True/False questions do not shuffle
  }

}
