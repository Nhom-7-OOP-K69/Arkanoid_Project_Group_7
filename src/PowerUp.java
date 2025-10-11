public abstract class PowerUp extends GameObject {
    protected int type;
    protected int duration;
    protected boolean active;

    public PowerUp(double x, double y, double width, double height, int type, int duration) {
        super(x, y, width, height);
        this.type = type;
        this.duration = duration;
        this.active = false;
    }

    public int getType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isActive() {
        return active;
    }

    public void start() {
        this.active = true;
    }

    public boolean tick() {
        if (!active) return false;
        duration--;
        if (duration <= 0) {
            active = false;
            return false;
        }
        return true;
    }

    public abstract void applyEffect(Paddle paddle, Ball ball);
    public abstract void removeEffect(Paddle paddle, Ball ball);
}