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
        textures.put("BLUE_BRICK", new Image("file:images/01-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("GREEN_BRICK", new Image("file:images/03-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("PURPLE_BRICK", new Image("file:images/05-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
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
