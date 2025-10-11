public class ExtraDamagePowerUp extends PowerUp {
    private int extraDamege = GameConstants.EXTRA_DAMAGE;

    public ExtraDamagePowerUp(double x, double y, double width, double height, int duration){
        super(x, y, width, height, 2, duration);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball){
        if(!active){
            //ball.setDamage(ball.getDamage() + extraDamage);
            paddle.setCurrentPowerUp(this.type);
            start();
        }
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball){
        //ball.setDamage(ball.getDamage() - extraDamage);
        paddle.setCurrentPowerUp(0);
        active = false;
    }
}