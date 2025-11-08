package manager;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestFont {
    @BeforeAll
    static void setupFX() {
        // Khởi động môi trường JavaFX (bắt buộc để dùng Font)
        new JFXPanel();
    }

    // test1: get same font
    @Test
    void testSingletonInstance() {
        FontManager f1 = FontManager.getInstance();
        FontManager f2 = FontManager.getInstance();
        assertSame(f1, f2);
    }
}
