public class Ball extends MovableObject {
    private double speed;
    private double directionX;
    private double directionY;

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public void setDirectionX(double directionX) {
        this.directionX = directionX;
    }

    public double getDirectionX() {
        return directionX;
    }

    public void setDirectionY(double directionY) {
        this.directionY = directionY;
    }

    public double getDirectionY() {
        return directionY;
    }

    public void bounceOff(GameObject other) {

    }

    public void checkCollision(GameObject other) {

    }
}
