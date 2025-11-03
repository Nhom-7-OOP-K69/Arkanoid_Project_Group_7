import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Lớp Explosion tạo ra vụ nổ bằng hạt (particle) hình vuông.
 * Các hạt sẽ nhạt dần (fade out) và tản ra,
 * bắt nguồn từ RÌA của viên gạch và bay ra ngoài.
 */
public class Explosion extends GameObject {

    // --- Cấu hình cho hiệu ứng hạt ---
    private static final int NUM_PARTICLES = 20;
    private static final int PARTICLE_SIZE = 4;
    private static final double MAX_SPEED_PPS = 120.0;
    private static final double MAX_LIFETIME_SEC = 0.75;

    private class Particle {
        double x, y;
        double velX, velY;
        double lifetime;
        double initialLifetime;
        Color particleBaseColor;

        public Particle(double x, double y, double velX, double velY, double lifetime, Color baseColor) {
            this.x = x;
            this.y = y;
            this.velX = velX;
            this.velY = velY;
            this.lifetime = lifetime;
            this.initialLifetime = lifetime;
            this.particleBaseColor = baseColor;
        }

        public void update(double deltaTime) {
            x += velX * deltaTime;
            y += velY * deltaTime;
            lifetime -= deltaTime;
        }

        public void draw(GraphicsContext gc) {
            double alpha = lifetime / initialLifetime;
            if (alpha < 0) alpha = 0;

            Color fadedColor = particleBaseColor.deriveColor(0, 1.0, 1.0, alpha);

            gc.setFill(fadedColor);
            gc.fillRect(
                    (int) (x - PARTICLE_SIZE / 2.0),
                    (int) (y - PARTICLE_SIZE / 2.0),
                    PARTICLE_SIZE,
                    PARTICLE_SIZE
            );
        }

        public boolean isAlive() {
            return lifetime > 0;
        }
    }

    private List<Particle> particles;
    private Random random;
    private boolean isFinished = false;

    /**
     * Constructor mặc định (sẽ tạo vụ nổ với hạt đỏ và cam).
     */
    public Explosion(double x, double y) {
        super(x, y, GameConstants.BRICK_WIDTH, GameConstants.BRICK_HEIGHT);

        this.particles = new ArrayList<>();
        this.random = new Random();

        // Giữ lại tâm để tính toán hướng bay
        double centerX = this.getX() + this.getWidth() / 2.0;
        double centerY = this.getY() + this.getHeight() / 2.0;

        // Tạo các hạt
        for (int i = 0; i < NUM_PARTICLES; i++) {
            double startX, startY;
            int edge = random.nextInt(4); // 0=Top, 1=Bottom, 2=Left, 3=Right

            if (edge == 0) { // Cạnh Trên
                startX = this.getX() + random.nextDouble() * this.getWidth();
                startY = this.getY();
            } else if (edge == 1) { // Cạnh Dưới
                startX = this.getX() + random.nextDouble() * this.getWidth();
                startY = this.getY() + this.getHeight();
            } else if (edge == 2) { // Cạnh Trái
                startX = this.getX();
                startY = this.getY() + random.nextDouble() * this.getHeight();
            } else { // Cạnh Phải
                startX = this.getX() + this.getWidth();
                startY = this.getY() + random.nextDouble() * this.getHeight();
            }

            // Tính toán hướng bay (vector) từ TÂM ra điểm bắt đầu (startX, startY)
            double deltaX = startX - centerX;
            double deltaY = startY - centerY;

            // Lấy góc của vector đó
            // Math.atan2 sẽ trả về góc chính xác (từ -PI đến PI)
            double angle = Math.atan2(deltaY, deltaX);

            // (Tùy chọn) Thêm một chút ngẫu nhiên vào góc bay để không bị thẳng hàng
            // Thêm/bớt ngẫu nhiên 0.2 Radian (khoảng 11 độ)
            angle += (random.nextDouble() - 0.5) * 0.4;

            // Lấy một tốc độ ngẫu nhiên
            double speed = (random.nextDouble() * 0.5 + 0.5) * MAX_SPEED_PPS;

            // Tính toán velX và velY dựa trên góc và tốc độ
            double velX = Math.cos(angle) * speed;
            double velY = Math.sin(angle) * speed;

            // Tuổi thọ ngẫu nhiên (tính bằng giây)
            double lifetime = (random.nextDouble() * (MAX_LIFETIME_SEC / 2.0)) + (MAX_LIFETIME_SEC / 2.0);

            // Chọn màu ngẫu nhiên (Đỏ hoặc Cam san hô)
            Color chosenColor;
            if (random.nextBoolean()) {
                chosenColor = Color.ORANGERED;
            } else {
                chosenColor = Color.LIGHTCORAL;
            }

            // Thêm hạt mới vào danh sách, bắt đầu từ (startX, startY)
            particles.add(new Particle(startX, startY, velX, velY, lifetime, chosenColor));
        }
    }

    public void update(double deltaTime) {
        if (isFinished) {
            return;
        }

        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle p = iterator.next();
            p.update(deltaTime);

            if (!p.isAlive()) {
                iterator.remove();
            }
        }

        if (particles.isEmpty()) {
            isFinished = true;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (isFinished) {
            return;
        }

        for (Particle p : particles) {
            p.draw(gc);
        }
    }

    public boolean isFinished() {
        return isFinished;
    }
}