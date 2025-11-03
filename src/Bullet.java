import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Bullet extends MovableObject {
    private double bullet_speed = GameConstants.BULLET_SPEED;
    private Image bulletImg; // ảnh đạn được lấy từ ImgManager

    public Bullet(double x, double y, double width, double height) {
        super(x, y, width, height);
        // load ảnh đạn (LASER)
        this.bulletImg = ImgManager.getInstance().getImage("BULLET");
    }

    public boolean intersects(Brick brick) {
        return getX() < brick.getX() + brick.getWidth() &&
                getX() + getWidth() > brick.getX() &&
                getY() < brick.getY() + brick.getHeight() &&
                getY() + getHeight() > brick.getY();
    }




    public void update() {
        setY(getY() - bullet_speed);
    }

    // Kiểm tra đạn đã ra khỏi màn hình chưa
    public boolean isOutOfScreen() {
        return getY() + getHeight() < 0;
    }

    public void render(GraphicsContext gc) {
        if (bulletImg != null) {
            gc.drawImage(bulletImg, getX(), getY(), getWidth(), getHeight());
        } else {
            // fallback nếu chưa load được ảnh
            gc.setFill(javafx.scene.paint.Color.RED);
            gc.fillRect(getX(), getY(), getWidth(), getHeight());
        }
    }
}
