package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        try {
            List<Rating> existing = entityManager.createNamedQuery("Rating.getRating", Rating.class)
                    .setParameter("game", rating.getGame())
                    .setParameter("player", rating.getPlayer())
                    .getResultList();

            if (!existing.isEmpty()) {
                Rating r = existing.get(0);
                r.setRating(rating.getRating());
                r.setRatedOn(rating.getRatedOn());
                entityManager.merge(r);
            } else {
                entityManager.persist(rating);
            }
        } catch (Exception e) {
            throw new RatingException("Error setting rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try {
            Double avg = (Double) entityManager.createNamedQuery("Rating.getAverageRating")
                    .setParameter("game", game)
                    .getSingleResult();
            return avg == null ? 0 : (int) Math.round(avg);
        } catch (NoResultException e) {
            return 0;
        } catch (Exception e) {
            throw new RatingException("Error getting average rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try {
            List<Rating> result = entityManager.createNamedQuery("Rating.getRating", Rating.class)
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .getResultList();
            return result.isEmpty() ? 0 : result.get(0).getRating();
        } catch (Exception e) {
            throw new RatingException("Error getting rating", e);
        }
    }

    @Override
    public void reset() throws RatingException {
        try {
            entityManager.createNamedQuery("Rating.resetRatings").executeUpdate();
        } catch (Exception e) {
            throw new RatingException("Error resetting ratings", e);
        }
    }
}
