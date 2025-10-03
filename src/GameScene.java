import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class GameScene {
    private Scene scene;

    public GameScene() {
        Pane root = new Pane();

        Text label = new Text("Arkanoid Game Running...");
        label.setFill(Color.BLACK);
        label.setX(200);
        label.setY(200);

        root.getChildren().add(label);

        this.scene = new Scene(root, 600, 400, Color.LIGHTGRAY);
    }

    public Scene getScene() {
        return scene;
    }
}
