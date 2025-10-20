import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class BallLayer {
    private List<Ball> ballList;

    public BallLayer() {
        ballList = new ArrayList<>();
    }

    public List<Ball> getBallList() {
        return ballList;
    }

    public void ready(Paddle paddle) {
        Ball ball = new Ball();
        ball.setX(paddle.getX() + (paddle.getWidth() / 2) - (ball.getWidth() / 2));
        ball.setY(paddle.getY() - ball.getHeight());
        ballList.clear();
        ballList.add(ball);
    }

    public void launch() {
        Ball ball = ballList.getFirst();
        ball.setDy(ball.getSpeed());
    }

    public int checkCollisionBricks(BrickLayer brickLayer, PowerUpManager powerUpManager) {
        int score = 0;

        List<Brick> brickList = brickLayer.getBrickList();
        List<Brick> bricksToRemove = new ArrayList<>();
        for (Brick brick : brickList) {
            for (Ball ball : ballList) {
                if (ball.checkCollision(brick)) {
                    brick.takeHit();
                    if (brick.isDestroyed()) {
                        powerUpManager.spawnPowerUp(brick.getX(), brick.getY());
                        bricksToRemove.add(brick);
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
                            default:
                                brickScore = 0;
                                break;
                        }
                        score += brickScore;
                    }
                    ball.bounceOff(brick);
                }
            }
        }

        brickList.removeAll(bricksToRemove);

        return score;
    }

    public void move(double deltaTime) {
        for (Ball ball : ballList) {
            ball.move(deltaTime);
        }
    }

    public void checkCollisionPaddle(Paddle paddle) {
        for (Ball ball : ballList) {
            if (ball.checkCollision(paddle)) {
                ball.bounceOff(paddle);
            }
        }
    }

    public void collisionWall(Canvas canvas) {
        List<Ball> ballsToRemove = new ArrayList<>();

        for (Ball ball : ballList) {
            if (ball.collisionWall(canvas)) {
                ballsToRemove.add(ball);
            }
        }

        ballList.removeAll(ballsToRemove);
    }

    public void addBall(Ball ball) {
        if (ball == null) {
            return;
        }

        ballList.add(ball);
    }

    public void clearBall() {
        ballList.clear();
    }

    public boolean isEmpty() {
        return ballList.isEmpty();
    }

    public void render(GraphicsContext gc) {
        for (Ball ball : ballList) {
            ball.render(gc);
        }
    }
}
