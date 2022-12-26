package gruppe_b.quizduell.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.interfaces.RequestHandler;
import gruppe_b.quizduell.application.user.queries.get_details.GetUserDetailQuery;
import gruppe_b.quizduell.domain.entities.User;

/**
 * Implementierung des Interfaces UserDetailsService für die Authentifizierung
 * mit Spring Security.
 * 
 * @author Christopher Burmeister
 */
@Service("UserDetailsService")
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    RequestHandler<GetUserDetailQuery, User> getUserHandler;

    UserDetails userDetails;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserHandler.handle(new GetUserDetailQuery(username));
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImp(user);
    }
}
