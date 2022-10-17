package gruppe_b.quizduell.authserver.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.nimbusds.jose.shaded.json.JSONObject;

import gruppe_b.quizduell.application.interfaces.RequestHandler;
import gruppe_b.quizduell.application.user.commands.create_user.CreateUserCommand;
import gruppe_b.quizduell.domain.entities.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

        @Autowired
        MockMvc mvc;

        @Autowired
        PasswordEncoder passwordEncoder;

        @Autowired
        RequestHandler<CreateUserCommand, User> createUserHandler;

        @Test
        void whenAuthenticatedThenSaysHelloUser() throws Exception {
                // Arrange
                String endcodePswd = passwordEncoder.encode("password");

                CreateUserCommand command = new CreateUserCommand(
                                "john",
                                "test@test.de",
                                endcodePswd,
                                "salt");
                createUserHandler.handle(command);

                MvcResult result = this.mvc.perform(get("/token")
                                .with(httpBasic("john", "password")))
                                .andExpect(status().isOk())
                                .andReturn();

                String token = result.getResponse().getContentAsString();

                // Act & Assert
                this.mvc.perform(get("/")
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isOk())
                                .andExpect(content().string(containsString("Hello, ")))
                                .andReturn();
        }

        @Test
        void whenRegisterThenRegistered() throws Exception {
                // Arrange
                JSONObject jObject = new JSONObject();
                jObject.put("name", "RegisterTestName");
                jObject.put("password", "password");

                // Act & Assert
                this.mvc.perform(post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jObject.toJSONString()))
                                .andExpect(status().isCreated());
        }

        @Test
        void whenRegisteredThenUserGetToken() throws Exception {
                // Arrange
                JSONObject jObject = new JSONObject();
                jObject.put("name", "RegisterTestName2");
                jObject.put("password", "password");

                // Act
                this.mvc.perform(post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jObject.toJSONString()));

                MvcResult result = this.mvc.perform(get("/token")
                                .with(httpBasic("RegisterTestName2", "password")))
                                .andExpect(status().isOk())
                                .andReturn();

                String token = result.getResponse().getContentAsString();

                // Assert
                assertNotNull(token);
                assertEquals(524, token.length());
        }

        @Test
        void whenRegisterWithEmptyNameWhenFail() throws Exception {
                // Arrange
                JSONObject jObject = new JSONObject();
                jObject.put("name", "");
                jObject.put("password", "password");

                // Act & Assert
                this.mvc.perform(post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jObject.toJSONString()))
                                .andExpect(status().isBadRequest());
                // .andExpect(jsonPath("$.name", Is.is("Name is mandatory")));
        }
}