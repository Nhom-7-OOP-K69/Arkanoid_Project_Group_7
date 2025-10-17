import java.io.File;
import java.util.*;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.input.KeyCode;

public class GameManager extends Application {
    private static final String BUTTON_BASE_STYLE = "-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-font-size: 16px; -fx-min-width: 120px; -fx-padding: 8px; -fx-border-color: #cccccc; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-background-radius: 8px;";
    private static final String BUTTON_HOVER_STYLE = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-min-width: 120px; -fx-padding: 8px; -fx-border-color: #ffffff; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-background-radius: 8px;";

    private Stage primaryStage;
    private Scene menuScene;
    private Scene gameScene;
    private StackPane gamePane;
    private VBox pauseOverlay;
    private VBox settingsOverlay;

    private enum GameState {MENU, READY, PLAYING, PAUSED}

    private GameState currentState;

    private boolean isSoundEffectsOn;
    private boolean isMusicOn;

    // --- CÁC NÚT BẤM UI ĐỂ QUẢN LÝ ---
    private Button startButton;
    private Button soundEffectsButton;
    private Button musicButton;
    private Button pauseButton;
    private Button resumeButtonPause;
    private Button menuButtonPause;
    private Text pauseText;
    private Text countdownText;

    // <<< DỌN DẸP: Xóa các canvas không cần thiết >>>
    private Canvas canvas = new Canvas(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
    private GraphicsContext ctx = canvas.getGraphicsContext2D();

    // Entities
    private Paddle paddle;
    private Ball ball;

    // Image variables
    private Image blueBrickImg;
    private Image greenBrickImg;
    private Image purpleBrickImg;
    private Image redBrickImg;
    private Image ballImg;
    private List<Image> paddleImgs = new ArrayList<>();
    private BrickLayer brickLayer = new BrickLayer();
    private List<Brick> brickList = new ArrayList<>();

    private Score score = new Score();

    private AnimationTimer gameLoop;

    @Override
    public void start(final Stage stage) {
        this.primaryStage = stage;
        initializeStateManager();
        loadResources();
        createMenuScene();
        createGameScene();
        createGameLoop();

        primaryStage.setTitle("ARKANOID");
        primaryStage.setScene(menuScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void initializeStateManager() {
        this.currentState = GameState.MENU;
        this.isSoundEffectsOn = true;
        this.isMusicOn = true;
    }

    private void loadResources() {
        blueBrickImg = new Image("file:images/01-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false);
        greenBrickImg = new Image("file:images/03-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false);
        purpleBrickImg = new Image("file:images/05-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false);
        redBrickImg = new Image("file:images/07-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false);
        ballImg = new Image("file:images/58-Breakout-Tiles.png", GameConstants.BALL_WIDTH, 0, true, false);
        for (int i = 0; i < GameConstants.PADDLE_FRAMES; i++) {
            int j = 50 + i;
            Image paddleimg = new Image("file:images/" + j + "-Breakout-Tiles.png", GameConstants.PADDLE_WIDTH, 0, true, false);
            paddleImgs.add(paddleimg);
        }
    }

    private void createMenuScene() {
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

        startButton.setOnAction(e -> startGame());
        settingsButton.setOnAction(e -> showSettings());
        exitButton.setOnAction(e -> exitGame());

        menuLayout.getChildren().addAll(title, startButton, settingsButton, exitButton);

        createSettingsOverlay();
        menuPane.getChildren().addAll(menuLayout, settingsOverlay);

        menuScene = new Scene(menuPane, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
    }

    private void createSettingsOverlay() {
        Text settingsTitle = new Text("CÀI ĐẶT");
        settingsTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 60));
        settingsTitle.setFill(Color.WHITE);

        soundEffectsButton = new Button("Âm thanh: " + (isSoundEffectsOn ? "Bật" : "Tắt"));
        soundEffectsButton.setOnAction(e -> toggleSoundEffects());

        musicButton = new Button("Nhạc nền: " + (isMusicOn ? "Bật" : "Tắt"));
        musicButton.setOnAction(e -> toggleMusic());

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

    private void createGameScene() {
        ball = new Ball(442, 570, GameConstants.BALL_WIDTH, GameConstants.BALL_HEIGHT);
        paddle = new Paddle(390, 600, GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT);
        File file = new File("data/Level_1.txt");
        brickLayer = new BrickLayer();
        brickLayer.loadBrick(file);

        Pane topUIPanel = new Pane();
        topUIPanel.setPrefHeight(GameConstants.UI_TOP_BAR_HEIGHT);
        topUIPanel.setMaxHeight(GameConstants.UI_TOP_BAR_HEIGHT + 5);
        topUIPanel.setStyle(
                "-fx-background-color: #2D3748; " +         // Màu mới cho thanh menu
                        "-fx-border-color: #4A5568; " +             // Giữ nguyên màu đường viền
                        "-fx-border-width: 0 0 2px 0;"
        );

        pauseButton = new Button("Tạm Dừng");
        pauseButton.setOnAction(e -> pauseGame());
        Button menuButton = new Button("Về Menu");
        menuButton.setOnAction(e -> returnToMenu());

        List<Button> gameControlButtons = Arrays.asList(pauseButton, menuButton);
        for (Button btn : gameControlButtons) {
            btn.setStyle(BUTTON_BASE_STYLE);
            btn.setOnMouseEntered(e -> btn.setStyle(BUTTON_HOVER_STYLE));
            btn.setOnMouseExited(e -> btn.setStyle(BUTTON_BASE_STYLE));
        }

        HBox gameButtons = new HBox(10, pauseButton, menuButton);
        gameButtons.setPadding(new Insets(5));
        gameButtons.setAlignment(Pos.CENTER_RIGHT);
        gameButtons.setPrefSize(GameConstants.SCREEN_WIDTH, GameConstants.UI_TOP_BAR_HEIGHT);
        topUIPanel.getChildren().add(gameButtons);

        createPauseOverlay();

        gamePane = new StackPane(canvas, topUIPanel, pauseOverlay);
        gamePane.setStyle("-fx-background-color: #28313B;");
        StackPane.setAlignment(topUIPanel, Pos.TOP_CENTER);
        gameScene = new Scene(gamePane);

        gameScene.setOnKeyPressed(event -> {
            // Cho phép di chuyển paddle ở cả 2 trạng thái READY và PLAYING
            if (currentState == GameState.PLAYING || currentState == GameState.READY) {
                if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
                    paddle.moveLeft();
                } else if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                    paddle.moveRight();
                }
            }

            // Chỉ phóng bóng khi ở trạng thái READY
            if (currentState == GameState.READY && event.getCode() == KeyCode.SPACE) {
                launchBall();
            }
        });

        gameScene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A ||
                    event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                paddle.stop();
            }
        });
    }

    private void createPauseOverlay() {
        // <<< SỬA LỖI: Bỏ `Text` để gán giá trị cho biến thành viên, không tạo biến cục bộ >>>
        pauseText = new Text("TẠM DỪNG");
        pauseText.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        pauseText.setFill(Color.WHITE);

        // <<< SỬA LỖI: Bỏ `Text` để gán giá trị cho biến thành viên, không tạo biến cục bộ >>>
        countdownText = new Text();
        countdownText.setFont(Font.font("Arial", FontWeight.BOLD, 90));
        countdownText.setFill(Color.WHITE);
        countdownText.setVisible(false);

        resumeButtonPause = new Button("Tiếp Tục");
        resumeButtonPause.setOnAction(e -> startResumeCountdown());

        menuButtonPause = new Button("Về Menu Chính");
        menuButtonPause.setOnAction(e -> returnToMenu());

        String buttonStyle = "-fx-font-size: 22px; -fx-min-width: 200px; -fx-padding: 8px;";
        resumeButtonPause.setStyle(buttonStyle);
        menuButtonPause.setStyle(buttonStyle);

        pauseOverlay = new VBox(20, pauseText, countdownText, resumeButtonPause, menuButtonPause);
        pauseOverlay.setAlignment(Pos.CENTER);
        pauseOverlay.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
        pauseOverlay.setVisible(false);
    }

    private void startResumeCountdown() {
        pauseText.setVisible(false);
        resumeButtonPause.setVisible(false);
        menuButtonPause.setVisible(false);

        countdownText.setText("3");
        countdownText.setVisible(true);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> countdownText.setText("2")),
                new KeyFrame(Duration.seconds(2), e -> countdownText.setText("1")),
                new KeyFrame(Duration.seconds(3), e -> resumeGame())
        );
        timeline.play();
    }

    private void createGameLoop() {
        gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (currentState == GameState.MENU || currentState == GameState.PAUSED) {
                    lastUpdate = 0;
                    return;
                }
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }
                double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;

                update(deltaTime);
                render();
            }
        };
    }

    private void update(double deltaTime) {
        paddle.move(deltaTime);
        paddle.checkCollisionWall(canvas);

        if (currentState == GameState.READY) {
            // Cho bóng đi theo thanh đỡ
            ball.setX(paddle.getX() + (paddle.getWidth() / 2) - (ball.getWidth() / 2));
            ball.setY(paddle.getY() - ball.getHeight());
        } else if (currentState == GameState.PLAYING) {
            ball.move(deltaTime);

            brickList = brickLayer.getBrickList();

            List<Brick> bricksToRemove = new ArrayList<>();

            for (Brick brick : brickList) {
                if (ball.checkCollision(brick)) {
                    brick.takeHit();
                    if (brick.isDestroyed()) {
                        bricksToRemove.add(brick);
                        score.updateScore();
                    }
                    ball.bounceOff(brick);
                }
            }

            brickList.removeAll(bricksToRemove);

            boolean isBallLost = ball.collisionWall(canvas);
            if (isBallLost) {
                score.resetSorePlus();
                resetGame();
                return;
            }

            if (ball.checkCollision(paddle)) {
                ball.bounceOff(paddle);
                score.resetSorePlus();
            }

            System.out.println(score.getScore());
        }
    }

    private void render() {
        ctx.clearRect(0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        paddle.render(ctx);
        ball.render(ctx);
        brickLayer.render(ctx);
    }

    private void resetGame() {
        currentState = GameState.READY;
        // 1. Reset vị trí của paddle trước
        paddle.setX((double) (GameConstants.SCREEN_WIDTH - GameConstants.PADDLE_WIDTH) / 2);
        paddle.setY(GameConstants.SCREEN_HEIGHT - 100);

        // 2. Sau đó reset vị trí của ball dựa trên vị trí MỚI của paddle
//        ball.setX((double) (GameConstants.SCREEN_WIDTH - GameConstants.BALL_WIDTH) / 2);
//        ball.setY(paddle.getY() - GameConstants.BALL_HEIGHT - 5);

        System.out.println("Bóng đã reset. Nhấn Space để chơi tiếp.");
    }

    public void startGame() {
        currentState = GameState.READY; // Bắt đầu ở trạng thái chờ phóng bóng
        primaryStage.setScene(gameScene);
        gamePane.requestFocus();
        gameLoop.start();

        pauseButton.setDisable(false);
        startButton.setText("Chơi Lại");
        resetGame(); // Gọi reset để đặt bóng và paddle vào vị trí
        System.out.println("Game bắt đầu! Nhấn Space để phóng bóng.");
    }

    public void pauseGame() {
        if (currentState == GameState.PLAYING) {
            currentState = GameState.PAUSED;

            pauseText.setVisible(true);
            resumeButtonPause.setVisible(true);
            menuButtonPause.setVisible(true);
            countdownText.setVisible(false);

            pauseOverlay.setVisible(true);
            System.out.println("Game đã tạm dừng!");
        }
    }

    public void resumeGame() {
        if (currentState == GameState.PAUSED) {
            currentState = GameState.PLAYING;
            pauseOverlay.setVisible(false);

            gamePane.requestFocus();
            System.out.println("Game tiếp tục!");
        }
    }

    public void returnToMenu() {
        currentState = GameState.MENU;
        primaryStage.setScene(menuScene);
        gameLoop.stop();

        pauseButton.setDisable(true);
        startButton.setText("Bắt Đầu");
        pauseOverlay.setVisible(false);
        System.out.println("Quay về menu chính.");
    }

    public void showSettings() {
        settingsOverlay.setVisible(true);
    }

    public void hideSettings() {
        settingsOverlay.setVisible(false);
    }

    public void toggleSoundEffects() {
        isSoundEffectsOn = !isSoundEffectsOn;
        String status = isSoundEffectsOn ? "Bật" : "Tắt";
        soundEffectsButton.setText("Âm thanh: " + status);
        System.out.println("Âm thanh hiệu ứng: " + status);
    }

    public void toggleMusic() {
        isMusicOn = !isMusicOn;
        String status = isMusicOn ? "Bật" : "Tắt";
        musicButton.setText("Nhạc nền: " + status);
        System.out.println("Nhạc nền: " + status);
    }

    private void launchBall() {
        currentState = GameState.PLAYING;
        // Đặt vận tốc ban đầu cho bóng
        ball.setDy(-300); // Bay lên trên
        // Có thể cho một chút vận tốc ngang ngẫu nhiên để game thú vị hơn
        if (Math.random() < 0.5) {
            ball.setDx(-150);
        } else {
            ball.setDx(150);
        }
    }

    public void exitGame() {
        System.out.println("Thoát game!");
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}