import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Lớp Ball đại diện cho đối tượng quả bóng trong game.
 * Lớp này quản lý vị trí, di chuyển, va chạm và hiển thị của bóng.
 */
public class Ball extends MovableObject {

    private double speed = 300; // Tốc độ tổng thể của quả bóng
    private final Image img;      // Hình ảnh của quả bóng

    /**
     * Constructor để tạo một đối tượng Ball mới.
     *
     * @param x      Vị trí ban đầu theo trục X.
     * @param y      Vị trí ban đầu theo trục Y.
     * @param width  Chiều rộng của bóng.
     * @param height Chiều cao của bóng.
     */
    public Ball(double x, double y, double width, double height) {
        super(x, y, width, height);
        // Khởi tạo vận tốc ban đầu cho bóng
        this.setDy(speed);
        this.setDx(speed);
        // Lấy hình ảnh của bóng từ trình quản lý ảnh
        this.img = ImgManager.getInstance().getImage("BALL");
    }

    /**
     * Xử lý va chạm của bóng với các bức tường của khu vực chơi.
     *
     * @param canvas Canvas của game, dùng để lấy kích thước khu vực chơi.
     * @return true nếu bóng chạm vào đáy (mất mạng), ngược lại trả về false.
     */
    public boolean collisionWall(Canvas canvas) {
        // Va chạm tường trái và phải
        if (this.getX() <= 0 || this.getX() + this.getWidth() >= canvas.getWidth()) {
            this.setDx(-this.getDx()); // Đảo ngược vận tốc ngang
            // Đẩy bóng ra khỏi tường một chút để tránh bị kẹt
            if (this.getX() <= 0) {
                this.setX(1);
            } else {
                this.setX(canvas.getWidth() - this.getWidth() - 1);
            }
        }

        // Va chạm với thanh UI ở cạnh trên
        if (this.getY() <= GameConstants.UI_TOP_BAR_HEIGHT) {
            this.setDy(-this.getDy()); // Đảo ngược vận tốc dọc
            this.setY(GameConstants.UI_TOP_BAR_HEIGHT + 1); // Đẩy bóng ra khỏi thanh UI
        }

        // Kiểm tra va chạm với đáy màn hình -> thua
        if (this.getY() + this.getHeight() >= canvas.getHeight()) {
            return true;
        }

        // Bóng vẫn còn trong sân chơi
        return false;
    }

    /**
     * Kiểm tra va chạm giữa bóng và một đối tượng khác bằng thuật toán AABB (Axis-Aligned Bounding Box).
     *
     * @param other Đối tượng GameObject khác cần kiểm tra.
     * @return true nếu có va chạm, ngược lại trả về false.
     */
    public boolean checkCollision(GameObject other) {
        return this.getX() < other.getX() + other.getWidth() &&
                this.getX() + this.getWidth() > other.getX() &&
                this.getY() < other.getY() + other.getHeight() &&
                this.getY() + this.getHeight() > other.getY();
    }


    /**
     * Xử lý vật lý khi bóng nảy ra khỏi một đối tượng khác (paddle hoặc gạch).
     *
     * @param other Đối tượng mà bóng đã va chạm.
     */
    public void bounceOff(GameObject other) {
        // TRƯỜNG HỢP 1: Va chạm với PADDLE
        if (other instanceof Paddle) {
            handlePaddleCollision((Paddle) other);
        }
        // TRƯỜNG HỢP 2: Va chạm với các đối tượng khác (ví dụ: gạch)
        else {
            handleGenericCollision(other);
        }
    }

    /**
     * Xử lý logic va chạm riêng cho Paddle để tạo góc nảy tự nhiên.
     *
     * @param paddle Paddle mà bóng va chạm.
     */
    private void handlePaddleCollision(Paddle paddle) {
        // 1. Tính toán điểm va chạm tương đối so với tâm của paddle (-1.0 là cạnh trái, 1.0 là cạnh phải)
        double paddleCenter = paddle.getX() + paddle.getWidth() / 2.0;
        double ballCenter = this.getX() + this.getWidth() / 2.0;
        double normalizedHit = (ballCenter - paddleCenter) / (paddle.getWidth() / 2.0);

        // 2. Tính toán vận tốc ngang mới (newDx) dựa vào điểm va chạm.
        // Càng xa tâm, góc nảy càng rộng (vận tốc ngang càng lớn).
        final double MAX_DX_RATIO = 0.8; // Tỷ lệ vận tốc ngang tối đa (để tránh góc nảy quá rộng)
        double newDx = normalizedHit * (this.speed * MAX_DX_RATIO);

        // 3. Tính toán vận tốc dọc mới (newDy) bằng định lý Pythagoras để giữ nguyên tổng tốc độ.
        // speed^2 = newDx^2 + newDy^2
        double newDyMagnitude = Math.sqrt(this.speed * this.speed - newDx * newDx);

        // Xử lý trường hợp hiếm gặp khi tính toán ra NaN (Not a Number)
        if (Double.isNaN(newDyMagnitude)) {
            newDyMagnitude = this.speed * Math.sqrt(1 - MAX_DX_RATIO * MAX_DX_RATIO);
        }

        // 4. Cập nhật vận tốc mới cho bóng
        this.setDx(newDx);
        this.setDy(-newDyMagnitude); // Vận tốc dọc luôn là số âm để bóng nảy lên

        // 5. Đặt lại vị trí của bóng ngay trên paddle để tránh va chạm lặp lại trong khung hình tiếp theo
        this.setY(paddle.getY() - this.getHeight());
    }

    /**
     * Xử lý logic va chạm chung cho các vật thể như gạch.
     * Bóng sẽ nảy ra dựa trên hướng va chạm chính (ngang hoặc dọc).
     *
     * @param other Đối tượng mà bóng va chạm.
     */
    private void handleGenericCollision(GameObject other) {
        // Tính toán độ giao nhau (overlap) trên cả hai trục X và Y
        double overlapX = Math.min(this.getX() + this.getWidth() - other.getX(),
                other.getX() + other.getWidth() - this.getX());
        double overlapY = Math.min(this.getY() + this.getHeight() - other.getY(),
                other.getY() + other.getHeight() - this.getY());

        // Nếu độ giao nhau theo trục X nhỏ hơn, va chạm xảy ra ở hai bên (trái/phải)
        if (overlapX < overlapY) {
            this.setDx(-this.getDx()); // Đảo ngược vận tốc ngang
            // Đẩy bóng ra khỏi đối tượng để tránh bị kẹt
            if (this.getX() < other.getX()) {
                this.setX(other.getX() - this.getWidth() - 1);
            } else {
                this.setX(other.getX() + other.getWidth() + 1);
            }
        }
        // Ngược lại, va chạm xảy ra ở trên/dưới
        else {
            this.setDy(-this.getDy()); // Đảo ngược vận tốc dọc
            // Đẩy bóng ra khỏi đối tượng để tránh bị kẹt
            if (this.getY() < other.getY()) {
                this.setY(other.getY() - this.getHeight() - 1);
            } else {
                this.setY(other.getY() + other.getHeight() + 1);
            }
        }
    }


    /**
     * Cập nhật vị trí của bóng dựa trên vận tốc và thời gian trôi qua (delta time).
     *
     * @param deltaTime Thời gian (tính bằng giây) kể từ lần cập nhật cuối cùng.
     */
    @Override
    public void move(double deltaTime) {
        this.setX(this.getX() + this.getDx() * deltaTime);
        this.setY(this.getY() + this.getDy() * deltaTime);
    }

    /**
     * Vẽ hình ảnh của bóng lên canvas.
     *
     * @param gc Đối tượng GraphicsContext để vẽ.
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(img, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    // --- Getters and Setters ---

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
