package game;

import javafx.animation.PauseTransition;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import manager.ImgManager;

public class LevelIntro {
    private final int level;
    private final Image background;
    private boolean active;
    private final PauseTransition timer;
    private final Font titleFont;

    public LevelIntro(int level, Runnable onFinish, Font titleFont) {
        this.level = level;
        this.active = true;
        this.background = ImgManager.getInstance().getImage("INTRO_BG");
        this.titleFont = titleFont;

        this.timer = new PauseTransition(Duration.seconds(3));
        // Tự động tắt sau 3 giây
        this.timer.setOnFinished(e -> {
            active = false;
            if (onFinish != null) onFinish.run();
        });
        this.timer.play();
    }

    public boolean isActive() {
        return active;
    }


    public void render(GraphicsContext gc) {
        gc.drawImage(background, 0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);

        gc.setFill(Color.web("#F5E027"));
        gc.setFont(titleFont);
        gc.fillText("LEVEL " + (level), 400, 320);

        gc.setFill(Color.web("#F5E027"));
        gc.fillText("GET READY!", 370, 390);
    }
}
