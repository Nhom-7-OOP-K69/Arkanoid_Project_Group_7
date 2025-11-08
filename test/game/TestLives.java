package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestLives {
    private Lives lives;

    @BeforeEach
    void setUp() {
        lives = new Lives();
    }

    //test1: initial hp = 3
    @Test
    void testInitialHP() {
        assertEquals(3, lives.getHP());
    }

    // test2: decreaselife
    @Test
    void testDecreaseLife() {
        lives.decreaseLife();
        assertEquals(2, lives.getHP());
    }

    //test3: reset hp
    @Test
    void testResetHP() {
        lives.decreaseLife();
        lives.reset();
        assertEquals(3, lives.getHP());
    }

}
