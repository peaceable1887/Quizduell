package gruppe_b.quizduell.lobbyserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

@SpringBootTest
@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
class LobbyserverApplicationTests {

	@Test
	void contextLoads() {
	}

}
