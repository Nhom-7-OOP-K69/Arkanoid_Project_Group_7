import java.util.ArrayList;
import java.util.List;

public class BulletPowerUp extends PowerUp {
    private long lastShotTime = 0;   // thời điểm bắn lần trước (tính bằng milliseconds)
    private long shotInterval = 500; // thời gian chờ giữa hai lần bắn (0.5 giây)

    public BulletPowerUp(double x, double y, double width, double height, int duration) {
        super(x, y, width, height, 2, duration); // type = 4 (định danh riêng cho BulletPowerUp)
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        if (!active) {
            paddle.setCurrentPowerUp(this.type);
            start(); // đánh dấu power-up đang hoạt động
            System.out.println("[BulletPowerUp] Paddle đã kích hoạt chế độ bắn đạn!");
        } else {
            // Nếu người chơi ăn thêm viên BulletPowerUp cùng loại -> reset lại thời gian hiệu lực
            resetDuration(duration);
        }
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        if (active) {
            System.out.println("[BulletPowerUp] Hết thời gian, Paddle ngừng bắn.");
            paddle.setCurrentPowerUp(0);
            active = false;
        }
    }

    /**
     * Hàm này được GameManager gọi mỗi frame/tick.
     * Nếu đến thời điểm bắn đạn -> trả về 2 viên Bullet mới.
     * Nếu chưa đến thời điểm -> trả về list rỗng.
     */
    public List<Bullet> maybeShoot(Paddle paddle) {
        List<Bullet> newBullets = new ArrayList<>();

        if (!active) return newBullets;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShotTime >= shotInterval) {
            // Xác định vị trí 2 viên đạn tại hai mép Paddle
            double leftX = paddle.getX();
            double rightX = paddle.getX() + paddle.getWidth() - 10;
            double y = paddle.getY() - 10;

            // Tạo 2 viên đạn
            Bullet b1 = new Bullet(leftX, y, 5, 10);
            Bullet b2 = new Bullet(rightX, y, 5, 10);

            newBullets.add(b1);
            newBullets.add(b2);

            // Ghi lại thời điểm bắn để chờ 0.5s lần tiếp theo
            lastShotTime = currentTime;

            System.out.println("[BulletPowerUp] Bắn 2 viên đạn tại Y=" + y);
            System.out.println("[BulletPowerUp] Bắn đạn tại X=" + leftX + " và " + rightX + ", Y=" + y);

        }

        return newBullets;
    }
}
