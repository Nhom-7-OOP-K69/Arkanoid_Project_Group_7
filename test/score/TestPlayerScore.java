package score;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlayerScore {
    //test1: compare to
    @Test
    void testCompareToDescendingOrder() {
        PlayerScore p1 = new PlayerScore("Huy", 1500);
        PlayerScore p2 = new PlayerScore("Khang", 2000);
        assertTrue(p2.compareTo(p1) < 0);
        assertTrue(p1.compareTo(p2) > 0);
        assertEquals(0, p1.compareTo(new PlayerScore("Huy", 1500)));
    }

    //test2: toString
    @Test
    void testToStringFormat() {
        PlayerScore player = new PlayerScore("Huy", 1500);
        assertEquals("Huy: 1500", player.toString());
    }
}
