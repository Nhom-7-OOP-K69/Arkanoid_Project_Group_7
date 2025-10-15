import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
public class StrongBrick extends Brick {
    private Image img;
    public StrongBrick(double x, double y) {
        super(x, y, GameConstants.STRONG_BRICK_HP, GameConstants.STRONG_TYPE);
        this.img = ImgManager.getInstance().getImage("RED_BRICK");
    }
    public void render(GraphicsContext gc) {
        if (!this.isDestroyed()) {
            gc.drawImage(this.img, this.getX(), this.getY());
        }
    }
}
