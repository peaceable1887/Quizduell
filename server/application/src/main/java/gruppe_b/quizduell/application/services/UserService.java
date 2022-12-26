package gruppe_b.quizduell.application.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.interfaces.RequestHandler;
import gruppe_b.quizduell.application.user.queries.get_details.GetUserDetailByUUIDQuery;
import gruppe_b.quizduell.application.user.queries.get_details.GetUserDetailQuery;
import gruppe_b.quizduell.application.common.UserDetailsDto;
import gruppe_b.quizduell.domain.entities.User;

/**
 * Service zum Verwalten von Usern.
 * 
 * @author Christopher Burmeister
 */
@Service
public class UserService {

    @Autowired
    RequestHandler<GetUserDetailByUUIDQuery, User> getUserByUUIDHandler;

    @Autowired
    RequestHandler<GetUserDetailQuery, User> getUserHandler;

    public UserDetailsDto getUserDetailsByUUID(UUID id) {
        return new UserDetailsDto(getUserByUUID(id));
    }

    public UserDetailsDto getUserDetailsByName(String name) {
        return new UserDetailsDto(getUserByName(name));
    }

    public User getUserByUUID(UUID id) {
        User user = getUserByUUIDHandler.handle(new GetUserDetailByUUIDQuery(id));
        if (user == null) {
            throw new UsernameNotFoundException(id.toString());
        }
        return user;
    }

    public User getUserByName(String name) {
        User user = getUserHandler.handle(new GetUserDetailQuery(name));
        if (user == null) {
            throw new UsernameNotFoundException(name);
        }

        return user;
    }
}
