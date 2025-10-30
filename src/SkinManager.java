import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.List;

import java.util.Arrays;
import java.util.List;

public class SkinManager {
    private final List<BrickSkinData> BRICK_SKIN_IDS;
    private final List<String> BALL_SKIN_IDS;
    private BrickSkinData currentBrickSkin;
    private String currentBallSkin;
    private int currentBrickSkinIndex = 0;
    private int currentBallSkinIndex = 0;

    public SkinManager() {
        BALL_SKIN_IDS = Arrays.asList(
                "BALL"
        );

        BRICK_SKIN_IDS = Arrays.asList(
                new BrickSkinData(
                        "BASIC",
                        "GREEN_BRICK",
                        Arrays.asList("GREEN_BRICK", "GREEN_BRICK_")
                ),
                new BrickSkinData(
                        "HYPER",
                        "HYPER_1",
                        Arrays.asList("HYPER_1", "HYPER_2", "HYPER_3", "HYPER_4",
                                "HYPER_5", "HYPER_6", "HYPER_7", "HYPER_8")
                )
        );
        currentBrickSkin = BRICK_SKIN_IDS.get(0);
        currentBallSkin = BALL_SKIN_IDS.get(0);
    }

    public List<BrickSkinData> getBrickSkins() {
        return BRICK_SKIN_IDS;
    }

    public List<String> getBallSkins() {
        return BALL_SKIN_IDS;
    }

    public BrickSkinData getCurrentBrickSkin() {
        return BRICK_SKIN_IDS.get(currentBrickSkinIndex);
    }

    public String getCurrentBallSkin() {
        return BALL_SKIN_IDS.get(currentBallSkinIndex);
    }

    public void setBrickSkinByIndex(int index) {
        // Đảm bảo index nằm trong giới hạn
        if (index >= 0 && index < BRICK_SKIN_IDS.size()) {
            this.currentBrickSkinIndex = index;
            // Cập nhật currentBrickSkin (tùy chọn)
            this.currentBrickSkin = BRICK_SKIN_IDS.get(index);

            // Ở đây bạn sẽ gọi hàm lưu skin này vào PlayerPrefs/File Cấu hình
            // saveSelection(this.currentBrickSkin.getFriendlyName());
        }
    }

    /**
     * Tăng index skin gạch hiện tại (quấn vòng nếu vượt quá giới hạn).
     */
    public void nextBrickSkin() {
        currentBrickSkinIndex = (currentBrickSkinIndex + 1) % BRICK_SKIN_IDS.size();
        setBrickSkinByIndex(currentBrickSkinIndex);
    }

    /**
     * Giảm index skin gạch hiện tại (quấn vòng nếu nhỏ hơn 0).
     */
    public void previousBrickSkin() {
        currentBrickSkinIndex = (currentBrickSkinIndex - 1 + BRICK_SKIN_IDS.size()) % BRICK_SKIN_IDS.size();
        setBrickSkinByIndex(currentBrickSkinIndex);
    }

    // ... (Thêm logic tương tự cho Paddle Skins nếu cần) ...

}


class BrickSkinData {
    private final String friendlyName;

    // ID của frame bình thường (dùng cho preview UI)
    private final String previewFrameId;

    // Danh sách các ID frame gạch vỡ/nứt (có thể là 1, 2, hoặc nhiều hơn)
    private final List<String> spriteIds;


    public BrickSkinData(String name, String previewFrameId, List<String> spriteIds) {
        this.friendlyName = name;
        this.previewFrameId = previewFrameId;
        this.spriteIds = spriteIds;
    }

    public List<String> getSpriteIds() {
        return spriteIds;
    }
    public Image getPreviewImage() {
        return ImgManager.getInstance().getImage("previewFrameId");
    }
}
