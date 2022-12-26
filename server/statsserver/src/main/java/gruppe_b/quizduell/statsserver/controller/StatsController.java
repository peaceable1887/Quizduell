package gruppe_b.quizduell.statsserver.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gruppe_b.quizduell.application.exceptions.PlayerStatsNotFoundException;
import gruppe_b.quizduell.application.services.StatsService;
import gruppe_b.quizduell.domain.entities.PlayerStats;
import gruppe_b.quizduell.statsserver.common.StatsRequest;

/**
 * Rest-Controller zum Abrufen der Statistiken.
 * 
 * @author Christopher Burmeister
 */
@RestController
@RequestMapping("/v1")
public class StatsController {

    private static final Logger logger = LoggerFactory.getLogger(StatsController.class);

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/get")
    public ResponseEntity<PlayerStats> get(@RequestBody StatsRequest request) throws PlayerStatsNotFoundException {
        PlayerStats stats;
        try {
            stats = statsService.getStatsByUserId(request.playerId);
        } catch (PlayerStatsNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(stats);
    }
}
