package quiz;

public abstract class Question {
  // A Question has to have a question
  protected String myQuestion;

  // Return the question text
  public abstract String getQuestion();

  // Cheat method to return the answer
  public abstract String getAnswer();

  // Check if the answer is correct, return true if choice matches the
  // answer, false otherwise.
  public abstract boolean checkAnswer(String choice);

  // Return the type of the question
  public abstract QuestionType getType();

  public String toString() {
    return getQuestion();
  }

}
