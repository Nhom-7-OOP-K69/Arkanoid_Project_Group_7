import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuManager {
    private Stage stage;

    public MenuManager(Stage stage) {
        this.stage = stage;
    }

    // Main Menu Scene
    public Scene getMainMenu() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        // Dùng ButtonFactory để tạo nút
        javafx.scene.control.Button startBtn = ButtonFactory.createButton("Start Game", 200);
        javafx.scene.control.Button optionsBtn = ButtonFactory.createButton("Options", 200);
        javafx.scene.control.Button exitBtn = ButtonFactory.createButton("Exit", 200);

        // Xử lý sự kiện
        startBtn.setOnAction(e -> {
            GameScene gameScene = new GameScene();
            stage.setScene(gameScene.getScene());
        });

        optionsBtn.setOnAction(e -> {
            stage.setScene(getOptionsMenu());
        });

        exitBtn.setOnAction(e -> stage.close());

        layout.getChildren().addAll(startBtn, optionsBtn, exitBtn);

        return new Scene(layout, 600, 400);
    }

    // Options Menu Scene
    public Scene getOptionsMenu() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        javafx.scene.control.Button backBtn = ButtonFactory.createButton("Back", 200);
        backBtn.setOnAction(e -> stage.setScene(getMainMenu()));

        layout.getChildren().addAll(backBtn);
        return new Scene(layout, 600, 400);
    }
}
