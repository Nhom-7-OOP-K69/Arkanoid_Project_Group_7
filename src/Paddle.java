package Arkanoid_Project_Group_7.src;

import java.util.*;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Paddle extends MovableObject {
    private double speed = 3;
    private int currentPowerUp;
    private int frame = 0;

    public Paddle(double x, double y, double width, double height) {
        super(x, y, width, height);
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

    public void applyPowerUp() {

    }

    public void render(List<Image> images, GraphicsContext ctx) {
        frame++;
        if (frame >= GameConstants.PADDLE_FRAMES) {
            frame = 0;
        }
        ctx.drawImage(images.get(frame), this.getX(), this.getY());
    }
}
