package object.ball;

import game.GameConstants;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import object.brick.NormalBrick;
import object.paddle.Paddle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BallTest {
    private Ball ball;
    private Canvas canvas;

    @BeforeAll
    public static void initToolkit() {
        Platform.startup(() -> {}); // khởi tạo JavaFX toolkit
    }

    @BeforeEach
    void setup() {
        ball = new Ball(GameConstants.SCREEN_WIDTH / 2, GameConstants.SCREEN_HEIGHT / 2, GameConstants.BALL_WIDTH, GameConstants.BALL_HEIGHT);
        ball.setDx(5);
        ball.setDy(3);
        canvas = new Canvas(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
    }

    // test1: test setSpeed && getSpeed
    @Test
    void testSpeedGetterSetter() {
        ball.setSpeed(GameConstants.BALL_SPEED);
        assertEquals(GameConstants.BALL_SPEED, ball.getSpeed());
    }

    // test2: check wall collision
    @Test
    void checkCollisionWalls() {
        // left wall
        ball.setX(0);
        double oldDx = ball.getDx();
        ball.collisionWall(canvas);
        assertEquals(-oldDx, ball.getDx(), 0.001, "Đảo hướng");

        // right wall
        ball.setX(canvas.getWidth() - ball.getWidth());
        oldDx = ball.getDx();
        ball.collisionWall(canvas);
        assertEquals(-oldDx, ball.getDx(), 0.001, "Đảo hướng");

        // top wall
        ball.setY(GameConstants.UI_TOP_BAR_HEIGHT + 1);
        double oldDy = ball.getDy();
        ball.collisionWall(canvas);
        assertEquals(oldDy, ball.getDy(), 0.001, "Nảy xuống");

        // bottom
        ball.setY(canvas.getHeight());
        boolean lost = ball.collisionWall(canvas);
        assertTrue(lost, "Bóng chạm đáy phải trả về true");
    }

    // test3: check paddle collision
    @Test
    void testBoundOff() {
        Paddle paddle = new Paddle((GameConstants.SCREEN_WIDTH - GameConstants.PADDLE_WIDTH) / 2.0,
                GameConstants.SCREEN_HEIGHT - 100, GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT);
        ball.setSpeed(GameConstants.BALL_SPEED);

        // centre
        ball.setX(paddle.getX() + paddle.getWidth() / 2.0 - ball.getWidth() / 2.0);
        ball.bounceOff(paddle);
        assertTrue(ball.getDy() < 0, "Bóng nảy lên");
        assertEquals(paddle.getY() - ball.getHeight(), ball.getY(), 0.01, "Bóng nằm trên paddle");

        // left
        ball.setX(paddle.getX());
        ball.bounceOff(paddle);
        assertTrue(ball.getDx() < 0, "Bóng nảy sang trái");

        // right
        ball.setX(paddle.getX() + paddle.getWidth() - ball.getWidth());
        ball.bounceOff(paddle);
        assertTrue(ball.getDx() > 0, "Bóng nảy sang phải");
    }

    // test4: check brick collision
    @Test
    void testBounceOffBrick() {
        NormalBrick brick = new NormalBrick(GameConstants.BRICK_WIDTH / 2, GameConstants.SCREEN_HEIGHT - 300);
        double oldDy = ball.getDy();
        ball.bounceOff(brick);
        assertEquals(oldDy, ball.getDy(), 0.001, "Đảo hướng dọc");
    }
}
