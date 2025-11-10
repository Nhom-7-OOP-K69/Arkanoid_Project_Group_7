package object.paddle;

import java.util.*;

import game.GameConstants;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import manager.ImgManager;
import object.gameObject.MovableObject;

public class Paddle extends MovableObject {
    private double speed = 500;
    private int activePowerUps = 0; // bitmask: bit0=Expand(1), bit2=Bullet(3)
    private int frame = 0;
    private List<Image> images;
    private double originalWidth; // Lưu kích thước gốc
    private double targetWidth;   // Chiều rộng mục tiêu (animation)
    private double currentWidth;  // Chiều rộng hiện tại (đang animate)
    private double startWidth;   // Chiều rộng lúc bắt đầu animation
    protected double animationProgress = 0; // 0.0 -> 1.0
    private long animationStartTime = 0;
    private static final double ANIMATION_DURATION = 0.6; // 300ms mượt
    protected boolean isAnimating = false;

    public Paddle(double x, double y, double width, double height) {
        super(x, y, width, height);
        images = new ArrayList<>();
        this.originalWidth = width;
        this.currentWidth = width;
        this.targetWidth = width;
        this.startWidth = width;
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

    public void setActivePowerUps(int activePowerUps) {
        this.activePowerUps = activePowerUps;
    }

    public void setAnimating(boolean animating) {
        isAnimating = animating;
    }

    public void setAnimationProgress(double animationProgress) {
        this.animationProgress = animationProgress;
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
        startWidth = currentWidth;
        targetWidth = originalWidth + expandAmount * 2;
        animationStartTime = System.currentTimeMillis();
        isAnimating = true;
        System.out.println("[Paddle] Bắt đầu mở rộng từ " + startWidth + " đến " + targetWidth);
    }

    // Method mới: Thu nhỏ về gốc, giữ center
    public void shrinkPaddle() {
        startWidth = currentWidth;
        targetWidth = originalWidth;
        animationStartTime = System.currentTimeMillis();
        isAnimating = true;
        System.out.println("[Paddle] Bắt đầu thu nhỏ từ " + startWidth + " về " + targetWidth);
    }

    // Cập nhật animation mỗi frame (gọi từ GameManager.update)
    public void updateAnimation(double deltaTime) {
        if (isAnimating) {
            long elapsedMs = System.currentTimeMillis() - animationStartTime;
            animationProgress = Math.min((double) elapsedMs / (ANIMATION_DURATION * 1000), 1.0);

            // Smooth interpolation (ease-out)
            double easeProgress = 1 - Math.pow(1 - animationProgress, 3);
            currentWidth = startWidth + (targetWidth - startWidth) * easeProgress;

            // Cập nhật vị trí để giữ center
            double centerX = super.getX() + super.getWidth() / 2; // Lấy X cũ + old width/2
            super.setWidth(currentWidth);
            super.setX(centerX - currentWidth / 2);

            if (animationProgress >= 1.0) {
                isAnimating = false;
                animationProgress = 0;
                System.out.println("[Paddle] Animation hoàn thành, width=" + currentWidth);
            }
        }
    }

    @Override
    public double getWidth() {
        return currentWidth;
    }

    @Override
    public void setWidth(double width) {
        currentWidth = width;
        targetWidth = width; // Đồng bộ target nếu set manual
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

        double renderWidth = getWidth();

        if (hasPowerUp(1)) { // Expand power-up
            Image expandImg = ImgManager.getInstance().getImage("EXPAND_PADDLE");
            ctx.drawImage(expandImg, getX(), getY(), renderWidth, getHeight());
        } else {
            // Animation bình thường
            ctx.drawImage(images.get(frame % images.size()), getX(), getY(), renderWidth, getHeight());
        }
    }
}
