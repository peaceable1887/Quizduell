package gruppe_b.quizduell.statsserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class StatsserverApplicationTests {

    @Test
    void contextLoads() {
    }

}
