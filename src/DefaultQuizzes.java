import java.util.LinkedHashMap;
import java.net.URL;

// Collection of default quizzes.
// I have to hardcode this or otherwise it doesn't work inside a jar
public class DefaultQuizzes {
  public static final LinkedHashMap<String, URL> quizzes;
  static {
    quizzes = new LinkedHashMap<String, URL>();
    quizzes.put("Space Quiz", DefaultQuizzes.class.getResource("/quizzes/main.txt"));
    quizzes.put("Debug Quiz", DefaultQuizzes.class.getResource("/quizzes/basic.txt"));
  }
}
