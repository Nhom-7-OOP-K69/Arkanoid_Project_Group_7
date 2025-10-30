import javafx.geometry.Pos;
import javafx.scene.Cursor; // Import Cursor
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.InputStream;
import java.util.Objects;

/**
 * Màn hình Game Over với style bằng Java và nút Image.
 * Phiên bản "Clean Code" với hằng số và hàm trợ giúp.
 * Sửa nút: Dùng ImageView, KHÔNG DÙNG TEXT (vì ảnh đã có chữ).
 */
public class GameOverScreen extends VBox {

    // --- Hằng số (Constants) ---
    private static final double BUTTON_WIDTH = 280;
    private static final double VBOX_SPACING = 30;
    private static final double SCREEN_PADDING = 50;

    // --- Hằng số Tài nguyên ---
    private static final String FONT_PATH = "/Arka_solid.ttf";

    // Nút Replay
    private static final String REPLAY_NORMAL_IMG = "/images/replay.png";
    private static final String REPLAY_HOVER_IMG = "/images/replay_hover.png";
    private static final String REPLAY_PRESSED_IMG = "/images/replay_press.png";

    // Nút Menu (Options)
    private static final String MENU_NORMAL_IMG = "/images/options.png";
    private static final String MENU_HOVER_IMG = "/images/options_hover.png";
    private static final String MENU_PRESSED_IMG = "/images/options_press.png";

    // Nút Exit
    private static final String EXIT_NORMAL_IMG = "/images/exit.png";
    private static final String EXIT_HOVER_IMG = "/images/exit_hover.png";
    private static final String EXIT_PRESSED_IMG = "/images/exit_press.png";

    // --- Biến lớp ---
    private GameManager gameManager; // <-- Biến để lưu GameManager
    private Font titleFont;
    private Font scoreFont;

    private Image replayNormal, replayHover, replayPressed;
    private Image menuNormal, menuHover, menuPressed;
    private Image exitNormal, exitHover, exitPressed;

    // --- Định nghĩa Hiệu ứng ---
    private final DropShadow TITLE_GLOW = new DropShadow(50, Color.rgb(255, 0, 0, 0.9));
    private final DropShadow SCORE_GLOW = new DropShadow(20, Color.rgb(255, 215, 0, 0.7));

    /**
     * HÀM KHỞI TẠO ĐÃ SỬA:
     * Nhận 'GameManager' thay vì 'Stage'
     */
    public GameOverScreen(GameManager gameManager, int score) {
        super(VBOX_SPACING); // Dùng hằng số
        this.gameManager = gameManager; // <-- Gán GameManager
        setAlignment(Pos.CENTER);

        loadResources();

        // --- Nền Màn hình Game Over ---
        this.setStyle("-fx-background-color: #2F0505;"); // Màu đỏ rất đậm
        this.setPadding(new javafx.geometry.Insets(SCREEN_PADDING)); // Dùng hằng số

        // --- Tiêu đề "GAME OVER" ---
        Text gameOverTitle = new Text("GAME OVER");
        gameOverTitle.setFont(titleFont);
        gameOverTitle.setFill(Color.RED);
        gameOverTitle.setStroke(Color.DARKRED);
        gameOverTitle.setStrokeWidth(2);
        gameOverTitle.setEffect(TITLE_GLOW);

        // --- Hiển thị điểm số ---
        Text scoreText = new Text("SCORE: " + score);
        scoreText.setFont(scoreFont);
        scoreText.setFill(Color.GOLD);
        scoreText.setEffect(SCORE_GLOW);

        // --- Các nút hành động (gọi lại GameManager) ---
        ImageView restartButton = createStyledImageButton(replayNormal, replayHover, replayPressed);
        restartButton.setOnMouseClicked(e -> gameManager.startGame()); // <-- Gọi GameManager

        ImageView mainMenuButton = createStyledImageButton(menuNormal, menuHover, menuPressed);
        mainMenuButton.setOnMouseClicked(e -> gameManager.returnToMenu()); // <-- Gọi GameManager

        ImageView exitButton = createStyledImageButton(exitNormal, exitHover, exitPressed);
        exitButton.setOnMouseClicked(e -> gameManager.exitGame()); // <-- Gọi GameManager

        // Thêm tất cả vào VBox
        getChildren().addAll(gameOverTitle, scoreText, restartButton, mainMenuButton, exitButton);
    }

    /**
     * Tải font Arka_solid.ttf và các file ảnh nút.
     */
    private void loadResources() {
        try {
            // --- Tải Font ---
            titleFont = loadFontFromFile(FONT_PATH, 100);
            scoreFont = Font.font("Impact", 48);

            // --- TẢI HÌNH ẢNH NÚT ---
            replayNormal = loadImageFromFile(REPLAY_NORMAL_IMG);
            replayHover = loadImageFromFile(REPLAY_HOVER_IMG);
            replayPressed = loadImageFromFile(REPLAY_PRESSED_IMG);

            menuNormal = loadImageFromFile(MENU_NORMAL_IMG);
            menuHover = loadImageFromFile(MENU_HOVER_IMG);
            menuPressed = loadImageFromFile(MENU_PRESSED_IMG);

            exitNormal = loadImageFromFile(EXIT_NORMAL_IMG);
            exitHover = loadImageFromFile(EXIT_HOVER_IMG);
            exitPressed = loadImageFromFile(EXIT_PRESSED_IMG);

        } catch (Exception e) {
            System.err.println("--- LỖI NGHIÊM TRỌNG KHI TẢI TÀI NGUYÊN ---");
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println("--- SỬ DỤNG FONT DỰ PHÒNG ---");

            // Fallback (dùng font hệ thống nếu không tải được)
            titleFont = Font.font("Impact", 100);
            scoreFont = Font.font("Arial Black", 48);
        }
    }

    /**
     * Hàm trợ giúp (Helper) để tải font an toàn.
     */
    private Font loadFontFromFile(String path, double size) throws Exception {
        try (InputStream isFont = getClass().getResourceAsStream(path)) {
            if (isFont == null) {
                throw new Exception("LỖI: Không tìm thấy font '" + path + "'.\nHãy đảm bảo nó ở trong 'resources/com/yourgame/'");
            }
            Font baseFont = Font.loadFont(isFont, size);
            if (baseFont == null) {
                throw new Exception("LỖI: Không thể tải font '" + path + "'. File có thể bị hỏng.");
            }
            return Font.font(baseFont.getFamily(), size);
        }
    }

    /**
     * Hàm trợ giúp (Helper) để tải ảnh an toàn.
     */
    private Image loadImageFromFile(String path) throws Exception {
        InputStream stream = Objects.requireNonNull(
                getClass().getResourceAsStream(path),
                "LỖI: Không tìm thấy ảnh '" + path + "'.\nHãy đảm bảo nó ở trong 'resources/com/yourgame/'"
        );
        return new Image(stream);
    }

    /**
     * Tạo một nút DỰA TRÊN ẢNH (không có Text).
     * Trả về một ImageView đã được gắn các hiệu ứng.
     */
    private ImageView createStyledImageButton(Image normalImg, Image hoverImg, Image pressedImg) {

        ImageView buttonImageView = new ImageView(normalImg);
        buttonImageView.setFitWidth(BUTTON_WIDTH); // Dùng hằng số
        buttonImageView.setPreserveRatio(true);
        buttonImageView.setCursor(Cursor.HAND);

        // Thêm hiệu ứng tương tác (Hover/Pressed)
        buttonImageView.setOnMouseEntered(e -> {
            buttonImageView.setImage(hoverImg);
        });

        buttonImageView.setOnMouseExited(e -> {
            buttonImageView.setImage(normalImg);
        });

        buttonImageView.setOnMousePressed(e -> {
            buttonImageView.setImage(pressedImg);
            buttonImageView.setScaleX(0.95);
            buttonImageView.setScaleY(0.95);
        });

        buttonImageView.setOnMouseReleased(e -> {
            buttonImageView.setScaleX(1.0);
            buttonImageView.setScaleY(1.0);
            if (buttonImageView.isHover()) {
                buttonImageView.setImage(hoverImg);
            } else {
                buttonImageView.setImage(normalImg);
            }
        });

        return buttonImageView; // Trả về ImageView
    }
}