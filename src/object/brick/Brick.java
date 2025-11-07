package object.brick;

import game.GameConstants;
import javafx.scene.canvas.GraphicsContext;
import manager.AudioManager;
import object.gameObject.GameObject;

public abstract class Brick extends GameObject {
    private int hitPoints;
    private int type;

    public Brick(double x, double y, int hitPoints, int type) {
        super(x, y, GameConstants.BRICK_WIDTH, GameConstants.BRICK_HEIGHT);
        this.hitPoints = hitPoints;
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
        if(hitPoints <= 0) {
            AudioManager.getInstance().playSfx("BREAK");
        }
        return hitPoints <= 0;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!this.isDestroyed()) {
            gc.drawImage(this.getImg(), this.getX(), this.getY());
        }
    }
}
