import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner keyboard = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Kreme: questions worth exploring.");

        QuestionBank bank = new QuestionBank();
        if (!bank.load()) {
            System.out.println("I couldn't read questions.txt, so starting with no questions.");
        }
        System.out.println("Loaded " + bank.size() + " questions.");

        boolean running = true;
        while (running) {
            System.out.println("""

                    What would you like to do?
                    1. Surprise me
                    2. Question of the day
                    3. Search
                    4. Browse by category
                    5. Add a question
                    6. Edit a question
                    7. Delete a question
                    0. Quit""");

            String choice = ask("Your choice: ");
            switch (choice) {
                case "1" -> surprise(bank);
                case "2" -> questionOfTheDay(bank);
                case "3" -> search(bank);
                case "4" -> browse(bank);
                case "5" -> addQuestion(bank);
                case "6" -> editQuestion(bank);
                case "7" -> deleteQuestion(bank);
                case "0" -> running = false;
                default -> System.out.println("Please type a number from the menu.");
            }
        }

        System.out.println("Bye! Keep wondering.");
    }

    // ---------- Reading questions ----------

    private static void surprise(QuestionBank bank) {
        if (bank.size() == 0) {
            System.out.println("There are no questions yet. Choose 5 to add one.");
            return;
        }
        System.out.println(bank.surprise());
    }

    private static void questionOfTheDay(QuestionBank bank) {
        if (bank.size() == 0) {
            System.out.println("There are no questions yet. Choose 5 to add one.");
            return;
        }
        System.out.println("Today's question: " + bank.ofTheDay());
    }

    private static void search(QuestionBank bank) {
        String word = ask("Type a word to search for: ");
        List<Question> found = bank.search(word);

        if (found.isEmpty()) {
            System.out.println("No questions found with \"" + word + "\".");
            return;
        }
        System.out.println("Found " + found.size() + ":");
        for (Question q : found) {
            System.out.println("- " + q);
        }
    }

    private static void browse(QuestionBank bank) {
        System.out.println("Categories: " + String.join(", ", bank.categories()));
        String category = ask("Type a category: ");
        List<Question> found = bank.inCategory(category);

        if (found.isEmpty()) {
            System.out.println("There's no category called \"" + category + "\".");
            return;
        }
        System.out.println(found.size() + " questions in " + category + ":");
        for (Question q : found) {
            System.out.println("- " + q.text);
        }
    }

    // ---------- Changing questions ----------

    private static void addQuestion(QuestionBank bank) {
        String category = ask("Category: ");
        String text = ask("Question: ");

        String problem = QuestionBank.problemWith(text, category);
        if (problem != null) {
            System.out.println(problem);
            return;
        }
        bank.add(new Question(text, category));
        saveChanges(bank, "Question added.");
    }

    private static void editQuestion(QuestionBank bank) {
        Question old = pickQuestion(bank, "edit");
        if (old == null) {
            return;
        }
        System.out.println("Press Enter to keep the current value.");
        String category = ask("Category [" + old.category + "]: ");
        String text = ask("Question [" + old.text + "]: ");
        if (category.isEmpty()) {
            category = old.category;
        }
        if (text.isEmpty()) {
            text = old.text;
        }

        String problem = QuestionBank.problemWith(text, category);
        if (problem != null) {
            System.out.println(problem);
            return;
        }
        bank.edit(old, new Question(text, category));
        saveChanges(bank, "Question updated.");
    }

    private static void deleteQuestion(QuestionBank bank) {
        Question chosen = pickQuestion(bank, "delete");
        if (chosen == null) {
            return;
        }
        String answer = ask("Delete \"" + chosen.text + "\"? (y/n): ");
        if (!answer.equalsIgnoreCase("y")) {
            System.out.println("Nothing was deleted.");
            return;
        }
        bank.remove(chosen);
        saveChanges(bank, "Question deleted.");
    }

    // Lists every question with a number and returns the one the user picks,
    // or null if they cancel or type something invalid.
    private static Question pickQuestion(QuestionBank bank, String action) {
        List<Question> all = bank.all();
        if (all.isEmpty()) {
            System.out.println("There are no questions yet.");
            return null;
        }
        for (int i = 0; i < all.size(); i++) {
            System.out.println((i + 1) + ". " + all.get(i));
        }

        String input = ask("Number of the question to " + action + " (Enter to cancel): ");
        if (input.isEmpty()) {
            return null;
        }
        try {
            int number = Integer.parseInt(input);
            if (number >= 1 && number <= all.size()) {
                return all.get(number - 1); // the list starts at 0, the numbers at 1
            }
        } catch (NumberFormatException e) {
            // not a number: fall through to the message below
        }
        System.out.println("Please type a number from 1 to " + all.size() + ".");
        return null;
    }

    // Saves the file and tells the user whether it worked.
    private static void saveChanges(QuestionBank bank, String successMessage) {
        if (bank.save()) {
            System.out.println(successMessage);
        } else {
            System.out.println("I couldn't save to questions.txt. The change will be lost when you quit.");
        }
    }

    // Prints a prompt and returns what the user typed, without extra spaces.
    private static String ask(String prompt) {
        System.out.print(prompt);
        return keyboard.nextLine().trim();
    }
}
