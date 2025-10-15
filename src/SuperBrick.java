import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SuperBrick extends Brick {
    private Image img;

    public SuperBrick(double x, double y) {
        super(x, y, GameConstants.STRONG_BRICK_HP, GameConstants.STRONG_TYPE);
        this.img = ImgManager.getInstance().getImage("PURPLE_BRICK");
    }

    public void render(GraphicsContext gc) {
        if (!this.isDestroyed()) {
            gc.drawImage(this.img, this.getX(), this.getY());
        }
    }
}
