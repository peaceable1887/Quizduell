package gruppe_b.quizduell.application.questions.queries;

import java.util.List;
import java.util.UUID;

/**
 * Objekt zum Anfragen einer Zufallsfrage.
 * Bekommt eine Liste übergeben mit Fragen die nicht mehr zurückgegeben werden
 * sollen.
 * 
 * @author Christopher Burmeister
 */
public class GetQuestionRandomQuery {

    private final List<UUID> excludeQuestions;

    public GetQuestionRandomQuery(List<UUID> excludeQuestions) {
        this.excludeQuestions = excludeQuestions;
    }

    public List<UUID> getExcludeQuestions() {
        return this.excludeQuestions;
    }
}
