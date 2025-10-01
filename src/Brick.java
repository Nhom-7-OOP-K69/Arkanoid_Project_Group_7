public class Brick extends GameObject {
    private int hitPoints;
    private String type;

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public String getType() {
        return type;
    }

    public Brick(int hitPoints, String type) {
        this.hitPoints = hitPoints;
        this.type = type;
    }

    public void takeHit() {
        hitPoints--;
    }

    public boolean isDestroyed() {
        return hitPoints <= 0;
    }
}
