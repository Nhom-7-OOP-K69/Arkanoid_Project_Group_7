package Arkanoid_Project_Group_7.src;

public class ExpandPaddlePowerUp extends PowerUp {
    private double expandSize = 40;

    public ExpandPaddlePowerUp(double x, double y, double width, double height, int duration) {
        super(x, y, width, height, 1, duration);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        paddle.setWidth(paddle.getWidth() + expandSize);
        paddle.setCurrentPowerUp(this.type);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball){
        paddle.setWidth(paddle.getWidth() - expandSize);
        paddle.setCurrentPowerUp(0);
    }
}