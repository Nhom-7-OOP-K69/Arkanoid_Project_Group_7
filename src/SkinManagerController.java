
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SkinManagerController {
    private UIManager uiManager;

    @FXML private ImageView ballPreview;
    @FXML private ImageView brickPreview;

    @FXML private Button ballPrevButton;
    @FXML private Button ballNextButton;
    @FXML private Button brickPrevButton;
    @FXML private Button brickNextButton;
    @FXML private Button backButton;

    private final SkinManager skinManager = new SkinManager();

    @FXML
    public void initialize() {
        updatePreviews();

        ballPrevButton.setOnAction(e -> {
            // hiện tại Ball mới có 1 skin nên bạn có thể mở rộng sau
            updatePreviews();
        });

        ballNextButton.setOnAction(e -> {
            updatePreviews();
        });

        brickPrevButton.setOnAction(e -> {
            skinManager.previousBrickSkin();
            updatePreviews();
        });

        brickNextButton.setOnAction(e -> {
            skinManager.nextBrickSkin();
            updatePreviews();
        });
    }

    private void updatePreviews() {
        // Lấy ID frame preview của Brick
        String brickPreviewId = skinManager.getCurrentBrickSkin().getSpriteIds().get(0);
        String ballPreviewId = skinManager.getCurrentBallSkin();

        brickPreview.setImage(skinManager.getCurrentBrickSkin().getPreviewImage());
        ballPreview.setImage(ImgManager.getInstance().getImage(skinManager.getCurrentBallSkin()));
    }

    /*@FXML
    private void onBack() {
        if (uiManager != null) {
            uiManager.showMainMenu();
        } else {
            System.err.println("UIManager chưa được gán!");
        }
    }

     */


    public void setUIManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }
}
