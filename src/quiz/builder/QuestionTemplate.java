package quiz.builder;

import java.util.ArrayList;
import java.util.List;

import quiz.Question;

// A question template is a collection of many questions. It is used to
// select a question from many variants.
public class QuestionTemplate<T extends Question> {
  private List<T> questions;

  public QuestionTemplate() {
    questions = new ArrayList<T>();
  }

  public void addQuestion(T q) {
    questions.add(q);
  }

  public T getOne() {
    int i = (int)(Math.random() * questions.size());
    return questions.get(i);
  }
}
