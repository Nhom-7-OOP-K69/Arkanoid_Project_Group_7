import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Ball extends MovableObject {
    private double speed = 300;
    private Image img;

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

    public void bounceOff(GameObject other) {
        // KIỂM TRA: Nếu đối tượng va chạm là PADDLE
        if (other instanceof Paddle paddle) {

            // 1. Nảy bóng theo chiều dọc (luôn nảy lên)
            // Chỉ đảo chiều khi bóng đang đi xuống để tránh lỗi kẹt bóng
            if (this.getDy() > 0) {
                this.setDy(-this.getDy());
            }

            // 2. TÍNH TOÁN QUÁN TÍNH - CẢI TIẾN CỐT LÕI
            // Lấy vận tốc ngang của paddle (giả sử paddle có phương thức getDx())
            double paddleDx = paddle.getDx();

            // Vận tốc ngang mới của bóng = vận tốc hiện tại + một phần vận tốc của paddle
            double newBallDx = this.getDx() + (paddleDx * PADDLE_INFLUENCE_FACTOR);

            // 3. Giới hạn tốc độ ngang của bóng
            // Dùng Math.signum để giữ nguyên hướng (dương/âm) của vận tốc
            if (Math.abs(newBallDx) > MAX_BALL_SPEED_X) {
                newBallDx = MAX_BALL_SPEED_X * Math.signum(newBallDx);
            }

            this.setDx(newBallDx);

            // 4. Đặt lại vị trí của bóng ngay trên paddle để tránh bị kẹt
            this.setY(paddle.getY() - this.getHeight());
        }
        // NẾU LÀ ĐỐI TƯỢNG KHÁC (ví dụ: gạch) -> giữ nguyên logic cũ
        else {
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
