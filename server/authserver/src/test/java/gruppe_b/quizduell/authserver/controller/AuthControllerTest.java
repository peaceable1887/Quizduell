package gruppe_b.quizduell.authserver.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.MimeType;

import com.nimbusds.jose.shaded.json.JSONObject;

import gruppe_b.quizduell.authserver.common.UserCredentialsDto;
import gruppe_b.quizduell.authserver.config.SecurityConfig;
import gruppe_b.quizduell.authserver.service.TokenService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.core.Is;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Import({ SecurityConfig.class, TokenService.class })
public class AuthControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void whenAuthenticatedThenSaysHelloUser() throws Exception {
        // Arrange
        MvcResult result = this.mvc.perform(post("/token")
                .with(httpBasic("john", "password")))
                .andExpect(status().isCreated())
                .andReturn();

        String token = result.getResponse().getContentAsString();

        // Act & Assert
        this.mvc.perform(get("/")
                .header("Authorization", "Bearer " + token))
                .andExpect(content().string("Hello, john"));
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

        MvcResult result = this.mvc.perform(post("/token")
                .with(httpBasic("john", "password")))
                .andExpect(status().isCreated())
                .andReturn();

        String token = result.getResponse().getContentAsString();

        // Assert
        assertNotNull(token);
        assertEquals(460, token.length());
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