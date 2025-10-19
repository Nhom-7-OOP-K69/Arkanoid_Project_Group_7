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

/**
 * Lớp GameManager là trung tâm điều khiển của toàn bộ trò chơi.
 * Nó quản lý vòng lặp game, các đối tượng (paddle, ball, bricks),
 * trạng thái game (menu, playing, paused), và các tương tác UI.
 */
public class GameManager {

    // Các thành phần chính của JavaFX
    private Stage primaryStage;
    private Canvas canvas = new Canvas(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
    private GraphicsContext ctx = canvas.getGraphicsContext2D();

    // Các trình quản lý (Managers)
    private GameStateManager gameStateManager;
    private UIManager uiManager;
    private AssetManager assetManager;
    private InputHandler inputHandler;

    // Các đối tượng trong game
    private Paddle paddle;
    private Ball ball;
    private BrickLayer brickLayer = new BrickLayer();

    // Vòng lặp chính của game
    private AnimationTimer gameLoop;

    /**
     * Phương thức khởi tạo và chạy toàn bộ game. Được gọi từ lớp Main.
     * @param stage Stage chính của ứng dụng JavaFX.
     */
    public void initializeAndRun(Stage stage) {
        this.primaryStage = stage;

        // 1. Khởi tạo các trình quản lý cốt lõi
        this.gameStateManager = new GameStateManager();
        this.assetManager = new AssetManager();
        this.uiManager = new UIManager(this, this.gameStateManager);

        // 2. Tải tài nguyên, tạo các đối tượng game và giao diện
        this.assetManager.loadResources();
        this.createGameEntities();
        this.uiManager.createMenuScene();
        this.uiManager.createGameScene(this.canvas);

        // 3. Khởi tạo trình xử lý input sau khi các scene đã sẵn sàng
        this.inputHandler = new InputHandler(uiManager.gameScene, this.gameStateManager, this, this.paddle);

        // 4. Tạo vòng lặp game
        this.createGameLoop();

        // 5. Cấu hình và hiển thị cửa sổ game
        primaryStage.setTitle("ARKANOID");
        primaryStage.setScene(uiManager.menuScene); // Bắt đầu từ màn hình menu
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Khởi tạo các đối tượng chính trong game (Bóng và Thanh đỡ).
     */
    private void createGameEntities() {
        this.ball = new Ball(442, 570, GameConstants.BALL_WIDTH, GameConstants.BALL_HEIGHT);
        this.paddle = new Paddle(390, 600, GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT);
    }

    /**
     * Thiết lập lại trạng thái của màn chơi (khi mất bóng hoặc chơi lại).
     */
    private void resetGame() {
        // 1. Tải lại màn chơi từ file level
        File file = new File("data/Level_1.txt");
        brickLayer = new BrickLayer(); // Tạo mới để xóa hết gạch cũ
        brickLayer.loadBrick(file);

        // 2. Đặt lại vị trí ban đầu cho thanh đỡ
        paddle.setX((double) (GameConstants.SCREEN_WIDTH - GameConstants.PADDLE_WIDTH) / 2);
        paddle.setY(GameConstants.SCREEN_HEIGHT - 100);

        // 3. Chuyển game về trạng thái sẵn sàng phóng bóng
        gameStateManager.setCurrentState(GameStateManager.GameState.READY);

        System.out.println("Bóng đã reset. Nhấn Space để chơi tiếp.");
    }

    /**
     * Tạo và cấu hình AnimationTimer để tạo ra vòng lặp game (game loop).
     */
    private void createGameLoop() {
        gameLoop = new AnimationTimer() {
            private long lastUpdate = 0; // Thời điểm của lần cập nhật cuối cùng

            @Override
            public void handle(long now) {
                // Nếu đang ở Menu hoặc Paused, không cập nhật game
                if (gameStateManager.getCurrentState() == GameStateManager.GameState.MENU || gameStateManager.getCurrentState() == GameStateManager.GameState.PAUSED) {
                    lastUpdate = 0; // Reset lastUpdate để khi resume game sẽ không có bước nhảy lớn
                    return;
                }

                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }

                // Tính toán thời gian trôi qua (delta time) giữa các frame, tính bằng giây
                double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;

                // Cập nhật logic và vẽ lại game
                update(deltaTime);
                render();
            }
        };
    }

    /**
     * Cập nhật toàn bộ logic của game cho mỗi frame.
     * @param deltaTime Thời gian đã trôi qua kể từ frame cuối cùng.
     */
    private void update(double deltaTime) {
        // Luôn cập nhật di chuyển và va chạm tường của thanh đỡ
        paddle.move(deltaTime);
        paddle.checkCollisionWall(canvas);

        // Cập nhật logic dựa trên trạng thái hiện tại của game
        if (gameStateManager.getCurrentState() == GameStateManager.GameState.READY) {
            // Khi game sẵn sàng, bóng sẽ dính vào giữa thanh đỡ
            ball.setX(paddle.getX() + (paddle.getWidth() / 2) - (ball.getWidth() / 2));
            ball.setY(paddle.getY() - ball.getHeight());
        } else if (gameStateManager.getCurrentState() == GameStateManager.GameState.PLAYING) {
            ball.move(deltaTime); // Cho bóng di chuyển

            // --- Xử lý va chạm giữa bóng và gạch ---
            List<Brick> brickList = brickLayer.getBrickList();
            List<Brick> destroyedBricks = new ArrayList<>();
            for (Brick brick : brickList) {
                if (ball.checkCollision(brick)) {
                    ball.bounceOff(brick); // Xử lý nảy bóng và giảm máu của gạch
                    if (brick.isDestroyed()) {
                        destroyedBricks.add(brick); // Nếu gạch bị phá hủy, thêm vào danh sách chờ xóa
                    }
                }
            }
            // Xóa tất cả các viên gạch đã bị phá hủy khỏi danh sách chính
            brickList.removeAll(destroyedBricks);

            // --- Xử lý va chạm với tường và paddle ---
            boolean isBallLost = ball.collisionWall(canvas);
            if (isBallLost) {
                resetGame(); // Nếu bóng rơi xuống đáy, reset game
                return; // Dừng việc update frame này ngay lập tức
            }

            if (ball.checkCollision(paddle)) {
                ball.bounceOff(paddle); // Xử lý nảy bóng khỏi thanh đỡ
            }
        }
    }

    /**
     * Vẽ lại toàn bộ các đối tượng của game lên canvas.
     */
    private void render() {
        // Xóa sạch màn hình trước khi vẽ frame mới
        ctx.clearRect(0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        // Vẽ các đối tượng
        paddle.render(ctx);
        ball.render(ctx);
        brickLayer.render(ctx);
    }

    // =================================================================
    // CÁC PHƯƠNG THỨC QUẢN LÝ TRẠNG THÁI GAME (được gọi từ UIManager và InputHandler)
    // =================================================================

    public void startGame() {
        gameStateManager.setCurrentState(GameStateManager.GameState.READY);
        primaryStage.setScene(uiManager.gameScene);
        uiManager.gamePane.requestFocus(); // Yêu cầu focus để nhận input
        gameLoop.start();
        uiManager.pauseButton.setDisable(false);
        uiManager.startButton.setText("Chơi Lại");
        resetGame();
        System.out.println("Game bắt đầu! Nhấn Space để phóng bóng.");
    }

    public void pauseGame() {
        if (gameStateManager.getCurrentState() == GameStateManager.GameState.PLAYING) {
            gameStateManager.setCurrentState(GameStateManager.GameState.PAUSED);
            uiManager.pauseOverlay.setVisible(true); // Hiển thị màn hình mờ
            // Hiển thị các nút trong menu pause
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
            uiManager.pauseOverlay.setVisible(false); // Ẩn màn hình pause
            uiManager.gamePane.requestFocus();
            System.out.println("Game tiếp tục!");
        }
    }

    public void returnToMenu() {
        gameStateManager.setCurrentState(GameStateManager.GameState.MENU);
        primaryStage.setScene(uiManager.menuScene);
        gameLoop.stop();
        // Reset lại trạng thái các nút
        uiManager.pauseButton.setDisable(true);
        uiManager.startButton.setText("Bắt Đầu");
        uiManager.pauseOverlay.setVisible(false);
        System.out.println("Quay về menu chính.");
    }

    /**
     * Bắt đầu đếm ngược 3 giây trước khi tiếp tục game.
     */
    public void startResumeCountdown() {
        // Ẩn các nút pause và hiển thị text đếm ngược
        uiManager.pauseText.setVisible(false);
        uiManager.resumeButtonPause.setVisible(false);
        uiManager.menuButtonPause.setVisible(false);
        uiManager.countdownText.setText("3");
        uiManager.countdownText.setVisible(true);
        // Tạo một Timeline để đếm ngược
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> uiManager.countdownText.setText("2")),
                new KeyFrame(Duration.seconds(2), e -> uiManager.countdownText.setText("1")),
                new KeyFrame(Duration.seconds(3), e -> resumeGame()) // Sau 3 giây, gọi resumeGame
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

    /**
     * Phóng bóng từ thanh đỡ khi người dùng nhấn phím (Space).
     */
    public void launchBall() {
        // Chỉ phóng bóng khi game đang ở trạng thái READY
        if (gameStateManager.getCurrentState() == GameStateManager.GameState.READY) {
            gameStateManager.setCurrentState(GameStateManager.GameState.PLAYING);
            ball.setDy(-300); // Bắn bóng lên trên
            // Ngẫu nhiên hướng ban đầu của bóng (trái hoặc phải)
            if (Math.random() < 0.5) {
                ball.setDx(-150);
            } else {
                ball.setDx(150);
            }
        }
    }

    public void exitGame() {
        System.out.println("Thoát game!");
        Platform.exit();
    }
}
