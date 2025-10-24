import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.Arrays;
import java.util.List;

/**
 * Lớp UIManager quản lý tất cả các thành phần giao diện người dùng (UI).
 * Phiên bản này có giao diện được thiết kế lại chuyên nghiệp và hiển thị điểm số.
 */
public class UIManager {

    // --- CÁC HẰNG SỐ STYLE ---
    private static final String BUTTON_BASE_STYLE = "-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-font-size: 16px; -fx-min-width: 120px; -fx-padding: 8px; -fx-border-color: #cccccc; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-background-radius: 8px;";
    private static final String BUTTON_HOVER_STYLE = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-min-width: 120px; -fx-padding: 8px; -fx-border-color: #ffffff; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-background-radius: 8px;";

    // --- CÁC BIẾN THAM CHIẾU ---
    private final GameManager gameManager;
    private final GameStateManager gameStateManager;

    // --- CÁC THÀNH PHẦN UI ---
    public Scene menuScene, gameScene;
    public StackPane gamePane;
    public VBox pauseOverlay, settingsOverlay;
    public Button startButton, soundEffectsButton, musicButton, pauseButton, resumeButtonPause, menuButtonPause;
    public Text pauseText, countdownText;
    private Label scoreLabel;
    private Label highScoreLabel;

    public UIManager(GameManager gameManager, GameStateManager gameStateManager) {
        this.gameManager = gameManager;
        this.gameStateManager = gameStateManager;
    }

    // --- GIAO DIỆN MENU VÀ CÀI ĐẶT ---
    public void createMenuScene() {
        StackPane menuPane = new StackPane();
        VBox menuLayout = new VBox(25);
        menuLayout.setAlignment(Pos.CENTER);
        Stop[] stops = new Stop[]{new Stop(0, Color.web("#28313B")), new Stop(1, Color.web("#485461"))};
        LinearGradient lg = new LinearGradient(0, 0, 0, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE, stops);
        menuLayout.setBackground(new Background(new BackgroundFill(lg, CornerRadii.EMPTY, Insets.EMPTY)));
        Text title = new Text("ARKANOID");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 80));
        title.setFill(Color.WHITE);
        title.setEffect(new javafx.scene.effect.DropShadow(10, Color.BLACK));
        String menuButtonBaseStyle = "-fx-font-size: 24px; -fx-min-width: 280px; -fx-padding: 12px; " + BUTTON_BASE_STYLE.substring(BUTTON_BASE_STYLE.indexOf("-fx-background-color:"));
        String menuButtonHoverStyle = "-fx-font-size: 24px; -fx-min-width: 280px; -fx-padding: 12px; " + BUTTON_HOVER_STYLE.substring(BUTTON_HOVER_STYLE.indexOf("-fx-background-color:"));
        startButton = new Button("Start");
        Button settingsButton = new Button("Setting");
        Button exitButton = new Button("Exit");
        List<Button> buttons = Arrays.asList(startButton, settingsButton, exitButton);
        for (Button btn : buttons) {
            btn.setStyle(menuButtonBaseStyle);
            btn.setOnMouseEntered(e -> btn.setStyle(menuButtonHoverStyle));
            btn.setOnMouseExited(e -> btn.setStyle(menuButtonBaseStyle));
        }
        startButton.setOnAction(e -> gameManager.startGame());
        settingsButton.setOnAction(e -> showSettings());
        exitButton.setOnAction(e -> gameManager.exitGame());
        menuLayout.getChildren().addAll(title, startButton, settingsButton, exitButton);
        createSettingsOverlay();
        menuPane.getChildren().addAll(menuLayout, settingsOverlay);
        menuScene = new Scene(menuPane, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
    }

    public void showSettings() { settingsOverlay.setVisible(true); }
    public void hideSettings() { settingsOverlay.setVisible(false); }

    private void createSettingsOverlay() {
        Text settingsTitle = new Text("Setting");
        settingsTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 60));
        settingsTitle.setFill(Color.WHITE);
        soundEffectsButton = new Button("Sound: " + (gameStateManager.isSoundEffectsOn() ? "On" : "Off"));
        soundEffectsButton.setOnAction(e -> gameManager.toggleSoundEffects());
        musicButton = new Button("Music: " + (gameStateManager.isMusicOn() ? "On" : "Off"));
        musicButton.setOnAction(e -> gameManager.toggleMusic());
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> hideSettings());
        String buttonStyle = "-fx-font-size: 22px; -fx-min-width: 250px; -fx-padding: 10px;";
        soundEffectsButton.setStyle(buttonStyle);
        musicButton.setStyle(buttonStyle);
        backButton.setStyle(buttonStyle);
        settingsOverlay = new VBox(25, settingsTitle, soundEffectsButton, musicButton, backButton);
        settingsOverlay.setAlignment(Pos.CENTER);
        settingsOverlay.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.85), CornerRadii.EMPTY, Insets.EMPTY)));
        settingsOverlay.setVisible(false);
    }

    // --- GIAO DIỆN MÀN HÌNH GAME ---
    public void createGameScene(Canvas canvasPane) {
        // 1. TẠO LABEL ĐIỂM SỐ
        scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        highScoreLabel = new Label("High Score: 0");
        highScoreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #A0AEC0;"); // Màu khác để phân biệt
        VBox scoreBox = new VBox(5, highScoreLabel, scoreLabel);
        scoreBox.setAlignment(Pos.CENTER_LEFT);

        // 2. TẠO CÁC NÚT ĐIỀU KHIỂN
        pauseButton = new Button("Pause");
        pauseButton.setOnAction(e -> gameManager.pauseGame());
        Button menuButton = new Button("Back to Menu");
        menuButton.setOnAction(e -> gameManager.returnToMenu());
        List<Button> gameControlButtons = Arrays.asList(pauseButton, menuButton);
        for (Button btn : gameControlButtons) {
            btn.setStyle(BUTTON_BASE_STYLE);
            btn.setOnMouseEntered(e -> btn.setStyle(BUTTON_HOVER_STYLE));
            btn.setOnMouseExited(e -> btn.setStyle(BUTTON_BASE_STYLE));
        }
        HBox buttonBox = new HBox(10, pauseButton, menuButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        // 3. TẠO THANH UI TRÊN CÙNG
        BorderPane topUIPanel = new BorderPane();
        topUIPanel.setPadding(new Insets(10, 20, 10, 20));
        topUIPanel.setStyle("-fx-background-color: rgba(45, 55, 72, 0.7);"); // Nền mờ để nổi bật
        topUIPanel.setLeft(scoreBox);
        topUIPanel.setRight(buttonBox);

        // 4. SẮP XẾP BỐ CỤC GAME
        // Sử dụng BorderPane làm layout chính để đảm bảo topUIPanel luôn ở trên cùng
        BorderPane gameLayout = new BorderPane();
        gameLayout.setCenter(canvasPane); // Đặt canvas vào trung tâm
        gameLayout.setTop(topUIPanel);    // Đặt thanh UI lên trên cùng

        createPauseOverlay();
        // StackPane giờ chỉ dùng để xếp chồng lớp overlay lên trên màn hình game
        gamePane = new StackPane(gameLayout, pauseOverlay);
        gamePane.setStyle("-fx-background-color: #28313B;");

        // Dòng setAlignment không còn cần thiết nữa vì BorderPane đã xử lý việc này
        // StackPane.setAlignment(topUIPanel, Pos.TOP_CENTER);

        gameScene = new Scene(gamePane);
    }

    // --- GIAO DIỆN PAUSE ---
    private void createPauseOverlay() {
        pauseText = new Text("Pause");
        pauseText.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        pauseText.setFill(Color.WHITE);
        countdownText = new Text();
        countdownText.setFont(Font.font("Arial", FontWeight.BOLD, 90));
        countdownText.setFill(Color.WHITE);
        countdownText.setVisible(false);
        resumeButtonPause = new Button("Resume");
        resumeButtonPause.setOnAction(e -> gameManager.startResumeCountdown());
        menuButtonPause = new Button("Back to Menu");
        menuButtonPause.setOnAction(e -> gameManager.returnToMenu());
        String buttonStyle = "-fx-font-size: 22px; -fx-min-width: 200px; -fx-padding: 8px;";
        resumeButtonPause.setStyle(buttonStyle);
        menuButtonPause.setStyle(buttonStyle);
        pauseOverlay = new VBox(20, pauseText, countdownText, resumeButtonPause, menuButtonPause);
        pauseOverlay.setAlignment(Pos.CENTER);
        pauseOverlay.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
        pauseOverlay.setVisible(false);
    }

    // --- CÁC PHƯƠNG THỨC CẬP NHẬT UI ---
    public void updateScoreLabel(int newScore) {
        Platform.runLater(() -> {
            if (scoreLabel != null) {
                scoreLabel.setText("Score: " + newScore);
            }
        });
    }

    public void updateHighScoreLabel(int newHighScore) {
        Platform.runLater(() -> {
            if (highScoreLabel != null) {
                highScoreLabel.setText("High Score: " + newHighScore);
            }
        });
    }
}


