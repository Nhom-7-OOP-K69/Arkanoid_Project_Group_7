import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PowerUpManager {
    private List<PowerUp> activePowerUps = new ArrayList<>();
    private List<Ball> gameBalls; // tham chiếu đến danh sách bóng trong GameManager
    private Image img;

    public PowerUpManager(List<Ball> gameBalls) {
        this.gameBalls = gameBalls;
    }

    // gọi khi brick bị phá
    public void spawnPowerUp(double x, double y) {
        if (Math.random() < 0.5) { // 30% tỉ lệ rơi
            PowerUp powerUp = new ExtraBallPowerUp(
                    x, y,
                    GameConstants.POWERUP_WIDTH,
                    GameConstants.POWERUP_HEIGHT,
                    10,
                    gameBalls);
            activePowerUps.add(powerUp);
        }
    }

    public void update(double deltaTime, Paddle paddle, Ball ball) {
        Iterator<PowerUp> iterator = activePowerUps.iterator();

        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            powerUp.setY(powerUp.getY() + GameConstants.SPAWN_POWER_UP_SPEED * deltaTime);

            // check va chạm
            if (checkCollision(powerUp, paddle)) {
                System.out.println("Paddle đã hứng PowerUp!");
                powerUp.applyEffect(paddle, ball);
                iterator.remove();
            }
            else if (powerUp.getY() > GameConstants.SCREEN_HEIGHT) {
                iterator.remove();
            }
        }
    }

    private boolean checkCollision(PowerUp powerUp, Paddle paddle) {
        return powerUp.getX() < paddle.getX() + paddle.getWidth() &&
                powerUp.getX() + powerUp.getWidth() > paddle.getX() &&
                powerUp.getY() < paddle.getY() + paddle.getHeight() &&
                powerUp.getY() + powerUp.getHeight() > paddle.getY();
    }

    public void render(GraphicsContext gc) {
        for (PowerUp powerUp : activePowerUps) {
            gc.drawImage(ImgManager.getInstance().getImage("EXTRA_BALL"),
                    powerUp.getX(), powerUp.getY(),
                    powerUp.getWidth(), powerUp.getHeight());
        }
    }
}
