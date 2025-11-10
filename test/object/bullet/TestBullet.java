package object.bullet;

import object.brick.NormalBrick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestBullet {
    private Bullet bullet;
    private NormalBrick brick;

    @BeforeEach
    public void setUp() {
        bullet = new Bullet(100, 100, 10, 20);
        brick = new NormalBrick(95, 80);
    }

    // test1: initial values
    @Test
    public void testInitialValues() {
        assertEquals(100, bullet.getX(), 0.001);
        assertEquals(100, bullet.getY(), 0.001);
        assertEquals(10, bullet.getWidth(), 0.001);
        assertEquals(20, bullet.getHeight(), 0.001);
    }

    // test2: check brick collision
    @Test
    public void testIntersects() {
        bullet.setX(brick.getX() + brick.getWidth() - bullet.getWidth());
        bullet.setY(brick.getY() + brick.getHeight() - bullet.getHeight());
        assertTrue(bullet.intersects(brick));
    }

    // test3: check out of screen
    @Test
    void testIsOutOfScreen() {
        bullet.setY(-30);
        assertTrue(bullet.isOutOfScreen());
    }
}
