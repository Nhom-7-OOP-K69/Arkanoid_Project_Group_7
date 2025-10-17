// File: UIManager.java
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.Arrays;
import java.util.List;

public class UIManager {
    private static final String BUTTON_BASE_STYLE = "-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-font-size: 16px; -fx-min-width: 120px; -fx-padding: 8px; -fx-border-color: #cccccc; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-background-radius: 8px;";
    private static final String BUTTON_HOVER_STYLE = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-min-width: 120px; -fx-padding: 8px; -fx-border-color: #ffffff; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-background-radius: 8px;";

    private final GameManager gameManager;
    private final GameStateManager gameStateManager;
    public Scene menuScene, gameScene;
    public StackPane gamePane;
    public VBox pauseOverlay, settingsOverlay;
    public Button startButton, soundEffectsButton, musicButton, pauseButton, resumeButtonPause, menuButtonPause;
    public Text pauseText, countdownText;

    public UIManager(GameManager gameManager, GameStateManager gameStateManager) {
        this.gameManager = gameManager;
        this.gameStateManager = gameStateManager;
    }

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
        startButton = new Button("Bắt Đầu");
        Button settingsButton = new Button("Cài Đặt");
        Button exitButton = new Button("Thoát");
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
        Text settingsTitle = new Text("CÀI ĐẶT");
        settingsTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 60));
        settingsTitle.setFill(Color.WHITE);
        soundEffectsButton = new Button("Âm thanh: " + (gameStateManager.isSoundEffectsOn() ? "Bật" : "Tắt"));
        soundEffectsButton.setOnAction(e -> gameManager.toggleSoundEffects());
        musicButton = new Button("Nhạc nền: " + (gameStateManager.isMusicOn() ? "Bật" : "Tắt"));
        musicButton.setOnAction(e -> gameManager.toggleMusic());
        Button backButton = new Button("Quay Lại");
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

    public void createGameScene(Canvas canvasPane) {
        pauseButton = new Button("Tạm Dừng");
        pauseButton.setOnAction(e -> gameManager.pauseGame());
        Button menuButton = new Button("Về Menu");
        menuButton.setOnAction(e -> gameManager.returnToMenu());
        List<Button> gameControlButtons = Arrays.asList(pauseButton, menuButton);
        for (Button btn : gameControlButtons) {
            btn.setStyle(BUTTON_BASE_STYLE);
            btn.setOnMouseEntered(e -> btn.setStyle(BUTTON_HOVER_STYLE));
            btn.setOnMouseExited(e -> btn.setStyle(BUTTON_BASE_STYLE));
        }
        HBox topUIPanel = new HBox(10, pauseButton, menuButton);
        topUIPanel.setPadding(new Insets(5));
        topUIPanel.setAlignment(Pos.CENTER_RIGHT);
        topUIPanel.setPrefHeight(GameConstants.UI_TOP_BAR_HEIGHT);
        topUIPanel.setMaxHeight(GameConstants.UI_TOP_BAR_HEIGHT + 5);
        topUIPanel.setStyle("-fx-background-color: #2D3748; -fx-border-color: #4A5568; -fx-border-width: 0 0 2px 0;");
        createPauseOverlay();
        gamePane = new StackPane(canvasPane, topUIPanel, pauseOverlay);
        gamePane.setStyle("-fx-background-color: #28313B;");
        StackPane.setAlignment(topUIPanel, Pos.TOP_CENTER);
        gameScene = new Scene(gamePane);
    }

    private void createPauseOverlay() {
        pauseText = new Text("TẠM DỪNG");
        pauseText.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        pauseText.setFill(Color.WHITE);
        countdownText = new Text();
        countdownText.setFont(Font.font("Arial", FontWeight.BOLD, 90));
        countdownText.setFill(Color.WHITE);
        countdownText.setVisible(false);
        resumeButtonPause = new Button("Tiếp Tục");
        resumeButtonPause.setOnAction(e -> gameManager.startResumeCountdown());
        menuButtonPause = new Button("Về Menu Chính");
        menuButtonPause.setOnAction(e -> gameManager.returnToMenu());
        String buttonStyle = "-fx-font-size: 22px; -fx-min-width: 200px; -fx-padding: 8px;";
        resumeButtonPause.setStyle(buttonStyle);
        menuButtonPause.setStyle(buttonStyle);
        pauseOverlay = new VBox(20, pauseText, countdownText, resumeButtonPause, menuButtonPause);
        pauseOverlay.setAlignment(Pos.CENTER);
        pauseOverlay.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
        pauseOverlay.setVisible(false);
    }
}