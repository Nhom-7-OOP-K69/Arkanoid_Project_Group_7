package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import manager.ImgManager;

public class Lives {
    private Image img;
    private int HP;

    public Lives() {
        HP = 3;
        img = ImgManager.getInstance().getImage("LIFE");
    }

    public void render(GraphicsContext gc) {
        if (img == null) {
            System.err.println("Ảnh 'LIFE' chưa được tải!");
            return;
        }

        double screenHeight = GameConstants.SCREEN_HEIGHT;

        double paddingX = 10.0;
        double paddingY = 10.0;
        double spacing = 5.0;

        double imageWidth = img.getWidth();
        double imageHeight = img.getHeight();

        double y = screenHeight - imageHeight - paddingY;

        for (int i = 0; i < HP; i++) {
            double x = paddingX + (imageWidth + spacing) * i;

            gc.drawImage(img, x, y);
        }
    }

    public void decreaseLife() {
        if (HP > 0) {
            HP--;
        }
    }

    public int getHP() {
        return HP;
    }

    public void reset() {
        HP = 3;
    }

    public boolean isGameOver() {
        return HP <= 0;
    }
}