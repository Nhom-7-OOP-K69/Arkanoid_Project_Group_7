// File: InputHandler.java
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class InputHandler {
    public InputHandler(Scene gameScene, GameStateManager gameStateManager, GameManager gameManager, Paddle paddle) {
        gameScene.setOnKeyPressed(event -> {
            GameStateManager.GameState currentState = gameStateManager.getCurrentState();
            if (currentState == GameStateManager.GameState.PLAYING || currentState == GameStateManager.GameState.READY) {
                if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
                    paddle.moveLeft();
                } else if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                    paddle.moveRight();
                }
            }
            if (currentState == GameStateManager.GameState.READY && event.getCode() == KeyCode.SPACE) {
                gameManager.launchBall();
            }
        });

        gameScene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A ||
                    event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                paddle.stop();
            }
        });
    }
}