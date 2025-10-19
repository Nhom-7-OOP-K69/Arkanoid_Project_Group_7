import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 * Lớp InputHandler chịu trách nhiệm xử lý tất cả các sự kiện đầu vào từ bàn phím của người chơi.
 * Nó lắng nghe các sự kiện nhấn và thả phím trên màn hình game (gameScene)
 * và gọi các phương thức tương ứng trong GameManager hoặc Paddle.
 */
public class InputHandler {

    /**
     * Constructor (phương thức khởi tạo) cho InputHandler.
     * Nó thiết lập các trình lắng nghe sự kiện (event listeners) cho các hành động nhấn và thả phím.
     *
     * @param gameScene        Scene của màn hình chơi game để gán các trình lắng nghe vào.
     * @param gameStateManager Trình quản lý trạng thái game, dùng để kiểm tra trạng thái hiện tại.
     * @param gameManager      Trình quản lý game chính, dùng để gọi các hành động như phóng bóng.
     * @param paddle           Đối tượng thanh đỡ để điều khiển di chuyển.
     */
    public InputHandler(Scene gameScene, GameStateManager gameStateManager, GameManager gameManager, Paddle paddle) {

        // 1. Thiết lập trình lắng nghe cho sự kiện KHI MỘT PHÍM ĐƯỢC NHẤN XUỐNG (setOnKeyPressed)
        gameScene.setOnKeyPressed(event -> {
            // Lấy trạng thái game hiện tại để quyết định hành động nào được phép.
            GameStateManager.GameState currentState = gameStateManager.getCurrentState();

            // Chỉ cho phép di chuyển thanh đỡ khi game đang ở trạng thái PLAYING hoặc READY.
            if (currentState == GameStateManager.GameState.PLAYING || currentState == GameStateManager.GameState.READY) {
                if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
                    paddle.moveLeft(); // Di chuyển thanh đỡ sang trái.
                } else if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                    paddle.moveRight(); // Di chuyển thanh đỡ sang phải.
                }
            }

            // Chỉ cho phép phóng bóng khi game ở trạng thái READY và người chơi nhấn SPACE.
            if (currentState == GameStateManager.GameState.READY && event.getCode() == KeyCode.SPACE) {
                gameManager.launchBall(); // Gọi phương thức phóng bóng.
            }
        });

        // 2. Thiết lập trình lắng nghe cho sự kiện KHI MỘT PHÍM ĐƯỢC THẢ RA (setOnKeyReleased)
        gameScene.setOnKeyReleased(event -> {
            // Kiểm tra nếu phím được thả ra là một trong các phím di chuyển.
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A ||
                    event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                paddle.stop(); // Dừng di chuyển thanh đỡ.
            }
        });
    }
}
