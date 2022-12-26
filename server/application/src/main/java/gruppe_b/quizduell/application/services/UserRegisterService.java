package gruppe_b.quizduell.application.services;

import gruppe_b.quizduell.application.common.UserCredentialsDto;

/**
 * Interface für einen Service zum Erstellen eines neuen User's.
 * 
 * @author Christopher Burmeister
 */
public interface UserRegisterService {
    void saveUser(UserCredentialsDto userCredentialsDto);
}
