package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Rating;

public class RatingServiceRestClient implements RatingService {

    private final String url = "http://localhost:8080/api/rating";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void setRating(Rating rating) throws RatingException {
        restTemplate.postForEntity(url, rating, Rating.class);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        Integer result = restTemplate.getForEntity(url + "/average/" + game, Integer.class).getBody();
        return result == null ? 0 : result;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        Integer result = restTemplate.getForEntity(url + "/" + game + "/" + player, Integer.class).getBody();
        return result == null ? 0 : result;
    }

    @Override
    public void reset() throws RatingException {
        throw new UnsupportedOperationException("Not supported via REST");
    }
}
