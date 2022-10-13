package gruppe_b.quizduell.application.user.queries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.user.UserRepository;
import gruppe_b.quizduell.domain.entities.User;

@Service
public class GetUserDetailQueryHandler {

    @Autowired
    private UserRepository repo;

    public User handle(GetUserDetailQuery query) {
        return repo.findByName(query.name);
    }
}
