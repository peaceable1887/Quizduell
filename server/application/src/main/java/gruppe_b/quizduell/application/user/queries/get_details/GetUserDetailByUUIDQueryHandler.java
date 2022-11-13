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
public class GetUserDetailByUUIDQueryHandler implements RequestHandler<GetUserDetailByUUIDQuery, User> {

    private UserRepository repo;

    public GetUserDetailByUUIDQueryHandler(UserRepository repo) {
        this.repo = repo;
    }

    public User handle(GetUserDetailByUUIDQuery query) {
        return repo.findByUUID(query.id);
    }
}
