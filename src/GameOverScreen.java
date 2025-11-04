import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
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

public class GameOverScreen extends VBox {

    // --- Hằng số (Constants) ---
    private static final double BUTTON_WIDTH = 280;
    private static final double VBOX_SPACING = 30;
    private static final double SCREEN_PADDING = 50;

    // --- Hằng số Tài nguyên ---
    private static final String FONT_PATH = "/fonts/Arka_solid.ttf";
    private static final String BACKGROUND_IMAGE_PATH = "/images/GameOverScreenBg.png";

    // Nút Replay
    private final Image replay_normal = ImgManager.getInstance().getImage("REPLAY_NORMAL");
    private final Image replay_hover = ImgManager.getInstance().getImage("REPLAY_HOVER");
    private final Image replay_press = ImgManager.getInstance().getImage("REPLAY_PRESS");
    private final Image options_normal = ImgManager.getInstance().getImage("OPTIONS_NORMAL");
    private final Image options_hover = ImgManager.getInstance().getImage("OPTIONS_HOVER");
    private final Image options_press = ImgManager.getInstance().getImage("OPTIONS_PRESS");
    private final Image exit_normal = ImgManager.getInstance().getImage("EXIT_NORMAL");
    private final Image exit_hover = ImgManager.getInstance().getImage("EXIT_HOVER");
    private final Image exit_press = ImgManager.getInstance().getImage("EXIT_PRESS");

    public ImageView replayNormalButton = new ImageView(replay_normal);
    public ImageView replayHoverButton = new ImageView(replay_hover);
    public ImageView replayPressButton = new ImageView(replay_press);
    public ImageView optionsNormalButton = new ImageView(options_normal);
    public ImageView optionsHoverButton = new ImageView(options_hover);
    public ImageView optionsPressButton = new ImageView(options_press);
    public ImageView exitNormalButton = new ImageView(exit_normal);
    public ImageView exitHoverButton = new ImageView(exit_hover);
    public ImageView exitPressButton = new ImageView(exit_press);





    // --- Biến lớp ---
    private GameManager gameManager; // <-- Biến để lưu GameManager
    private Font titleFont;
    private Font scoreFont;

    private Image replayNormal, replayHover, replayPressed;
    private Image menuNormal, menuHover, menuPressed;
    private Image exitNormal, exitHover, exitPressed;
    private Image backgroundImage;

    // --- Định nghĩa Hiệu ứng ---
    private final DropShadow TITLE_GLOW = new DropShadow(50, Color.rgb(255, 0, 0, 0.9));
    private final DropShadow SCORE_GLOW = new DropShadow(20, Color.rgb(255, 215, 0, 0.7));

    public GameOverScreen(GameManager gameManager, int score) {
        super(VBOX_SPACING); // Dùng hằng số
        this.gameManager = gameManager; // <-- Gán GameManager
        setAlignment(Pos.CENTER);

        loadResources();

        BackgroundImage myBI = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT, // Không lặp lại
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,  // Căn giữa
                // Kích thước: "cover" (phủ đầy, giữ tỷ lệ)
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        this.setBackground(new Background(myBI));

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

    private void loadResources() {
        try {
            // --- Tải Font ---
            titleFont = loadFontFromFile(FONT_PATH, 100);
            scoreFont = Font.font("Impact", 48);

            backgroundImage = loadImageFromFile(BACKGROUND_IMAGE_PATH);

            // --- GÁN HÌNH ẢNH NÚT (Chúng đã được tải khi khai báo biến ở trên) ---

            // Gán cho biến 'replay...' mà constructor sử dụng
            replayNormal = this.replay_normal;
            replayHover = this.replay_hover;
            replayPressed = this.replay_press;

            // Gán cho biến 'menu...' mà constructor sử dụng
            // (Lấy từ biến 'options...' đã khai báo ở trên)
            menuNormal = this.options_normal;
            menuHover = this.options_hover;
            menuPressed = this.options_press;

            // Gán cho biến 'exit...' mà constructor sử dụng
            exitNormal = this.exit_normal;
            exitHover = this.exit_hover;
            exitPressed = this.exit_press;

            // Kiểm tra xem ImgManager có trả về null hay không
            if (replayNormal == null || menuNormal == null || exitNormal == null) {
                // Thêm thông báo lỗi rõ ràng về các key có thể bị sai (ví dụ "NORNAL")
                throw new Exception("LỖI: ImgManager đã trả về null cho một hoặc nhiều ảnh. \n" +
                        "Hãy kiểm tra các key trong ImgManager.");
            }

        } catch (Exception e) {
            System.err.println("--- LỖI NGHIÊM TRỌNG KHI TẢI TÀI NGUYÊN ---");
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println("--- SỬ DỤNG FONT DỰ PHÒNG ---");

            // Fallback (dùng font hệ thống nếu không tải được)
            titleFont = Font.font("Impact", 100);
            scoreFont = Font.font("Arial Black", 48);

            System.err.println("--- CẢNH BÁO: Không thể gán hình ảnh nút. Ứng dụng có thể sẽ crash. ---");
        }
    }

    private Font loadFontFromFile(String path, double size) throws Exception {
        try (InputStream isFont = getClass().getResourceAsStream(path)) {
            if (isFont == null) {
                throw new Exception("LỖI: Không tìm thấy font '" + path + "'.\nHãy đảm bảo nó ở trong 'resources'");
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