import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class ExtraBallPowerUp extends PowerUp {
    private List<Ball> gameBalls;

    public ExtraBallPowerUp(double x, double y, double width, double height, int duration, List<Ball> gameBalls){
        super(x, y, width, height, 2, duration);
        this.gameBalls = gameBalls;
    }

    @Override
    public void applyEffect(Paddle paddle, BallLayer ballLayer){
        System.out.println("Thêm 2 bóng mới");

        // tạo 2 bóng mới
        double x = paddle.getX() + paddle.getWidth() / 2.0 - GameConstants.BALL_WIDTH / 2.0;
        double y = paddle.getY() - GameConstants.BALL_HEIGHT;

        // tốc độ bóng
        double speed = 300;

        // góc bay ra của 2 quả bóng (45 độ)
        double angle = Math.toRadians(45);

        // vận tốc của 2 quả bóng mới
        double dx1 = speed * Math.cos(angle);     // bay sang phải
        double dx2 = -speed * Math.cos(angle);    // bay sang trái
        double dy = -speed * Math.sin(angle);    // bay lên trên


        // tạo 2 bóng mới
        Ball ballLeft = new Ball(x, y, GameConstants.BALL_WIDTH, GameConstants.BALL_HEIGHT);
        ballLeft.setDx(dx1);
        ballLeft.setDy(dy);
        Ball ballRight = new Ball(x, y, GameConstants.BALL_WIDTH, GameConstants.BALL_HEIGHT);
        ballRight.setDx(dx2);
        ballRight.setDy(dy);

        // thêm vào danh sách bóng của game
        gameBalls.add(ballLeft);
        gameBalls.add(ballRight);


        System.out.println("Thêm 2 bóng mới");
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball){

    }

    @Override
    public void render(GraphicsContext gc) {

    }
}