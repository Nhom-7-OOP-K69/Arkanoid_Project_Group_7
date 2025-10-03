import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MenuManager menu = new MenuManager(primaryStage);
        Scene menuScene = menu.getMainMenu();

        primaryStage.setTitle("Arkanoid Menu");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    static void main(String[] args) {
        launch(args);
    }
}
