import javafx.scene.canvas.GraphicsContext;

import javafx.scene.image.Image;

public class MovableObject extends GameObject {
    protected double dx;
    protected double dy;
    private Image image;

    public MovableObject(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDx() {
        return dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getDy() {
        return dy;
    }

    public void move(double deltaTime) {
    }
    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(image, 0, 0);
    }
}
