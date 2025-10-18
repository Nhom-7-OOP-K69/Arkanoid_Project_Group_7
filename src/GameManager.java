// File: GameManager.java
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameManager {

    private Stage primaryStage;
    private GameStateManager gameStateManager;
    private UIManager uiManager;
    private AssetManager assetManager;
    private InputHandler inputHandler;

    private Canvas canvas = new Canvas(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
    private GraphicsContext ctx = canvas.getGraphicsContext2D();

    private Paddle paddle;
    private Ball ball;
    private BrickLayer brickLayer = new BrickLayer();
    private List<Brick> brickList = new ArrayList<>();

    // ==== TEST POWER-UP ====
    private ExpandPaddlePowerUp expandPU;
    private double lastPaddleWidth = -1;
    private BulletPowerUp bulletPU;
    private List<Bullet> bullets = new ArrayList<>();

    private AnimationTimer gameLoop;

    // Phương thức được gọi bởi Main.java để khởi chạy toàn bộ game
    public void initializeAndRun(Stage stage) {
        this.primaryStage = stage;

        // Khởi tạo các trình quản lý
        this.gameStateManager = new GameStateManager();
        this.assetManager = new AssetManager();
        this.uiManager = new UIManager(this, this.gameStateManager);

        // Tải tài nguyên và tạo giao diện
        this.assetManager.loadResources();
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

    // Tạo các thực thể trong game
    private void createGameEntities() {
        this.ball = new Ball(442, 570, GameConstants.BALL_WIDTH, GameConstants.BALL_HEIGHT);
        this.paddle = new Paddle(390, 600, GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT);

    }

    // Phương thức reset game và tải lại gạch
    private void resetGame() {
        // 1. Tải lại các viên gạch từ file
        File file = new File("data/Level_1.txt");
        brickLayer = new BrickLayer(); // Tạo lại để đảm bảo không còn gạch cũ
        brickLayer.loadBrick(file);

        // 2. Reset vị trí của paddle
        paddle.setX((double) (GameConstants.SCREEN_WIDTH - GameConstants.PADDLE_WIDTH) / 2);
        paddle.setY(GameConstants.SCREEN_HEIGHT - 100);

        // 3. Đặt trạng thái về sẵn sàng
        gameStateManager.setCurrentState(GameStateManager.GameState.READY);

        System.out.println("Bóng đã reset. Nhấn Space để chơi tiếp.");
    }

    private void createGameLoop() {
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
            brickList = brickLayer.getBrickList();
            List<Brick> bricksToRemove = new ArrayList<>();
            for (Brick brick : brickList) {
                if (ball.checkCollision(brick)) {
                    brick.isDestroyed();
                    bricksToRemove.add(brick);
                    ball.bounceOff(brick);
                }
            }
            brickList.removeAll(bricksToRemove);
            boolean isBallLost = ball.collisionWall(canvas);
            if (isBallLost) {
                resetGame();
                return;
            }
            if (ball.checkCollision(paddle)) {
                ball.bounceOff(paddle);
            }
        }

        // ==== TEST POWER-UP UPDATE ====
        if (expandPU != null && expandPU.isActive()) {
            if (!expandPU.tick()) {
                expandPU.removeEffect(paddle, ball);
            }
        }

        if (bulletPU != null && bulletPU.isActive()) {
            if (!bulletPU.tick()) {
                bulletPU.removeEffect(paddle, ball);
            } else {
                // mỗi frame có thể bắn nếu đủ thời gian
                List<Bullet> newBullets = bulletPU.maybeShoot(paddle);
                bullets.addAll(newBullets);
            }
        }

        // Cập nhật vị trí đạn (nếu có)
        List<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet b : bullets) {
            b.update();
            if (b.getY() < 0) { // ra khỏi màn hình
                bulletsToRemove.add(b);
            }
        }
        bullets.removeAll(bulletsToRemove);

        if (paddle.getWidth() != lastPaddleWidth) {
            System.out.println("Paddle width thay đổi: " + paddle.getWidth());
            lastPaddleWidth = paddle.getWidth();
        }

    }

    private void render() {
        ctx.clearRect(0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        paddle.render(ctx);
        ball.render(ctx);
        brickLayer.render(ctx);

    }

    public void startGame() {
        gameStateManager.setCurrentState(GameStateManager.GameState.READY);
        primaryStage.setScene(uiManager.gameScene);
        uiManager.gamePane.requestFocus();
        gameLoop.start();
        uiManager.pauseButton.setDisable(false);
        uiManager.startButton.setText("Chơi Lại");
        resetGame();

        // Tạo mới các power-up mỗi khi bắt đầu hoặc chơi lại
        expandPU = new ExpandPaddlePowerUp(0, 0, 0, 0, 300);
        bulletPU = new BulletPowerUp(0, 0, 0, 0, 600);

        // Kích hoạt ngay Expand, sau 5 giây thì Bullet
        expandPU.applyEffect(paddle, ball);
        System.out.println("[TEST] ExpandPaddlePowerUp được kích hoạt khi bắt đầu game.");

        new Thread(() -> {
            try {
                Thread.sleep(5000); // 5 giây sau mới bắn đạn
                bulletPU.applyEffect(paddle, ball);
                System.out.println("[TEST] BulletPowerUp được kích hoạt sau 5 giây.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


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
        uiManager.startButton.setText("Bắt Đầu");
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
        ball.setDy(-300);
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
}