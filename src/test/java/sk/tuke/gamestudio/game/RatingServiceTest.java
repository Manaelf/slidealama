package sk.tuke.gamestudio.game;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingException;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.RatingServiceJDBC;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class RatingServiceTest {
    private final RatingService ratingService = new RatingServiceJDBC();

    @Test
    public void testSetAndGetRating() throws RatingException {
        ratingService.reset();
        Rating rating = new Rating("SlideaLama", "User1", 5, new Date());
        ratingService.setRating(rating);

        assertEquals(5, ratingService.getRating("SlideaLama", "User1"));
    }

    @Test
    public void testAverageRating() throws RatingException {
        ratingService.reset();
        ratingService.setRating(new Rating("SlideaLama", "User1", 3, new Date()));
        ratingService.setRating(new Rating("SlideaLama", "User2", 5, new Date()));

        // Среднее между 3 и 5 должно быть 4
        assertEquals(4, ratingService.getAverageRating("SlideaLama"));
    }
}