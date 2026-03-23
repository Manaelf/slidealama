package sk.tuke.gamestudio.game;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.ScoreException;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.service.ScoreServiceJDBC;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreServiceTest {
    private final ScoreService scoreService = new ScoreServiceJDBC();

    @Test
    public void testReset() throws ScoreException {
        scoreService.addScore(new Score("SlideaLama", "TestUser", 100, new Date()));
        scoreService.reset();
        List<Score> scores = scoreService.getTopScores("SlideaLama");
        assertEquals(0, scores.size(), "Table should be empty after reset");
    }

    @Test
    public void testAddScore() throws ScoreException {
        scoreService.reset();
        Date date = new Date();
        Score score = new Score("SlideaLama", "Player1", 500, date);
        scoreService.addScore(score);

        List<Score> scores = scoreService.getTopScores("SlideaLama");
        assertEquals(1, scores.size());
        assertEquals("Player1", scores.get(0).getPlayer());
        assertEquals(500, scores.get(0).getPoints());
    }
}