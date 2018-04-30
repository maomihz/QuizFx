package quiz;


// ConfirmationQuestion is so simple: whatever you choose, the answer is correct.
public class ConfirmationQuestion extends ChoiceQuestion {
  private boolean defaultChoice;

  public ConfirmationQuestion(String question) {
    this(question, true);
  }

  public ConfirmationQuestion(String question, boolean defaultChoice) {
    this(question, new String[]{"Ok"}, defaultChoice);
  }

  public ConfirmationQuestion(String question, String[] answers) {
    this(question, answers, true);
  }

  public ConfirmationQuestion(String question, String[] answers, boolean defaultChoice) {
    super(question, answers, 0);
    this.defaultChoice = defaultChoice;
  }


  // Confirmation question doesn't care what you choose
  @Override()
  public boolean checkAnswer(String choice) {
    return defaultChoice;
  }


  @Override
  public QuestionType getType() {
    return QuestionType.CONFIRMATION;
  }


  // Confirmation question don't shuffle
  @Override
  public void shuffle() {}

}
