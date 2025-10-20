import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SpawnPowerUp extends MovableObject {
    private double speed = GameConstants.SPAWN_POWER_UP_SPEED;
    private Image img;
    private PowerUp poweUpEffect;
    private boolean isActive = true;


    // constructor
    public SpawnPowerUp(double x, double y, PowerUp poweUpEffect){
        super(x, y, GameConstants.POWERUP_WIDTH, GameConstants.POWERUP_HEIGHT);
        this.setDx(0);
        this.setDy(speed);
        this.poweUpEffect = poweUpEffect;
        this.img = ImgManager.getInstance().getImage("POWER UP");

    }


    // di chuyen xuong
    public void move(double deltaTime) {
        this.setY(this.getY() + this.dy * deltaTime);
    }

    // check collision
    public boolean checkCollision(Paddle paddle) {
        return this.getX() < paddle.getX() + paddle.getWidth() &&
                this.getX() + this.getWidth() > paddle.getX() &&
                this.getY() < paddle.getY() + paddle.getHeight() &&
                this.getY() + this.getHeight() > paddle.getY();
    }

    // active power up
    public void activate(Paddle paddle, BallLayer ballLayer) {
        if (isActive && poweUpEffect != null) {
            poweUpEffect.applyEffect(paddle, ballLayer);
            isActive = false;
        }
    }

    // roi ra khoi man hinh
    public boolean isOutOfScreen(double screenHeight) {
        return this.getY() > screenHeight;
    }

    public boolean isActive() {
        return isActive;
    }

    //render
    public void render(GraphicsContext gc) {
        gc.drawImage(img, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
