// File: GameManager.java
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.StackPane;

public class GameManager {

    private Stage primaryStage;
    private GameStateManager gameStateManager;
    private UIManager uiManager;
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
    private int currentLevel = 0;

    private Scene gameOverScene;
    private GameOverScreen gameOverScreen;

    private Lives lives = new Lives();

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
        this.loadFileName();
        this.uiManager.createMenuScene();
        this.createGameEntities();
        this.uiManager.createGameScene(this.canvas);

        // Khởi tạo trình xử lý input sau khi gameScene đã được tạo
        this.inputHandler = new InputHandler(uiManager.gameScene, this.gameStateManager, this, this.paddle);

        this.createGameLoop();
        AudioManager.getInstance().playBackgroundMusic();

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
        // TÁI CẤU TRÚC: Sử dụng hằng số/tính toán thay vì magic numbers
        double ballStartX = PADDLE_START_X + (GameConstants.PADDLE_WIDTH / 2.0) - (GameConstants.BALL_WIDTH / 2.0);
        double ballStartY = PADDLE_START_Y + BALL_START_Y_OFFSET;

        ball = new Ball(ballStartX, ballStartY, GameConstants.BALL_WIDTH, GameConstants.BALL_HEIGHT);
        this.paddle = new Paddle(PADDLE_START_X, PADDLE_START_Y, GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT);
        ballLayer.addBall(this.ball);
    }

    // MỚI: Phương thức gom logic reset paddle và ball, tránh lặp code
    private void resetPaddleAndBall() {
        // 1. Reset vị trí/trạng thái paddle
        paddle.setX(PADDLE_START_X);
        paddle.setY(PADDLE_START_Y);
        paddle.setWidth(GameConstants.PADDLE_WIDTH);
        paddle.activePowerUps = 0;
        paddle.isAnimating = false;
        paddle.animationProgress = 0;

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
        // System.out.println(fileName[currentLevel]);
        // System.out.println(currentLevel);

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
        }, uiManager.titleFont);
    }

    private void createGameLoop() {
        gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                // TÁI CẤU TRÚC: Kiểm tra trạng thái game ngay từ đầu
                if (gameStateManager.getCurrentState() == GameStateManager.GameState.MENU ||
                        gameStateManager.getCurrentState() == GameStateManager.GameState.PAUSED ||
                        gameStateManager.getCurrentState() == GameStateManager.GameState.GAME_OVER) {
                    lastUpdate = 0; // Reset lastUpdate khi game không chạy
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
        if (uiManager.isShowingIntro()) return;

        // Cập nhật UI chung
        uiManager.updateScoreLabel(score.getScore());

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

    // MỚI: Gom logic kiểm tra trạng thái game (thua/thắng)
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

    // MỚI: Tách logic xử lý khi Game Over
    private void handleGameOver() {
        try {
            Ranking.saveScore(playerName, score.getScore());
        } catch (IOException e) {
            // Cân nhắc hiển thị lỗi cho người dùng thay vì crash
            System.err.println("Không thể lưu điểm: " + e.getMessage());
            // throw new RuntimeException(e); // Tạm thời comment lại để game không crash
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
        // TÁI CẤU TRÚC: Đặt trạng thái trước, reset sau
        gameStateManager.setCurrentState(GameStateManager.GameState.READY);
        primaryStage.setScene(uiManager.gameScene);
        uiManager.gamePane.requestFocus();

        uiManager.pauseButton.setVisible(true);

        resetGame(); // Đã bao gồm cả việc set state về READY

        uiManager.showLevelIntro(currentLevel, () -> {
            gameStateManager.setCurrentState(GameStateManager.GameState.READY);
        }, uiManager.titleFont);

        gameLoop.start(); // Bắt đầu loop sau khi mọi thứ sẵn sàng
        // System.out.println("Game bắt đầu! Nhấn Space để phóng bóng.");
    }

    public void pauseGame() {
        if (gameStateManager.getCurrentState() == GameStateManager.GameState.PLAYING) {
            gameStateManager.setCurrentState(GameStateManager.GameState.PAUSED);
            //uiManager.showPauseOverlay(true);
            // System.out.println("Game đã tạm dừng!");
        }
    }

    public void resumeGame() {
        if (gameStateManager.getCurrentState() == GameStateManager.GameState.PAUSED) {
            // TÁI CẤU TRÚC: Đặt trạng thái về PLAYING *sau khi* đếm ngược
            // (Việc này được chuyển vào startResumeCountdown)
            // Ẩn overlay
            //uiManager.showPauseOverlay(false);
            uiManager.countdownText.setVisible(false);

            // Trả lại focus
            uiManager.gamePane.requestFocus();

            // System.out.println("Game tiếp tục!");
            // Trạng thái sẽ được set bởi startResumeCountdown
        }
    }

    public void returnToMenu() {
        gameStateManager.setCurrentState(GameStateManager.GameState.MENU);
        primaryStage.setScene(uiManager.menuScene);
        gameLoop.stop(); // Dừng game loop hoàn toàn

        // Ẩn tất cả overlay khi quay lại menu
        //uiManager.showPauseOverlay(false);
        uiManager.countdownText.setVisible(false);

        // System.out.println("Quay về menu chính.");
    }

    public void startResumeCountdown() {
        // Ẩn các nút pause, hiện text đếm ngược
        uiManager.pauseText.setVisible(false);
        uiManager.resumeButton.setVisible(false);
        uiManager.menuButton.setVisible(false);
        uiManager.countdownText.setText("3");
        uiManager.countdownText.setVisible(true);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> uiManager.countdownText.setText("2")),
                new KeyFrame(Duration.seconds(2), e -> uiManager.countdownText.setText("1")),
                new KeyFrame(Duration.seconds(3), e -> {
                    // MỚI: Chỉ set PLAYING và gọi resumeGame() khi đếm ngược kết thúc
                    gameStateManager.setCurrentState(GameStateManager.GameState.PLAYING);
                    resumeGame();
                })
        );
        timeline.play();
    }


    /* Các hàm âm thanh (giữ nguyên)
    public void toggleSoundEffects() { ... }
    public void toggleMusic() { ... }
    */

    public void launchBall() {
        // Chỉ phóng khi đang READY
        if (gameStateManager.getCurrentState() == GameStateManager.GameState.READY) {
            gameStateManager.setCurrentState(GameStateManager.GameState.PLAYING);
            ballLayer.launch();
        }
    }

    public void exitGame() {
        System.out.println("Thoát game!");
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
}