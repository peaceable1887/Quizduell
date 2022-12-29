package gruppe_b.quizduell.lobbyserver.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import gruppe_b.quizduell.lobbyserver.common.LobbyHelper;

@SpringBootTest()
@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
public class LobbyServiceTests {

    @Autowired
    LobbyHelper lobbyHelper;

    @Test
    void multithreadTest() throws Exception {
        // Arrange
        int threadCount = 100;
        int threadLoopCount = 10_000;

        ExecutorService executor = Executors.newFixedThreadPool(100);
        Collection<Callable<Void>> tasks = new ArrayList<>();

        LobbyService lobbyService = lobbyHelper.getLobbyService();

        // Act
        for (int i = 0; i < threadCount; i++) {
            Callable<Void> task = () -> {
                for (int j = 0; j < threadLoopCount; j++) {
                    lobbyService.createLobby(UUID.randomUUID(), String.valueOf(j), "");
                }
                return null;
            };

            tasks.add(task);
        }

        executor.invokeAll(tasks);

        // Assert
        assertEquals(threadCount * threadLoopCount, lobbyService.getAllLobbies().size());
    }
}
