import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class BulletPowerUp extends PowerUp {
    private long lastShotTime = 0;   // thời điểm bắn lần trước
    private long shotInterval = 1000; // 0.25 giây mỗi phát
    protected long startTime;          // thời điểm bắt đầu hiệu lực
    private int durationMs;  // thời gian hiệu lực (ms)
    private Image img;

    public BulletPowerUp(double x, double y, double width, double height, int duration) {
        super(x, y, width, height, 3, duration); // type = 2
        this.durationMs = duration * 1000; // Sửa thành *1000 để duration là giây -> ms (3*1000=3000ms=3s)
        this.shotInterval = 500;
        this.img = ImgManager.getInstance().getImage("LASER");
    }

    @Override
    public void applyEffect(Paddle paddle, BallLayer ballLayer) {
        if (!active) {
            paddle.activatePowerUp(this.type); // Thêm vào bitmask
            startTime = System.currentTimeMillis();
            lastShotTime = startTime - shotInterval; //reset để bắt đầu bắn liên tục
            start();
            System.out.println("[BulletPowerUp] Paddle đã kích hoạt chế độ bắn đạn!");
        }
        // Bỏ phần else vì giờ xử lý reset ở PowerUpManager
    }

    public boolean isExpired() {
        if (!active) return true;
        long elapsed = System.currentTimeMillis() - startTime;
        return elapsed >= durationMs;
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        if (active) {
            System.out.println("[BulletPowerUp] Hết thời gian, Paddle ngừng bắn.");
            paddle.deactivatePowerUp(this.type); // Xóa khỏi bitmask
            active = false;
        }
    }

    // Mỗi frame gọi từ GameManager, nếu đến thời gian thì tạo 2 viên đạn mới
    public List<Bullet> maybeShoot(Paddle paddle) {
        List<Bullet> newBullets = new ArrayList<>();
        if (!active) return newBullets;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShotTime >= shotInterval) {
            lastShotTime = currentTime;
            double centerX = paddle.getX() + paddle.getWidth() / 2;
            double offset = 10; // khoảng nhỏ giữa 2 viên
            double bulletHalfW = GameConstants.BULLET_WIDTH / 2;
            double leftX = centerX - offset - bulletHalfW;
            double rightX = centerX + offset - bulletHalfW;
            double y = paddle.getY() - 10;

            // tạo 2 viên đạn
            Bullet b1 = new Bullet(leftX, y, GameConstants.BULLET_WIDTH, GameConstants.BULLET_HEIGHT);
            Bullet b2 = new Bullet(rightX, y, GameConstants.BULLET_WIDTH, GameConstants.BULLET_HEIGHT);

            newBullets.add(b1);
            newBullets.add(b2);

            System.out.println("[BulletPowerUp] Bắn đạn tại X=" + leftX + ", " + rightX + " | Y=" + y);
        }

        return newBullets;
    }

    @Override
    public boolean tick() {
        if (!active) return false;
        if (isExpired()) {
            active = false;
            return false;
        }
        return true; // Không decrement duration, dùng time-based
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(img, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}