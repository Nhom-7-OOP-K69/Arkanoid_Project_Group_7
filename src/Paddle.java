public class Paddle extends MovableObject {
    private double speed;
    private int currentPowerUp;

    public Paddle(double x, double y, double width, double height) {
        super(x, y, width, height);
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

    }

    public void moveRight() {

    }

    public void applyPowerUp() {

    }
}
