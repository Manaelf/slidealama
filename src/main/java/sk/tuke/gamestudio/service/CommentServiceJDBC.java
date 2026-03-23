package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Comment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentServiceJDBC implements CommentService {
    public static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    public static final String USER = "postgres";
    public static final String PASSWORD = "2GA7&nyK";

    private static final String INSERT = "INSERT INTO comment (game, player, comment, commentedon) VALUES (?, ?, ?, ?)";
    private static final String SELECT = "SELECT game, player, comment, commentedon FROM comment WHERE game = ? ORDER BY commentedon DESC";
    private static final String DELETE = "DELETE FROM comment";

    @Override
    public void addComment(Comment comment) throws CommentException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(INSERT)) {
            ps.setString(1, comment.getGame());
            ps.setString(2, comment.getPlayer());
            ps.setString(3, comment.getComment());
            ps.setTimestamp(4, new Timestamp(comment.getCommentedOn().getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new CommentException("Error adding comment", e);
        }
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(SELECT)) {
            ps.setString(1, game);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    comments.add(new Comment(rs.getString(1), rs.getString(2), rs.getString(3), rs.getTimestamp(4)));
                }
            }
        } catch (SQLException e) {
            throw new CommentException("Error getting comments", e);
        }
        return comments;
    }

    @Override
    public void reset() throws CommentException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new CommentException("Error deleting comments", e);
        }
    }
}