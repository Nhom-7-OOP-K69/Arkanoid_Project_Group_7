public class ExpandPaddlePowerUp extends PowerUp {
    private double expandSize = GameConstants.PADDLE_EXPAND_SIZE;

    public ExpandPaddlePowerUp(double x, double y, double width, double height, int duration) {
        super(x, y, width, height, 1, duration);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        if(!active){
            paddle.setWidth(paddle.getWidth() + expandSize);
            paddle.setCurrentPowerUp(this.type);
            start();
        }
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball){
        paddle.setWidth(paddle.getWidth() - expandSize);
        paddle.setCurrentPowerUp(0);
        active = false;
    }
}