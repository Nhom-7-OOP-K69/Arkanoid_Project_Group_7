// File: GameStateManager.java
public class GameStateManager {
    public enum GameState {MENU, READY, PLAYING, PAUSED}
    private GameState currentState;
    private boolean isSoundEffectsOn;
    private boolean isMusicOn;

    public GameStateManager() {
        this.currentState = GameState.MENU;
        this.isSoundEffectsOn = true;
        this.isMusicOn = true;
    }

    public GameState getCurrentState() { return currentState; }
    public void setCurrentState(GameState currentState) { this.currentState = currentState; }
    public boolean isSoundEffectsOn() { return isSoundEffectsOn; }
    public void setSoundEffectsOn(boolean soundEffectsOn) { isSoundEffectsOn = soundEffectsOn; }
    public boolean isMusicOn() { return isMusicOn; }
    public void setMusicOn(boolean musicOn) { isMusicOn = musicOn; }
}