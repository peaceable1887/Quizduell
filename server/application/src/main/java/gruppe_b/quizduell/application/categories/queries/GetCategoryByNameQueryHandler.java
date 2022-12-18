package gruppe_b.quizduell.application.categories.queries;

import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.categories.CategoryRepository;
import gruppe_b.quizduell.application.interfaces.RequestHandler;
import gruppe_b.quizduell.domain.entities.Category;

/**
 * QueryHandler für eine Kategorie.
 * 
 * @author Christopher Burmeister
 */
@Service
public class GetCategoryByNameQueryHandler implements RequestHandler<GetCategoryByNameQuery, Category> {

    private CategoryRepository repo;

    public GetCategoryByNameQueryHandler(CategoryRepository repo) {
        this.repo = repo;
    }

    public Category handle(GetCategoryByNameQuery query) {
        return repo.findByName(query.name);
    }
}
