package gruppe_b.quizduell.authserver.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import gruppe_b.quizduell.application.user.queries.get_details.GetUserDetailQuery;
import gruppe_b.quizduell.application.user.queries.get_details.GetUserDetailQueryHandler;
import gruppe_b.quizduell.authserver.common.UserCredentialsDto;
import gruppe_b.quizduell.authserver.service.UserRegisterService;

@SpringBootTest
public class UserRegisterServiceTest {

    static {
        System.setProperty("DB_PORT", "3306");
        System.setProperty("DB_HOSTNAME", "localhost");
        System.setProperty("DB_USERNAME", "root");
        System.setProperty("DB_PASSWORD", "root");
    }

    @Autowired
    UserRegisterService userRegisterService;

    @Autowired
    GetUserDetailQueryHandler getUserDetailQueryHandler;

    @Test
    void whenRegisterTwoUsersWithEmptyStringAsMailThenSaveBothInDb() {
        // Arrange
        UserCredentialsDto johnDto = new UserCredentialsDto();
        johnDto.name = "John";
        johnDto.password = "123";
        johnDto.mail = "";

        UserCredentialsDto janeDto = new UserCredentialsDto();
        janeDto.name = "Jane";
        janeDto.password = "123";
        janeDto.mail = "";

        // Act
        userRegisterService.saveUser(johnDto);
        userRegisterService.saveUser(janeDto);

        // Assert
        assertNull(getUserDetailQueryHandler.handle(new GetUserDetailQuery(johnDto.name)).getMail());
        assertNull(getUserDetailQueryHandler.handle(new GetUserDetailQuery(janeDto.name)).getMail());
    }
}
