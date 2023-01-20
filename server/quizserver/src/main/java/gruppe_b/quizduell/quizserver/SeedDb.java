package gruppe_b.quizduell.quizserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.categories.CategoryRepository;
import gruppe_b.quizduell.application.questions.QuestionRepository;
import gruppe_b.quizduell.domain.entities.Category;
import gruppe_b.quizduell.domain.entities.Question;

@Service
public class SeedDb {

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    QuestionRepository questionRepo;

    public void seed() {
        java();
        patterns();
        framework();
        python();
    }

    void java() {
        if (categoryRepo.findByName("Java") == null) {
            Category category = categoryRepo
                    .save(new Category("Java", "Fragen zur Programmiersprache Java"));

            questionRepo.save(new Question(category,
                    "Was ist Java?",
                    "Eine Programmiersprache ohne Objektorientierung",
                    "Eine Firma die Joghurt produziert",
                    "Eine Objekt-Orientierte Programmiersprache",
                    "Ein Planet der nicht bewohnbar ist",
                    3));

            questionRepo.save(new Question(category,
                    "Welchen Namen hatte Java ursprünglich?",
                    "Oak",
                    "Spruce",
                    "Latte",
                    "Chai",
                    1));

            questionRepo.save(new Question(category,
                    "Wie heißt der korrekte Datentyp für eine Zeichenkette?",
                    "string",
                    "String",
                    "str",
                    "charchain",
                    2));

            questionRepo.save(new Question(category,
                    "Welches Schlüsselwort wird zur Vererbung genutzt?",
                    "implements",
                    "extends",
                    "inherit",
                    "join",
                    2));

            questionRepo.save(new Question(category,
                    "Welches Schlüsselwort wird zum Implementieren eines Interfaces genutzt?",
                    "interface",
                    "join",
                    "extends",
                    "implements",
                    4));

            questionRepo.save(new Question(category,
                    "Welches Schlüsselwort ist für eine Methode ohne Rückgabewert korrekt?",
                    "void",
                    "nothing",
                    "null",
                    "empty",
                    1));

            questionRepo.save(new Question(category,
                    "Was ist Spring?",
                    "Baukasten",
                    "Framework",
                    "Anleitung",
                    "Frühling",
                    2));

            questionRepo.save(new Question(category,
                    "Was ist ein Beispiel für eine statische Methode in Java?",
                    "public void printName()",
                    "public static int addNumbers(int x, int y)",
                    "public String getAddress()",
                    "public static String getCurrentDate()",
                    4));

            questionRepo.save(new Question(category,
                    "Welches Schlüsselwort wird verwendet, um eine Methode zu überschreiben?",
                    "override",
                    "overwrite",
                    "redefine",
                    "@override",
                    4));
        }
    }

    void patterns() {
        if (categoryRepo.findByName("Patterns") == null) {
            Category category = categoryRepo
                    .save(new Category("Patterns", "Fragen zu Patterns"));

            questionRepo.save(new Question(category,
                    "Welches Muster wird verwendet, um die Erstellung von Objekten zu vereinfachen?",
                    "Observer-Muster",
                    "Singleton-Muster",
                    "Factory-Muster",
                    "Adapter-Muster",
                    3));

            questionRepo.save(new Question(category,
                    "Welches Muster wird verwendet, um die Kommunikation zwischen unterschiedlichen Klassen zu ermöglichen?",
                    "Observer-Muster",
                    "Singleton-Muster",
                    "Factory-Muster",
                    "Adapter-Muster",
                    4));

            questionRepo.save(new Question(category,
                    "Welches Muster wird verwendet, um die Anzahl der Objekte einer bestimmten Klasse zu begrenzen?",
                    "Observer-Muster",
                    "Singleton-Muster",
                    "Pool-Muster",
                    "Adapter-Muster",
                    2));
        }
    }

    void framework() {
        if (categoryRepo.findByName("Frameworks") == null) {
            Category category = categoryRepo
                    .save(new Category("Frameworks", "Fragen zu Frameworks"));

            questionRepo.save(new Question(category,
                    "Welches Framework wird hauptsächlich für die Entwicklung von Desktop-Anwendungen verwendet?",
                    "ReactJS",
                    "AngularJS",
                    "Electron",
                    "Node.js",
                    3));

            questionRepo.save(new Question(category,
                    "Welches Framework wird hauptsächlich für die Entwicklung von Machine-Learning-Modellen verwendet?",
                    "TensorFlow",
                    "AngularJS",
                    "Spring Framework",
                    "Keras",
                    1));

            questionRepo.save(new Question(category,
                    "Welches Framework wird hauptsächlich für die Entwicklung von künstlicher Intelligenz verwendet?",
                    "TensorFlow",
                    "AngularJS",
                    "Spring Framework",
                    "PyTorch",
                    4));
        }
    }

    void python() {
        if (categoryRepo.findByName("Python") == null) {
            Category category = categoryRepo
                    .save(new Category("Python", "Fragen zu Python"));

            questionRepo.save(new Question(category,
                    "Welche Funktion in Python verwendet man, um einen String in eine Zahl umzuwandeln?",
                    "to_string()",
                    "to_int()",
                    "int()",
                    "parse_int()",
                    3));

            questionRepo.save(new Question(category,
                    "Welche Funktion in Python verwendet man, um eine Liste von Strings in eine Liste von Integers umzuwandeln?",
                    "to_integers()",
                    "int_list()",
                    "list(map(int, list_of_strings))",
                    "parse_int_list()",
                    3));

            questionRepo.save(new Question(category,
                    "Welche Bibliothek in Python verwendet man, um CSV-Dateien zu lesen und zu schreiben?",
                    "pandas",
                    "xlrd",
                    "openpyxl",
                    "csv",
                    4));

            questionRepo.save(new Question(category,
                    "Welche Funktion in Python verwendet man, um einen String in Großbuchstaben umzuwandeln?",
                    "upper()",
                    "to_uppercase()",
                    "capitalize()",
                    "to_upper()",
                    1));

            questionRepo.save(new Question(category,
                    "Welche Funktion in Python verwendet man, um die Länge einer Liste zu erhalten?",
                    "length()",
                    "size()",
                    "len()",
                    "count()",
                    3));

            questionRepo.save(new Question(category,
                    "Welche Bibliothek in Python verwendet man, um mit Datenbanken zu arbeiten?",
                    "sqlite3",
                    "pandas",
                    "openpyxl",
                    "csv",
                    1));

            questionRepo.save(new Question(category,
                    "Welche Funktion in Python verwendet man, um eine Liste zu sortieren?",
                    "sort()",
                    "order()",
                    "sorted()",
                    "organize()",
                    1));

            questionRepo.save(new Question(category,
                    "Welche Bibliothek in Python verwendet man, um mit regulären Ausdrücken zu arbeiten?",
                    "re",
                    "string",
                    "os",
                    "json",
                    1));

            questionRepo.save(new Question(category,
                    "Welche Bibliothek in Python verwendet man, um HTTP-Requests zu senden?",
                    "http",
                    "requests",
                    "os",
                    "json",
                    2));

            questionRepo.save(new Question(category,
                    "Welche Bibliothek in Python verwendet man, um mit JSON-Daten zu arbeiten?",
                    "json",
                    "http",
                    "os",
                    "requests",
                    1));
        }
    }
}
