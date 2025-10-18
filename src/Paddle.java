import java.util.*;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Paddle extends MovableObject {
    private double speed = GameConstants.PADDLE_SPEED;
    private int currentPowerUp;
    private int frame = 0;
    private List<Image> images;

    public Paddle(double x, double y, double width, double height) {
        super(x, y, width, height);
        images = new ArrayList<>();
        images.add(ImgManager.getInstance().getImage("PADDLE0"));
        images.add(ImgManager.getInstance().getImage("PADDLE1"));
        images.add(ImgManager.getInstance().getImage("PADDLE2"));
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public void setCurrentPowerUp(int currentPowerUp) {
        this.currentPowerUp = currentPowerUp;
    }

    public int getCurrentPowerUp() {
        return currentPowerUp;
    }

    public void moveLeft() {
        dx = -speed;
    }

    public void moveRight() {
        dx = speed;
    }
    public void stop() {
        dx = 0;
    }

    public void move(double deltaTime) {
        this.setX(this.getX() + this.dx * deltaTime);
    }

    public void applyPowerUp() {

    }

    public void checkCollisionWall(Canvas canvas) {
        if (this.getX() <= 0) {
            this.setX(0);
        }
        if (this.getX() + this.getWidth() >= canvas.getWidth()) {
            this.setX(canvas.getWidth() - this.getWidth());
        }
    }

    public void render(GraphicsContext ctx) {
        frame++;
        if (frame >= GameConstants.PADDLE_FRAMES) {
            frame = 0;
        }
        ctx.drawImage(images.get(frame), this.getX(), this.getY());
    }
}
