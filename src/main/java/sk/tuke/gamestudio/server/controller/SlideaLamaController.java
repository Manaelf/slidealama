package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.SlideaLama.core.BlockType;
import sk.tuke.gamestudio.game.SlideaLama.core.Field;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Date;
import java.util.List;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class SlideaLamaController {

    private static final String GAME_NAME = "SlideaLama";

    private Field field = new Field(5, 5);

    @Autowired
    private ScoreService scoreService;

    @RequestMapping("/slidealama")
    public String slidealama(
            @RequestParam(value = "command", required = false) String command,
            @RequestParam(value = "player", required = false) String player,
            Model model) {

        if (command != null && !field.isGameOver()) {
            field.pushBlock(command);
        }

        if (field.isGameOver() && player != null && !player.trim().isEmpty()) {
            try {
                int winnerScore = Math.max(field.getScoreP1(), field.getScoreP2());
                scoreService.addScore(new Score(GAME_NAME, player.trim(), winnerScore, new Date()));
            } catch (Exception e) {
                model.addAttribute("error", "Could not save score: " + e.getMessage());
            }
        }

        if ("new".equals(command)) {
            field = new Field(5, 5);
        }

        model.addAttribute("field", field);
        model.addAttribute("scoreP1", field.getScoreP1());
        model.addAttribute("scoreP2", field.getScoreP2());
        model.addAttribute("gameOver", field.isGameOver());
        model.addAttribute("state", field.getState());
        model.addAttribute("nextBlock", getSymbol(field.getNextBlock()));

        String[][] board = new String[5][5];
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                board[r][c] = getSymbol(field.getBlock(r, c));
            }
        }
        model.addAttribute("board", board);

        // Топ скоры
        try {
            List<Score> topScores = scoreService.getTopScores(GAME_NAME);
            model.addAttribute("topScores", topScores);
        } catch (Exception e) {
            model.addAttribute("topScores", List.of());
        }

        return "slidealama";
    }

    private String getSymbol(BlockType type) {
        if (type == null) return "";
        switch (type) {
            case LAMA:  return "L";
            case SUN:   return "S";
            case MOON:  return "M";
            case FRUIT: return "F";
            case SNAKE: return "X";
            case BELL:  return "B";
            default:    return "?";
        }
    }
}