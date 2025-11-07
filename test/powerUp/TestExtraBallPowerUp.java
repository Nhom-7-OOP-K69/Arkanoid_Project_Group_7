package powerUp;

import javafx.application.Platform;
import object.ball.Ball;
import object.ball.BallLayer;
import object.paddle.Paddle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestExtraBallPowerUp {
    private List<Ball> balls;
    private Paddle paddle;
    private BallLayer balllayer;
    private ExtraBallPowerUp extraBallPowerUp;

    @BeforeAll
    public static void initToolkit() {
        Platform.startup(() -> {}); // khởi tạo JavaFX toolkit
    }

    @BeforeEach
    void setUp() {
        balls = new ArrayList<>();
        paddle = new Paddle(100, 400, 80, 20);
        balllayer = null;
        extraBallPowerUp = new ExtraBallPowerUp(200, 100, 20, 20, 5, balls);
    }

    // test1: apply effect
    @Test
    void testApplyEffectAddsTwoBalls() {
        int before = balls.size();
        extraBallPowerUp.applyEffect(paddle, balllayer);
        int after = balls.size();
        assertEquals(before + 2, after, "Thêm đúng 2 bóng");
    }

    // test2: correct number of ball
    @Test
    void testNewBallsHaveCorrectInitialVelocity() {
        extraBallPowerUp.applyEffect(paddle, balllayer);

        assertEquals(2, balls.size(), "Có 2 bóng được thêm vào");
        Ball b1 = balls.get(0);
        Ball b2 = balls.get(1);

        assertTrue(b1.getDy() < 0, "Bóng đầu tiên phải bay lên trên");
        assertTrue(b2.getDy() < 0, "Bóng thứ hai phải bay lên trên");
        assertTrue(b1.getDx() * b2.getDx() < 0, "Hai bóng phải bay theo hai hướng ngược nhau");
    }
}
