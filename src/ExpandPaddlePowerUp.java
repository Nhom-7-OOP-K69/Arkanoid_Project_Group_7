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
            paddle.expandPaddle(expandSize); // Animation tự động bắt đầu
            paddle.activatePowerUp(this.type);
            startTime = System.currentTimeMillis();
            start();
            System.out.println("[ExpandPaddlePowerUp] Paddle đã kích hoạt mở rộng!");
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
            paddle.shrinkPaddle(); // Animation tự động thu nhỏ
            paddle.deactivatePowerUp(this.type);
            active = false;
            System.out.println("[ExpandPaddlePowerUp] Paddle bắt đầu thu nhỏ.");
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