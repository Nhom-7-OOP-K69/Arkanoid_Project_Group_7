public abstract class PowerUp extends GameObject{
    protected int type;
    protected int duration;
    protected boolean active;

    public PowerUp(double x, double y, double width, double height, int type, int duration){
        super(x, y, width, height);
        this.type = type;
        this.duration = duration;
        this.active = false;
    }

    public int getType(){
        return type;
    }

    public int getDuration(){
        return duration;
    }

    public boolean isActive() {
        return active;
    }

    public void start() {
        this.active = true;
    }

    public void resetDuration(int newDuration) {
        this.duration = newDuration;
        this.active = true;
    }

    public boolean tick() {
        if (!active) return false;

        duration--;
        if (duration <= 0) {
            active = false;
            return false; //hết thời gian
        }
        return true; //vẫn còn hiệu lực
    }

    public abstract void applyEffect(Paddle paddle, BallLayer ballLayer);
    public abstract void removeEffect(Paddle paddle, Ball ball);
}
