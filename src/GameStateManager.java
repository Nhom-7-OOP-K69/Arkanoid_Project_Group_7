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

    // --- Các phương thức Getter và Setter ---

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    public boolean isSoundEffectsOn() {
        return isSoundEffectsOn;
    }

    public void setSoundEffectsOn(boolean soundEffectsOn) {
        isSoundEffectsOn = soundEffectsOn;
    }

    public boolean isMusicOn() {
        return isMusicOn;
    }

    public void setMusicOn(boolean musicOn) {
        isMusicOn = musicOn;
    }

    // --- CÁC PHƯƠNG THỨC TIỆN ÍCH  ---
    public void toggleSoundEffects() {
        this.isSoundEffectsOn = !this.isSoundEffectsOn;
        System.out.println("Sound: " + (isSoundEffectsOn ? "On" : "Off"));
    }

    public void toggleMusic() {
        this.isMusicOn = !this.isMusicOn;
        System.out.println("Music: " + (isMusicOn ? "On" : "Off"));
    }

    public boolean isPausedOrOnMenu() {
        return currentState == GameState.PAUSED || currentState == GameState.MENU;
    }
}

