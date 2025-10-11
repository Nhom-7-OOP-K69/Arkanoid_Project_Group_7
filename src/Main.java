import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.event.KeyEvent;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Ve paddle
        Paddle pd = new Paddle(340, 550, 90, 15);

        // them keyevent
        Scene scene = new Scene(new StackPane(canvas), 800, 600);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
                pd.moveLeft();
            } else if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                pd.moveRight();
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT
                    || event.getCode() == KeyCode.A || event.getCode() == KeyCode.D) {
                pd.dx = 0;  // paddle dung lai.
            }
        });


        // ve bong
        Ball ball = new Ball(50, 100, 20, 20);


        /**
         * test game.
         */

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

               // render ball

                ball.checkCollisionWall(canvas);
                ball.checkCollision(pd);
                ball.move();
                ball.render(gc);

                //render paddle
                pd.move();
                pd.checkCollisionWall(canvas);
                pd.render(gc);
                pd.setSpeed(4);

            }
        }.start();

        primaryStage.setScene(scene);
        primaryStage.setTitle("GameObject Render Demo");
        primaryStage.show();
        primaryStage.requestFocus(); // nhận lệnh phím khi mở sang cửa sổ khác.
    }
}
