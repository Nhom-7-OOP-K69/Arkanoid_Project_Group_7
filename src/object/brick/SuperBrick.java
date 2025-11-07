package object.brick;

import game.GameConstants;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import manager.ImgManager;

import java.util.ArrayList;
import java.util.List;

public class SuperBrick extends Brick {
    private List<Image> img;

    public SuperBrick(double x, double y) {
        super(x, y, GameConstants.SUPER_BRICK_HP, GameConstants.SUPER_TYPE);
        this.img = new ArrayList<>();
        img.add(ImgManager.getInstance().getImage("YELLOW_BRICK"));
        img.add(ImgManager.getInstance().getImage("YELLOW_BRICK_"));
        img.add(ImgManager.getInstance().getImage("YELLOW_BRICK__"));
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!this.isDestroyed()) {
            if (this.getHitPoints() >= GameConstants.SUPER_BRICK_HP) {
                gc.drawImage(img.get(0), this.getX(), this.getY());
            } else if (this.getHitPoints() >= GameConstants.STRONG_BRICK_HP) {
                gc.drawImage(img.get(1), this.getX(), this.getY());
            } else  {
                gc.drawImage(img.get(2), this.getX(), this.getY());
            }
        }
    }
}
