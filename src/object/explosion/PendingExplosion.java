package object.explosion;

import object.brick.Brick;

public class PendingExplosion {
    public Brick brick;
    public long activationTime;
    private long delayTime = 80;

    public PendingExplosion(Brick brick) {
        this.brick = brick;
        this.activationTime = System.currentTimeMillis() + delayTime;
    }
}
