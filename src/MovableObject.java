public class MovableObject extends GameObject{
    protected double dx;
    protected double dy;

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

    public void move(double deltaTime) {}
}
