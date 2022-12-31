package gruppe_b.quizduell.application.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.interfaces.RequestHandler;
import gruppe_b.quizduell.application.user.commands.update_user.UpdateUserCommand;
import gruppe_b.quizduell.application.user.queries.get_details.GetUserDetailByUUIDQuery;
import gruppe_b.quizduell.application.user.queries.get_details.GetUserDetailQuery;
import gruppe_b.quizduell.application.common.UserDetailsDto;
import gruppe_b.quizduell.application.common.UserDetailsUpdateDto;
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

    @Autowired
    RequestHandler<UpdateUserCommand, User> updateUserHandler;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserDetailsDto getUserDetailsByUUID(UUID id) {
        return new UserDetailsDto(getUserByUUID(id));
    }

    public UserDetailsDto getUserDetailsByName(String name) {
        return new UserDetailsDto(getUserByName(name));
    }

    public UserDetailsDto updateUserDetailsByUUID(UUID id, UserDetailsUpdateDto dto) {
        return new UserDetailsDto(updateUserByUUID(id, dto));
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

    public User updateUserByUUID(UUID id, UserDetailsUpdateDto dto) {
        User user = getUserByUUID(id);

        if (dto.name != null && !dto.name.equals("")) {
            user.setName(dto.name);
        }

        if (dto.mail != null && !dto.mail.equals("")) {
            user.setMail(dto.mail);
        }

        if (dto.password != null && !dto.password.equals("")) {
            user.setPasswordHash(passwordEncoder.encode(dto.password));
        }

        return updateUserHandler.handle(new UpdateUserCommand(user));
    }
}
