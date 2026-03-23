package sk.tuke.gamestudio.game;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.CommentException;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.CommentServiceJDBC;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommentServiceTest {
    private final CommentService commentService = new CommentServiceJDBC();

    @Test
    public void testAddComment() throws CommentException {
        commentService.reset();
        Comment comment = new Comment("SlideaLama", "UserA", "Great game!", new Date());
        commentService.addComment(comment);

        List<Comment> comments = commentService.getComments("SlideaLama");
        assertEquals(1, comments.size());
        assertEquals("Great game!", comments.get(0).getComment());
    }
}