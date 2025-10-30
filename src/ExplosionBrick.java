import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class ExplosionBrick extends Brick {
    private List<Image> img;
    private Image current_img;

    private double totalDuration = 1.0;
    private double age = 0;

    public ExplosionBrick(double x, double y) {
        super(x, y, GameConstants.EXPLOSION_BRICK_HP, GameConstants.EXPLOSION_TYPE);
        img = new ArrayList<>();
        img.add(ImgManager.getInstance().getImage("EXPLOSIONBRICK0"));
        img.add(ImgManager.getInstance().getImage("EXPLOSIONBRICK1"));
        img.add(ImgManager.getInstance().getImage("EXPLOSIONBRICK2"));
        img.add(ImgManager.getInstance().getImage("EXPLOSIONBRICK3"));
        img.add(ImgManager.getInstance().getImage("EXPLOSIONBRICK4"));
        img.add(ImgManager.getInstance().getImage("EXPLOSIONBRICK5"));
        img.add(ImgManager.getInstance().getImage("EXPLOSIONBRICK6"));
        img.add(ImgManager.getInstance().getImage("EXPLOSIONBRICK7"));

        if (!img.isEmpty()) {
            current_img = img.getFirst();
        }
    }

    public void update(double deltaTime) {
        if (this.isDestroyed() || img.isEmpty()) {
            return;
        }

        age += deltaTime;

        age = age % totalDuration;

        double timePerFrame = totalDuration / img.size();
        int currentIndex = (int) (age / timePerFrame);

        if (currentIndex < img.size()) {
            current_img = img.get(currentIndex);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!this.isDestroyed() && current_img != null) {
            gc.drawImage(current_img, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        }
    }
}