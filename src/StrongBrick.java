import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class StrongBrick extends Brick {
    private List<Image> img;
    private Image current_img;

    public StrongBrick(double x, double y) {
        super(x, y, GameConstants.STRONG_BRICK_HP, GameConstants.STRONG_TYPE);
        img = new ArrayList<>();
        img.add(ImgManager.getInstance().getImage("RED_BRICK"));
        img.add(ImgManager.getInstance().getImage("RED_BRICK_"));
        current_img = img.getFirst();
    }

    @Override
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
