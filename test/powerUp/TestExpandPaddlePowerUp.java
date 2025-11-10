package powerUp;

import javafx.application.Platform;
import object.ball.Ball;
import object.ball.BallLayer;
import object.paddle.Paddle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestExpandPaddlePowerUp {
    private Paddle paddle;
    private ExpandPaddlePowerUp powerUp;
    private BallLayer balllayer;

    @BeforeAll
    public static void initToolkit() {
        Platform.startup(() -> {}); // khởi tạo JavaFX toolkit
    }

    @BeforeEach
    void setUp() {
        paddle = new Paddle(100, 400, 80, 20);
        powerUp = new ExpandPaddlePowerUp(200, 100, 20, 20, 5); // 5s hiệu lực
        balllayer = new BallLayer();
    }

    // test1: apply effect
    @Test
    void testApplyEffectExpandsAndActivates() {
        double oldWidth = paddle.getWidth();
        powerUp.applyEffect(paddle, balllayer);
        assertTrue(powerUp.isActive());
        assertTrue(paddle.hasPowerUp(1));
        assertTrue(paddle.getWidth() >= oldWidth);
    }

    // test2: remove effect
    @Test
    void testRemoveEffectShrinksAndDeactivates() {
        powerUp.applyEffect(paddle, balllayer);
        assertTrue(powerUp.isActive());
        powerUp.removeEffect(paddle, new Ball(0, 0, 10, 10));
        assertFalse(powerUp.isActive());
        assertFalse(paddle.hasPowerUp(1));
    }

    // test3: is expired
    @Test
    void testIsExpiredAfterDuration() throws InterruptedException {
        powerUp.applyEffect(paddle, balllayer);
        assertFalse(powerUp.isExpired());
        Thread.sleep(5000);
        assertTrue(powerUp.isExpired());
    }

    // test4: tick
    @Test
    void testTickReturnsFalseWhenExpired() throws InterruptedException {
        powerUp.applyEffect(paddle, balllayer);
        Thread.sleep(5000);
        boolean result = powerUp.tick();
        assertFalse(result);
        assertTrue(powerUp.isExpired());
    }
}
