import java.util.Map;
import java.util.HashMap;

import javafx.scene.image.Image;

public class ImgManager {
    private static volatile ImgManager instance;
    private Map<String, Image> textures = new HashMap<>();

    private ImgManager() {
        textures = new HashMap<>();
        loadTextures();
    }

    private void loadTextures() {
        textures.put("RED_BRICK", new Image("file:images/07-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("RED_BRICK_", new Image("file:images/08-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("BLUE_BRICK", new Image("file:images/01-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("BLUE_BRICK_", new Image("file:images/02-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("GREEN_BRICK", new Image("file:images/03-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("GREEN_BRICK_", new Image("file:images/04-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("PURPLE_BRICK", new Image("file:images/05-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("PURPLE_BRICK_", new Image("file:images/06-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("EXTRA_BALL",new Image("file:images/43-Breakout-Tiles.png", GameConstants.POWERUP_WIDTH, 0, true, false));
        textures.put("LASER",new Image("file:images/48-Breakout-Tiles.png", GameConstants.POWERUP_WIDTH, 0, true, false));
        textures.put("EXPAND_PADDLE",new Image("file:images/56-Breakout-Tiles.png", GameConstants.EXTRAPADDLE_WIDTH, 0, true, false));
        textures.put("HEART",new Image("file:images/60-Breakout-Tiles.png", GameConstants.HEART_WIDTH, 0, true, false));
        textures.put("BULLET",new Image("file:images/61-Breakout-Tiles.png", GameConstants.BULLET_WIDTH, 0, true, false));
        for (int i = 0; i < GameConstants.PADDLE_FRAMES; i++) {
            int j = 50 + i;
            Image paddleimg = new Image("file:images/" + j + "-Breakout-Tiles.png", GameConstants.PADDLE_WIDTH, 0, true, false);
            textures.put("PADDLE" + i, paddleimg);
        }
        textures.put("BALL", new Image("file:images/58-Breakout-Tiles.png", GameConstants.BALL_WIDTH, 0, true, false));
    }

    public Image getImage(String id) {
        return textures.get(id);
    }

    public static ImgManager getInstance() {
        if (instance == null) {
            synchronized (ImgManager.class) {
                if (instance == null) {
                    instance = new ImgManager();
                }
            }
        }
        return instance;
    }
}
