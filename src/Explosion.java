import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Explosion extends GameObject {
    private List<Image> img;
    private Image current_img;

    private double totalDuration = 1.0;
    private double age = 0;
    private boolean isFinished = false;

    public Explosion(double x, double y) {
        super(x, y, GameConstants.BRICK_WIDTH, GameConstants.BRICK_HEIGHT);
        img = new ArrayList<>();
        img.add(ImgManager.getInstance().getImage("EXPLOSION0"));
        img.add(ImgManager.getInstance().getImage("EXPLOSION1"));
        img.add(ImgManager.getInstance().getImage("EXPLOSION2"));
        img.add(ImgManager.getInstance().getImage("EXPLOSION3"));
        img.add(ImgManager.getInstance().getImage("EXPLOSION4"));
        img.add(ImgManager.getInstance().getImage("EXPLOSION5"));
        img.add(ImgManager.getInstance().getImage("EXPLOSION6"));
        img.add(ImgManager.getInstance().getImage("EXPLOSION7"));
        img.add(ImgManager.getInstance().getImage("EXPLOSION8"));
        img.add(ImgManager.getInstance().getImage("EXPLOSION9"));

        if (!img.isEmpty()) {
            current_img = img.getFirst();
        }
    }

    public void update(double deltaTime) {
        if (isFinished || img.isEmpty()) {
            return;
        }

        age += deltaTime;

        double timePerFrame = totalDuration / img.size();
        int currentIndex = (int) (age / timePerFrame);

        if (currentIndex >= img.size()) {
            isFinished = true;
        } else {
            current_img = img.get(currentIndex);
        }
    }

    public void render(GraphicsContext gc) {
        if (!isFinished && current_img != null) {
            gc.drawImage(current_img, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        }
    }

    public boolean isFinished() {
        return isFinished;
    }
}