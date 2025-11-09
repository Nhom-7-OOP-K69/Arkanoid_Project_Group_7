package powerUp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import game.GameConstants;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import manager.ImgManager;
import object.ball.Ball;
import object.ball.BallLayer;
import object.brick.Brick;
import object.brick.BrickLayer;
import object.bullet.Bullet;
import object.paddle.Paddle;

public class PowerUpManager {
    private List<PowerUp> fallingPowerUps = new ArrayList<>(); // Danh sách power-up đang rơi
    private List<PowerUp> activePowerUps = new ArrayList<>(); // Danh sách power-up đang hoạt động (đã ăn)
    private List<Ball> gameBalls; // tham chiếu đến danh sách bóng trong GameManager
    private List<Bullet> bullets = new ArrayList<>();
    private Image img;

    public PowerUpManager(BallLayer ballLayer) {
        this.gameBalls = ballLayer.getBallList();
    }

    // gọi khi brick bị phá
    public void spawnPowerUp(double x, double y) {
        if (Math.random() < 0.2) { // 20% tỉ lệ rơi
            double typeRand = Math.random();
            if (typeRand < 0.33) {
                // Bullet
                PowerUp powerUp = new BulletPowerUp(
                        x, y,
                        GameConstants.POWERUP_WIDTH,
                        GameConstants.POWERUP_HEIGHT,
                        3); // 3s
                fallingPowerUps.add(powerUp);
            } else if (typeRand < 0.66) {
                // Expand
                PowerUp powerUp = new ExpandPaddlePowerUp(
                        x, y,
                        GameConstants.POWERUP_WIDTH,
                        GameConstants.POWERUP_HEIGHT,
                        5); // 5s
                fallingPowerUps.add(powerUp);
            } else {
                // Extra Ball
                PowerUp powerUp = new ExtraBallPowerUp(
                        x, y,
                        GameConstants.POWERUP_WIDTH,
                        GameConstants.POWERUP_HEIGHT,
                        10,
                        gameBalls);
                fallingPowerUps.add(powerUp);
            }
        }
    }

    public int update(double deltaTime, Paddle paddle, BallLayer ballLayer, BrickLayer brickLayer) {
        // 🔥 Cập nhật power-up đang rơi
        Iterator<PowerUp> iterator = fallingPowerUps.iterator();

        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            powerUp.setY(powerUp.getY() + GameConstants.SPAWN_POWER_UP_SPEED * deltaTime);

            // check va chạm với paddle
            if (checkCollision(powerUp, paddle)) {
                System.out.println("Paddle đã hứng PowerUp!");

                // Kiểm tra xem đã có power-up cùng loại đang hoạt động chưa
                boolean alreadyActive = false;
                for (PowerUp ap : activePowerUps) {
                    if (ap.getType() == powerUp.getType()) {
                        // Reset thời gian cho power-up đang hoạt động
                        if (ap instanceof BulletPowerUp bpu) {
                            bpu.startTime = System.currentTimeMillis();
                            System.out.println("[BulletPowerUp] Reset thời gian bắn đạn!");
                        }
                        else if (ap instanceof ExpandPaddlePowerUp epu) {
                            epu.startTime = System.currentTimeMillis();
                            System.out.println("[ExpandPaddlePowerUp] Reset thời gian mở rộng!");
                        }
                        alreadyActive = true;
                        break;
                    }
                }

                if (!alreadyActive) {
                    powerUp.applyEffect(paddle, ballLayer);
                    if (powerUp.isActive()) { // Chỉ add nếu power-up set active (Bullet=yes, Extra=no)
                        activePowerUps.add(powerUp);
                    }
                }

                iterator.remove(); // Xóa power-up rơi khỏi danh sách
            } else if (powerUp.getY() > GameConstants.SCREEN_HEIGHT) {
                iterator.remove();
            }
        }

        // 🔥 Cập nhật các power-up đang hoạt động
        Iterator<PowerUp> activeIterator = activePowerUps.iterator();
        while (activeIterator.hasNext()) {
            PowerUp p = activeIterator.next();

            // Xử lý chung: Tick duration cho mọi power-up active
            if (!p.tick()) {
                p.removeEffect(paddle, ballLayer.getBallList().isEmpty() ? null : ballLayer.getBallList().get(0));
                activeIterator.remove();
                continue;
            }

            // Xử lý cụ thể cho Bullet
            if (p instanceof BulletPowerUp bulletPU && bulletPU.isActive()) {
                bullets.addAll(bulletPU.maybeShoot(paddle));

                if (bulletPU.isExpired()) {
                    bulletPU.removeEffect(paddle, ballLayer.getBallList().get(0));
                    activeIterator.remove();
                }
            }

            // Xử lý cụ thể cho Expand để đảm bảo hết thời gian thu lại
            if (p instanceof ExpandPaddlePowerUp epu && epu.isActive()) {
                if (epu.isExpired()) {
                    epu.removeEffect(paddle, ballLayer.getBallList().get(0));
                    activeIterator.remove();
                }
            }
        }

        // 🔥 Cập nhật đạn
        updateBullets();

        // 🔥 Kiểm tra va chạm đạn - gạch
        int bulletScore = checkBulletBrickCollision(brickLayer);

        return bulletScore; // Trả về điểm từ đạn
    }


    private void updateBullets() {
        List<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet b : bullets) {
            b.update();
            if (b.isOutOfScreen()) {
                bulletsToRemove.add(b);
            }
        }
        bullets.removeAll(bulletsToRemove);
    }

    // ⚡ Va chạm giữa đạn và gạch
    private int checkBulletBrickCollision(BrickLayer brickLayer) {
        int score = 0;
        List<Bullet> bulletsToRemove = new ArrayList<>();
        List<Brick> bricksToRemove = new ArrayList<>();

        for (Bullet b : bullets) {
            for (Brick brick : brickLayer.getBrickList()) {
                if (b.intersects(brick)) {
                    brick.takeHit(); // trừ máu
                    bulletsToRemove.add(b);

                    if (brick.isDestroyed() && !bricksToRemove.contains(brick)) {
                        spawnPowerUp(brick.getX(), brick.getY());
                        bricksToRemove.add(brick);
                        int brickScore;
                        switch (brick.getType()) {
                            case GameConstants.NORMAL_TYPE:
                                brickScore = 10;
                                break;
                            case GameConstants.STRONG_TYPE:
                                brickScore = 20;
                                break;
                            case GameConstants.SUPER_TYPE:
                                brickScore = 30;
                                break;
                            case GameConstants.EXPLOSION_TYPE:
                                brickScore = 10;
                                brickLayer.addExplosionBrick(brick);
                                break;
                            default:
                                brickScore = 0;
                                break;
                        }
                        score += brickScore;
                    }

                    System.out.println("[Bullet] Va chạm với gạch tại (" + brick.getX() + ", " + brick.getY() + ")");
                    break; // 1 viên đạn chỉ trúng 1 gạch
                }
            }
        }

        // Xóa đạn và gạch bị phá
        bullets.removeAll(bulletsToRemove);
        brickLayer.getBrickList().removeAll(bricksToRemove);

        return score;
    }

    public void clearPowerUp() {
        fallingPowerUps.clear();
        activePowerUps.clear();
        bullets.clear();
    }

    private boolean checkCollision(PowerUp powerUp, Paddle paddle) {
        return powerUp.getX() < paddle.getX() + paddle.getWidth() &&
                powerUp.getX() + powerUp.getWidth() > paddle.getX() &&
                powerUp.getY() < paddle.getY() + paddle.getHeight() &&
                powerUp.getY() + powerUp.getHeight() > paddle.getY();
    }

    public void render(GraphicsContext gc) {
        for (PowerUp powerUp : fallingPowerUps) { // Chỉ render power-up đang rơi
            String imageKey;
            switch (powerUp.getType()) {
                case 1: imageKey = "EXPAND_PADDLE"; break;
                case 3: imageKey = "LASER"; break;
                default: imageKey = "EXTRA_BALL"; break; // type 2
            }
            gc.drawImage(ImgManager.getInstance().getImage(imageKey),
                    powerUp.getX(), powerUp.getY(),
                    powerUp.getWidth(), powerUp.getHeight());
        }
        for (Bullet b : bullets) {
            gc.drawImage(ImgManager.getInstance().getImage("BULLET"),
                    b.getX(), b.getY(),
                    b.getWidth(), b.getHeight());
        }
    }
}
