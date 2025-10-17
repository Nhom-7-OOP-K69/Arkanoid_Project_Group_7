import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class NormalBrick extends Brick {
    private Image img;

    public NormalBrick(double x, double y) {
        super(x, y, GameConstants.NORMAL_BRICK_HP, GameConstants.NORMAL_TYPE);
        this.img = ImgManager.getInstance().getImage("GREEN_BRICK");
    }


    public void render(GraphicsContext gc) {
        if (!this.isDestroyed()) {
            gc.drawImage(this.img, this.getX(), this.getY());
        }
    }

}
