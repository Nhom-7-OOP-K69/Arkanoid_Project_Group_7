import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

public class GameManager {

    // --- Các thành phần cốt lõi ---
    private Stage primaryStage;
    private GameStateManager gameStateManager;
    private UIManager uiManager;
    private InputHandler inputHandler;
    private final Canvas canvas = new Canvas(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
    private final GraphicsContext ctx = canvas.getGraphicsContext2D();

    // --- Quản lý màn chơi ---
    private String[] fileName = new String[5];
    private int currentLevel = 0;

    // --- Các đối tượng game ---
    private Paddle paddle;
    private Ball ball;
    private BrickLayer brickLayer = new BrickLayer();

    // --- Biến quản lý điểm số ---
    private int score;
    private int highScore;

    private AnimationTimer gameLoop;

    public void initializeAndRun(Stage stage) {
        this.primaryStage = stage;
        this.gameStateManager = new GameStateManager();
        this.uiManager = new UIManager(this, this.gameStateManager);

        // Tải điểm cao từ file ngay khi bắt đầu game
        this.highScore = ScoreManager.loadHighScore();

        this.loadFileName();
        this.createGameEntities();

        // Tạo các giao diện (menu và game) - Chỉ gọi 1 lần
        this.uiManager.createMenuScene();
        this.uiManager.createGameScene(this.canvas);

        // Cập nhật giao diện với điểm cao đã tải
        this.uiManager.updateHighScoreLabel(this.highScore);

        this.inputHandler = new InputHandler(uiManager.gameScene, this.gameStateManager, this, this.paddle);
        this.createGameLoop();

        primaryStage.setTitle("ARKANOID");
        primaryStage.setScene(uiManager.menuScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void createGameEntities() {
        this.ball = new Ball(442, 570, GameConstants.BALL_WIDTH, GameConstants.BALL_HEIGHT);
        this.paddle = new Paddle(390, 600, GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT);
    }

    private void loadFileName() {
        // Giả sử GameConstants.LEVEL là số màn chơi
        for (int i = 0; i < GameConstants.LEVEL; i++) {
            fileName[i] = "data/Level_" + (i + 1) + ".txt";
        }
    }

    private void addScore(int points) {
        this.score += points;
        uiManager.updateScoreLabel(this.score);
    }

    private void checkAndSaveHighScore() {
        if (score > highScore) {
            highScore = score;
            ScoreManager.saveHighScore(highScore);
            uiManager.updateHighScoreLabel(highScore);
            System.out.println("Điểm cao mới! " + highScore);
        }
    }

    private void resetGame() {
        // Kiểm tra và lưu điểm cao từ lượt chơi trước khi reset
        checkAndSaveHighScore();

        // Reset điểm hiện tại về 0
        this.score = 0;
        uiManager.updateScoreLabel(this.score);

        currentLevel = 0;
        brickLayer = new BrickLayer();
        brickLayer.loadBrick(fileName[currentLevel]);

        paddle.setX((double) (GameConstants.SCREEN_WIDTH - GameConstants.PADDLE_WIDTH) / 2);
        paddle.setY(GameConstants.SCREEN_HEIGHT - 100);

        gameStateManager.setCurrentState(GameStateManager.GameState.READY);
    }

    private void nextLevel() {
        currentLevel++;
        if (currentLevel >= GameConstants.LEVEL) {
            // Xử lý khi người chơi hoàn thành tất cả các màn
            System.out.println("Chúc mừng! Bạn đã hoàn thành game!");
            returnToMenu(); // Quay về menu sau khi thắng
            return;
        }

        brickLayer = new BrickLayer();
        brickLayer.loadBrick(fileName[currentLevel]);
        paddle.setX((double) (GameConstants.SCREEN_WIDTH - GameConstants.PADDLE_WIDTH) / 2);
        paddle.setY(GameConstants.SCREEN_HEIGHT - 100);
        gameStateManager.setCurrentState(GameStateManager.GameState.READY);
    }

    private void createGameLoop() {
        gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                // Sử dụng phương thức kiểm tra trong GameStateManager để code gọn hơn
                if (gameStateManager.isPausedOrOnMenu()) {
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

        if (gameStateManager.getCurrentState() == GameStateManager.GameState.READY) {
            ball.setX(paddle.getX() + (paddle.getWidth() / 2) - (ball.getWidth() / 2));
            ball.setY(paddle.getY() - ball.getHeight());
        } else if (gameStateManager.getCurrentState() == GameStateManager.GameState.PLAYING) {
            ball.move(deltaTime);

            List<Brick> brickList = brickLayer.getBrickList();
            List<Brick> bricksToRemove = new ArrayList<>();
            for (Brick brick : brickList) {
                if (ball.checkCollision(brick)) {
                    brick.takeHit();
                    if (brick.isDestroyed()) {
                        bricksToRemove.add(brick);
                        // Sửa logic cộng điểm để linh hoạt hơn
                        addScore(brick.getPoints());
                    }
                    ball.bounceOff(brick);
                }
            }
            brickList.removeAll(bricksToRemove);

            if (ball.checkCollision(paddle)) {
                ball.bounceOff(paddle);
            }

            boolean isBallLost = ball.collisionWall(canvas);
            if (isBallLost) {
                resetGame(); // resetGame đã bao gồm việc lưu điểm cao
                return;
            }

            if (brickLayer.isEmpty()) {
                nextLevel();
            }
        }
    }

    private void render() {
        ctx.clearRect(0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        paddle.render(ctx);
        ball.render(ctx);
        brickLayer.render(ctx);
    }

    // --- Các phương thức quản lý trạng thái game ---
    public void startGame() {
        gameStateManager.setCurrentState(GameStateManager.GameState.READY);
        primaryStage.setScene(uiManager.gameScene);
        uiManager.gamePane.requestFocus();
        gameLoop.start();
        uiManager.pauseButton.setDisable(false);
        uiManager.startButton.setText("Chơi Lại");
        resetGame(); // resetGame sẽ bắt đầu một lượt chơi mới hoàn toàn
    }

    public void pauseGame() {
        if (gameStateManager.getCurrentState() == GameStateManager.GameState.PLAYING) {
            gameStateManager.setCurrentState(GameStateManager.GameState.PAUSED);
            uiManager.pauseOverlay.setVisible(true);
        }
    }

    public void resumeGame() {
        if (gameStateManager.getCurrentState() == GameStateManager.GameState.PAUSED) {
            gameStateManager.setCurrentState(GameStateManager.GameState.PLAYING);
            uiManager.pauseOverlay.setVisible(false);
            uiManager.gamePane.requestFocus();
        }
    }

    public void returnToMenu() {
        checkAndSaveHighScore(); // Lưu điểm cao trước khi về menu
        gameStateManager.setCurrentState(GameStateManager.GameState.MENU);
        primaryStage.setScene(uiManager.menuScene);
        gameLoop.stop();
    }

    public void startResumeCountdown() {
        // Tinh chỉnh để không lặp lại code
        uiManager.pauseText.setVisible(false);
        uiManager.resumeButtonPause.setVisible(false);
        uiManager.menuButtonPause.setVisible(false);
        uiManager.countdownText.setText("3");
        uiManager.countdownText.setVisible(true);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> uiManager.countdownText.setText("2")),
                new KeyFrame(Duration.seconds(2), e -> uiManager.countdownText.setText("1")),
                new KeyFrame(Duration.seconds(3), e -> {
                    uiManager.countdownText.setVisible(false);
                    // Hiển thị lại các nút pause sau khi đếm ngược xong (để có thể pause lại)
                    uiManager.pauseText.setVisible(true);
                    uiManager.resumeButtonPause.setVisible(true);
                    uiManager.menuButtonPause.setVisible(true);
                    resumeGame();
                })
        );
        timeline.play();
    }

    public void toggleSoundEffects() {
        gameStateManager.toggleSoundEffects();
        uiManager.soundEffectsButton.setText("Sound: " + (gameStateManager.isSoundEffectsOn() ? "On" : "Off"));
    }

    public void toggleMusic() {
        gameStateManager.toggleMusic();
        uiManager.musicButton.setText("Music: " + (gameStateManager.isMusicOn() ? "On" : "Off"));
    }

    public void launchBall() {
        if(gameStateManager.getCurrentState() == GameStateManager.GameState.READY){
            gameStateManager.setCurrentState(GameStateManager.GameState.PLAYING);
            ball.setDy(-ball.getSpeed()); // Phóng bóng lên trên
        }
    }

    public void exitGame() {
        checkAndSaveHighScore(); // Lưu điểm cao trước khi thoát
        Platform.exit();
    }
}

