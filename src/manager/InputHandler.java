package manager;

import game.GameManager;
import game.GameStateManager;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import object.paddle.Paddle;

import java.util.HashSet;
import java.util.Set;

public class InputHandler {

    private Set<KeyCode> activeKeys = new HashSet<>();

    public void Handle(Scene gameScene, GameStateManager gameStateManager, GameManager gameManager, Paddle paddle) {

        gameScene.setOnKeyPressed(event -> {
            GameStateManager.GameState currentState = gameStateManager.getCurrentState();
            KeyCode code = event.getCode();

            activeKeys.add(code);

            if (currentState == GameStateManager.GameState.PLAYING || currentState == GameStateManager.GameState.READY) {
                // Ưu tiên phím được nhấn sau cùng
                if (code == KeyCode.LEFT || code == KeyCode.A) {
                    paddle.moveLeft();
                } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
                    paddle.moveRight();
                }
            }

            // Xử lý phóng bóng
            if (currentState == GameStateManager.GameState.READY && code == KeyCode.SPACE) {
                gameManager.launchBall();
            }
        });

        gameScene.setOnKeyReleased(event -> {
            GameStateManager.GameState currentState = gameStateManager.getCurrentState();
            KeyCode code = event.getCode();

            // Xóa phím khỏi tập hợp
            activeKeys.remove(code);

            // Chỉ xử lý logic di chuyển nếu game đang chạy
            if (currentState == GameStateManager.GameState.PLAYING || currentState == GameStateManager.GameState.READY) {

                // Nếu phím vừa nhả là phím Trái (hoặc A)
                if (code == KeyCode.LEFT || code == KeyCode.A) {
                    // Kiểm tra xem phím Phải (hoặc D) có còn được nhấn không
                    if (activeKeys.contains(KeyCode.RIGHT) || activeKeys.contains(KeyCode.D)) {
                        paddle.moveRight();
                    } else {
                        paddle.stop();
                    }
                }
                // Nếu phím vừa nhả là phím Phải (hoặc D)
                else if (code == KeyCode.RIGHT || code == KeyCode.D) {
                    // Kiểm tra xem phím Trái (hoặc A) có còn được nhấn không
                    if (activeKeys.contains(KeyCode.LEFT) || activeKeys.contains(KeyCode.A)) {
                        paddle.moveLeft();
                    } else {
                        paddle.stop();
                    }
                }
            }
        });
    }
}