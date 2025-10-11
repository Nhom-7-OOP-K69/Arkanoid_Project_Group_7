import java.util.*;

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
import javafx.scene.image.Image;

public class GameManager extends Application {
    //JavaFX variables
    private Canvas bkgCanvas = new Canvas(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);      // Canvas chứa background
    private GraphicsContext bkgCtx = bkgCanvas.getGraphicsContext2D();                                  //Context vẽ background
    private Canvas canvas = new Canvas(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);          // Canvas chứa enity: ball,paddle,...
    private GraphicsContext ctx = canvas.getGraphicsContext2D();                                         // Context vẽ entity
    private Canvas UICanvas = new Canvas(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);        // Canvas chứa UI
    private GraphicsContext UIctx = UICanvas.getGraphicsContext2D();                                     // Context vẽ UI


    //Entity
    private Paddle paddle;
    private Ball ball;
    private List<NormalBrick> normal_bricks;
    private List<StrongBrick> strong_bricks;
    private List<SuperBrick> super_bricks;


    //Img variables
    private Image blueBrickImg;
    private Image greenBrickImg;
    private Image purpleBrickImg;
    private Image redBrickImg;
    private Image break_blueBrickImg;
    private Image break_greenBrickImg;
    private Image break_redBrickImg;
    private Image break_purpleBrickImg;
    private Image ballImg;
    private List<Image> paddleImgs = new  ArrayList<>();

    private void LoadImages() {
        blueBrickImg = new Image("file:images/01-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false);
        greenBrickImg = new Image("file:images/03-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false);
        purpleBrickImg = new Image("file:images/05-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false);
        redBrickImg = new Image("file:images/07-Breakout-Tiles.png", GameConstants.BRICK_WIDTH, 0, true, false);
        ballImg = new Image("file:images/58-Breakout-Tiles.png", GameConstants.BALL_WIDTH, 0, true, false);
        for(int i = 0 ;i < GameConstants.PADDLE_FRAMES; i++){
            int j = 50+i;
            Image paddleimg = new Image("file:images/"+j+"-Breakout-Tiles.png", GameConstants.PADDLE_WIDTH, 0, true, false);
            paddleImgs.add(paddleimg);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) {
        LoadImages();

        ball = new Ball(500, 400, GameConstants.BALL_WIDTH, GameConstants.BALL_HEIGHT);
        paddle = new Paddle(500,500, GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT);
        
        StackPane pane = new StackPane(bkgCanvas,canvas,UICanvas);
        Scene scene = new Scene(pane);

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT,A : paddle.moveLeft(); break;
                case RIGHT,D : paddle.moveRight(); break;
            }
        });
        scene.setOnKeyReleased(event -> {
            paddle.stop();
        });
        new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }
                double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;

                ctx.clearRect(0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);

                paddle.render(paddleImgs,ctx);
                paddle.move(deltaTime);

                ball.render(ballImg, ctx);
                ball.collisionWall(canvas);
                if (ball.checkCollision(paddle)) {
                    ball.bounceOff(paddle);
                }
                ball.move(deltaTime);

            }
        }.start();

        stage.setScene(scene);
        stage.setTitle("Kiểm tra load ảnh");
        stage.show();

        canvas.requestFocus();
    }
}
