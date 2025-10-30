import java.util.ArrayList;
import java.util.List;

public class ExpandPaddlePowerUp extends PowerUp {
    protected long startTime;          // thời điểm bắt đầu hiệu lực
    private int durationMs;          // thời gian hiệu lực (ms)
    private double expandSize = 40;  // +40 mỗi bên, tổng +80

    public ExpandPaddlePowerUp(double x, double y, double width, double height, int duration) {
        super(x, y, width, height, 1, duration); // type=1
        this.durationMs = duration * 1000; // 5s = 5000ms
    }

    @Override
    public void applyEffect(Paddle paddle, BallLayer ballLayer) {
        if (!active) {
            paddle.expandPaddle(expandSize); // Gọi method mới ở Paddle
            paddle.activatePowerUp(this.type); // Thêm vào bitmask
            startTime = System.currentTimeMillis();
            start(); // set active=true
            System.out.println("[ExpandPaddlePowerUp] Paddle đã mở rộng!");
        }
        // Reset sẽ handle ở PowerUpManager
    }

    public boolean isExpired() {
        if (!active) return true;
        long elapsed = System.currentTimeMillis() - startTime;
        return elapsed >= durationMs;
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        if (active) {
            paddle.shrinkPaddle(); // Gọi method mới ở Paddle
            paddle.deactivatePowerUp(this.type); // Xóa khỏi bitmask
            active = false;
            System.out.println("[ExpandPaddlePowerUp] Paddle trở lại bình thường.");
        }
    }

    @Override
    public boolean tick() {
        if (!active) return false;
        if (isExpired()) {
            return false;
        }
        return true; // time-based, không decrement
    }
}