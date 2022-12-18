package gruppe_b.quizduell.application.categories.queries;

/**
 * Query zum Laden einer Kategorie, anhand des Namens. Wird an einen
 * QueryHandler übergeben.
 */
public class GetCategoryByNameQuery {

    public final String name;

    public GetCategoryByNameQuery(String name) {
        this.name = name;
    }
}
