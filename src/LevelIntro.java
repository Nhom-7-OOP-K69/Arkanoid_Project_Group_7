import javafx.animation.PauseTransition;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.awt.*;

public class LevelIntro {
    private boolean active;
    private int level;
    private double elapsedTime;

    public LevelIntro(int level) {
        this.level = level;
        this.active = true;
        this.elapsedTime = 0;

        // Tự động tắt sau 3 giây
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> this.active = false);
        delay.play();
    }


    public boolean isActive() {
        return active;
    }

    public void update(double deltaTime) {
        if(active) {
            elapsedTime += elapsedTime;
        }
    }
}
