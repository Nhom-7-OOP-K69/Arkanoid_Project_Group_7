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

public class Main extends Application {
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
    private List<Image> paddleImgs = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Arkanoid Group7");
    }
}
