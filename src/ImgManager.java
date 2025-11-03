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
        textures.put("PADDLE0",Load.loadImage("paddle0.png",GameConstants.PADDLE_WIDTH,0,true,false));
        textures.put("PADDLE1",Load.loadImage("paddle1.png",GameConstants.PADDLE_WIDTH,0,true,false));
        textures.put("PADDLE2",Load.loadImage("paddle2.png",GameConstants.PADDLE_WIDTH,0,true,false));
        textures.put("PADDLE3",Load.loadImage("paddle3.png",GameConstants.PADDLE_WIDTH,0,true,false));
        textures.put("BALL0", Load.loadImage("ball0.png", GameConstants.BALL_WIDTH, 0, true, false));
        textures.put("BALL1", Load.loadImage("ball1.png", GameConstants.BALL_WIDTH, 0, true, false));
        textures.put("BALL2", Load.loadImage("ball2.png", GameConstants.BALL_WIDTH, 0, true, false));
        textures.put("LIFE", Load.loadImage("heart_1.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("SILVER_BRICK", Load.loadImage("17-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("RED_BRICK", Load.loadImage("07-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("RED_BRICK_", Load.loadImage("08-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("BLUE_BRICK", Load.loadImage("01-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("BLUE_BRICK_", Load.loadImage("02-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("GREEN_BRICK", Load.loadImage("07-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("GREEN_BRICK_", Load.loadImage("08-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("PURPLE_BRICK", Load.loadImage("13-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("PURPLE_BRICK_", Load.loadImage("14-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
        textures.put("EXTRA_BALL", Load.loadImage("43-Breakout-Tiles.png", GameConstants.POWERUP_WIDTH, 0, true, false));
        textures.put("LASER", Load.loadImage("48-Breakout-Tiles.png", GameConstants.POWERUP_WIDTH, 0, true, false));
        textures.put("EXPAND_PADDLE", Load.loadImage("56-Breakout-Tiles.png", GameConstants.EXTRAPADDLE_WIDTH, 0, true, false));
        textures.put("HEART", Load.loadImage("60-Breakout-Tiles.png", GameConstants.HEART_WIDTH, 0, true, false));
        textures.put("BULLET", Load.loadImage("bullet_1.png", GameConstants.BULLET_WIDTH, 0, true, false));
        textures.put("PLAYER_ICON", Load.loadImage("player_icon.png", GameConstants.ICON_WIDTH, 0, true, false));
        textures.put("GAMER", Load.loadImage("gamer.png", GameConstants.ICON_WIDTH, 0, true, false));

        textures.put("REPLAY_NORMAl", Load.loadImage("replay.png", GameConstants.ICON_WIDTH, 0, true, false));
        textures.put("REPLAY_HOVER", Load.loadImage("replay_hover.png", GameConstants.ICON_WIDTH, 0, true, false));
        textures.put("REPLAY_PRESS", Load.loadImage("replay_press.png", GameConstants.ICON_WIDTH, 0, true, false));
        textures.put("MENU_NORMAL", Load.loadImage("options.png", GameConstants.ICON_WIDTH, 0, true, false));
        textures.put("MENU_HOVER", Load.loadImage("options_hover.png", GameConstants.ICON_WIDTH, 0, true, false));
        textures.put("MENU_PRESS", Load.loadImage("options_press.png", GameConstants.ICON_WIDTH, 0, true, false));
        textures.put("EXIT_NORMAL", Load.loadImage("exit.png", GameConstants.ICON_WIDTH, 0, true, false));
        textures.put("EXIT_HOVER", Load.loadImage("exit_hover.png", GameConstants.ICON_WIDTH, 0, true, false));
        textures.put("EXIT_PRESS", Load.loadImage("exit_press.png", GameConstants.ICON_WIDTH, 0, true, false));
        textures.put("INTRO_BG", Load.loadImage("Intro.png", GameConstants.SCREEN_WIDTH, 0, true, false));

        textures.put("STORE", Load.loadImage("store.png", GameConstants.ICON_WIDTH, 0, true, false));
        textures.put("OK_BUTTON", Load.loadImage("ok_button.png", GameConstants.OKBUTTON_WIDTH, 0, true, false));
        textures.put("BACK_BUTTON", Load.loadImage("back_button.png", GameConstants.OKBUTTON_WIDTH, 0, true, false));
        textures.put("START_BUTTON", Load.loadImage("play_button1.png", GameConstants.START_BUTTON_WIDTH, 0, true, false));
        textures.put("OPTIONS_BUTTON", Load.loadImage("options_button1.png", GameConstants.START_BUTTON_WIDTH, 0, true, false));
        textures.put("RANKING_BUTTON", Load.loadImage("ranking_button1.png", GameConstants.START_BUTTON_WIDTH, 0, true, false));
        textures.put("EXIT_BUTTON", Load.loadImage("exit_button1.png", GameConstants.START_BUTTON_WIDTH, 0, true, false));
        textures.put("SETTING_BG", Load.loadImage("setting_bg.png", GameConstants.SETTINGBG_WIDTH, 0, true, false));
        textures.put("RANKING_BG", Load.loadImage("ranking_bg.png", GameConstants.SETTINGBG_WIDTH, 0, true, false));
        textures.put("MENU_BG",Load.loadImage("menu_bg1.png", GameConstants.SCREEN_WIDTH,0, true, false));
        textures.put("GAME_BG",Load.loadImage("game_bg1.png", GameConstants.SCREEN_WIDTH,0, true, false));
        textures.put("PAUSE_BUTTON_IC",Load.loadImage("pause_button1.png",GameConstants.ICON_WIDTH,0,true,false));
        textures.put("RESUME_BUTTON_IC",Load.loadImage("resume_button1.png",GameConstants.ICON_WIDTH,0,true,false));
        textures.put("MENU_BUTTON_IC",Load.loadImage("menu_button1.png",GameConstants.ICON_WIDTH,0,true,false));

        for (int i = 0; i < 8; i++) {
            int j = 1 + i;
            Image explosionimg = Load.loadImage("HYPER_" + j + ".png", GameConstants.PADDLE_WIDTH, 0, true, false);
            textures.put("EXPLOSIONBRICK" + i, explosionimg);
        }
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
