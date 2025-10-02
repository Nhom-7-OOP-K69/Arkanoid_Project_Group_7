public class MovableObject extends GameObject{
    private double dx;
    private double dy;

    public MovableObject(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDx() {
        return dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getDy() {
        return dy;
    }

    public void move() {
        this.setX(this.getX() + this.dx);
        this.setY(this.getY() + this.dy);
    }
}
