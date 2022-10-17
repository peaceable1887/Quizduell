package gruppe_b.quizduell.application.user.queries.get_details;

import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.interfaces.RequestHandler;
import gruppe_b.quizduell.application.user.UserRepository;
import gruppe_b.quizduell.domain.entities.User;

/**
 * QueryHandler für einen User.
 * 
 * @author Christopher Burmeister
 */
@Service
public class GetUserDetailQueryHandler implements RequestHandler<GetUserDetailQuery, User> {

    private UserRepository repo;

    public GetUserDetailQueryHandler(UserRepository repo) {
        this.repo = repo;
    }

    public User handle(GetUserDetailQuery query) {
        return repo.findByName(query.name);
    }
}
