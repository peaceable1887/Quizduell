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

    public void Seed() {
        if (categoryRepo.findAll().isEmpty()) {
            Category category = categoryRepo
                    .save(new Category("Java", "Fragen zur Programmiersprache Java"));

            questionRepo.save(new Question(category.getId(),
                    "Was ist Java?",
                    "Eine Programmiersprache ohne Objektorientierung",
                    "Eine Firma die Joghurt produziert",
                    "Eine Objekt-Orientierte Programmiersprache",
                    "Ein Planet der nicht bewohnbar ist",
                    3));

            questionRepo.save(new Question(category.getId(),
                    "Welchen Namen hatte Java ursprünglich?",
                    "Oak",
                    "Spruce",
                    "Latte",
                    "Chai",
                    1));

            questionRepo.save(new Question(category.getId(),
                    "Wie heißt der korrekte Datentyp für eine Zeichenkette?",
                    "string",
                    "String",
                    "str",
                    "charchain",
                    2));

            questionRepo.save(new Question(category.getId(),
                    "Welches Schlüsselwort wird zur Vererbung genutzt?",
                    "implements",
                    "extends",
                    "inherit",
                    "join",
                    2));

            questionRepo.save(new Question(category.getId(),
                    "Welches Schlüsselwort wird zum Implementieren eines Interfaces genutzt?",
                    "interface",
                    "join",
                    "extends",
                    "implements",
                    4));

            questionRepo.save(new Question(category.getId(),
                    "Welches Schlüsselwort ist für eine Methode ohne Rückgabewert korrekt?",
                    "void",
                    "nothing",
                    "null",
                    "empty",
                    1));

            questionRepo.save(new Question(category.getId(),
                    "Was ist Spring?",
                    "Baukasten",
                    "Framework",
                    "Anleitung",
                    "Frühling",
                    2));
        }
    }
}
