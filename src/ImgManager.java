import java.net.URL;
import java.util.Map;
import java.util.HashMap;

import javafx.scene.image.Image;
import javafx.scene.media.Media;

public class ImgManager {
    private static volatile ImgManager instance;
    private Map<String, Image> textures = new HashMap<>();

    private ImgManager() {
        textures = new HashMap<>();
        loadTextures();
    }

    private void loadTextures() {
        textures.put("RED_BRICK", Load.loadImage("07-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("RED_BRICK_", Load.loadImage("08-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("BLUE_BRICK", Load.loadImage("01-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("BLUE_BRICK_", Load.loadImage("02-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("GREEN_BRICK", Load.loadImage("03-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("GREEN_BRICK_", Load.loadImage("04-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("PURPLE_BRICK", Load.loadImage("05-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("PURPLE_BRICK_", Load.loadImage("06-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("EXTRA_BALL", Load.loadImage("43-Breakout-Tiles.png", GameConstants.POWERUP_WIDTH, 0, true, false));
        textures.put("LASER", Load.loadImage("48-Breakout-Tiles.png", GameConstants.POWERUP_WIDTH, 0, true, false));
        textures.put("EXPAND_PADDLE", Load.loadImage("56-Breakout-Tiles.png", GameConstants.EXTRAPADDLE_WIDTH, 0, true, false));
        textures.put("HEART", Load.loadImage("60-Breakout-Tiles.png", GameConstants.HEART_WIDTH, 0, true, false));
        textures.put("BULLET", Load.loadImage("61-Breakout-Tiles.png", GameConstants.BULLET_WIDTH, 0, true, false));
        textures.put("PLAYER_ICON", Load.loadImage("player_icon.png", GameConstants.ICON_WIDTH, 0, true, false));
        textures.put("GAMER", Load.loadImage("gamer.png", GameConstants.ICON_WIDTH, 0, true, false));
        for (int i = 0; i < GameConstants.PADDLE_FRAMES; i++) {
            int j = 50 + i;
            Image paddleimg = Load.loadImage(j + "-Breakout-Tiles.png", GameConstants.PADDLE_WIDTH, 0, true, false);
            textures.put("PADDLE" + i, paddleimg);
        }
        textures.put("BALL", Load.loadImage("58-Breakout-Tiles.png", GameConstants.BALL_WIDTH, 0, true, false));
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

class Load {
    private static String getResourceUrl(String path) {

        URL resource = Load.class.getResource(path);

        if (resource == null) {
            // Ném RuntimeException để báo lỗi rõ ràng về file không tìm thấy
            throw new RuntimeException("ERROR: Resource file not found at classpath: " + path);
        }

        return resource.toExternalForm();
    }

    public static Image loadImage(
            String fileName,
            double requestedWidth,
            double requestedHeight,
            boolean preserveRatio,
            boolean smooth
    ) {
        try {

            String resourceUrl = getResourceUrl("/images/" + fileName);

            return new Image(
                    resourceUrl,
                    requestedWidth,
                    requestedHeight,
                    preserveRatio,
                    smooth
            );
        } catch (RuntimeException e) {
            System.err.println("Failed to load image '" + fileName + "'. Details: " + e.getMessage());
            return null;
        }
    }
}

