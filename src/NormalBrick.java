import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class NormalBrick extends Brick {

    public NormalBrick(double x, double y) {
        super(x, y, GameConstants.NORMAL_BRICK_HP, GameConstants.NORMAL_TYPE);
    }


    public void render(GraphicsContext gc) {
        if (!this.isDestroyed()) {
            gc.setFill(Color.RED);
            gc.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        }
    }

}
