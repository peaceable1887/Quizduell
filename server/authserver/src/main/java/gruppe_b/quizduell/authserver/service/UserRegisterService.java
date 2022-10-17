package gruppe_b.quizduell.authserver.service;

import gruppe_b.quizduell.authserver.common.UserCredentialsDto;

/**
 * Interface für einen Service zum Erstellen eines neuen User's.
 * 
 * @author Christopher Burmeister
 */
public interface UserRegisterService {
    void saveUser(UserCredentialsDto userCredentialsDto);
}
