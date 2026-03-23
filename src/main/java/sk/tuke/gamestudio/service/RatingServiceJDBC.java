package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;
import java.sql.*;


public class RatingServiceJDBC implements RatingService {
    private static final String DELETE_RATINGS = "DELETE FROM rating";
    public static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    public static final String USER = "postgres";
    public static final String PASSWORD = "2GA7&nyK";

    private static final String INSERT = "INSERT INTO rating (game, player, rating, ratedon) VALUES (?, ?, ?, ?)";
    private static final String SELECT_AVG = "SELECT CAST(AVG(rating) AS INTEGER) FROM rating WHERE game = ?";
    private static final String SELECT_SINGLE = "SELECT rating FROM rating WHERE game = ? AND player = ?";

    @Override
    public void setRating(Rating rating) throws RatingException {
        // Simple implementation: delete old rating if exists, then insert new one
        // (Or just INSERT if your DB schema doesn't enforce uniqueness)
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(INSERT)) {
            ps.setString(1, rating.getGame());
            ps.setString(2, rating.getPlayer());
            ps.setInt(3, rating.getRating());
            ps.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RatingException("Error setting rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(SELECT_AVG)) {
            ps.setString(1, game);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Error getting average rating", e);
        }
        return 0;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(SELECT_SINGLE)) {
            ps.setString(1, game);
            ps.setString(2, player);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Error getting player rating", e);
        }
        return 0;
    }

    public void reset() throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            // Выполняем удаление всех записей из таблицы rating
            statement.executeUpdate(DELETE_RATINGS);

        } catch (SQLException e) {
            throw new RatingException("Error resetting ratings in database", e);
        }
    }
}