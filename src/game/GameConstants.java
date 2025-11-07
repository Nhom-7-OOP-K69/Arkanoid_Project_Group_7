package game;

public final class GameConstants {

    // --- CẤU HÌNH MÀN HÌNH VÀ GIAO DIỆN CHUNG (SCREEN & UI) ---

    /** Chiều rộng màn hình trò chơi (tính bằng pixel). */
    public static final int SCREEN_WIDTH = 900;
    /** Chiều cao màn hình trò chơi (tính bằng pixel). */
    public static final int SCREEN_HEIGHT = 700;
    /** Chiều cao của thanh UI phía trên cùng. */
    public static final double UI_TOP_BAR_HEIGHT = 50;

    /** Chiều rộng của các biểu tượng (icon) trên UI. */
    public static final int ICON_WIDTH = 30;
    /** Chiều rộng của nút "OK" trong các hộp thoại. */
    public static final int OKBUTTON_WIDTH = 100;
    /** Chiều rộng của background (nền) cho màn hình cài đặt. */
    public static final int SETTINGBG_WIDTH = 600;
    /** Chiều rộng của nút "START". */
    public static final int START_BUTTON_WIDTH = 200;


    // --- HẰNG SỐ LIÊN QUAN ĐẾN GẠCH (BRICK) ---

    /** Chiều rộng của viên gạch. */
    public static final int BRICK_WIDTH = 60;
    /** Chiều cao của viên gạch. */
    public static final int BRICK_HEIGHT = 20;

    // Sức khỏe (HP) của các loại gạch
    /** Sức khỏe của loại gạch Thường (Normal Brick). */
    public static final int NORMAL_BRICK_HP = 1;
    /** Sức khỏe của loại gạch Mạnh (Strong Brick). */
    public static final int STRONG_BRICK_HP = 2;
    /** Sức khỏe của loại gạch Siêu mạnh (Super Brick). */
    public static final int SUPER_BRICK_HP = 3;
    /** Sức khỏe của loại gạch Bùng nổ (Explosion Brick). */
    public static final int EXPLOSION_BRICK_HP = 1;

    // Định danh loại gạch
    /** Định danh loại gạch Thường. */
    public static final int NORMAL_TYPE = 1;
    /** Định danh loại gạch Mạnh. */
    public static final int STRONG_TYPE = 2;
    /** Định danh loại gạch Siêu mạnh. */
    public static final int SUPER_TYPE = 3;
    /** Định danh loại gạch Bùng nổ. */
    public static final int EXPLOSION_TYPE = 4;
    /** Định danh loại gạch Tường (không phá hủy được). */
    public static final int WALL_TYPE = 5;


    // --- HẰNG SỐ LIÊN QUAN ĐẾN BÓNG (BALL) ---

    /** Chiều rộng của bóng. */
    public static final int BALL_WIDTH = 20;
    /** Chiều cao của bóng. */
    public static final int BALL_HEIGHT = 20;
    /** Tốc độ di chuyển mặc định của bóng. */
    public static final int BALL_SPEED = 300;
    /** Tốc độ cộng thêm khi có power-up tăng tốc. */
    public static final double EXTRA_SPEED = 3;


    // --- HẰNG SỐ LIÊN QUAN ĐẾN THANH ĐỠ (PADDLE) ---

    /** Chiều rộng mặc định của thanh đỡ. */
    public static final int PADDLE_WIDTH = 120;
    /** Chiều cao của thanh đỡ. */
    public static final int PADDLE_HEIGHT = 32;
    /** Số lượng khung hình (frames) cho animation của thanh đỡ. */
    public static final int PADDLE_FRAMES = 3;
    /** Tốc độ di chuyển của thanh đỡ. */
    public static final int PADDLE_SPEED = 300;
    /** Kích thước mở rộng thêm khi nhận power-up. */
    public static final int PADDLE_EXPAND_SIZE = 20;
    /** Chiều rộng của thanh đỡ mở rộng (có thể là một asset hình ảnh cụ thể). */
    public static final int EXTRAPADDLE_WIDTH = 173;


    // --- HẰNG SỐ LIÊN QUAN ĐẾN POWER-UP VÀ VŨ KHÍ ---

    // Power-up
    /** Chiều rộng của vật phẩm Power-up. */
    public static final int POWERUP_WIDTH = 40;
    /** Chiều cao của vật phẩm Power-up. */
    public static final int POWERUP_HEIGHT = 11;
    /** Thời gian hiệu lực của Power-up mở rộng thanh đỡ. */
    public static final int POWERUP_EXPAND_DURATION = 30;
    /** Tốc độ rơi của các vật phẩm Power-up. */
    public static final int SPAWN_POWER_UP_SPEED = 100;

    // Đạn (Bullet)
    /** Chiều rộng của đạn. */
    public static final int BULLET_WIDTH = 20;
    /** Chiều cao của đạn. */
    public static final int BULLET_HEIGHT = 40;
    /** Tốc độ di chuyển của đạn. */
    public static final int BULLET_SPEED = 2;
    /** Khoảng thời gian giữa các lần bắn liên tiếp (tính bằng mili giây). */
    public static final int BULLET_SHOT_INTERVAL = 500;


    // --- HẰNG SỐ KHÁC (GAMEPLAY & SCORING) ---

    /** Chiều rộng của biểu tượng Mạng sống (Heart). */
    public static final int HEART_WIDTH = 30;
    /** Chiều cao của biểu tượng Mạng sống (Heart). */
    public static final int HEART_HEIGHT = 30;
    /** Điểm cộng thêm khi phá hủy một viên gạch. */
    public static final int SCORE_PLUS = 10;
    /** Số lượng màn chơi (Level) tối đa (hoặc đang làm việc). */
    public static final int LEVEL = 5;
    /** Số lượng người chơi tối đa được hiển thị trong bảng xếp hạng (Ranking). */
    public static final int MAX_RANKING = 5;
}