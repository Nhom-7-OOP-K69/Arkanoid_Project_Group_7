package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import game.*;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.StackPane;
import manager.AudioManager;
import manager.ImgManager;
import manager.InputHandler;
import manager.UIManager;
import object.ball.Ball;
import object.ball.BallLayer;
import object.brick.Brick;
import object.brick.BrickLayer;
import object.explosion.ExplosionLayer;
import object.paddle.Paddle;
import powerUp.PowerUpManager;
import score.Ranking;
import score.Score;

public class GameManager {

    private Stage primaryStage;
    private GameStateManager gameStateManager;
    private UIManager uiManager;
    private ImgManager imgManager = ImgManager.getInstance();
    private InputHandler inputHandler;

    private final Canvas canvas = new Canvas(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
    private final GraphicsContext ctx = canvas.getGraphicsContext2D();

    // MỚI: Khởi tạo mảng fileName với kích thước động từ hằng số
    private String[] fileName = new String[GameConstants.LEVEL];

    private Paddle paddle;
    private Ball ball; // Quả bóng "chính", được dùng để khởi tạo
    private BrickLayer brickLayer = new BrickLayer();
    private BallLayer ballLayer = new BallLayer();
    private PowerUpManager powerUpManager = new PowerUpManager(ballLayer);
    private ExplosionLayer explosionLayer = new ExplosionLayer();

    private String playerName;
    private final Score score = new Score();
    private int currentLevel = 1;

    private Scene gameOverScene;
    private GameOverScreen gameOverScreen;

    private Scene gameWinScene;
    private GameWinScreen gameWinScreen;

    private Lives lives;

    private AnimationTimer gameLoop;

    // MỚI: Hằng số để giới hạn delta time, tránh "spiral of death" khi game lag
    private static final double MAX_DELTA_TIME = 0.05; // 50ms

    // MỚI: Hằng số cho vị trí bắt đầu (thay vì magic numbers)
    private static final double PADDLE_START_Y = GameConstants.SCREEN_HEIGHT - 100;
    private static final double PADDLE_START_X = (GameConstants.SCREEN_WIDTH - GameConstants.PADDLE_WIDTH) / 2.0;
    private static final double BALL_START_Y_OFFSET = -GameConstants.BALL_HEIGHT; // Offset so với paddle


    // Phương thức được gọi bởi Main.java để khởi chạy toàn bộ game
    public void initializeAndRun(Stage stage) {
        this.primaryStage = stage;

        // Khởi tạo các trình quản lý
        this.gameStateManager = new GameStateManager();
        this.uiManager = new UIManager(this, this.gameStateManager);

        // Tải tài nguyên và tạo giao diện
        this.imgManager.loadTextures();
        this.loadFileName();
        this.uiManager.createMenuScene();
        this.createGameEntities();
        this.uiManager.createGameScene(this.canvas);

        // Khởi tạo trình xử lý input sau khi gameScene đã được tạo
        this.inputHandler = new InputHandler(uiManager.getGameScene(), this.gameStateManager, this, this.paddle);

        this.createGameLoop();
        AudioManager.getInstance().playBackgroundMusic();

        primaryStage.setTitle("ARKANOID");
        primaryStage.setScene(uiManager.getMenuScene());
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
        // TÁI CẤU TRÚC: Sử dụng hằng số/tính toán thay vì magic numbers
        double ballStartX = PADDLE_START_X + (GameConstants.PADDLE_WIDTH / 2.0) - (GameConstants.BALL_WIDTH / 2.0);
        double ballStartY = PADDLE_START_Y + BALL_START_Y_OFFSET;

        ball = new Ball(ballStartX, ballStartY, GameConstants.BALL_WIDTH, GameConstants.BALL_HEIGHT);
        this.paddle = new Paddle(PADDLE_START_X, PADDLE_START_Y, GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT);
        ballLayer.addBall(this.ball);
        lives = new Lives();
    }

    // MỚI: Phương thức gom logic reset paddle và ball, tránh lặp code
    private void resetPaddleAndBall() {
        // 1. Reset vị trí/trạng thái paddle
        paddle.setX(PADDLE_START_X);
        paddle.setY(PADDLE_START_Y);
        paddle.setWidth(GameConstants.PADDLE_WIDTH);
        paddle.setActivePowerUps(0);
        paddle.setAnimating(false);
        paddle.setAnimationProgress(0);

        // 2. Reset ball
        ballLayer.clearBall();
        double ballStartX = PADDLE_START_X + (paddle.getWidth() / 2.0) - (GameConstants.BALL_WIDTH / 2.0);
        double ballStartY = PADDLE_START_Y + BALL_START_Y_OFFSET;
        ball = new Ball(ballStartX, ballStartY, GameConstants.BALL_WIDTH, GameConstants.BALL_HEIGHT);
        ballLayer.addBall(ball);

        // 3. Xóa power-up
        powerUpManager.clearPowerUp();

        // 4. Đặt trạng thái
        gameStateManager.setCurrentState(GameStateManager.GameState.READY);

        // System.out.println("Bóng đã reset. Nhấn Space để chơi tiếp.");
    }

    // TÁI CẤU TRÚC: Phương thức reset game (khi bắt đầu game mới)
    private void resetGame() {
        currentLevel = 0;
        lives.reset();
        score.resetScore();

        brickLayer = new BrickLayer(); // Tạo lại để đảm bảo không còn gạch cũ
        brickLayer.loadBrick(fileName[currentLevel]);

        // Gọi phương thức reset chung
        resetPaddleAndBall();
    }

    // TÁI CẤU TRÚC: Phương thức reset khi mất mạng
    public void resetLaunch() {
        // Chỉ cần reset paddle và ball
        resetPaddleAndBall();
    }

    // TÁI CẤU TRÚC: Phương thức qua màn
    private void nextLevel() {
        currentLevel++;

        // Kiểm tra nếu hết màn
        if (currentLevel >= GameConstants.LEVEL) {
            // TODO: Hiển thị màn hình chiến thắng
            System.out.println("YOU WIN!");
            // Tạm thời quay về menu
            returnToMenu();
            return;
        }

        brickLayer = new BrickLayer();
        brickLayer.loadBrick(fileName[currentLevel]);

        // Gọi phương thức reset chung
        resetPaddleAndBall();

        // intro màn mới
        uiManager.showLevelIntro(currentLevel, () -> {
            gameStateManager.setCurrentState(GameStateManager.GameState.READY);
        }, uiManager.medievalFont);
    }

    private void createGameLoop() {
        gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // TÁI CẤU TRÚC: Kiểm tra trạng thái game ngay từ đầu
                if (gameStateManager.getCurrentState() == GameStateManager.GameState.MENU ||
                        gameStateManager.getCurrentState() == GameStateManager.GameState.PAUSED ||
                        gameStateManager.getCurrentState() == GameStateManager.GameState.GAME_OVER ||
                        gameStateManager.getCurrentState() == GameStateManager.GameState.GAME_WIN) {
                    lastUpdate = 0;
                    return;
                }

                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }

                double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;

                // MỚI: Giới hạn deltaTime để tránh lỗi vật lý khi lag
                if (deltaTime > MAX_DELTA_TIME) {
                    deltaTime = MAX_DELTA_TIME;
                }

                update(deltaTime);
                render();
            }
        };
    }

    // MỚI: Tách logic tính điểm ra khỏi va chạm
    private int getScoreForBrick(Brick brick) {
        switch (brick.getType()) {
            case GameConstants.NORMAL_TYPE:
                return 10;
            case GameConstants.STRONG_TYPE:
                return 20;
            case GameConstants.SUPER_TYPE:
                return 30;
            case GameConstants.EXPLOSION_TYPE:
                brickLayer.addExplosionBrick(brick);
                return 10;
            default:
                return 0;
        }
    }

    // TÁI CẤU TRÚC: Hàm va chạm gạch
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
                        // MỚI: Gọi hàm tính điểm
                        score += getScoreForBrick(brick);
                    }
                    ball.bounceOff(brick);
                }
            }
        }

        brickLayer.removeBricks(bricksToRemove);
        return score;
    }

    // TÁI CẤU TRÚC: `update` chính, giờ chỉ điều hướng
    private void update(double deltaTime) {
        uiManager.updateScoreLabel(score.getScore());
        if (uiManager.isShowingIntro()) return;

        // Cập nhật UI chung
        //uiManager.updateScoreLabel(score.getScore());

        // Xử lý logic dựa trên trạng thái
        switch (gameStateManager.getCurrentState()) {
            case READY:
                updateReadyState(deltaTime);
                break;
            case PLAYING:
                updatePlayingState(deltaTime);
                break;
            // Các trạng thái khác (PAUSED, MENU, GAME_OVER) đã được xử lý trong gameLoop
        }
    }

    // MỚI: Tách logic cho trạng thái READY
    private void updateReadyState(double deltaTime) {
        // Paddle vẫn di chuyển
        paddle.move(deltaTime);
        paddle.checkCollisionWall(canvas);

        // Ball đi theo paddle
        ball.setX(paddle.getX() + (paddle.getWidth() / 2) - (ball.getWidth() / 2));
        ball.setY(paddle.getY() + BALL_START_Y_OFFSET);

        // Vẫn cập nhật gạch và hiệu ứng nổ (nếu có)
        brickLayer.update(deltaTime);
        explosionLayer.addExplosionList(brickLayer.getExplosionList());
        explosionLayer.update(deltaTime);
        brickLayer.explosionClear();
        int explosionScore = brickLayer.processPendingExplosions();
        score.updateScore(explosionScore);
    }

    // MỚI: Tách logic cho trạng thái PLAYING
    private void updatePlayingState(double deltaTime) {
        int scorePlus = 0;

        // Cập nhật di chuyển
        paddle.move(deltaTime);
        paddle.checkCollisionWall(canvas);
        ballLayer.move(deltaTime);

        // Cập nhật gạch và hiệu ứng nổ
        brickLayer.update(deltaTime);
        explosionLayer.addExplosionList(brickLayer.getExplosionList());
        explosionLayer.update(deltaTime);
        brickLayer.explosionClear();
        scorePlus += brickLayer.processPendingExplosions();

        // Xử lý va chạm
        scorePlus += handleCollisions();

        if (brickLayer.isEmpty()) {
            if (currentLevel == GameConstants.LEVEL - 1) {
                try {
                    Ranking.saveScore(playerName, score.getScore());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                gameStateManager.setCurrentState(GameStateManager.GameState.GAME_WIN);
                showGameWinScreen(score.getScore());
                gameLoop.stop();
            } else {
                nextLevel();
            }
        }
        // Cập nhật power-up
        paddle.updateAnimation(deltaTime);
        int powerUpScore = powerUpManager.update(deltaTime, paddle, ballLayer, brickLayer);
        scorePlus += powerUpScore;

        score.updateScore(scorePlus);
        // System.out.println(score.getScore());

        // Kiểm tra điều kiện kết thúc (thua/qua màn)
        checkGameStatus();
    }

    // MỚI: Gom logic xử lý va chạm
    private int handleCollisions() {
        int brickScore = checkCollisionBricks();
        ballLayer.checkCollisionPaddle(paddle);
        ballLayer.collisionWall(canvas);
        return brickScore;
    }

    private void checkGameStatus() {
        if (ballLayer.isEmpty()) {
            lives.decreaseLife();
            if (lives.isGameOver()) {
                handleGameOver();
            } else {
                resetLaunch();
            }
        } else if (brickLayer.isEmpty()) {
            nextLevel();
        }
    }

    private void handleGameOver() {
        try {
            Ranking.saveScore(playerName, score.getScore());
        } catch (IOException e) {
            System.err.println("Không thể lưu điểm: " + e.getMessage());
        }
        gameStateManager.setCurrentState(GameStateManager.GameState.GAME_OVER);
        showGameOverScreen(score.getScore());
        gameLoop.stop();
    }


    private void render() {
        ctx.clearRect(0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);

        if (uiManager.isShowingIntro()) {
            uiManager.getCurrentIntro().render(ctx);
            return;
        }

        lives.render(ctx);
        paddle.render(ctx);
        ballLayer.render(ctx);
        brickLayer.render(ctx);
        powerUpManager.render(ctx);
        explosionLayer.render(ctx);
    }


    public void startGame() {
        gameStateManager.setCurrentState(GameStateManager.GameState.READY);
        primaryStage.setScene(uiManager.getGameScene());
        uiManager.getGamePane().requestFocus();
        uiManager.getPauseOverlay().setVisible(false);

        resetGame();
        uiManager.showLevelIntro(currentLevel, () -> {
            gameStateManager.setCurrentState(GameStateManager.GameState.READY);
        }, uiManager.medievalFont);

        gameLoop.start();
    }

    public void pauseGame() {
        if (gameStateManager.getCurrentState() == GameStateManager.GameState.PLAYING) {
            gameStateManager.setCurrentState(GameStateManager.GameState.PAUSED);
            uiManager.getPauseOverlay().setVisible(true);
        }
    }

    public void resumeGame() {
        if (gameStateManager.getCurrentState() == GameStateManager.GameState.PAUSED) {
            gameStateManager.setCurrentState(GameStateManager.GameState.PLAYING);

            // Ẩn overlay và text
            if (uiManager.getPauseOverlay() != null)
                uiManager.getPauseOverlay().setVisible(false);

            if (uiManager.getCountdownText() != null) {
                uiManager.getCountdownText().textProperty().unbind();
                uiManager.getCountdownText().setVisible(false);
            }

            // Focus lại vào game để nhận điều khiển bàn phím
            if (uiManager.getGamePane() != null)
                uiManager.getGamePane().requestFocus();

            System.out.println("Game tiếp tục!");
        }
    }

    public void startResumeCountdown(Text countdownText) {
        if (countdownText == null) {
            System.err.println("countdownText bị null!");
            resumeGame();
            return;
        }

        countdownText.textProperty().unbind();
        countdownText.setVisible(true);
        countdownText.setText("3");

        IntegerProperty counter = new SimpleIntegerProperty(3);
        countdownText.textProperty().bind(counter.asString());

        // Dùng mảng để tránh lỗi "might not be initialized"
        final Timeline[] timeline = new Timeline[1];

        timeline[0] = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    counter.set(counter.get() - 1);

                    if (counter.get() <= 0) {
                        timeline[0].stop();
                        countdownText.textProperty().unbind();
                        countdownText.setVisible(false);
                        resumeGame();
                    }
                })
        );

        timeline[0].setCycleCount(Animation.INDEFINITE);
        timeline[0].playFromStart();

        System.out.println("Bắt đầu đếm ngược để tiếp tục game...");
    }

    public void returnToMenu() {
        gameStateManager.setCurrentState(GameStateManager.GameState.MENU);
        primaryStage.setScene(uiManager.getMenuScene());
        gameLoop.stop();

        uiManager.getCountdownText().setVisible(false);

    }

    public void launchBall() {
        // Chỉ phóng khi đang READY
        if (gameStateManager.getCurrentState() == GameStateManager.GameState.READY) {
            gameStateManager.setCurrentState(GameStateManager.GameState.PLAYING);
            ballLayer.launch();
        }
    }

    public void exitGame() {
        System.out.println("Thoát game!");
        imgManager.shutdown();
        Platform.exit();
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void showGameOverScreen(int finalScore) {
        gameOverScreen = new GameOverScreen(this, finalScore);
        StackPane root = new StackPane(gameOverScreen);
        gameOverScene = new Scene(root, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        primaryStage.setScene(gameOverScene);
    }
    public void showGameWinScreen(int finalScore) {
        gameWinScreen = new GameWinScreen(this, finalScore);
        StackPane root = new StackPane(gameWinScreen);
        gameWinScene = new Scene(root, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        primaryStage.setScene(gameWinScene);
    }
}