import java.util.List;

public class ExtraBallPowerUp extends PowerUp {
    private int extraCount;
    private List<Ball> ballsRef;
    private boolean active;

    /**
     * constructor.
     */
    public ExtraBallPowerUp(double x, double y, double width, double height, int type, int duration
                            , List<Ball> ballsRef, int extraCount) {
        super(x, y, width, height, 3, 0);
        this.ballsRef = ballsRef;
        this.extraCount = extraCount;
    }

    /**
     * apply effect.
     */
    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        for (int i = 0; i < extraCount; i++) {
            Ball newball = new Ball(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight());
            double newDx = ball.getDx() * (Math.random() > 0.5 ? 1 : -1);
            double newDy = -Math.abs(ball.getDy());
            newball.setDx(newDx);
            newball.setDy(newDy);

            ballsRef.add(newball);
        }
        this.active = false;
    }

    /**
     * remove effect.
     */
    public void removeEffect(Paddle paddle, Ball ball) {

    }
}
