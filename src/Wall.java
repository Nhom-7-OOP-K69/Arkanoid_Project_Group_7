public class Wall extends Brick{
    public Wall(double x, double y) {
        super(x, y, 0, GameConstants.WALL_TYPE);
        this.setImg(ImgManager.getInstance().getImage("SILVER_BRICK"));
    }

    @Override
    public boolean isDestroyed() {
        return false;
    }
}
