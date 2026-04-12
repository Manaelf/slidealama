package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.ScoreException;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoreServiceRest {

    @Autowired
    private ScoreService scoreService;

    @GetMapping("/{game}")
    public List<Score> getTopScores(@PathVariable String game) {
        try {
            return scoreService.getTopScores(game);
        } catch (ScoreException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    public void addScore(@RequestBody Score score) {
        try {
            scoreService.addScore(score);
        } catch (ScoreException e) {
            throw new RuntimeException(e);
        }
    }
}