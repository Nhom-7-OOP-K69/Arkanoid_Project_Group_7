public class Bullet extends MovableObject {
    private double bullet_speed = GameConstants.BULLET_SPEED;

    public Bullet(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void move(double deltaTime) {

    }

    public void update() {
        setY(getY() - bullet_speed);
    }

    public void render() {
        System.out.println("[Bullet] render tại (" + getX() + ", " + getY() + ")");
    }
}
