public class FastBallPowerUp extends PowerUp {
    private double extraSpeed = 1.5;

    public FastBallPowerUp(double x, double y, double width, double height, int duration){
        super(x, y, width, height, 2, duration);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball){
        ball.setDx(ball.getDx() * extraSpeed);
        ball.setDy(ball.getDy() * extraSpeed);
        paddle.setCurrentPowerUp(this.type);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball){
        ball.setDx(ball.getDx() / extraSpeed);
        ball.setDy(ball.getDy() / extraSpeed);
        paddle.setCurrentPowerUp(0);
    }
}