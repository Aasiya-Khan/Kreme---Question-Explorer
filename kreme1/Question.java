public class Question {
    public String text;
    public String category;

    public Question(String text, String category) {
        this.text = text;
        this.category = category;
    }

    @Override
    public String toString() {
        return "[" + category + "] " + text;
    }
}
