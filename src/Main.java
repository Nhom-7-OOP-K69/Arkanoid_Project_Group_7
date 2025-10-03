import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(400, 300);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Ball ball = new Ball(50, 100, 40, 40);
        Ball ball1 = new Ball(200, 50, 40, 40);
        Ball ball2 = new Ball(250, 200, 40, 40);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, 500, 500);

                ball2.checkCollisionWall(canvas);
                ball2.checkCollision(ball);
                ball2.checkCollision(ball1);
                ball2.move();
                ball2.render(gc);

                ball1.checkCollisionWall(canvas);
                ball1.checkCollision(ball);
                ball1.checkCollision(ball2);
                ball1.move();
                ball1.render(gc);

                ball.checkCollisionWall(canvas);
                ball.checkCollision(ball1);
                ball.checkCollision(ball2);
                ball.move();
                ball.render(gc);
            }
        }.start();

        primaryStage.setScene(new Scene(new StackPane(canvas), 400, 300));
        primaryStage.setTitle("GameObject Render Demo");
        primaryStage.show();
    }
}
