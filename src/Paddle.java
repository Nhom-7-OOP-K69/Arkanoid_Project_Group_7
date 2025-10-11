import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Paddle extends MovableObject {
    private double speed;
    private int currentPowerUp;

    /**
     * constructor.
     */

    public Paddle(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    /**
     * gioi han paddle trong khung hinh.
     */

    public void checkCollisionWall(Canvas canvas) {
        if (this.getX() <= 0) {
            this.setX(0);
        }
        if (this.getX() + this.getWidth() >= canvas.getWidth()) {
            this.setX(canvas.getWidth() - this.getWidth());
        }
    }

    /**
     * set speed.
     */

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * get speed.
     */

    public double getSpeed() {
        return speed;
    }

    /**
     * set current powerup.
     */
    public void setCurrentPowerUp(int currentPowerUp) {
        this.currentPowerUp = currentPowerUp;
    }

    /**
     * get current power up.
     */
    public int getCurrentPowerUp() {
        return currentPowerUp;
    }

    /**
     * di chuyen sang trai.
     */

    public void moveLeft() {
        dx = -speed;
    }

    /**
     * di chuyen sang phai
     */

    public void moveRight() {
        dx = speed;
    }

    /**
     * them suc manh moi
     */

    public void applyPowerUp() {

    }

    /**
     * ve paddle.
     */
    public void render(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillRect(this.getX(),this.getY(), this.getWidth(), this.getHeight());
    }
}
