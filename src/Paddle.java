public class Paddle extends MovableObject {
    private double speed;
    private int currentPowerUp;

    public Paddle(int x, int y, int w, int h) {
        setX(x);
        setY(y);
        setHeight(h);
        setWidth(w);
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public void setCurrentPowerUp(int currentPowerUp) {
        this.currentPowerUp = currentPowerUp;
    }

    public int getCurrentPowerUp() {
        return currentPowerUp;
    }

    public void moveLeft() {
        dx = -speed;
    }

    public void moveRight() {
        dx = speed;
    }

    public void applyPowerUp() {

    }
}
