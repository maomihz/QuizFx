package quiz;

// A Simple question is just a very simple question of the Question abstract class.
// The answer must match the exact string for it to be correct.
public class SimpleQuestion extends Question {
  protected String myAnswer;

  public SimpleQuestion(String question, String answer) {
    myQuestion = question;
    myAnswer = answer;
  }

  // Cheat method to return the answer
  public String getAnswer() {
    return myAnswer;
  }

  // Implement questions
  @Override
  public String getQuestion() {
    return myQuestion;
  }

  @Override()
  public boolean checkAnswer(String choice) {
    return myAnswer.equals(choice);
  }


  @Override
  public QuestionType getType() {
    return QuestionType.SIMPLE;
  }


}
