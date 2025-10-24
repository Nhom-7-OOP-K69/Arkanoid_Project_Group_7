import javafx.scene.canvas.GraphicsContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class BrickLayer {
    private List<Brick> brickList = new ArrayList<>();
    private List<Brick> explosionBrickList = new ArrayList<>();
    private List<PendingExplosion> pendingExplosions = new ArrayList<>();
    private List<Brick> explosionList = new ArrayList<>();

    public List<Brick> getBrickList() {
        return brickList;
    }

    public List<Brick> getExplosionList() {
        return explosionList;
    }

    public void loadBrick(String fileName) {
        File file = new File(fileName);

        try (Scanner scanner = new Scanner(file)) {
            int j = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                for (int i = 0; i < line.length(); i++) {
                    Brick brick = null;
                    switch (line.charAt(i)) {
                        case '1':
                            brick = new NormalBrick(GameConstants.BRICK_WIDTH * i, GameConstants.BRICK_HEIGHT * j + 50 + GameConstants.UI_TOP_BAR_HEIGHT);
                            break;
                        case '2':
                            brick = new StrongBrick(GameConstants.BRICK_WIDTH * i, GameConstants.BRICK_HEIGHT * j + 50 + GameConstants.UI_TOP_BAR_HEIGHT);
                            break;
                        case '3':
                            brick = new SuperBrick(GameConstants.BRICK_WIDTH * i, GameConstants.BRICK_HEIGHT * j + 50 + GameConstants.UI_TOP_BAR_HEIGHT);
                            break;
                        case '4':
                            brick = new ExplosionBrick(GameConstants.BRICK_WIDTH * i, GameConstants.BRICK_HEIGHT * j + 50 + GameConstants.UI_TOP_BAR_HEIGHT);
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

    public void removeBricks(List<Brick> brickToRemove) {
        brickList.removeAll(brickToRemove);
    }

    public void addExplosionBrick(Brick brick) {
        if (brick == null) {
            return;
        }

        explosionBrickList.add(brick);
    }

    public int explosion(List<Brick> bricksToRemove) {
        int score = 0;

        Set<Brick> currentExplosions = new HashSet<>(explosionBrickList);
        explosionBrickList.clear();

        for (Brick explosionBrick : currentExplosions) {
            double expX = explosionBrick.getX();
            double expY = explosionBrick.getY();

            for (Brick brick : brickList) {
                if (brick == explosionBrick || bricksToRemove.contains(brick)) {
                    continue;
                }

                double brickX = brick.getX();
                double brickY = brick.getY();

                boolean yMatch = (brickY == expY - GameConstants.BRICK_HEIGHT) ||
                        (brickY == expY) ||
                        (brickY == expY + GameConstants.BRICK_HEIGHT);

                boolean xMatch = (brickX == expX - GameConstants.BRICK_WIDTH) ||
                        (brickX == expX) ||
                        (brickX == expX + GameConstants.BRICK_WIDTH);

                if (yMatch && xMatch) {
                    bricksToRemove.add(brick);
                    explosionList.add(brick);
                    int brickScore;
                    switch (brick.getType()) {
                        case GameConstants.NORMAL_TYPE:
                            brickScore = 10;
                            break;
                        case GameConstants.STRONG_TYPE:
                            brickScore = 20;
                            break;
                        case GameConstants.SUPER_TYPE:
                            brickScore = 30;
                            break;
                        case GameConstants.EXPLOSION_TYPE:
                            brickScore = 10;
                            pendingExplosions.add(new PendingExplosion(brick));
                            break;
                        default:
                            brickScore = 0;
                            break;
                    }
                    score += brickScore;
                }
            }
        }

        return score;
    }

    public int processPendingExplosions() {
        int scorePlus = 0;
        long currentTime = System.currentTimeMillis();

        Iterator<PendingExplosion> iterator = pendingExplosions.iterator();
        while (iterator.hasNext()) {
            PendingExplosion pExp = iterator.next();

            if (currentTime >= pExp.activationTime) {
                explosionBrickList.add(pExp.brick);

                iterator.remove();
            }
        }

        if (!explosionBrickList.isEmpty()) {
            List<Brick> bricksToRemove = new ArrayList<>();
            scorePlus += this.explosion(bricksToRemove);

            brickList.removeAll(bricksToRemove);
        }

        return scorePlus;
    }

    public void explosionClear() {
        explosionList.clear();
    }

    public void update(double deltaTime) {
        for (Brick brick : brickList) {
            if (brick instanceof ExplosionBrick) {
                ((ExplosionBrick) brick).update(deltaTime);
            }
        }
    }

    public boolean isEmpty() {
        return brickList.isEmpty();
    }

    public void render(GraphicsContext gc) {
        for (Brick brick : brickList) {
            brick.render(gc);
        }
    }
}
