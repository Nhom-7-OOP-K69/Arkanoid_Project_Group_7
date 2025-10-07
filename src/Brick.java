package Arkanoid_Project_Group_7.src;

public class Brick extends GameObject {
    private int hitPoints;
    private int type;

    public Brick(double x, double y, int hitPoints, int type) {
        super(x, y, GameConstants.BRICK_WIDTH, GameConstants.BRICK_HEIGHT);
        this.hitPoints = hitPoints;
        this.type = type;
    }

    public void HandleHit() {
        hitPoints--;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getType() {
        return type;
    }

    public void takeHit() {
        hitPoints--;
    }

    public boolean isDestroyed() {
        return hitPoints <= 0;
    }
}
