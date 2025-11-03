package game;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import manager.ImgManager;

import java.io.InputStream;
import java.util.Objects;

/**
 * Màn hình "YOU WIN"
 * Được sao chép và chỉnh sửa từ GameOverScreen.
 */
public class GameWinScreen extends VBox {

    // --- Hằng số (Constants) ---
    private static final double BUTTON_WIDTH = 280;
    private static final double VBOX_SPACING = 30;
    private static final double SCREEN_PADDING = 50;

    // --- Hằng số Tài nguyên ---
    private static final String FONT_PATH = "/Arka_solid.ttf";
    // QUAN TRỌNG: Bạn cần cung cấp file ảnh nền cho màn hình "Win"
    private static final String GAME_WIN_BACKGROUND_IMAGE_PATH = "/images/GameOverScreenBg.png";

    // --- Nút (Tải trực tiếp từ ImgManager) ---
    // Chúng ta tái sử dụng các nút từ màn hình Game Over
    private final Image replay_normal = ImgManager.getInstance().getImage("REPLAY_NORMAL");
    private final Image replay_hover = ImgManager.getInstance().getImage("REPLAY_HOVER");
    private final Image replay_press = ImgManager.getInstance().getImage("REPLAY_PRESS");
    private final Image options_normal = ImgManager.getInstance().getImage("OPTIONS_NORMAL");
    private final Image options_hover = ImgManager.getInstance().getImage("OPTIONS_HOVER");
    private final Image options_press = ImgManager.getInstance().getImage("OPTIONS_PRESS");
    private final Image exit_normal = ImgManager.getInstance().getImage("EXIT_NORMAL");
    private final Image exit_hover = ImgManager.getInstance().getImage("EXIT_HOVER");
    private final Image exit_press = ImgManager.getInstance().getImage("EXIT_PRESS");

    // --- Biến lớp ---
    private GameManager gameManager; // <-- Biến để lưu GameManager
    private Font titleFont;
    private Font scoreFont;
    private Image backgroundImage;

    // --- Định nghĩa Hiệu ứng ---
    // Đổi hiệu ứng sang màu Vàng/Gold cho "YOU WIN"
    private final DropShadow TITLE_GLOW = new DropShadow(50, Color.rgb(255, 215, 0, 0.9));
    private final DropShadow SCORE_GLOW = new DropShadow(20, Color.rgb(255, 255, 100, 0.7));

    /**
     * HÀM KHỞI TẠO:
     * Nhận 'GameManager' và 'score'
     */
    public GameWinScreen(GameManager gameManager, int score) {
        super(VBOX_SPACING); // Dùng hằng số
        this.gameManager = gameManager; // <-- Gán GameManager
        setAlignment(Pos.CENTER);

        loadResources();

        // --- Đặt ảnh nền ---
        BackgroundImage myBI = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        this.setBackground(new Background(myBI));
        this.setPadding(new javafx.geometry.Insets(SCREEN_PADDING)); // Dùng hằng số

        // --- Tiêu đề "YOU WIN" ---
        Text gameWinTitle = new Text("YOU WIN"); // <-- THAY ĐỔI
        gameWinTitle.setFont(titleFont);
        gameWinTitle.setFill(Color.GOLD); // <-- THAY ĐỔI
        gameWinTitle.setStroke(Color.DARKGOLDENROD); // <-- THAY ĐỔI
        gameWinTitle.setStrokeWidth(2);
        gameWinTitle.setEffect(TITLE_GLOW);

        // --- Hiển thị điểm số ---
        Text scoreText = new Text("SCORE: " + score);
        scoreText.setFont(scoreFont);
        scoreText.setFill(Color.WHITE); // Đổi thành màu trắng cho dễ đọc trên nền mới
        scoreText.setEffect(SCORE_GLOW);

        // --- Các nút hành động (Tái sử dụng từ Game Over) ---
        ImageView restartButton = createStyledImageButton(replay_normal, replay_hover, replay_press);
        restartButton.setOnMouseClicked(e -> gameManager.startGame());

        ImageView mainMenuButton = createStyledImageButton(options_normal, options_hover, options_press);
        mainMenuButton.setOnMouseClicked(e -> gameManager.returnToMenu());

        ImageView exitButton = createStyledImageButton(exit_normal, exit_hover, exit_press);
        exitButton.setOnMouseClicked(e -> gameManager.exitGame());

        // Thêm tất cả vào VBox
        getChildren().addAll(gameWinTitle, scoreText, restartButton, mainMenuButton, exitButton);
    }

    /**
     * Tải font và các tài nguyên ảnh.
     */
    private void loadResources() {
        try {
            // --- Tải Font ---
            titleFont = loadFontFromFile(FONT_PATH, 100);
            scoreFont = Font.font("Impact", 48);

            // --- Tải ảnh nền (cho màn hình Win) ---
            backgroundImage = loadImageFromFile(GAME_WIN_BACKGROUND_IMAGE_PATH);

            // --- KIỂM TRA HÌNH ẢNH NÚT (đã được tải ở phần khai báo) ---
            if (replay_normal == null || options_normal == null || exit_normal == null) {
                throw new Exception("LỖI: ImgManager đã trả về null cho một hoặc nhiều ảnh nút. \n" +
                        "Hãy kiểm tra các key trong ImgManager.");
            }

        } catch (Exception e) {
            System.err.println("--- LỖI NGHIÊM TRỌNG KHI TẢI TÀI NGUYÊN (GameWinScreen) ---");
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println("--- SỬ DỤNG FONT DỰ PHÒNG ---");

            // Fallback (dùng font hệ thống nếu không tải được)
            titleFont = Font.font("Impact", 100);
            scoreFont = Font.font("Arial Black", 48);
        }
    }

    private Font loadFontFromFile(String path, double size) throws Exception {
        try (InputStream isFont = getClass().getResourceAsStream(path)) {
            if (isFont == null) {
                throw new Exception("LỖI: Không tìm thấy font '" + path + "'.");
            }
            Font baseFont = Font.loadFont(isFont, size);
            if (baseFont == null) {
                throw new Exception("LỖI: Không thể tải font '" + path + "'. File có thể bị hỏng.");
            }
            return Font.font(baseFont.getFamily(), size);
        }
    }

    private Image loadImageFromFile(String path) throws Exception {
        InputStream stream = Objects.requireNonNull(
                getClass().getResourceAsStream(path),
                "LỖI: Không tìm thấy ảnh '" + path + "'.\nHãy đảm bảo nó ở trong 'resources'"
        );
        return new Image(stream);
    }

    /**
     * Tạo một nút DỰA TRÊN ẢNH (không có Text).
     * (Giữ nguyên từ GameOverScreen)
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