package quiz.builder;

import quiz.Question;
import quiz.ChoiceQuestion;
import quiz.TrueFalseQuestion;
import quiz.MultipleChoiceQuestion;

import java.util.ArrayList;
import java.util.List;

import java.util.Collections;

// The builder class collects questions, and builds
// question objects on the run
public class ChoiceQuestionBuilder {
  private List<QuestionTemplate<ChoiceQuestion>> questions;

  public ChoiceQuestionBuilder() {
    questions = new ArrayList<QuestionTemplate<ChoiceQuestion>>();
  }

  // Add a question template to the list
  public void add(QuestionTemplate<ChoiceQuestion> q) {
    questions.add(q);
  }

  // Get random questions from the builder
  public List<ChoiceQuestion> getQuestions(int count) {
    // Shuffle the question collection
    Collections.shuffle(questions);

    // Choose real_count number of questions
    int real_count = (int)Math.min(count, questions.size());
    List<ChoiceQuestion> result = new ArrayList<ChoiceQuestion>();

    for (int i = 0; i < real_count; i++) {
      // Get a variant, and shuffle it
      ChoiceQuestion q = questions.get(i).getOne();
      q.shuffle();
      result.add(q);
    }

    return result;

  }
}
