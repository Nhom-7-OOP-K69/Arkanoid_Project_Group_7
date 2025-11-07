package powerUp;

import javafx.application.Platform;
import object.ball.BallLayer;
import object.paddle.Paddle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestSpawnPowerUp {
    private Paddle paddle;
    private BallLayer balllayer;
    private ExpandPaddlePowerUp expandPowerUp;
    private SpawnPowerUp spawnPowerUp;

    @BeforeAll
    public static void initToolkit() {
        Platform.startup(() -> {}); // khởi tạo JavaFX toolkit
    }

    @BeforeEach
    void setUp() {
        paddle = new Paddle(100, 400, 80, 20);
        balllayer = new BallLayer();
        expandPowerUp = new ExpandPaddlePowerUp(200, 100, 20, 20, 5);
        spawnPowerUp = new SpawnPowerUp(110, 390, expandPowerUp);
    }

    // test1: move
    @Test
    void testMoveDownward() {
        double oldY = spawnPowerUp.getY();
        spawnPowerUp.move(1.0);
        assertTrue(spawnPowerUp.getY() > oldY);
    }

    // test2; check collision
    @Test
    void testCheckCollisionTrue() {
        Paddle nearPaddle = new Paddle(100, 380, 100, 20);
        assertTrue(spawnPowerUp.checkCollision(nearPaddle));
    }

    //test3: active
    @Test
    void testActivateTriggersEffectAndDeactivates() {
        assertTrue(spawnPowerUp.isActive());
        spawnPowerUp.activate(paddle, balllayer);
        assertFalse(spawnPowerUp.isActive());
        assertTrue(expandPowerUp.isActive());
    }

    //test4: out of screen
    @Test
    void testIsOutOfScreen() {
        SpawnPowerUp falling = new SpawnPowerUp(200, 900, expandPowerUp);
        assertTrue(falling.isOutOfScreen(800));
        SpawnPowerUp visible = new SpawnPowerUp(200, 500, expandPowerUp);
        assertFalse(visible.isOutOfScreen(800));
    }

}
