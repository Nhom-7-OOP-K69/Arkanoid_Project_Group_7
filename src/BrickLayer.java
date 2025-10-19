import javafx.scene.canvas.GraphicsContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Lớp BrickLayer quản lý việc tạo, xóa và hiển thị tất cả các viên gạch trong một màn chơi.
 * Lớp này hoạt động như một "container" cho các đối tượng Brick.
 */
public class BrickLayer {
    // Danh sách lưu trữ tất cả các đối tượng gạch hiện có trong màn chơi.
    private List<Brick> brickList = new ArrayList<>();

    /**
     * Trả về danh sách các viên gạch hiện có.
     * @return Danh sách các đối tượng Brick.
     */
    public List<Brick> getBrickList() {
        return brickList;
    }

    /**
     * Tải và tạo các viên gạch từ một file cấu hình màn chơi.
     * File này định nghĩa vị trí và loại của từng viên gạch.
     * @param file File văn bản chứa layout của các viên gạch.
     */
    public void loadBrick(File file) {
        // Sử dụng try-with-resources để đảm bảo Scanner được đóng tự động sau khi dùng.
        try (Scanner scanner = new Scanner(file)) {
            int j = 0; // Biến đếm chỉ số hàng (tương ứng với dòng trong file).
            // Đọc file theo từng dòng.
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Duyệt qua từng ký tự trong một dòng.
                for (int i = 0; i < line.length(); i++) {
                    Brick brick = null; // Khởi tạo brick là null.
                    // Sử dụng switch-case để xác định loại gạch dựa trên ký tự.
                    switch (line.charAt(i)) {
                        case '1': // Nếu là ký tự '1', tạo NormalBrick.
                            brick = new NormalBrick(GameConstants.BRICK_WIDTH * i, GameConstants.BRICK_HEIGHT * j + GameConstants.UI_TOP_BAR_HEIGHT);
                            break;
                        case '2': // Nếu là ký tự '2', tạo StrongBrick.
                            brick = new StrongBrick(GameConstants.BRICK_WIDTH * i, GameConstants.BRICK_HEIGHT * j + GameConstants.UI_TOP_BAR_HEIGHT);
                            break;
                        case '3': // Nếu là ký tự '3', tạo SuperBrick.
                            brick = new SuperBrick(GameConstants.BRICK_WIDTH * i, GameConstants.BRICK_HEIGHT * j + GameConstants.UI_TOP_BAR_HEIGHT);
                            break;
                        default:
                            // Bỏ qua các ký tự khác (ví dụ: '0' hoặc dấu cách thể hiện ô trống).
                            break;
                    }
                    // Thêm viên gạch vừa được tạo vào danh sách.
                    addBrick(brick);
                }
                j++; // Tăng chỉ số hàng để xử lý dòng tiếp theo.
            }

        } catch (FileNotFoundException e) {
            // In ra thông báo lỗi nếu không tìm thấy file level.
            System.err.println("Lỗi: Không tìm thấy file!");
        }
    }

    /**
     * Thêm một viên gạch vào danh sách quản lý.
     * @param brick Đối tượng Brick cần thêm.
     */
    public void addBrick(Brick brick) {
        // Nếu đối tượng brick là null (ví dụ: ô trống trong file), không làm gì cả.
        if (brick == null) {
            return;
        }
        // Thêm viên gạch hợp lệ vào danh sách.
        brickList.add(brick);
    }

    /**
     * Xóa một viên gạch khỏi danh sách.
     * Việc xóa dựa trên việc so sánh tọa độ X và Y.
     * @param brick Đối tượng Brick cần xóa.
     */
    public void removeBrick(Brick brick) {
        // Sử dụng removeIf để duyệt qua danh sách và xóa phần tử thỏa mãn điều kiện.
        brickList.removeIf(brick1 -> brick1.getX() == brick.getX()
                && brick1.getY() == brick.getY());
    }

    /**
     * Vẽ tất cả các viên gạch trong danh sách lên màn hình.
     * @param gc Đối tượng GraphicsContext được sử dụng để vẽ.
     */
    public void render(GraphicsContext gc) {
        // Dùng vòng lặp for-each để gọi phương thức render của từng viên gạch.
        for (Brick brick : brickList) {
            brick.render(gc);
        }
    }
}
