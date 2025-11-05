package manager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.*;

import game.GameConstants;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

public class ImgManager {
    private static volatile ImgManager instance;
    private Map<String, Image> textures = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    private ImgManager() {
    }

    public void loadTextures() {
        List<Callable<Void>> tasks = new ArrayList<>();
        //=============BALL=====================//
        tasks.add(() -> {
            put("BALL", Load.loadImage("ball0.png", GameConstants.BALL_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("EXTRA_BALL", Load.loadImage("43-Breakout-Tiles.png", GameConstants.POWERUP_WIDTH, 0, true, false));
            return null;
        });

        //=============BRICK===================//
        tasks.add(() -> {
            put("SILVER_BRICK", Load.loadImage("17-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("RED_BRICK", Load.loadImage("07-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("RED_BRICK_", Load.loadImage("08-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("BLUE_BRICK", Load.loadImage("01-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("BLUE_BRICK_", Load.loadImage("02-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("GREEN_BRICK", Load.loadImage("07-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("GREEN_BRICK_", Load.loadImage("08-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("PURPLE_BRICK", Load.loadImage("13-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("PURPLE_BRICK_", Load.loadImage("14-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false));
            return null;
        });
        for (int i = 0; i < 8; i++) {
            final int index = i; // Cần final cho biến được sử dụng trong Lambda
            tasks.add(() -> {
                int j = 1 + index;
                Image explosionimg = Load.loadImage("HYPER_" + j + ".png", GameConstants.PADDLE_WIDTH, 0, true, false);
                put("EXPLOSIONBRICK" + index, explosionimg);
                return null;
            });
        }

        //==============PADDLE===============//
        tasks.add(() -> {
            put("EXPAND_PADDLE", Load.loadImage("56-Breakout-Tiles.png", GameConstants.EXTRAPADDLE_WIDTH, 0, true, false));
            return null;
        });
        for (int i = 0; i < GameConstants.PADDLE_FRAMES; i++) {
            final int index = i; // Cần final cho biến được sử dụng trong Lambda
            tasks.add(() -> {
                int j = 50 + index;
                Image paddleimg = Load.loadImage(j + "-Breakout-Tiles.png", GameConstants.PADDLE_WIDTH, 0, true, false);
                put("PADDLE" + index, paddleimg);
                return null;
            });
        }

        //=============BACKGROUND===========//
        tasks.add(() -> {
            put("SETTING_BG", Load.loadImage("setting_bg.png", GameConstants.SETTINGBG_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("RANKING_BG", Load.loadImage("ranking_bg.png", GameConstants.SETTINGBG_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("MENU_BG", Load.loadImage("menu_bg1.png", GameConstants.SCREEN_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("GAME_BG", Load.loadImage("game_bg1.png", GameConstants.SCREEN_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("INTRO_BG", Load.loadImage("Intro.png", GameConstants.SCREEN_WIDTH, 0, true, false));
            return null;
        });

        //=============BUTTON=============//
        tasks.add(() -> {
            put("REPLAY_NORMAL", Load.loadImage("replay.png", 0, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("REPLAY_HOVER", Load.loadImage("replay_hover.png", 0, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("REPLAY_PRESS", Load.loadImage("replay_press.png", 0, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("OPTIONS_NORMAL", Load.loadImage("options.png", 0, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("OPTIONS_HOVER", Load.loadImage("options_hover.png", 0, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("OPTIONS_PRESS", Load.loadImage("options_press.png", 0, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("EXIT_NORMAL", Load.loadImage("exit.png", 0, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("EXIT_HOVER", Load.loadImage("exit_hover.png", 0, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("EXIT_PRESS", Load.loadImage("exit_press.png", 0, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("STORE", Load.loadImage("store.png", GameConstants.ICON_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("OK_BUTTON", Load.loadImage("ok_button.png", GameConstants.OKBUTTON_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("BACK_BUTTON", Load.loadImage("back_button.png", GameConstants.OKBUTTON_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("START_BUTTON", Load.loadImage("play_button1.png", GameConstants.START_BUTTON_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("OPTIONS_BUTTON", Load.loadImage("options_button1.png", GameConstants.START_BUTTON_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("RANKING_BUTTON", Load.loadImage("ranking_button1.png", GameConstants.START_BUTTON_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("EXIT_BUTTON", Load.loadImage("exit_button1.png", GameConstants.START_BUTTON_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("PAUSE_BUTTON_IC", Load.loadImage("pause_button1.png", GameConstants.ICON_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("RESUME_BUTTON_IC", Load.loadImage("resume_button1.png", GameConstants.ICON_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("MENU_BUTTON_IC", Load.loadImage("menu_button1.png", GameConstants.ICON_WIDTH, 0, true, false));
            return null;
        });

        //=============ENTITY============//
        tasks.add(() -> {
            put("LIFE", Load.loadImage("heart_1.png", GameConstants.BRICK_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("LASER", Load.loadImage("48-Breakout-Tiles.png", GameConstants.POWERUP_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("LIFE", Load.loadImage("heart_1.png", GameConstants.HEART_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("BULLET", Load.loadImage("bullet_1.png", GameConstants.BULLET_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("PLAYER_ICON", Load.loadImage("player_icon.png", GameConstants.ICON_WIDTH, 0, true, false));
            return null;
        });
        tasks.add(() -> {
            put("GAMER", Load.loadImage("gamer.png", GameConstants.ICON_WIDTH, 0, true, false));
            return null;
        });
        try {
            // Thực thi tất cả task song song
            List<Future<Void>> futures = executor.invokeAll(tasks);
            // Chờ tất cả hoàn thành
            for (Future<Void> f : futures) {
                try {
                    f.get(); // block từng task để bắt lỗi (nếu có)
                } catch (ExecutionException e) {
                    System.err.println("Error while loading image: " + e.getCause());
                }
            }

            System.out.println("Tất cả ảnh đã được load");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Image loading interrupted: " + e.getMessage());
        }
    }

    private void put(String key, Image img) {
        if (img != null) {
            textures.put(key, img);
            System.out.println("Loaded: " + key);
        } else {
            System.err.println("Failed to load: " + key);
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
    public void shutdown() {
        executor.shutdown();
        textures.clear();
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
