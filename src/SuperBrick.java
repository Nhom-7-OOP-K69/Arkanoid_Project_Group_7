import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

public class SuperBrick extends Brick {
    private List<Image> img;

    public SuperBrick(double x, double y) {
        super(x, y, GameConstants.STRONG_BRICK_HP, GameConstants.STRONG_TYPE);
        img.add(ImgManager.getInstance().getImage("PURPLE_BRICK"));
        img.add(ImgManager.getInstance().getImage("PURPLE_BRICK_"));
    }

    public void render(GraphicsContext gc) {
        if (!this.isDestroyed()) {
            if (this.getHitPoints() >= GameConstants.STRONG_BRICK_HP) {
                gc.drawImage(img.get(0), this.getX(), this.getY());
            }
            else  {
                gc.drawImage(img.get(1), this.getX(), this.getY());
            }
        }
    }
}
