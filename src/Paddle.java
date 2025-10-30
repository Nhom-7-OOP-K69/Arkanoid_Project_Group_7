import java.util.*;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Paddle extends MovableObject {
    private double speed = 500;
    protected int activePowerUps = 0; // bitmask: bit0=Expand(1), bit2=Bullet(3)
    private int frame = 0;
    private List<Image> images;
    private double originalWidth; // Lưu kích thước gốc

    public Paddle(double x, double y, double width, double height) {
        super(x, y, width, height);
        images = new ArrayList<>();
        this.originalWidth = width;
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

    // Thêm methods cho bitmask
    public void activatePowerUp(int type) {
        activePowerUps |= (1 << (type - 1));
        System.out.println("[Paddle] Activate power-up type " + type + ", active: " + activePowerUps);
    }

    public void deactivatePowerUp(int type) {
        activePowerUps &= ~(1 << (type - 1));
        System.out.println("[Paddle] Deactivate power-up type " + type + ", active: " + activePowerUps);
    }

    public boolean hasPowerUp(int type) {
        return (activePowerUps & (1 << (type - 1))) != 0;
    }

    // Method mới: Mở rộng paddle, giữ center
    public void expandPaddle(double expandAmount) {
        double centerX = getX() + getWidth() / 2;
        setWidth(originalWidth + expandAmount * 2); // +80 tổng
        setX(centerX - getWidth() / 2);
        System.out.println("[Paddle] Mở rộng thành " + getWidth());
    }

    // Method mới: Thu nhỏ về gốc, giữ center
    public void shrinkPaddle() {
        double centerX = getX() + getWidth() / 2;
        setWidth(originalWidth);
        setX(centerX - getWidth() / 2);
        System.out.println("[Paddle] Thu nhỏ về " + getWidth());
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

    @Override
    public void render(GraphicsContext ctx) {
        frame++;
        if (frame >= GameConstants.PADDLE_FRAMES) {
            frame = 0;
        }
        if (hasPowerUp(1)) { // Expand power-up
            // Dùng ảnh EXPAND_PADDLE (static, không animation)
            Image expandImg = ImgManager.getInstance().getImage("EXPAND_PADDLE");
            ctx.drawImage(expandImg, getX(), getY(), getWidth(), getHeight());
        } else {
            // Animation bình thường
            ctx.drawImage(images.get(frame % images.size()), getX(), getY());
        }
    }
}
