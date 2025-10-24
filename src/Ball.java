import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Ball extends MovableObject {
    private double speed = 400;

    public void setSpeed(double speed) {
        this.speed = speed;
    }

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
    /**
     * Hệ số ảnh hưởng của paddle lên bóng.
     * Tăng giá trị này để paddle "đẩy" bóng mạnh hơn.
     * 0.0 = không ảnh hưởng, 1.0 = ảnh hưởng rất mạnh.
     * Giá trị tốt thường nằm trong khoảng 0.3 đến 0.6.
     */
    private static final double PADDLE_INFLUENCE_FACTOR = 0.4;

    /**
     * Tốc độ ngang tối đa của bóng, để tránh bóng di chuyển quá nhanh không thể kiểm soát.
     */
    private static final double MAX_BALL_SPEED_X = 600.0; // Đơn vị: pixel/giây

    /**
     * Xử lý va chạm dựa trên loại đối tượng.
     * Cập nhật logic va chạm với Paddle để thay đổi góc nảy.
     * @param other Đối tượng mà bóng va chạm.
     */
    public void bounceOff(GameObject other) {
        // KIỂM TRA: Nếu đối tượng va chạm là PADDLE
        if (other instanceof Paddle) {
            Paddle paddle = (Paddle) other; // Ép kiểu để xử lý riêng

            // 1. Tính toán vị trí va chạm so với tâm Paddle (từ -1.0 đến 1.0)
            double paddleCenter = paddle.getX() + paddle.getWidth() / 2.0;
            double ballCenter = this.getX() + this.getWidth() / 2.0;

            // normalizedHit: -1.0 (cực trái) đến 1.0 (cực phải)
            double normalizedHit = (ballCenter - paddleCenter) / (paddle.getWidth() / 2.0);

            // 2. Định nghĩa góc nảy tối đa (ví dụ: tối đa 75% tốc độ tổng vào phương ngang)
            // Tỷ lệ tối đa của tốc độ có thể là dx (khoảng 0.8 là hợp lý)
            final double MAX_DX_RATIO = 0.8;

            // Tính toán vận tốc ngang mới (newDx)
            double newDx = normalizedHit * (this.speed * MAX_DX_RATIO);

            // 3. Tính toán vận tốc dọc mới (newDy) để duy trì tốc độ tổng thể (speed)
            // Áp dụng định lý Pythagoras: speed^2 = newDx^2 + newDy^2
            // Lấy giá trị tuyệt đối, sau đó đặt dấu âm vì bóng luôn nảy lên
            double newDyMagnitude = Math.sqrt(this.speed * this.speed - newDx * newDx);

            // Đảm bảo không có NaN (trường hợp hiếm xảy ra lỗi tính toán)
            if (Double.isNaN(newDyMagnitude)) {
                newDyMagnitude = this.speed * Math.sqrt(1 - MAX_DX_RATIO * MAX_DX_RATIO);
            }

            // 4. Áp dụng vận tốc mới
            this.setDx(newDx);
            // newDy luôn âm (nảy lên)
            this.setDy(-newDyMagnitude);

            // 5. Đặt lại vị trí bóng ngay trên paddle để tránh va chạm lặp
            this.setY(paddle.getY() - this.getHeight());
        }
        // NẾU LÀ ĐỐI TƯỢNG KHÁC (ví dụ: gạch)
        else {
            // Logic va chạm của gạch (chỉ đảo hướng dựa trên trục va chạm)
            double overlapX = Math.min(this.getX() + this.getWidth() - other.getX(),
                    other.getX() + other.getWidth() - this.getX());
            double overlapY = Math.min(this.getY() + this.getHeight() - other.getY(),
                    other.getY() + other.getHeight() - this.getY());

            if (overlapX < overlapY) {
                this.setDx(-this.getDx());

                if (this.getX() < other.getX()) {
                    this.setX(other.getX() - this.getWidth() - 1); // Đẩy bóng ra khỏi gạch (trái)
                } else {
                    this.setX(other.getX() + other.getWidth() + 1); // Đẩy bóng ra khỏi gạch (phải)
                }
            } else {
                this.setDy(-this.getDy());

                if (this.getY() < other.getY()) {
                    this.setY(other.getY() - this.getHeight() - 1); // Đẩy bóng ra khỏi gạch (trên)
                } else {
                    this.setY(other.getY() + other.getHeight() + 1); // Đẩy bóng ra khỏi gạch (dưới)
                }
            }
        }
    }
}
