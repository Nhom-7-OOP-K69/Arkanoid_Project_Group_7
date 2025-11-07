package powerUp;

import javafx.application.Platform;
import object.ball.Ball;
import object.ball.BallLayer;
import object.bullet.Bullet;
import object.paddle.Paddle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestBulletPowerUp {
    private Paddle paddle;
    private BulletPowerUp powerUp;
    private BallLayer balllayer; // chưa dùng đến, chỉ để truyền vào applyEffect()

    @BeforeAll
    public static void initToolkit() {
        Platform.startup(() -> {}); // khởi tạo JavaFX toolkit
    }

    @BeforeEach
    void setUp() {
        paddle = new Paddle(100, 400, 80, 20);
        powerUp = new BulletPowerUp(200, 100, 20, 20, 3);
        balllayer = new BallLayer();
    }

    // test1: apply effect
    @Test
    void testApplyEffectActivatesPowerUp() {
        assertFalse(powerUp.isActive(), "Trước khi apply effect, powerup chưa kích hoạt");
        powerUp.applyEffect(paddle, balllayer);
        assertTrue(powerUp.isActive(), "Sau khi apply effect, powerup phải được kích hoạt");
    }

    // test2: remove effect
    @Test
    void testRemoveEffectDeactivatesPowerUp() {
        powerUp.applyEffect(paddle, balllayer);
        assertTrue(powerUp.isActive());
        powerUp.removeEffect(paddle, new Ball(0, 0, 10, 10));
        assertFalse(powerUp.isActive(), "Sau remove effect, powerup phải bị tắt");
    }

    // test3: is expired
    @Test
    void testIsExpired() {
        assertTrue(powerUp.isExpired(), "Khi chưa active, powerup mặc định hết hạn");
    }

    // test4: maybe shoot
    @Test
    void testMaybeShootCreatesBulletsWhenActive() throws InterruptedException {
        powerUp.applyEffect(paddle, balllayer);
        // Chờ một chút để vượt qua shotInterval
        Thread.sleep(600);

        List<Bullet> bullets = powerUp.maybeShoot(paddle);

        assertEquals(2, bullets.size(), "Powerup đang active bắn ra 2 viên đạn");
        assertTrue(bullets.get(0).getY() < paddle.getY(), "Đạn phải nằm phía trên paddle");
    }

    // test5: tick
    @Test
    void testTickReturnsFalseWhenExpired() throws InterruptedException {
        powerUp.applyEffect(paddle, balllayer);
        Thread.sleep(3500); // chờ quá thời gian duration (3 giây)
        assertFalse(powerUp.tick(), "Sau khi hết hạn, tick() trả về false");
        assertFalse(powerUp.isActive(), "Powerup bị vô hiệu sau tick()");
    }
}
