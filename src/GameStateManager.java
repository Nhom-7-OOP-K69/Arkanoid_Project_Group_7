/**
 * Lớp GameStateManager chịu trách nhiệm quản lý và theo dõi trạng thái chung của trò chơi,
 * cũng như các cài đặt như âm thanh và nhạc nền.
 * Nó hoạt động như một máy trạng thái (state machine) đơn giản.
 */
public class GameStateManager {

    /**
     * Enum (liệt kê) định nghĩa các trạng thái có thể có của game.
     * - MENU: Đang ở màn hình chính.
     * - READY: Vào màn chơi nhưng đang chờ người chơi phóng bóng.
     * - PLAYING: Trò chơi đang diễn ra.
     * - PAUSED: Trò chơi đã được tạm dừng.
     */
    public enum GameState {
        MENU,
        READY,
        PLAYING,
        PAUSED
    }

    private GameState currentState;     // Biến lưu trữ trạng thái hiện tại của game.
    private boolean isSoundEffectsOn;   // Cờ (flag) để bật/tắt âm thanh hiệu ứng.
    private boolean isMusicOn;          // Cờ (flag) để bật/tắt nhạc nền.

    /**
     * Constructor (phương thức khởi tạo) cho GameStateManager.
     * Mặc định, game bắt đầu ở trạng thái MENU và cả âm thanh/nhạc nền đều được bật.
     */
    public GameStateManager() {
        this.currentState = GameState.MENU;
        this.isSoundEffectsOn = true;
        this.isMusicOn = true;
    }

    /**
     * Lấy (get) trạng thái hiện tại của game.
     * @return Trạng thái game hiện tại (GameState).
     */
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * Thiết lập (set) một trạng thái mới cho game.
     * @param currentState Trạng thái mới cần đặt.
     */
    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    /**
     * Kiểm tra xem âm thanh hiệu ứng có đang được bật hay không.
     * @return true nếu đang bật, ngược lại là false.
     */
    public boolean isSoundEffectsOn() {
        return isSoundEffectsOn;
    }

    /**
     * Bật hoặc tắt âm thanh hiệu ứng.
     * @param soundEffectsOn Trạng thái mới (true = bật, false = tắt).
     */
    public void setSoundEffectsOn(boolean soundEffectsOn) {
        isSoundEffectsOn = soundEffectsOn;
    }

    /**
     * Kiểm tra xem nhạc nền có đang được bật hay không.
     * @return true nếu đang bật, ngược lại là false.
     */
    public boolean isMusicOn() {
        return isMusicOn;
    }

    /**
     * Bật hoặc tắt nhạc nền.
     * @param musicOn Trạng thái mới (true = bật, false = tắt).
     */
    public void setMusicOn(boolean musicOn) {
        isMusicOn = musicOn;
    }
}
