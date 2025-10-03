import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball extends MovableObject {
    public Ball(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.setDy(2);
        this.setDx(2);
    }

    public void checkCollisionWall(Canvas canvas) {
        if (this.getX() <= 0 || this.getX() + this.getWidth() >= canvas.getWidth()) {
            this.setDx(-this.getDx());
        }

        if (this.getY() <= 0 || this.getY() + this.getHeight() >= canvas.getHeight()) {
            this.setDy(-this.getDy());
        }
    }

    public void checkCollision(GameObject other) {
        boolean collision = this.getX() < other.getX() + other.getWidth() &&
                            this.getX() + this.getWidth() > other.getX() &&
                            this.getY() < other.getY() + other.getHeight() &&
                            this.getY() + this.getHeight() > other.getY();

        if (collision) {
            double overlapX = Math.min(this.getX() + this.getWidth() - other.getX(),
                                        other.getX() + other.getWidth() - this.getX());
            double overlapY = Math.min(this.getY() + this.getHeight() - other.getY(),
                                        other.getY() + other.getHeight() - this.getY());

            if (overlapX < overlapY) {
                this.setDx(-this.getDx());
            }
            else {
                this.setDy(-this.getDy());
            }
        }
    }

    public void render(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.fillOval(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
