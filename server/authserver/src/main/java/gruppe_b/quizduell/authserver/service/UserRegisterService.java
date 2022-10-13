package gruppe_b.quizduell.authserver.service;

import gruppe_b.quizduell.authserver.common.UserCredentialsDto;

public interface UserRegisterService {
    void saveUser(UserCredentialsDto userCredentialsDto);
}
