import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Ball extends MovableObject {
    private double speed = 350;
    private Image img;

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public Ball(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.img = ImgManager.getInstance().getImage("BALL");
    }

    public boolean collisionWall(Canvas canvas) {
        // Va chạm tường trái và phải (Giữ nguyên logic của bạn)
        if (this.getX() <= 0 || this.getX() + this.getWidth() >= canvas.getWidth()) {
            this.setDx(-this.getDx());
            if (this.getX() <= 0) {
                this.setX(1);
            } else {
                this.setX(canvas.getWidth() - this.getWidth() - 1);
            }
        }

        // Va chạm với thanh menu ở trên
        if (this.getY() <= GameConstants.UI_TOP_BAR_HEIGHT) {
            this.setDy(-this.getDy());
            this.setY(GameConstants.UI_TOP_BAR_HEIGHT + 1); // Đẩy bóng ra khỏi thanh
        }

        // Kiểm tra va chạm đáy
        if (this.getY() + this.getHeight() >= canvas.getHeight()) {
            return true; // Báo hiệu bóng đã rơi
        }

        return false; // Bóng vẫn trong sân
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

    /**
     * Xử lý va chạm dựa trên loại đối tượng.
     * @param other Đối tượng mà bóng va chạm.
     */
    public void bounceOff(GameObject other) {
        // KIỂM TRA: Nếu đối tượng va chạm là PADDLE
        if (other instanceof Paddle) {
            Paddle paddle = (Paddle) other; // Ép kiểu để xử lý riêng

            // 1. Luôn nảy lên
            if (this.getDy() > 0) {
                this.setDy(-this.getDy());
            }

            // 2. Thay đổi góc nảy dựa trên vị trí va chạm
            double paddleCenter = paddle.getX() + paddle.getWidth() / 2;
            double ballCenter = this.getX() + this.getWidth() / 2;
            double difference = ballCenter - paddleCenter;
            this.setDx(difference * 5); // Thay đổi vận tốc ngang

            // 3. Đặt lại vị trí bóng ngay trên paddle
            this.setY(paddle.getY() - this.getHeight());
        }
        // NẾU LÀ ĐỐI TƯỢNG KHÁC (ví dụ: gạch)
        else {
            // Giữ nguyên logic va chạm chung của bạn
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
    }

    public void move(double deltaTime) {
        this.setX(this.getX() + this.dx * deltaTime);
        this.setY(this.getY() + this.dy * deltaTime);
    }

    public void render( GraphicsContext gc) {
        gc.drawImage(img, this.getX(), this.getY());
    }
}
