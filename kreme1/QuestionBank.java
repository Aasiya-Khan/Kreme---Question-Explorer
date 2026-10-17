import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionBank {
    private final List<Question> questions = new ArrayList<>();

    // Questions not yet shown by surprise(), in random order.
    private final List<Question> unseen = new ArrayList<>();

    // ---------- Loading and saving ----------

    // Reads questions.txt. Returns true if the file was read, false if it could not be.
    public boolean load() {
        try {
            for (String line : Files.readAllLines(Path.of("questions.txt"))) {
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    String category = parts[0].trim();
                    String text = parts[1].trim();
                    questions.add(new Question(text, category));
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Writes every question back to questions.txt. Returns true if it worked.
    public boolean save() {
        List<String> lines = new ArrayList<>();
        for (Question q : questions) {
            lines.add(q.category + " | " + q.text);
        }
        try {
            Files.write(Path.of("questions.txt"), lines);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // ---------- Create, update, delete ----------

    public void add(Question q) {
        questions.add(q);
        unseen.clear();
    }

    public void edit(Question oldQuestion, Question newQuestion) {
        int index = questions.indexOf(oldQuestion);
        if (index != -1) {
            questions.set(index, newQuestion);
            unseen.clear();
        }
    }

    public void remove(Question q) {
        questions.remove(q);
        unseen.clear();
    }

    // Returns a reason the input is not allowed, or null if it is fine.
    public static String problemWith(String text, String category) {
        if (text.isBlank() || category.isBlank()) {
            return "Please fill in both the question and the category.";
        }
        if (category.contains("|")) {
            return "The category can't contain the | character.";
        }
        if (category.startsWith("#")) {
            return "The category can't start with #.";
        }
        return null;
    }

    // ---------- Reading ----------

    public int size() {
        return questions.size();
    }

    public List<Question> all() {
        return new ArrayList<>(questions);
    }

    // Shows every question once, in random order, before any repeats.
    public Question surprise() {
        if (unseen.isEmpty()) {
            unseen.addAll(questions);
            Collections.shuffle(unseen);
        }
        return unseen.remove(unseen.size() - 1);
    }

    // The same question all day; a different one tomorrow.
    public Question ofTheDay() {
        long dayNumber = LocalDate.now().toEpochDay();
        int index = (int) (dayNumber % questions.size());
        return questions.get(index);
    }

    public List<Question> search(String word) {
        String lookFor = word.toLowerCase();
        List<Question> results = new ArrayList<>();
        for (Question q : questions) {
            if (q.text.toLowerCase().contains(lookFor)) {
                results.add(q);
            }
        }
        return results;
    }

    public List<String> categories() {
        List<String> names = new ArrayList<>();
        for (Question q : questions) {
            if (!names.contains(q.category)) {
                names.add(q.category);
            }
        }
        return names;
    }

    public List<Question> inCategory(String category) {
        List<Question> results = new ArrayList<>();
        for (Question q : questions) {
            if (q.category.equalsIgnoreCase(category)) {
                results.add(q);
            }
        }
        return results;
    }
}
