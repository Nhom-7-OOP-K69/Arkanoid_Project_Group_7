import javafx.scene.canvas.GraphicsContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BrickLayer {
    private List<Brick> brickList = new ArrayList<>();

    public List<Brick> getBrickList() {
        return brickList;
    }

    public void loadBrick(File file) {
        try (Scanner scanner = new Scanner(file)) {
            int j = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                for (int i = 0; i < line.length(); i++) {
                    Brick brick = null;
                    switch (line.charAt(i)) {
                        case '1':
                            brick = new NormalBrick(GameConstants.BRICK_WIDTH * i, GameConstants.BRICK_HEIGHT * j + GameConstants.UI_TOP_BAR_HEIGHT);
                            break;
                        case '2':
                            brick = new StrongBrick(GameConstants.BRICK_WIDTH * i, GameConstants.BRICK_HEIGHT * j + GameConstants.UI_TOP_BAR_HEIGHT);
                            break;
                        case '3':
                            brick = new SuperBrick(GameConstants.BRICK_WIDTH * i, GameConstants.BRICK_HEIGHT * j + GameConstants.UI_TOP_BAR_HEIGHT);
                            break;
                        default:
                            break;
                    }
                    addBrick(brick);
                }
                j++;
            }

        } catch (FileNotFoundException e) {
            System.err.println("Lỗi: Không tìm thấy file!");
        }
    }

    public void addBrick(Brick brick) {
        if (brick == null) {
            return;
        }

        brickList.add(brick);
    }

    public void removeBrick(Brick brick) {
        brickList.removeIf(brick1 -> brick1.getX() == brick.getX()
                            && brick1.getY() == brick.getY());
    }

    public void render(GraphicsContext gc) {
        for (Brick brick : brickList) {
            brick.render(gc);
        }
    }
}
