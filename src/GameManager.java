// File: GameManager.java
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameManager {

    private Stage primaryStage;
    private GameStateManager gameStateManager;
    private UIManager uiManager;
    private InputHandler inputHandler;

    private final Canvas canvas = new Canvas(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
    private final GraphicsContext ctx = canvas.getGraphicsContext2D();
    private String[] fileName = new String[5];

    private Paddle paddle;
    private Ball ball;
    private BrickLayer brickLayer = new BrickLayer();
    private ExplosionLayer explosionLayer = new ExplosionLayer();
    private BallLayer ballLayer = new BallLayer();
    private PowerUpManager powerUpManager = new PowerUpManager(ballLayer);

    private String playerName;
    private final Score score = new Score();
    private int currentLevel = 0;

    private AnimationTimer gameLoop;

    // Phương thức được gọi bởi Main.java để khởi chạy toàn bộ game
    public void initializeAndRun(Stage stage) {
        this.primaryStage = stage;

        // Khởi tạo các trình quản lý
        this.gameStateManager = new GameStateManager();
        this.uiManager = new UIManager(this, this.gameStateManager);

        // Tải tài nguyên và tạo giao diện
        this.loadFileName();
        this.uiManager.createMenuScene();
        this.createGameEntities();
        this.uiManager.createGameScene(this.canvas);

        // Khởi tạo trình xử lý input sau khi gameScene đã được tạo
        this.inputHandler = new InputHandler(uiManager.gameScene, this.gameStateManager, this, this.paddle);

        this.createGameLoop();

        primaryStage.setTitle("ARKANOID");
        primaryStage.setScene(uiManager.menuScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // Tạo đường dẫn cho các file txt
    public void loadFileName() {
        for (int i = 0; i < GameConstants.LEVEL; i++) {
            fileName[i] = "data/Level_" + (i + 1) + ".txt";
        }
    }

    // Tạo các thực thể trong game
    private void createGameEntities() {
        this.ball = new Ball(442, 570, GameConstants.BALL_WIDTH, GameConstants.BALL_HEIGHT);
        this.paddle = new Paddle(390, 600, GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT);
    }

    // Phương thức reset game và tải lại gạch
    private void resetGame() {
        // 1. Tải lại các viên gạch từ file
        currentLevel = 0;
        brickLayer = new BrickLayer(); // Tạo lại để đảm bảo không còn gạch cũ
        brickLayer.loadBrick(fileName[currentLevel]);
        System.out.println(fileName[currentLevel]);
        System.out.println(currentLevel);

        // 2. Reset vị trí của paddle
        paddle.setX((double) (GameConstants.SCREEN_WIDTH - GameConstants.PADDLE_WIDTH) / 2);
        paddle.setY(GameConstants.SCREEN_HEIGHT - 100);

        // 3. Đặt trạng thái về sẵn sàng
        gameStateManager.setCurrentState(GameStateManager.GameState.READY);

        score.resetScore();

        System.out.println("Bóng đã reset. Nhấn Space để chơi tiếp.");
    }

    private void nextLevel() {
        currentLevel++;

        brickLayer = new BrickLayer();
        brickLayer.loadBrick(fileName[currentLevel]);

        paddle.setX((double) (GameConstants.SCREEN_WIDTH - GameConstants.PADDLE_WIDTH) / 2);
        paddle.setY(GameConstants.SCREEN_HEIGHT - 100);

        // 3. Đặt trạng thái về sẵn sàng
        gameStateManager.setCurrentState(GameStateManager.GameState.READY);

        System.out.println("Bóng đã reset. Nhấn Space để chơi tiếp.");
    }

    private void createGameLoop() {
        AudioManager.getInstance().playSfx("GAME_MUSIC");
        gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (gameStateManager.getCurrentState() == GameStateManager.GameState.MENU || gameStateManager.getCurrentState() == GameStateManager.GameState.PAUSED) {
                    lastUpdate = 0;
                    return;
                }
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }
                double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;
                try {
                    update(deltaTime);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                render();
            }
        };
    }

    public int checkCollisionBricks() {
        int score = 0;
        List<Brick> bricksToRemove = new ArrayList<>();

        for (Brick brick : brickLayer.getBrickList()) {
            for (Ball ball : ballLayer.getBallList()) {
                if (ball.checkCollision(brick)) {
                    brick.takeHit();
                    if (brick.isDestroyed()) {
                        powerUpManager.spawnPowerUp(brick.getX(), brick.getY());
                        bricksToRemove.add(brick);
                        int brickScore;
                        switch (brick.getType()) {
                            case GameConstants.NORMAL_TYPE:
                                brickScore = 10;
                                break;
                            case GameConstants.STRONG_TYPE:
                                brickScore = 20;
                                break;
                            case GameConstants.SUPER_TYPE:
                                brickScore = 30;
                                break;
                            case GameConstants.EXPLOSION_TYPE:
                                brickScore = 10;
                                brickLayer.addExplosionBrick(brick);
                                explosionLayer.addExplosionFromBrick(brick);
                                break;
                            default:
                                brickScore = 0;
                                break;
                        }
                        score += brickScore;
                    }
                    ball.bounceOff(brick);
                }
            }
        }

        brickLayer.removeBricks(bricksToRemove);

        return score;
    }

    private void update(double deltaTime) {
        paddle.move(deltaTime);
        paddle.checkCollisionWall(canvas);

        int scorePlus = 0;

        if (gameStateManager.getCurrentState() == GameStateManager.GameState.READY) {
            ball.setX(paddle.getX() + (paddle.getWidth() / 2) - (ball.getWidth() / 2));
            ball.setY(paddle.getY() - ball.getHeight());
            brickLayer.update(deltaTime);
            explosionLayer.addExplosionList(brickLayer.getExplosionList());
            explosionLayer.update(deltaTime);
            brickLayer.explosionClear();
            scorePlus += brickLayer.processPendingExplosions();
        } else if (gameStateManager.getCurrentState() == GameStateManager.GameState.PLAYING) {
            brickLayer.update(deltaTime);

            explosionLayer.addExplosionList(brickLayer.getExplosionList());
            explosionLayer.update(deltaTime);

            brickLayer.explosionClear();

            ballLayer.move(deltaTime);

            scorePlus += checkCollisionBricks();
            scorePlus += brickLayer.processPendingExplosions();

            if (ball.checkCollision(paddle)) {
                ball.bounceOff(paddle);
            }

            boolean isBallLost = ball.collisionWall(canvas);
            if (isBallLost) {
                Ranking.saveScore(playerName,score.getScore());
                resetGame();
                return;
            }

            if (brickLayer.isEmpty()) {
                nextLevel();
            }

        }
        score.updateScore(scorePlus);
    }

    private void render() {
        ctx.clearRect(0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        paddle.render(ctx);
        ball.render(ctx);
        brickLayer.render(ctx);
        explosionLayer.render(ctx);
        powerUpManager.render(ctx);
    }

    public void startGame() {
        gameStateManager.setCurrentState(GameStateManager.GameState.READY);
        primaryStage.setScene(uiManager.gameScene);
        uiManager.gamePane.requestFocus();
        gameLoop.start();
        uiManager.pauseButton.setDisable(false);
        uiManager.startButton.setText("Chơi Lại");
        resetGame();
        System.out.println("Game bắt đầu! Nhấn Space để phóng bóng.");
    }

    public void pauseGame() {
        if (gameStateManager.getCurrentState() == GameStateManager.GameState.PLAYING) {
            gameStateManager.setCurrentState(GameStateManager.GameState.PAUSED);
            uiManager.pauseOverlay.setVisible(true);
            // Đồng bộ lại trạng thái các nút khi tạm dừng
            uiManager.pauseText.setVisible(true);
            uiManager.resumeButtonPause.setVisible(true);
            uiManager.menuButtonPause.setVisible(true);
            uiManager.countdownText.setVisible(false);
            System.out.println("Game đã tạm dừng!");
        }
    }

    public void resumeGame() {
        if (gameStateManager.getCurrentState() == GameStateManager.GameState.PAUSED) {
            gameStateManager.setCurrentState(GameStateManager.GameState.PLAYING);
            uiManager.pauseOverlay.setVisible(false);
            uiManager.gamePane.requestFocus();
            System.out.println("Game tiếp tục!");
        }
    }

    public void returnToMenu() {
        gameStateManager.setCurrentState(GameStateManager.GameState.MENU);
        primaryStage.setScene(uiManager.menuScene);
        gameLoop.stop();
        uiManager.pauseButton.setDisable(true);
        uiManager.startButton.setText("PLAY");
        uiManager.pauseOverlay.setVisible(false);
        System.out.println("Quay về menu chính.");
    }

    public void startResumeCountdown() {
        uiManager.pauseText.setVisible(false);
        uiManager.resumeButtonPause.setVisible(false);
        uiManager.menuButtonPause.setVisible(false);
        uiManager.countdownText.setText("3");
        uiManager.countdownText.setVisible(true);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> uiManager.countdownText.setText("2")),
                new KeyFrame(Duration.seconds(2), e -> uiManager.countdownText.setText("1")),
                new KeyFrame(Duration.seconds(3), e -> resumeGame())
        );
        timeline.play();
    }

    public void toggleSoundEffects() {
        gameStateManager.setSoundEffectsOn(!gameStateManager.isSoundEffectsOn());
        String status = gameStateManager.isSoundEffectsOn() ? "Bật" : "Tắt";
        uiManager.soundEffectsButton.setText("Âm thanh: " + status);
        System.out.println("Âm thanh hiệu ứng: " + status);
    }

    public void toggleMusic() {
        gameStateManager.setMusicOn(!gameStateManager.isMusicOn());
        String status = gameStateManager.isMusicOn() ? "Bật" : "Tắt";
        uiManager.musicButton.setText("Nhạc nền: " + status);
        System.out.println("Nhạc nền: " + status);
    }

    public void launchBall() {
        gameStateManager.setCurrentState(GameStateManager.GameState.PLAYING);
        ball.setDy(ball.getSpeed());
    }

    public void exitGame() {
        System.out.println("Thoát game!");
        Platform.exit();
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}