package gruppe_b.quizduell.authserver.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.interfaces.RequestHandler;
import gruppe_b.quizduell.application.user.queries.get_details.GetUserDetailByUUIDQuery;
import gruppe_b.quizduell.authserver.common.UserDetailsDto;
import gruppe_b.quizduell.authserver.common.UserJwtDto;
import gruppe_b.quizduell.domain.entities.User;

/**
 * Service zum Verwalten von Usern.
 * 
 * @author Christopher Burmeister
 */
@Service
public class UserService {

    @Autowired
    RequestHandler<GetUserDetailByUUIDQuery, User> getUserHandler;

    public UserDetailsDto getUserByUUID(UUID id) {
        User user = getUserHandler.handle(new GetUserDetailByUUIDQuery(id));
        if (user == null) {
            throw new UsernameNotFoundException(id.toString());
        }

        return new UserDetailsDto(user);
    }
}
