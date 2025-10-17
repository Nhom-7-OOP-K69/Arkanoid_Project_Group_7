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

    @Override
    public void start(Stage primaryStage) {
        // 1. Tạo một đối tượng quản lý game
        GameManager gameManager = new GameManager();

        // 2. Yêu cầu gameManager bắt đầu và "giao" cửa sổ chính (primaryStage) cho nó
        gameManager.initializeAndRun(primaryStage);
    }

    public static void main(String[] args) {
        // Đây là nơi chương trình thực sự bắt đầu
        launch(args);
    }
}
