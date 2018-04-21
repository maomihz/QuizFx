package quiz.parser;

public class ParseException extends RuntimeException {
    private int myLineCount;
    public ParseException(String message, int lineCount) {
        super(message);
        myLineCount = lineCount;
    }

    @Override
    public String getMessage()
    {
        return String.format("ParseError:line %d: %s",
          myLineCount, super.getMessage());
    }
}
