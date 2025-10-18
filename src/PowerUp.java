import java.util.List;

public abstract class PowerUp extends GameObject{
    protected int type;
    private int duration;
    protected boolean active;

    public PowerUp(double x, double y, double width, double height, int type, int duration){
        super(x, y, width, height);
        this.type = type;
        this.duration = duration;
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

    public abstract void applyEffect(Paddle paddle, Ball ball);
    public abstract void removeEffect(Paddle paddle, Ball ball);
}
