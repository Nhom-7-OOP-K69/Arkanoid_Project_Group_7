import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Lớp GameObject là lớp cơ sở (base class) cho tất cả các đối tượng
 * có thể nhìn thấy và tương tác trong trò chơi.
 * Nó chứa các thuộc tính cơ bản nhất như vị trí (x, y) và kích thước (width, height),
 * cùng với các phương thức chung để cập nhật trạng thái và vẽ đối tượng.
 */
public class GameObject {
    // Tọa độ của đối tượng trên màn hình.
    private double x;
    private double y;

    // Kích thước của đối tượng.
    private double height;
    private double width;

    /**
     * Constructor (phương thức khởi tạo) để tạo một GameObject
     * với vị trí và kích thước được chỉ định.
     * @param x Tọa độ X ban đầu của đối tượng.
     * @param y Tọa độ Y ban đầu của đối tượng.
     * @param width Chiều rộng của đối tượng.
     * @param height Chiều cao của đối tượng.
     */
    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Constructor mặc định không tham số.
     */
    public GameObject() {}

    // --- GETTERS ---
    /**
     * Lấy tọa độ X hiện tại của đối tượng.
     * @return Tọa độ X.
     */
    public double getX() {
        return x;
    }

    /**
     * Lấy tọa độ Y hiện tại của đối tượng.
     * @return Tọa độ Y.
     */
    public double getY() {
        return y;
    }

    /**
     * Lấy chiều cao của đối tượng.
     * @return Chiều cao.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Lấy chiều rộng của đối tượng.
     * @return Chiều rộng.
     */
    public double getWidth() {
        return width;
    }

    // --- SETTERS ---
    /**
     * Thiết lập một tọa độ X mới cho đối tượng.
     * @param x Tọa độ X mới.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Thiết lập một tọa độ Y mới cho đối tượng.
     * @param y Tọa độ Y mới.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Thiết lập một chiều cao mới cho đối tượng.
     * @param height Chiều cao mới.
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Thiết lập một chiều rộng mới cho đối tượng.
     * @param width Chiều rộng mới.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Cập nhật trạng thái của đối tượng (ví dụ: di chuyển, thay đổi logic).
     * Phương thức này được thiết kế để các lớp con (subclasses) ghi đè (override)
     * để triển khai hành vi cụ thể của chúng.
     * @param deltaTime Thời gian đã trôi qua kể từ frame cuối cùng, dùng để đảm bảo di chuyển mượt mà.
     */
    public void update(double deltaTime) {
        // Lớp cơ sở không có logic cập nhật, sẽ được định nghĩa ở lớp con.
    }

    /**
     * Vẽ đối tượng lên màn hình.
     * Phương thức này cũng được thiết kế để các lớp con ghi đè
     * để vẽ hình ảnh hoặc hình dạng cụ thể của chúng.
     * @param image Hình ảnh cần vẽ (có thể không dùng nếu lớp con tự vẽ hình dạng).
     * @param gc Đối tượng GraphicsContext được sử dụng để thực hiện các thao tác vẽ.
     */
    public void render(Image image, GraphicsContext gc) {
        // Lớp cơ sở không có logic vẽ, sẽ được định nghĩa ở lớp con.
    }
}
