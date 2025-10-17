// File: AssetManager.java
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;

public class AssetManager {
    public Image blueBrickImg, greenBrickImg, purpleBrickImg, redBrickImg, ballImg;
    public List<Image> paddleImgs = new ArrayList<>();

    public void loadResources() {
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
}