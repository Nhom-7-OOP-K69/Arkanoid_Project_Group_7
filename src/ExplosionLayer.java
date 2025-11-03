import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExplosionLayer {
    private List<Explosion> explosionList;

    public ExplosionLayer() {
        explosionList = new ArrayList<>();
    }

    public List<Explosion> getExplosionList() {
        return explosionList;
    }

    public void addExplosionFromBrick(Brick brick) {
        Explosion explosion = new Explosion(brick.getX(), brick.getY());
        explosionList.add(explosion);
    }

    public void addExplosionList(List<Brick> brickList) {
        for (Brick brick : brickList) {
            addExplosionFromBrick(brick);
        }
    }

    public void update(double deltaTime) {
        Iterator<Explosion> iterator = explosionList.iterator();
        while (iterator.hasNext()) {
            Explosion ex = iterator.next();
            ex.update(deltaTime);

            if (ex.isFinished()) {
                iterator.remove();
            }
        }
    }

    public void render(GraphicsContext gc) {
        for (Explosion explosion : explosionList) {
            explosion.render(gc);
        }
    }
}
