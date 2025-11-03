
import javafx.scene.image.Image;

public class SkinManager {

    private static volatile SkinManager instance;

    // Skin hiện tại của paddle và ball
    private String currentPaddleSkin;
    private String currentBallSkin;

    // Danh sách các key có trong ImgManager
    private static final String[] PADDLE_SKINS = {
            "PADDLE0", "PADDLE1", "PADDLE2", "PADDLE3", "PADDLE4"
    };

    private static final String[] BALL_SKINS = {
            "BALL", "BALL1", "BALL2", "BALL3"
    };

    private SkinManager() {
        // Gán skin mặc định
        currentPaddleSkin = "PADDLE0";
        currentBallSkin = "BALL";
    }

    public static SkinManager getInstance() {
        if (instance == null) {
            synchronized (SkinManager.class) {
                if (instance == null) {
                    instance = new SkinManager();
                }
            }
        }
        return instance;
    }

    // --- LẤY ẢNH HIỆN TẠI ---

    public Image getPaddleImage() {
        return ImgManager.getInstance().getImage(currentPaddleSkin);
    }

    public Image getBallImage() {
        return ImgManager.getInstance().getImage(currentBallSkin);
    }

    // --- ĐỔI SKIN ---

    public void setPaddleSkin(String skinId) {
        if (ImgManager.getInstance().getImage(skinId) != null) {
            this.currentPaddleSkin = skinId;
            System.out.println("Paddle skin changed to: " + skinId);
        } else {
            System.err.println("Invalid paddle skin ID: " + skinId);
        }
    }

    public void setBallSkin(String skinId) {
        if (ImgManager.getInstance().getImage(skinId) != null) {
            this.currentBallSkin = skinId;
            System.out.println("Ball skin changed to: " + skinId);
        } else {
            System.err.println("Invalid ball skin ID: " + skinId);
        }
    }

    // --- LẤY ID SKIN HIỆN TẠI ---
    public String getCurrentPaddleSkinId() {
        return currentPaddleSkin;
    }

    public String getCurrentBallSkinId() {
        return currentBallSkin;
    }

    // --- TIỆN ÍCH CHO MENU ---
    public String[] getAvailablePaddleSkins() {
        return PADDLE_SKINS;
    }

    public String[] getAvailableBallSkins() {
        return BALL_SKINS;
    }
}
