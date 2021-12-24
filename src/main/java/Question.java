import java.util.Map;

public class Question {
    int questionNumber;
    String subject;
    String tags;
    String statement;
    String hint;
    int correctOptionNumber;
    boolean bookmarked;
    int userResponse;

    static final String kQuestion_Subject_Field = "subject";
    static final String kQuestion_tags_Field = "tags";
    static final String kQuestionNumber_Field = "Qno";
    static final String kQuestion_Statement_Field = "statement";
    static final String kQuestion_correct_option_Field = "correct";
    static final String kQuestion_hint_Field = "hint";
    static final String kQuestion_bookmarked_Field = "bookmarked";
    static final String kQUESTION_USER_RESPONSE_FIELD = "userResponse";

    public Question(int questionNumber, String subject, String tags, String statement, String hint, int correctOptionNumber, boolean bookmarked, int userResponse) {
        this.questionNumber = questionNumber;
        this.subject = subject;
        this.tags = tags;
        this.statement = statement;
        this.hint = hint;
        this.correctOptionNumber = correctOptionNumber;
        this.bookmarked = bookmarked;
        this.userResponse = userResponse;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getCorrectOptionNumber() {
        return correctOptionNumber;
    }

    public void setCorrectOptionNumber(int correctOptionNumber) {
        this.correctOptionNumber = correctOptionNumber;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public int getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(int userResponse) {
        this.userResponse = userResponse;
    }

    static Question fromQuestionDataMap(Map<String, Object> questionDataMap) {
        return new Question(
                Integer.parseInt(questionDataMap.get(kQuestionNumber_Field).toString()),
                questionDataMap.get(kQuestion_Subject_Field).toString(),
                questionDataMap.get(kQuestion_tags_Field).toString(),
                questionDataMap.get(kQuestion_Statement_Field).toString(),
                questionDataMap.get(kQuestion_hint_Field).toString(),
                Integer.parseInt(questionDataMap.get(kQuestion_correct_option_Field).toString()),
                false,
                5
        );
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionNumber=" + questionNumber +
                ", subject='" + subject + '\'' +
                ", tags='" + tags + '\'' +
                ", statement='" + statement + '\'' +
                ", hint='" + hint + '\'' +
                ", correctOptionNumber=" + correctOptionNumber +
                ", bookmarked=" + bookmarked +
                ", userResponse=" + userResponse +
                '}';
    }
}
