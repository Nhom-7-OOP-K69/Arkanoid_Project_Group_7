import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Ball extends MovableObject {
    private double speed = 300;

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public Ball(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.setDy(speed);
        this.setDx(speed);
    }

    public void collisionWall(Canvas canvas) {
        if (this.getX() <= 0 || this.getX() + this.getWidth() >= canvas.getWidth()) {
            this.setDx(-this.getDx());
            if (this.getX() <= 0) {
                this.setX(1);
            } else {
                this.setX(canvas.getWidth() - this.getWidth() - 1);
            }
        }

        if (this.getY() <= 0 || this.getY() + this.getHeight() >= canvas.getHeight()) {
            this.setDy(-this.getDy());
            if (this.getY() <= 0) {
                this.setY(1);
            } else {
                this.setY(canvas.getHeight() - this.getHeight() - 1);
            }
        }
    }

    public boolean checkCollision(GameObject other) {
        return this.getX() <= other.getX() + other.getWidth() &&
                this.getX() + this.getWidth() >= other.getX() &&
                this.getY() <= other.getY() + other.getHeight() &&
                this.getY() + this.getHeight() >= other.getY();
    }

    public void bounceOff(GameObject other) {
        double overlapX = Math.min(this.getX() + this.getWidth() - other.getX(),
                other.getX() + other.getWidth() - this.getX());
        double overlapY = Math.min(this.getY() + this.getHeight() - other.getY(),
                other.getY() + other.getHeight() - this.getY());

        if (overlapX < overlapY) {
            this.setDx(-this.getDx());

            if (this.getX() < other.getX()) {
                this.setX(this.getX() - overlapX);
            } else {
                this.setX(this.getX() + overlapX);
            }
        } else {
            this.setDy(-this.getDy());

            if (this.getY() < other.getY()) {
                this.setY(this.getY() - overlapY);
            } else {
                this.setY(this.getY() + overlapY);
            }
        }
    }

    public void move(double deltaTime) {
        this.setX(this.getX() + this.dx * deltaTime);
        this.setY(this.getY() + this.dy * deltaTime);
    }

    public void render(Image img,GraphicsContext gc) {
        gc.drawImage(img,this.getX(),this.getY());
    }
}
