package object.paddle;

import game.GameConstants;
import javafx.scene.canvas.Canvas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestPaddle {
    private Paddle paddle;
    private Canvas canvas;

    @BeforeEach
    public void setUp() {
        canvas = new Canvas(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        paddle = new Paddle((GameConstants.SCREEN_WIDTH - GameConstants.PADDLE_WIDTH) / 2.0,
                GameConstants.SCREEN_HEIGHT - 100, GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT);
    }

    // test1: initial value
    @Test
    void testInitialValues() {
        assertEquals((GameConstants.SCREEN_WIDTH - GameConstants.PADDLE_WIDTH) / 2.0, paddle.getX(), 0.001);
        assertEquals(GameConstants.SCREEN_HEIGHT - 100, paddle.getY(), 0.001);
        assertEquals(GameConstants.PADDLE_WIDTH, paddle.getWidth(), 0.001);
        assertEquals(GameConstants.PADDLE_HEIGHT, paddle.getHeight(), 0.001);
        assertEquals(0, paddle.getDx(), 0.001);
    }

    // test2: check move
    @Test
    void testMove() {
        // left
        paddle.moveLeft();
        assertTrue(paddle.getDx() < 0);

        // right
        paddle.moveRight();
        assertTrue(paddle.getDx() > 0);

        // stop
        paddle.stop();
        assertEquals(0, paddle.getDx(), 0.001);
    }

    // test3: check wall collision
    @Test
    void testCollisionWall() {
        // left
        paddle.setX(-10);
        paddle.checkCollisionWall(canvas);
        assertEquals(0, paddle.getX(), 0.001);

        // right
        paddle.setX(canvas.getWidth() - paddle.getWidth() + 10);
        paddle.checkCollisionWall(canvas);
        assertEquals(canvas.getWidth() - paddle.getWidth(), paddle.getX(), 0.001);
    }

    // test4: active power up
    @Test
    void testActivePowerUp() {
        paddle.activatePowerUp(1);
        assertTrue(paddle.hasPowerUp(1));
    }

    // test5: deactive power up
    @Test
    void testDeactivePowerUp() {
        paddle.activatePowerUp(1);
        paddle.deactivatePowerUp(1);
        assertFalse(paddle.hasPowerUp(1));
    }

    // test6: expand
    @Test
    void testExpandPaddle() {
        paddle.expandPaddle(150);
        assertTrue(paddle.getWidth() > 0);
        assertTrue(paddle.animationProgress >= 0);
        assertTrue(paddle.animationProgress <= 1);
    }

    // test7: shrink
    @Test
    public void testShrinkPaddle() {
        paddle.expandPaddle(150);
        paddle.shrinkPaddle();
        assertTrue(paddle.isAnimating);
    }
}
