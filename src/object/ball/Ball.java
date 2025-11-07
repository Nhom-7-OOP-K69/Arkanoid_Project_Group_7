package object.ball;

import game.GameConstants;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import manager.AudioManager;
import manager.ImgManager;
import object.gameObject.GameObject;
import object.gameObject.MovableObject;
import object.paddle.Paddle;

public class Ball extends MovableObject {
    private double speed = GameConstants.BALL_SPEED;

    public double getSpeed() {
        return speed;
    }

    public Ball() {
        super();
    }

    public Ball(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.setImg(ImgManager.getInstance().getImage("BALL"));
    }

    public boolean collisionWall(Canvas canvas) {
        // Va chạm tường trái và phải
        if (this.getX() <= 0 || this.getX() + this.getWidth() >= canvas.getWidth()) {
            this.setDx(-this.getDx());
            if (this.getX() <= 0) {
                this.setX(1);
            } else {
                this.setX(canvas.getWidth() - this.getWidth() - 1);
            }
        }

        // Va chạm với thanh menu ở trên
        if (this.getY() <= 0) {
            this.setDy(-this.getDy());
            this.setY(1);
        }

        // Kiểm tra va chạm đáy
        if (this.getY() + this.getHeight() >= canvas.getHeight()) {
            return true;
        }

        return false;
    }

    /**
     * Kiểm tra va chạm với một đối tượng khác.
     */
    public boolean checkCollision(GameObject other) {
        return this.getX() < other.getX() + other.getWidth() &&
                this.getX() + this.getWidth() > other.getX() &&
                this.getY() < other.getY() + other.getHeight() &&
                this.getY() + this.getHeight() > other.getY();
    }

    private static final double PADDLE_INFLUENCE_FACTOR = 0.4;

    private static final double MAX_BALL_SPEED_X = 600.0;

    /**
     * Xử lý va chạm dựa trên loại đối tượng.
     * Cập nhật logic va chạm với Paddle để thay đổi góc nảy.
     * @param other Đối tượng mà bóng va chạm.
     */
    public void bounceOff(GameObject other) {
        AudioManager.getInstance().playSfx("COLLISION");
        if (other instanceof Paddle) {
            Paddle paddle = (Paddle) other;

            // 1. Tính toán vị trí va chạm so với tâm Paddle (từ -1.0 đến 1.0)
            double paddleCenter = paddle.getX() + paddle.getWidth() / 2.0;
            double ballCenter = this.getX() + this.getWidth() / 2.0;

            double normalizedHit = (ballCenter - paddleCenter) / (paddle.getWidth() / 2.0);

            // 2. Định nghĩa góc nảy tối đa
            final double MAX_DX_RATIO = 0.8;

            double newDx = normalizedHit * (this.speed * MAX_DX_RATIO);

            // 3. Tính toán vận tốc dọc mới (newDy) để duy trì tốc độ tổng thể (speed)
            double newDyMagnitude = Math.sqrt(this.speed * this.speed - newDx * newDx);

            if (Double.isNaN(newDyMagnitude)) {
                newDyMagnitude = this.speed * Math.sqrt(1 - MAX_DX_RATIO * MAX_DX_RATIO);
            }

            // 4. Áp dụng vận tốc mới
            this.setDx(newDx);
            this.setDy(-newDyMagnitude);

            // 5. Đặt lại vị trí bóng ngay trên paddle để tránh va chạm lặp
            this.setY(paddle.getY() - this.getHeight());
        } else {
            double overlapX = Math.min(this.getX() + this.getWidth() - other.getX(),
                    other.getX() + other.getWidth() - this.getX());
            double overlapY = Math.min(this.getY() + this.getHeight() - other.getY(),
                    other.getY() + other.getHeight() - this.getY());

            if (overlapX < overlapY) {
                this.setDx(-this.getDx());

                if (this.getX() < other.getX()) {
                    this.setX(other.getX() - this.getWidth() - 1);
                } else {
                    this.setX(other.getX() + other.getWidth() + 1);
                }
            } else {
                this.setDy(-this.getDy());

                if (this.getY() < other.getY()) {
                    this.setY(other.getY() - this.getHeight() - 1);
                } else {
                    this.setY(other.getY() + other.getHeight() + 1);
                }
            }
        }
    }
}
