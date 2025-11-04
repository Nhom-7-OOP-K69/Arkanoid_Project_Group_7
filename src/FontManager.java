import java.io.InputStream;
import javafx.scene.text.Font;

public class FontManager {

    private static final String DEFAULT_FONT_FILE = "font1";
    private static final float DEFAULT_SIZE = 16f;
    private static final String FONT_PATH = "Arka_solid";

    private static volatile FontManager instance;
    private Font customFont; // Bây giờ là javafx.scene.text.Font

    private FontManager() {
        loadFont(DEFAULT_FONT_FILE, DEFAULT_SIZE);
    }

    public static FontManager getInstance() {
        if (instance == null) {
            synchronized (FontManager.class) {
                if (instance == null) {
                    instance = new FontManager();
                }
            }
        }
        return instance;
    }

    private void loadFont(String fileName, float fontSize) {
        try (InputStream is = FontManager.class.getResourceAsStream("/" + fileName)) {

            if (is == null) {
                // Tải font hệ thống mặc định nếu tệp không được tìm thấy
                customFont = Font.font("Arial", fontSize);
                return;
            }

            // Dùng Font.loadFont của JavaFX để tải font từ InputStream
            customFont = Font.loadFont(is, fontSize);

            if (customFont == null) {
                // loadFont có thể trả về null nếu font không hợp lệ
                customFont = Font.font("Arial", fontSize);
            }

        } catch (Exception e) {
            e.printStackTrace();
            customFont = Font.font("Arial", fontSize);
        }
    }

    public Font getFont() {
        return customFont;
    }

    public Font getFont(double newSize) {
        if (customFont == null || "Arial".equals(customFont.getFamily())) {
            return Font.font("Arial", newSize);
        }
        return Font.font(customFont.getFamily(), newSize);
    }
}