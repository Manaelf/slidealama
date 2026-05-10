package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.SlideaLama.core.BlockType;
import sk.tuke.gamestudio.game.SlideaLama.core.Field;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class SlideaLamaController {

    private static final String GAME_NAME = "SlideaLama";

    private Field field = new Field(5, 5);

    // имя игрока запоминаем на сессию
    private String currentPlayer = "";

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    @RequestMapping("/slidealama")
    public String slidealama(
            @RequestParam(value = "command",  required = false) String command,
            @RequestParam(value = "player",   required = false) String player,
            @RequestParam(value = "comment",  required = false) String comment,
            @RequestParam(value = "rating",   required = false) String rating,
            Model model) {

        // запоминаем имя игрока если пришло
        if (player != null && !player.trim().isEmpty()) {
            currentPlayer = player.trim();
        }

        // новая игра
        if ("new".equals(command)) {
            field = new Field(5, 5);
        }

        // ход если игра не закончена
        if (command != null && !command.equals("new") && !field.isGameOver()) {
            field.pushBlock(command);
        }

        // сохранение скора при конце игры
        if (field.isGameOver() && !currentPlayer.isEmpty()) {
            try {
                int winnerScore = Math.max(field.getScoreP1(), field.getScoreP2());
                scoreService.addScore(new Score(GAME_NAME, currentPlayer, winnerScore, new Date()));
            } catch (Exception e) {
                model.addAttribute("scoreError", e.getMessage());
            }
        }

        // сохранение комментария
        if (comment != null && !comment.trim().isEmpty() && !currentPlayer.isEmpty()) {
            try {
                commentService.addComment(new Comment(GAME_NAME, currentPlayer, comment.trim(), new Date()));
            } catch (Exception e) {
                model.addAttribute("commentError", e.getMessage());
            }
        }

        // сохранение рейтинга
        if (rating != null && !currentPlayer.isEmpty()) {
            try {
                int ratingValue = Integer.parseInt(rating);
                if (ratingValue >= 1 && ratingValue <= 5) {
                    ratingService.setRating(new Rating(GAME_NAME, currentPlayer, ratingValue, new Date()));
                }
            } catch (Exception e) {
                model.addAttribute("ratingError", e.getMessage());
            }
        }

        // доска
        List<List<String>> board = new ArrayList<>();
        for (int r = 0; r < 5; r++) {
            List<String> row = new ArrayList<>();
            for (int c = 0; c < 5; c++) {
                row.add(getSymbol(field.getBlock(r, c)));
            }
            board.add(row);
        }

        // топ скоры
        List<Score> topScores = new ArrayList<>();
        try { topScores = scoreService.getTopScores(GAME_NAME); } catch (Exception ignored) {}

        // комментарии
        List<Comment> comments = new ArrayList<>();
        try { comments = commentService.getComments(GAME_NAME); } catch (Exception ignored) {}

        // средний рейтинг
        int avgRating = 0;
        try { avgRating = ratingService.getAverageRating(GAME_NAME); } catch (Exception ignored) {}

        // рейтинг текущего игрока
        int playerRating = 0;
        if (!currentPlayer.isEmpty()) {
            try { playerRating = ratingService.getRating(GAME_NAME, currentPlayer); } catch (Exception ignored) {}
        }

        model.addAttribute("board",        board);
        model.addAttribute("scoreP1",      field.getScoreP1());
        model.addAttribute("scoreP2",      field.getScoreP2());
        model.addAttribute("gameOver",     field.isGameOver());
        model.addAttribute("state",        field.getState());
        model.addAttribute("nextBlock",    getSymbol(field.getNextBlock()));
        model.addAttribute("currentPlayer", currentPlayer);
        model.addAttribute("topScores",    topScores);
        model.addAttribute("comments",     comments);
        model.addAttribute("avgRating",    avgRating);
        model.addAttribute("playerRating", playerRating);

        return "slidealama";
    }

    private String getSymbol(BlockType type) {
        if (type == null) return "";
        switch (type) {
            case LAMA:  return "lama";
            case SUN:   return "sun";
            case MOON:  return "moon";
            case FRUIT: return "fruit";
            case SNAKE: return "snake";
            case BELL:  return "bell";
            default:    return "";
        }
    }
}