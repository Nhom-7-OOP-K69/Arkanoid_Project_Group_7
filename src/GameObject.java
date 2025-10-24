import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
public abstract class GameObject {
    private double x;
    private double y;
    private double height;
    private double width;
    protected Image img;

    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public GameObject() {}

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public Image getImg() {
        return img;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(this.img, this.x, this.y);
    }
}
