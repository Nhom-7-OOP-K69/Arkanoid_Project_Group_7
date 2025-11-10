package manager;

import game.GameConstants;
import game.GameManager;
import game.GameStateManager;
import game.LevelIntro;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
// THÊM CÁC IMPORT CẦN THIẾT CHO GRIDPANE VÀ CĂN CHỈNH
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
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

import javafx.util.Duration;
import score.PlayerScore;
import score.Ranking;

import java.util.Collections;
import java.util.Optional;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UIManager {
    //=============Font=============
    private static final Font CUSTOM_BASE_FONT = FontManager.getInstance().getFont();
    private static final String CUSTOM_FONT_FAMILY = CUSTOM_BASE_FONT.getFamily();
    private static final String FONT_CSS = "-fx-font-family: \"" + CUSTOM_FONT_FAMILY + "\"; ";
    private static final String FONT_PATH = "/fonts/Arka_solid.ttf";

    public final Font medievalFont = Font.loadFont(getClass().getResourceAsStream("/fonts/MedievalSharp-Book.ttf"), 24);
    public final Color Text_Color = Color.web("#b08b58");

    //============== Cập nhật các style để bao gồm font mới=============
    private static final String BUTTON_BASE_STYLE = FONT_CSS + "-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-font-size: 16px; -fx-min-width: 120px; -fx-padding: 8px; -fx-border-color: #cccccc; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-background-radius: 8px;";
    private static final String BUTTON_HOVER_STYLE = FONT_CSS + "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-min-width: 120px; -fx-padding: 8px; -fx-border-color: #ffffff; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-background-radius: 8px;";
    //===============Các biến GameManager và GameStateManager========
    private final GameManager gameManager;

    // ===============Các Scene và Panes chính===============
    private Scene menuScene, gameScene;
    private StackPane gamePane;

    //============Các Overlay và các nút liên quan==========
    private StackPane settingsOverlay;
    private VBox rankingOverlay;
    private StackPane pauseOverlay;

    private Text countdownText;

    private Label scoreLabel;

    // THÊM: Biến cho danh sách xếp hạng để có thể cập nhật
    private VBox rankingListContainer;

    //================== Khởi tạo các nút===============
    private ImageView startButton;
    private ImageView rankingButton;
    private ImageView optionsButton;
    private ImageView exitButton;
    private ImageView skinButton;
    private ImageView pauseButton_ic;
    private ImageView resumeButton_ic ;
    private ImageView menuButton_ic;
    private ImageView okButton ;
    private ImageView backButton;
    private void createButtons() {
        startButton = new ImageView(ImgManager.getInstance().getImage("START_BUTTON"));
        rankingButton = new ImageView(ImgManager.getInstance().getImage("RANKING_BUTTON"));
        optionsButton = new ImageView(ImgManager.getInstance().getImage("OPTIONS_BUTTON"));
        exitButton = new ImageView(ImgManager.getInstance().getImage("EXIT_BUTTON"));
        skinButton = new ImageView(ImgManager.getInstance().getImage("SKIN_BUTTON"));
        pauseButton_ic = new ImageView(ImgManager.getInstance().getImage("PAUSE_BUTTON_IC"));
        resumeButton_ic = new ImageView(ImgManager.getInstance().getImage("RESUME_BUTTON_IC"));
        menuButton_ic = new ImageView(ImgManager.getInstance().getImage("MENU_BUTTON_IC"));
        okButton = new ImageView(ImgManager.getInstance().getImage("OK_BUTTON"));
        backButton = new ImageView(ImgManager.getInstance().getImage("BACK_BUTTON"));
    }
    //========Khởi tạo background=======
    private ImageView setting_bg;
    private ImageView ranking_bg;
    private ImageView menu_bg;
    private ImageView game_bg;
    private void createBackgrounds() {
        setting_bg = new ImageView(ImgManager.getInstance().getImage("SETTING_BG"));
        ranking_bg = new ImageView(ImgManager.getInstance().getImage("RANKING_BG"));
        menu_bg = new ImageView(ImgManager.getInstance().getImage("MENU_BG"));
        game_bg = new ImageView(ImgManager.getInstance().getImage("GAME_BG"));
    }
    //===================Nhập tên===========================
    private VBox nameInputOverlay;
    private TextField nameField;

    //=================== Intro Scene ==================================
    private boolean isShowingIntro = true;
    private int level;
    private LevelIntro levelIntro;

    private LevelIntro currentIntro = new LevelIntro(level, () -> {
        isShowingIntro = false;
    }, medievalFont);

    public void showLevelIntro(int level, Runnable onFinish, Font titleFont) {
        levelIntro = new LevelIntro(level, onFinish, titleFont);
        currentIntro = levelIntro;
        isShowingIntro = true;
    }

    public boolean isIntroActive() {
        return levelIntro != null && levelIntro.isActive();
    }

    public void renderIntro(GraphicsContext gc) {
        if (levelIntro != null && levelIntro.isActive()) {
            levelIntro.render(gc);
        }
    }

    public boolean isShowingIntro() {
        return currentIntro != null && currentIntro.isActive();
    }

    public LevelIntro getCurrentIntro() {
        return currentIntro;
    }

    //=================================================================
    public void createMenuScene() {
        createButtons();
        createBackgrounds();
        StackPane menuPane = new StackPane();
        VBox menuLayout = new VBox(25);
        menuLayout.setAlignment(Pos.CENTER);
        StackPane.setMargin(menuLayout, new Insets(150, 0, 0, 0));

        // ⚡ Thiết lập hình nền
        menu_bg.setFitWidth(GameConstants.SCREEN_WIDTH);
        menu_bg.setFitHeight(GameConstants.SCREEN_HEIGHT);
        menu_bg.setPreserveRatio(false);
        menu_bg.setOpacity(1.0);

        Text title = new Text("ARKANOID");
        try {
            // Tải font Arka_solid.ttf từ FONT_PATH đã định nghĩa
            Font titleFont = Font.loadFont(getClass().getResourceAsStream(FONT_PATH), 80);

            if (titleFont != null) {
                title.setFont(titleFont);
            } else {
                // Fallback nếu không tải được font
                title.setFont(Font.font("Arial", FontWeight.BOLD, 80));
                System.err.println("LỖI: Không thể tải font: " + FONT_PATH);
            }
        } catch (Exception e) {
            e.printStackTrace();
            title.setFont(Font.font("Arial", FontWeight.BOLD, 80));
        }

        title.setFill(Color.WHITE);
        DropShadow redGlow = new DropShadow();
        redGlow.setColor(Color.RED);
        redGlow.setRadius(55);
        redGlow.setSpread(0.5);

        DropShadow darkShadow = new DropShadow();
        darkShadow.setOffsetY(3.0);
        darkShadow.setColor(Color.rgb(0, 0, 0, 0.5));
        darkShadow.setRadius(5);

        // Kết hợp cả hai hiệu ứng: bóng đổ trước, rồi ánh đỏ
        redGlow.setInput(darkShadow);
        title.setEffect(redGlow);

        // Tạo overlays
        createSettingsOverlay();
        createRankingOverlay();
        createNameInputOverlay();
        createPauseOverlay();


        // Set hiệu ứng hover cho các nút
        addHoverEffect(startButton);
        addHoverEffect(rankingButton);
        addHoverEffect(exitButton);
        addHoverEffect(skinButton);

        // Điều chỉnh kích thước
        scaleImageView(startButton, 0.7);
        scaleImageView(rankingButton, 0.7);
        scaleImageView(exitButton, 0.7);

        // Đặt vị trí skinButton góc phải trên
        StackPane.setAlignment(skinButton, Pos.TOP_RIGHT);
        StackPane.setMargin(skinButton, new Insets(20));
        VBox.setMargin(title, new Insets(-200, 0, 80, 0));

        // Gán sự kiện chuột
        MouseEvent();

        // Thêm các phần tử vào layout
        menuLayout.getChildren().addAll(
                title,
                startButton,
                rankingButton,
                exitButton
        );
        menuPane.getChildren().addAll(
                menu_bg,
                menuLayout,
                skinButton,
                rankingOverlay,
                nameInputOverlay,
                pauseOverlay,
                settingsOverlay
        );

        menuScene = new Scene(menuPane, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
    }


    public void createNameInputOverlay() {
        Text title = new Text("NAME");
        title.setFont(Font.font(CUSTOM_FONT_FAMILY, FontWeight.EXTRA_BOLD, 42));
        title.setFill(Color.web("#ff4d4d")); // đỏ neon sáng
        title.setEffect(new DropShadow(30, Color.web("#ff0000"))); // ánh sáng đỏ tỏa ra

// --- TextField ---
        nameField = new TextField();
        nameField.setPromptText("");
        nameField.setMaxWidth(300);
        nameField.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #3b0000, #1a0000);" + // nền đỏ sẫm
                        "-fx-text-fill: #ffeaea;" + // chữ trắng hơi hồng
                        "-fx-border-color: #ff2e00;" + // viền đỏ sáng
                        "-fx-border-width: 2;" +
                        "-fx-font-size: 20px;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;" +
                        "-fx-prompt-text-fill: #aa6666;" // màu placeholder mờ đỏ
        );

// --- Nút START ---
        Button confirmBtn = new Button("START");
        confirmBtn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #660000, #330000);" + // đỏ sẫm
                        "-fx-text-fill: #ffeaea;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 18px;" +
                        "-fx-border-color: #ff2e00;" + // viền đỏ neon
                        "-fx-border-width: 2;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, #ff0000, 15, 0.5, 0, 0);" // hiệu ứng phát sáng
        );

// --- Hover: sáng neon hơn ---
        confirmBtn.setOnMouseEntered(e ->
                confirmBtn.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #ff0000, #660000);" +
                                "-fx-text-fill: #fff;" +
                                "-fx-font-weight: bold;" +
                                "-fx-font-size: 18px;" +
                                "-fx-border-color: #ff4d4d;" +
                                "-fx-border-width: 2;" +
                                "-fx-background-radius: 10;" +
                                "-fx-border-radius: 10;" +
                                "-fx-effect: dropshadow(gaussian, #ff4d4d, 25, 0.8, 0, 0);"
                ));

        confirmBtn.setOnMouseExited(e ->
                confirmBtn.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #660000, #330000);" +
                                "-fx-text-fill: #ffeaea;" +
                                "-fx-font-weight: bold;" +
                                "-fx-font-size: 18px;" +
                                "-fx-border-color: #ff2e00;" +
                                "-fx-border-width: 2;" +
                                "-fx-background-radius: 10;" +
                                "-fx-border-radius: 10;" +
                                "-fx-effect: dropshadow(gaussian, #ff0000, 15, 0.5, 0, 0);"
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

    public void createSettingsOverlay() {
        settingsOverlay = new StackPane();
        settingsOverlay.setPrefSize(800, 600);
        settingsOverlay.setVisible(false);

        // Ảnh nền khung gỗ
        setting_bg.setFitWidth(500);
        setting_bg.setFitHeight(700);

        // Tạo Slider + Style + Giới hạn chiều rộng
        Slider soundSlider = new Slider(0, 100, 50);
        Slider sfxSlider = new Slider(0, 100, 40);

        styleSlider(soundSlider);
        styleSlider(sfxSlider);

        soundSlider.prefWidthProperty().bind(setting_bg.fitWidthProperty().multiply(0.6));
        sfxSlider.prefWidthProperty().bind(setting_bg.fitWidthProperty().multiply(0.6));
        soundSlider.maxWidthProperty().bind(setting_bg.fitWidthProperty().multiply(0.6));
        sfxSlider.maxWidthProperty().bind(setting_bg.fitWidthProperty().multiply(0.6));

        //Liên kết âm lượng với AudioManager
        soundSlider.valueProperty().addListener((obs, oldV, newV) ->
                AudioManager.getInstance().setMusicVolume(newV.doubleValue() / 100.0)
        );
        sfxSlider.valueProperty().addListener((obs, oldV, newV) ->
                AudioManager.getInstance().setSfxVolume(newV.doubleValue() / 100.0)
        );

        // Nhãn cho từng slider
        Text musicLabel = new Text("MUSIC");
        Text graphicsLabel = new Text("GRAPHICS");
        Text sfxLabel = new Text("SFX");

        // Style chữ (font, màu, bóng nhẹ)
        for (Text label : new Text[]{musicLabel, graphicsLabel, sfxLabel}) {
            label.setFont(medievalFont);
            label.setFill(Text_Color); // màu nâu phù hợp khung gỗ
            label.setEffect(new DropShadow(3, Color.rgb(255, 255, 200, 0.6)));
        }

        // Gom nhãn + slider theo cặp
        VBox musicBox = new VBox(8, musicLabel, soundSlider);
        VBox sfxBox = new VBox(8, sfxLabel, sfxSlider);

        musicBox.setAlignment(Pos.CENTER);
        sfxBox.setAlignment(Pos.CENTER);

        // Nút OK
        okButton.setFitWidth(120);
        okButton.setFitHeight(60);
        okButton.setOnMouseClicked(e -> {
            settingsOverlay.setVisible(false);
            gameManager.resumeGame();
        });

        // Nút BACK
        backButton.setFitWidth(120);
        backButton.setFitHeight(60);
        backButton.setOnMouseClicked(e -> {
            gameManager.returnToMenu();
            settingsOverlay.setVisible(false);
        });

        // HBox chứa 2 nút
        HBox buttonBox = new HBox(40, okButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        // VBox tổng chứa mọi thứ
        VBox contentBox = new VBox(50, musicBox, sfxBox, buttonBox);
        contentBox.setAlignment(Pos.CENTER);

        // Căn giữa
        StackPane.setAlignment(setting_bg, Pos.CENTER);
        StackPane.setAlignment(contentBox, Pos.CENTER);

        settingsOverlay.getChildren().addAll(setting_bg, contentBox);
    }


    //  Trang trí slider cho đồng bộ giao diện game
    private void styleSlider(Slider slider) {
        slider.setStyle(
                "-fx-control-inner-background: #b67d3d;" +
                        "-fx-base: #ffcc66;" +
                        "-fx-background-color: transparent;"
        );
    }


    private void createRankingOverlay() {
        // 1. Định nghĩa kích thước và hằng số

        // Kích thước vùng giấy (vùng chứa nội dung) trên ảnh nền
        final double CONTENT_WIDTH = 450;
        final double CONTENT_HEIGHT = GameConstants.SCREEN_HEIGHT * 0.8;

        //  Tải và thiết lập ImageView làm nền
        ranking_bg.setFitWidth(CONTENT_WIDTH);
        ranking_bg.setFitHeight(CONTENT_HEIGHT);

        //  VBox chính cho Ranking Overlay
        rankingOverlay = new VBox(20);
        rankingOverlay.setMaxWidth(GameConstants.SCREEN_WIDTH);
        rankingOverlay.setMaxHeight(GameConstants.SCREEN_HEIGHT);
        rankingOverlay.setAlignment(Pos.CENTER);
        rankingOverlay.setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 0, 0, 0.85), CornerRadii.EMPTY, Insets.EMPTY)));
        rankingOverlay.setVisible(false);

        rankingOverlay.setOnMouseClicked(event -> {
            Node targetNode = ranking_bg;
            double x = event.getX();
            double y = event.getY();
            double nodeX = targetNode.getLayoutX();
            double nodeY = targetNode.getLayoutY();
            double nodeWidth = targetNode.getBoundsInParent().getWidth();
            double nodeHeight = targetNode.getBoundsInParent().getHeight();

            if (x < nodeX || x > nodeX + nodeWidth || y < nodeY || y > nodeY + nodeHeight) {
                rankingOverlay.setVisible(false);
                event.consume();
            }
        });

        // Container cho danh sách xếp hạng
        rankingListContainer = new VBox(15);
        rankingListContainer.setPadding(new Insets(30, 40, 30, 40));

        Text loadingPlaceholder = new Text("Đang tải dữ liệu xếp hạng...");
        loadingPlaceholder.setFont(Font.font(CUSTOM_FONT_FAMILY, 20));
        loadingPlaceholder.setFill(Color.web("#AAAAAA"));
        rankingListContainer.getChildren().add(loadingPlaceholder);

        // 5. ScrollPane chứa danh sách
        ScrollPane scrollPane = new ScrollPane(rankingListContainer);
        scrollPane.setMaxWidth(CONTENT_WIDTH);
        scrollPane.setMaxHeight(CONTENT_HEIGHT * 0.8);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        //  StackPane để chồng ảnh nền và nội dung
        StackPane contentStack = new StackPane();
        contentStack.setAlignment(Pos.TOP_CENTER);

        // Đặt ảnh nền và danh sách cuộn
        contentStack.getChildren().addAll(ranking_bg, scrollPane);
        StackPane.setMargin(scrollPane, new Insets(90, 0, 0, 0));
        rankingOverlay.getChildren().add(contentStack);
    }


    // PHƯƠNG THỨC MỚI: Cập nhật danh sách xếp hạng theo hình ảnh
    public void updateRankingList() throws IOException {
        rankingListContainer.getChildren().clear(); // Xóa các mục cũ

        List<PlayerScore> scores = Ranking.getRankScores(); // Lấy dữ liệu

        if (scores.isEmpty()) {
            Text noScoreText = new Text("Chưa có điểm xếp hạng nào.");
            noScoreText.setFont(medievalFont);
            noScoreText.setFill(Text_Color);
            VBox.setMargin(noScoreText, new Insets(20, 0, 0, 0)); // Tạo khoảng trống
            rankingListContainer.getChildren().add(noScoreText);
            return;
        }

        int rank = 1;
        Collections.sort(scores);
        for (PlayerScore ps : scores) {
            GridPane rankEntry = createRankingEntry(rank, ps.getName(), ps.getScore());
            rankingListContainer.getChildren().add(rankEntry);
            rank++;
        }
    }

    /**
     * Tạo một GridPane cho một mục xếp hạng (Hạng | Tên | Điểm)
     */
    private GridPane createRankingEntry(int rank, String name, int score) {
        GridPane entry = new GridPane();
        entry.setHgap(10); // Khoảng cách giữa các cột
        entry.setPadding(new Insets(5, 0, 5, 0));
        entry.setStyle("-fx-border-color: rgba(0,0,0,0.1); -fx-border-width: 0 0 1 0; -fx-border-style: dashed;"); // Đường gạch ngang

        // Định nghĩa tỷ lệ cột
        ColumnConstraints col1 = new ColumnConstraints(); // Hạng
        col1.setPercentWidth(15);

        ColumnConstraints col2 = new ColumnConstraints(); // Tên
        col2.setPercentWidth(50); // Giảm lại một chút

        ColumnConstraints col3 = new ColumnConstraints(); // Điểm
        col3.setPercentWidth(25);

        entry.getColumnConstraints().addAll(col1, col2, col3);


        // 1. Hạng
        Text rankText = new Text(String.valueOf(rank));
        rankText.setFont(medievalFont);
        rankText.setFill(Text_Color);
        GridPane.setHalignment(rankText, javafx.geometry.HPos.CENTER);
        entry.add(rankText, 0, 0);

        // 2. Tên người chơi
        Text nameText = new Text(name);
        nameText.setFont(medievalFont);
        nameText.setFill(Text_Color);
        GridPane.setHalignment(nameText, javafx.geometry.HPos.LEFT);
        entry.add(nameText, 1, 0);

        // 3. Điểm
        Text scoreText = new Text(String.valueOf(score).replaceFirst("(\\d{1,3})(?=(?:\\d{3})+(?!\\d))", "$1,")); // Định dạng điểm
        scoreText.setFont(medievalFont);
        scoreText.setFill(Text_Color);
        GridPane.setHalignment(scoreText, javafx.geometry.HPos.RIGHT);
        entry.add(scoreText, 2, 0);

        // * Phong cách đặc biệt cho Top 3
        if (rank == 1) {
            rankText.setFont(Font.font(CUSTOM_FONT_FAMILY, FontWeight.BLACK, 30));
            rankText.setFill(Color.GOLD);
        } else if (rank == 2) {
            rankText.setFont(Font.font(CUSTOM_FONT_FAMILY, FontWeight.BLACK, 30));
            rankText.setFill(Color.SILVER);
        } else if (rank == 3) {
            rankText.setFont(Font.font(CUSTOM_FONT_FAMILY, FontWeight.BLACK, 30));
            rankText.setFill(Color.web("#CD7F32")); // Bronze
        } else {
            rankText.setFont(Font.font(CUSTOM_FONT_FAMILY, FontWeight.BLACK, 19));
            rankText.setFill(Color.web("#CD7F32"));
        }
        return entry;
    }


    public void createGameScene(Canvas canvasPane) {
        scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        VBox scoreBox = new VBox(5, scoreLabel);
        scoreBox.setAlignment(Pos.CENTER_LEFT);
        scoreBox.setPadding(new Insets(0, 0, 0, 15)); // Khoảng đệm trái

        MouseEvent();

        addHoverEffect(pauseButton_ic);
        addHoverEffect(menuButton_ic);
        addHoverEffect(resumeButton_ic);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS); // Đặt Spacer chiếm hết không gian

        HBox topUIPanel = new HBox(
                10,
                scoreBox,
                spacer,
                pauseButton_ic,
                resumeButton_ic,
                menuButton_ic
        );

        topUIPanel.setPadding(new Insets(5));
        topUIPanel.setAlignment(Pos.CENTER);
        topUIPanel.setPrefHeight(GameConstants.UI_TOP_BAR_HEIGHT);
        topUIPanel.setMaxHeight(GameConstants.UI_TOP_BAR_HEIGHT + 5);
        topUIPanel.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");

        gamePane = new StackPane(game_bg, canvasPane, topUIPanel, pauseOverlay, settingsOverlay);
        gamePane.setStyle("-fx-background-color: #000000;");
        StackPane.setAlignment(topUIPanel, Pos.TOP_CENTER);
        gameScene = new Scene(gamePane);
    }

    private void createPauseOverlay() {
        // --- Tạo Text hiển thị countdown ---
        countdownText = new Text();
        countdownText.setFont(Font.font("Arial", FontWeight.BOLD, 72));
        countdownText.setFill(Color.WHITE);
        countdownText.setVisible(false);

        // --- Layout chính (Overlay) chỉ chứa countdown ---
        pauseOverlay = new StackPane(countdownText);
        pauseOverlay.setAlignment(Pos.CENTER);

        // Làm nền hoàn toàn trong suốt
        pauseOverlay.setBackground(new Background(
                new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)
        ));

        // Quan trọng: không chặn sự kiện chuột
        pauseOverlay.setMouseTransparent(true);

        pauseOverlay.setVisible(false);
    }

    public void updateScoreLabel(int newHighScore) {
        Platform.runLater(() -> {
            if (scoreLabel != null) {
                scoreLabel.setText("Score: " + newHighScore);
            }
        });
    }

    // Hàm hỗ trợ tạo hiệu ứng (Bạn nên thêm hàm này vào lớp của mình)
    private void addHoverZoom(ImageView imageView) {
        imageView.setOnMouseEntered(e -> {
            scaleImageView(imageView, 1.0);
            imageView.setOpacity(1.3);
        });

        imageView.setOnMouseExited(e -> {
            scaleImageView(imageView, 0.7);
            imageView.setOpacity(1.0);
        });
    }

    private void addHoverEffect(ImageView imageView) {
        imageView.setOnMouseEntered(e -> imageView.setOpacity(0.7));
        imageView.setOnMouseExited(e -> imageView.setOpacity(1.0));
    }

    private void MouseEvent() {
        startButton.setOnMouseClicked(e -> showNameInputOverlay());
        optionsButton.setOnMouseClicked(e -> showSettings());
        exitButton.setOnMouseClicked(e -> gameManager.exitGame());
        menuButton_ic.setOnMouseClicked(e -> {
            showSettings();
            gameManager.pauseGame();
        });
        pauseButton_ic.setOnMouseClicked(e -> gameManager.pauseGame());
        resumeButton_ic.setOnMouseClicked(e -> gameManager.startResumeCountdown(countdownText));
        rankingButton.setOnMouseClicked(e -> {
            try {
                showRanking();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void hideRanking() {
        rankingOverlay.setVisible(false);
    }

    public void showSettings() {
        System.out.println("đã in ra");
        settingsOverlay.setVisible(true);
    }

    public void hideSettings() {
        settingsOverlay.setVisible(false);
    }

    public void scaleImageView(ImageView imageView, double rate) {
        if (imageView == null || imageView.getImage() == null) return;

        // Lấy kích thước gốc của ảnh
        double originalWidth = imageView.getImage().getWidth();
        double originalHeight = imageView.getImage().getHeight();

        // Tính lại kích thước mới theo tỉ lệ
        double newWidth = originalWidth * rate;
        double newHeight = originalHeight * rate;

        // Cập nhật lại cho ImageView
        imageView.setFitWidth(newWidth);
        imageView.setFitHeight(newHeight);
    }

    public UIManager(GameManager gameManager, GameStateManager gameStateManager) {
        this.gameManager = gameManager;
    }
    public StackPane getGamePane() {return gamePane;}
    public Scene getMenuScene() {return menuScene;}
    public Scene getGameScene() {return gameScene;}
    public StackPane getPauseOverlay() {return pauseOverlay;}
    public Text getCountdownText() {return countdownText;}
}