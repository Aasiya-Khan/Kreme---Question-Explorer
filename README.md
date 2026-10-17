# Kreme---Question-Explorer
Kreme: a Java command-line question explorer. Get a random question or the question of the day, search, browse by category, and add, edit or delete questions (CRUD), all saved to a text file. A prototype for a trusted-source learning platform.

---

## Features

- 🎲 **Random question**: pick any question at random
- 📅 **Question of the day**: the same question for everyone on a given day
- 🔍 **Search**: find questions by keyword
- 🗂️ **Browse by category**: list questions grouped by topic
- ✏️ **CRUD**: add, edit and delete questions
- 💾 **Persistent storage**: all changes are saved to `questions.txt`

---

## Requirements

- Java 8 or later (JDK)

Check your version with:

    java -version

---

## How to Run

1. Clone the repository:

       git clone https://github.com/Aasiya-Khan/Kreme---Question-Explorer.git
       cd Kreme---Question-Explorer/kreme1

2. Compile:

       javac *.java

3. Run:

       java Main

4. Follow the on-screen menu.

---

## Project Structure

    kreme1/
    ├── Main.java           # Entry point and command-line menu
    ├── Question.java       # Question model (text, category, etc.)
    ├── QuestionBank.java   # Loads, searches, and saves questions; CRUD logic
    └── questions.txt       # Question data (edited by the app)

---

## Roadmap

- [ ] Graphical user interface (GUI)
- [ ] Questions sourced only from approved websites
- [ ] Teacher and student modes (teachers control the allowed sources)
- [ ] Quizzes and scoring

---

## Author

**Aasiya Khan**: [GitHub](https://github.com/Aasiya-Khan)
