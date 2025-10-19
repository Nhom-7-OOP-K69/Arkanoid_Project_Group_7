import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Lớp Brick đại diện cho một viên gạch trong trò chơi.
 * Nó quản lý trạng thái, "máu" (hit points), và việc hiển thị của gạch.
 */
public class Brick extends GameObject {
    private int hitPoints; // Số lần va chạm cần thiết để phá hủy viên gạch.
    private int type;      // Loại gạch, có thể dùng để xác định hình ảnh, điểm số, hoặc vật phẩm rơi ra.
    private Image img ;    // Biến để lưu trữ hình ảnh của viên gạch.

    /**
     * Constructor (phương thức khởi tạo) cho một viên gạch mới.
     * @param x Vị trí ban đầu trên trục X.
     * @param y Vị trí ban đầu trên trục Y.
     * @param hitPoints Số "máu" ban đầu của gạch.
     * @param type Loại của gạch.
     */
    public Brick(double x, double y, int hitPoints, int type) {
        // Gọi constructor của lớp cha (GameObject) để thiết lập vị trí và kích thước.
        super(x, y, GameConstants.BRICK_WIDTH, GameConstants.BRICK_HEIGHT);
        // Gán các giá trị ban đầu cho gạch.
        this.hitPoints = hitPoints;
        this.type = type;
    }

    /**
     * Xử lý khi gạch bị va chạm, giảm "máu" đi 1.
     */
    public void HandleHit() {
        hitPoints--;
    }

    /**
     * Thiết lập (set) một giá trị "máu" mới cho viên gạch.
     * @param hitPoints Số "máu" mới.
     */
    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    /**
     * Thiết lập (set) một loại mới cho viên gạch.
     * @param type Loại gạch mới.
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Lấy (get) số "máu" hiện tại của viên gạch.
     * @return Số "máu" còn lại.
     */
    public int getHitPoints() {
        return hitPoints;
    }

    /**
     * Lấy (get) loại của viên gạch.
     * @return Loại gạch.
     */
    public int getType() {
        return type;
    }

    /**
     * Xử lý khi gạch nhận một cú va chạm, giảm "máu" đi 1.
     */
    public void takeHit() {
        hitPoints--;
    }

    /**
     * Kiểm tra xem viên gạch đã bị phá hủy hay chưa.
     * @return true nếu "máu" nhỏ hơn hoặc bằng 0, ngược lại là false.
     */
    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    /**
     * Vẽ viên gạch lên màn hình.
     * @param gc Đối tượng GraphicsContext được sử dụng để vẽ.
     */
    public void render(GraphicsContext gc) {
        // Chỉ vẽ viên gạch nếu nó chưa bị phá hủy.
        if (!this.isDestroyed()) {
            gc.drawImage(this.img, this.getX(), this.getY());
        }
    }
}