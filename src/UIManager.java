// File: UIManager.java (Phiên bản đã sửa đổi và tích hợp GridPane)

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
// THÊM CÁC IMPORT CẦN THIẾT CHO GRIDPANE VÀ CĂN CHỈNH
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.ColumnConstraints;
// END CÁC IMPORT CẦN THIẾT
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image; // Import cho Image
import javafx.scene.image.ImageView; // Import cho ImageView
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Optional;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Font; // Import này nếu bạn muốn nhúng font
import javafx.util.Duration;

import java.util.Optional;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UIManager {
    private VBox nameInputOverlay;
    private TextField nameField;

    private static final Font CUSTOM_BASE_FONT = FontManager.getInstance().getFont();
    private static final String CUSTOM_FONT_FAMILY = CUSTOM_BASE_FONT.getFamily();
    private static final String FONT_CSS = "-fx-font-family: \"" + CUSTOM_FONT_FAMILY + "\"; ";

    // Cập nhật các style để bao gồm font mới
    private static final String BUTTON_BASE_STYLE = FONT_CSS + "-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-font-size: 16px; -fx-min-width: 120px; -fx-padding: 8px; -fx-border-color: #cccccc; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-background-radius: 8px;";
    private static final String BUTTON_HOVER_STYLE = FONT_CSS + "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-min-width: 120px; -fx-padding: 8px; -fx-border-color: #ffffff; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-background-radius: 8px;";

    // Các biến GameManager và GameStateManager
    private final GameManager gameManager;
    private final GameStateManager gameStateManager;

    // Các Scene và Panes chính
    public Scene menuScene, gameScene;
    public StackPane gamePane;

    // Các Overlay và các nút liên quan
    public VBox pauseOverlay, settingsOverlay, rankingOverlay; // Thêm rankingOverlay
    public Button startButton, soundEffectsButton, musicButton, pauseButton, resumeButtonPause, menuButtonPause, rankingButton; // Thêm rankingButton
    public Text pauseText, countdownText;

    // THÊM: Biến cho danh sách xếp hạng để có thể cập nhật
    public VBox rankingListContainer;

    private Image playerIcon;

    public UIManager(GameManager gameManager, GameStateManager gameStateManager) {
        this.gameManager = gameManager;
        this.gameStateManager = gameStateManager;
        // Tải hình ảnh khi khởi tạo UIManager
        try {
            playerIcon = ImgManager.getInstance().getImage("GAMER");
        } catch (Exception e) {
            System.err.println("Lỗi khi tải biểu tượng xếp hạng: " + e.getMessage());
            // Có thể dùng một biểu tượng mặc định hoặc xử lý lỗi khác
        }
    }

    public void createMenuScene() {
        StackPane menuPane = new StackPane();
        VBox menuLayout = new VBox(25);
        menuLayout.setAlignment(Pos.CENTER);

        Stop[] stops = new Stop[]{new Stop(0, Color.web("#28313B")), new Stop(1, Color.web("#485461"))};
        LinearGradient lg = new LinearGradient(0, 0, 0, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE, stops);
        menuLayout.setBackground(new Background(new BackgroundFill(lg, CornerRadii.EMPTY, Insets.EMPTY)));

        Text title = new Text("ARKANOID");
        title.setFont(Font.font(CUSTOM_FONT_FAMILY, FontWeight.BOLD, 80));
        title.setFill(Color.WHITE);
        title.setEffect(new javafx.scene.effect.DropShadow(10, Color.BLACK));

        String menuButtonBaseStyle = FONT_CSS + "-fx-font-size: 24px; -fx-min-width: 280px; -fx-padding: 12px; " +
                BUTTON_BASE_STYLE.substring(BUTTON_BASE_STYLE.indexOf("-fx-background-color:"));
        String menuButtonHoverStyle = FONT_CSS + "-fx-font-size: 24px; -fx-min-width: 280px; -fx-padding: 12px; " +
                BUTTON_HOVER_STYLE.substring(BUTTON_HOVER_STYLE.indexOf("-fx-background-color:"));

        startButton = new Button("PLAY");
        rankingButton = new Button("RANK");
        Button settingsButton = new Button("SETTING");
        Button exitButton = new Button("EXIT");

        List<Button> buttons = Arrays.asList(startButton, rankingButton, settingsButton, exitButton);
        for (Button btn : buttons) {
            btn.setStyle(menuButtonBaseStyle);
            btn.setOnMouseEntered(e -> btn.setStyle(menuButtonHoverStyle));
            btn.setOnMouseExited(e -> btn.setStyle(menuButtonBaseStyle));
        }

        //  tạo overlay TRƯỚC khi set hành động cho nút
        createSettingsOverlay();
        createRankingOverlay();
        createNameInputOverlay();

        // Khi nhấn PLAY → hiển thị overlay nhập tên
        startButton.setOnAction(e -> showNameInputOverlay());

        rankingButton.setOnAction(e -> {
            try {
                showRanking();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        settingsButton.setOnAction(e -> showSettings());
        exitButton.setOnAction(e -> gameManager.exitGame());

        menuLayout.getChildren().addAll(title, startButton, rankingButton, settingsButton, exitButton);
        menuPane.getChildren().addAll(menuLayout, settingsOverlay, rankingOverlay, nameInputOverlay);

        menuScene = new Scene(menuPane, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
    }

    public void createNameInputOverlay() {
        Text title = new Text("ID");
        title.setFont(Font.font(CUSTOM_FONT_FAMILY, FontWeight.BOLD, 40));
        title.setFill(Color.web("#ff0033"));
        title.setEffect(new DropShadow(20, Color.web("#ff1a1a")));

        nameField = new TextField();
        nameField.setPromptText("");
        nameField.setMaxWidth(300);
        nameField.setStyle(
                "-fx-background-color: #1a1a1a;" +              // Nền tối
                        "-fx-text-fill: #ff0033;" +                     // Chữ đỏ neon
                        "-fx-prompt-text-fill: #ff6666;" +              // Màu placeholder
                        "-fx-border-color: #ff0033;" +                  // Viền đỏ
                        "-fx-border-width: 2;" +
                        "-fx-font-size: 20px;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;"
        );

        Button confirmBtn = new Button("START");
        confirmBtn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ff3333, #cc0000);" +
                        "-fx-text-fill: #fff;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 18px;" +
                        "-fx-background-radius: 10;"
        );

        // Hiệu ứng hover
        confirmBtn.setOnMouseEntered(e ->
                confirmBtn.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #ff6666, #ff0000);" +
                                "-fx-text-fill: #fff;" +
                                "-fx-font-weight: bold;" +
                                "-fx-font-size: 18px;" +
                                "-fx-background-radius: 10;"
                ));
        confirmBtn.setOnMouseExited(e ->
                confirmBtn.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #ff3333, #cc0000);" +
                                "-fx-text-fill: #fff;" +
                                "-fx-font-weight: bold;" +
                                "-fx-font-size: 18px;" +
                                "-fx-background-radius: 10;"
                ));

        confirmBtn.setOnAction(e -> {
            String playerName = nameField.getText().trim();
            if (playerName.isEmpty()) playerName = "Guest_" + (int) (Math.random() * 99);
            else if (playerName.length() > 10) playerName = playerName.substring(0, 10);
            gameManager.setPlayerName(playerName);
            hideNameInputOverlay();
            gameManager.startGame();
        });

        nameInputOverlay = new VBox(20, title, nameField, confirmBtn);
        nameInputOverlay.setAlignment(Pos.CENTER);
        nameInputOverlay.setBackground(new Background(
                new BackgroundFill(Color.rgb(0, 0, 0, 0.85), CornerRadii.EMPTY, Insets.EMPTY)
        ));
        nameInputOverlay.setVisible(false);
    }


    public VBox getNameInputOverlay() {
        return nameInputOverlay;
    }

    public void showNameInputOverlay() {
        nameInputOverlay.setOpacity(0);
        nameInputOverlay.setVisible(true);
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), nameInputOverlay);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    public void hideNameInputOverlay() {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), nameInputOverlay);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> nameInputOverlay.setVisible(false));
        fadeOut.play();
    }

    public void showRanking() throws IOException {
        updateRankingList(); // Cập nhật dữ liệu trước khi hiển thị
        rankingOverlay.setVisible(true);
    }

    public void hideRanking() {
        rankingOverlay.setVisible(false);
    }

    public void showSettings() {
        settingsOverlay.setVisible(true);
    }

    public void hideSettings() {
        settingsOverlay.setVisible(false);
    }

    private void createSettingsOverlay() {
        Text settingsTitle = new Text("SETTING");
        settingsTitle.setFont(Font.font(CUSTOM_FONT_FAMILY, FontWeight.BOLD, 60));
        settingsTitle.setFill(Color.WHITE);

        soundEffectsButton = new Button("SOUND: " + (gameStateManager.isSoundEffectsOn() ? "Bật" : "Tắt"));
        soundEffectsButton.setOnAction(e -> gameManager.toggleSoundEffects());
        musicButton = new Button("MUSIC: " + (gameStateManager.isMusicOn() ? "Bật" : "Tắt"));
        musicButton.setOnAction(e -> gameManager.toggleMusic());
        Button backButton = new Button("Quay Lại");
        backButton.setOnAction(e -> hideSettings());

        String buttonStyle = FONT_CSS + "-fx-font-size: 22px; -fx-min-width: 250px; -fx-padding: 10px;";
        soundEffectsButton.setStyle(buttonStyle);
        musicButton.setStyle(buttonStyle);
        backButton.setStyle(buttonStyle);

        settingsOverlay = new VBox(25, settingsTitle, soundEffectsButton, musicButton, backButton);
        settingsOverlay.setAlignment(Pos.CENTER);
        settingsOverlay.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.85), CornerRadii.EMPTY, Insets.EMPTY)));
        settingsOverlay.setVisible(false);
    }

    private void createRankingOverlay() {

        final double RANKING_DIALOG_WIDTH = GameConstants.SCREEN_WIDTH;
        final double RANKING_DIALOG_HEIGHT = GameConstants.SCREEN_HEIGHT;

        final double CONTENT_WIDTH = 450;
        final double SCROLL_PANE_HEIGHT = RANKING_DIALOG_HEIGHT - 100;

        // VBox chính cho Ranking Overlay
        rankingOverlay = new VBox(20);
        rankingOverlay.setMaxWidth(RANKING_DIALOG_WIDTH);
        rankingOverlay.setMaxHeight(RANKING_DIALOG_HEIGHT);
        rankingOverlay.setAlignment(Pos.CENTER);
        rankingOverlay.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.85), CornerRadii.EMPTY, Insets.EMPTY)));
        rankingOverlay.setVisible(false);

        rankingOverlay.setOnMouseClicked(event -> {

            if (event.getTarget() == rankingOverlay) {
                // Giả định rằng hideRanking() là phương thức đóng overlay
                hideRanking();
                event.consume();
            }
        });
        // *******************************************************************

        // Header "LEADERBOARD"
        HBox headerBox = new HBox(10);


        headerBox.setPrefWidth(CONTENT_WIDTH);
        headerBox.setMaxWidth(CONTENT_WIDTH);

        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(10, 20, 10, 20));
        headerBox.setStyle("-fx-background-color: #3f1a66; -fx-background-radius: 15px; -fx-border-color: #925ceb; -fx-border-width: 2px; -fx-border-radius: 15px;");

        Text leaderboardTitle = new Text("RANKING");
        leaderboardTitle.setFont(Font.font(CUSTOM_FONT_FAMILY, FontWeight.BOLD, 30));
        leaderboardTitle.setFill(Color.WHITE);
        headerBox.getChildren().add(leaderboardTitle);


        rankingListContainer = new VBox(15);
        rankingListContainer.setPadding(new Insets(20));
        rankingListContainer.setStyle("-fx-background-color: #3f1a66;");

        // Ban đầu thêm một placeholder
        Text loadingPlaceholder = new Text("Đang tải dữ liệu xếp hạng...");
        loadingPlaceholder.setFont(Font.font(CUSTOM_FONT_FAMILY, 20));
        loadingPlaceholder.setFill(Color.web("#AAAAAA"));
        rankingListContainer.getChildren().add(loadingPlaceholder);


        ScrollPane scrollPane = new ScrollPane(rankingListContainer);


        scrollPane.setMaxWidth(CONTENT_WIDTH);
        scrollPane.setMaxHeight(SCROLL_PANE_HEIGHT);
        scrollPane.setFitToWidth(true);

        scrollPane.setStyle("-fx-background-color: #3f1a66; -fx-background-radius: 10px; " +
                "-fx-border-color: #925ceb; -fx-border-width: 3px; -fx-border-radius: 10px; " +
                "-fx-effect: dropshadow(gaussian, rgba(146, 92, 235, 0.7), 15, 0.5, 0, 0);");

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        rankingOverlay.getChildren().addAll(headerBox, scrollPane);
        VBox.setMargin(scrollPane, new Insets(0, 0, 20, 0));
    }

    // PHƯƠNG THỨC MỚI: Cập nhật danh sách xếp hạng theo hình ảnh
    public void updateRankingList() throws IOException {
        rankingListContainer.getChildren().clear(); // Xóa các mục cũ và placeholder

        // Lớp Ranking cần được import (Giả định đã có)
        List<PlayerScore> scores = Ranking.getRankScores(); // Lấy PlayerScore từ lớp Ranking

        if (scores.isEmpty()) {
            Text noScoreText = new Text("Chưa có điểm xếp hạng nào.");
            noScoreText.setFont(Font.font(CUSTOM_FONT_FAMILY, 20));
            noScoreText.setFill(Color.web("#CCCCCC"));
            rankingListContainer.getChildren().add(noScoreText);
            return;
        }

        int rank = 1;
        for (PlayerScore ps : scores) {

            GridPane rankEntry = createRankingEntry(rank, ps.getName(), ps.getScore());
            rankingListContainer.getChildren().add(rankEntry);
            rank++;
        }
    }


    private GridPane createRankingEntry(int rank, String playerName, int score) {

        GridPane entry = new GridPane();
        entry.setHgap(10);
        entry.setPadding(new Insets(10, 15, 10, 15));

        // Nền cho mỗi mục (viền tím sáng)
        entry.setStyle("-fx-background-color: #2a0f4a; -fx-background-radius: 8px; " +
                "-fx-border-color: #925ceb; -fx-border-width: 1px; -fx-border-radius: 8px; " +
                "-fx-effect: dropshadow(gaussian, rgba(146, 92, 235, 0.5), 5, 0.3, 0, 0);");


        ColumnConstraints col0 = new ColumnConstraints();
        col0.setMinWidth(30);
        col0.setMaxWidth(30);
        col0.setHalignment(HPos.LEFT);


        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);
        col1.setHalignment(HPos.LEFT);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setMinWidth(80);
        col2.setHalignment(HPos.RIGHT);

        entry.getColumnConstraints().addAll(col0, col1, col2);
        // ---------------------------------------------------

        // Cột 0: Biểu tượng người chơi
        if (playerIcon != null) {
            ImageView playerImageView = new ImageView(playerIcon);
            playerImageView.setFitWidth(40);
            playerImageView.setFitHeight(40);

            entry.add(playerImageView, 0, 0);
        }


        HBox nameAndMedalBox = new HBox(5);
        nameAndMedalBox.setAlignment(Pos.CENTER_LEFT);

        // Tên người chơi
        Text nameText = new Text(playerName.toUpperCase());
        nameText.setFont(Font.font(CUSTOM_FONT_FAMILY, FontWeight.BOLD, 20));
        nameText.setFill(Color.WHITE);
        nameAndMedalBox.getChildren().add(nameText);

        entry.add(nameAndMedalBox, 1, 0);

        // Cột 2: Điểm số
        Text scoreText = new Text(String.valueOf(score));
        scoreText.setFont(Font.font(CUSTOM_FONT_FAMILY, FontWeight.BOLD, 22));
        scoreText.setFill(Color.WHITE);

        entry.add(scoreText, 2, 0);

        return entry;
    }


    public void createGameScene(Canvas canvasPane) {
        pauseButton = new Button("Tạm Dừng");
        pauseButton.setOnAction(e -> gameManager.pauseGame());
        Button menuButton = new Button("Về Menu");
        menuButton.setOnAction(e -> gameManager.returnToMenu());

        List<Button> gameControlButtons = Arrays.asList(pauseButton, menuButton);
        String gameButtonBaseStyle = FONT_CSS + BUTTON_BASE_STYLE;
        String gameButtonHoverStyle = FONT_CSS + BUTTON_HOVER_STYLE;

        for (Button btn : gameControlButtons) {
            btn.setStyle(gameButtonBaseStyle);
            btn.setOnMouseEntered(e -> btn.setStyle(gameButtonHoverStyle));
            btn.setOnMouseExited(e -> btn.setStyle(gameButtonBaseStyle));
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
        pauseText.setFont(Font.font(CUSTOM_FONT_FAMILY, FontWeight.BOLD, 60));
        pauseText.setFill(Color.WHITE);

        countdownText = new Text();
        countdownText.setFont(Font.font(CUSTOM_FONT_FAMILY, FontWeight.BOLD, 90));
        countdownText.setFill(Color.WHITE);
        countdownText.setVisible(false);

        resumeButtonPause = new Button("Tiếp Tục");
        resumeButtonPause.setOnAction(e -> gameManager.startResumeCountdown());
        menuButtonPause = new Button("Về Menu Chính");
        menuButtonPause.setOnAction(e -> gameManager.returnToMenu());

        String buttonStyle = FONT_CSS + "-fx-font-size: 22px; -fx-min-width: 200px; -fx-padding: 8px;";
        resumeButtonPause.setStyle(buttonStyle);
        menuButtonPause.setStyle(buttonStyle);

        pauseOverlay = new VBox(20, pauseText, countdownText, resumeButtonPause, menuButtonPause);
        pauseOverlay.setAlignment(Pos.CENTER);
        pauseOverlay.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
        pauseOverlay.setVisible(false);
    }
}