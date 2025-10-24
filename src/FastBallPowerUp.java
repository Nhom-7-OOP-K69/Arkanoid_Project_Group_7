import javafx.scene.canvas.GraphicsContext;

public class FastBallPowerUp extends PowerUp {

    public FastBallPowerUp(double x, double y, double width, double height, int duration){
        super(x, y, width, height, 2, duration);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball){
        AudioManager.getInstance().playSfx("get_item");

        ball.setDx(ball.getDx() * GameConstants.EXTRA_SPEED);
        ball.setDy(ball.getDy() * GameConstants.EXTRA_SPEED);
        paddle.setCurrentPowerUp(this.type);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball){
        ball.setDx(ball.getDx() / GameConstants.EXTRA_SPEED);
        ball.setDy(ball.getDy() / GameConstants.EXTRA_SPEED);
        paddle.setCurrentPowerUp(0);
    }
    @Override
    public void render(GraphicsContext gc) {

    }
}