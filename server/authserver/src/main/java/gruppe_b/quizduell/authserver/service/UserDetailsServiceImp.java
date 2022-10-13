package gruppe_b.quizduell.authserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.user.queries.GetUserDetailQuery;
import gruppe_b.quizduell.application.user.queries.GetUserDetailQueryHandler;
import gruppe_b.quizduell.domain.entities.User;

@Service("UserDetailsService")
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    GetUserDetailQueryHandler getUserHandler;

    UserDetails userDetails;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserHandler.handle(new GetUserDetailQuery(username));
        return new UserDetailsImp(user);
    }
}
