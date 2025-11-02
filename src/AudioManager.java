import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private static volatile AudioManager instance;

    private final Map<String, AudioClip> sfxClips = new HashMap<>();
    private MediaPlayer backgroundMusic;

    private double musicVolume = 0.5;
    private double sfxVolume = 0.5;

    private static final AudioLoader loader = new AudioLoader();

    private AudioManager() {
        loadSfx();
        loadMusic();
    }

    private void loadMusic() {
        Media media = new Media(getClass().getResource("/sound/game_music.wav").toExternalForm());
        backgroundMusic = new MediaPlayer(media);

    }
    private void loadSfx() {
        sfxClips.put("SHOT", loader.loadAudioClip("laser_shot"));
        sfxClips.put("COLLISION", loader.loadAudioClip("collision"));
        sfxClips.put("GET_ITEM", loader.loadAudioClip("get_item"));
        sfxClips.put("LEVEL_UP", loader.loadAudioClip("level_up"));
        sfxClips.put("BREAK", loader.loadAudioClip("break"));
        sfxClips.put("GAME_MUSIC", loader.loadAudioClip("game_music"));
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            synchronized (AudioManager.class) {
                if (instance == null) {
                    instance = new AudioManager();
                }
            }
        }
        return instance;
    }

    // ==================== NHẠC NỀN ====================

    /**
     * Phát nhạc nền loop vô hạn
     */
    public void playBackgroundMusic() {
        stopBackgroundMusic();
        if (backgroundMusic != null) {
            backgroundMusic.setCycleCount(AudioClip.INDEFINITE);
            backgroundMusic.setVolume(musicVolume);
            backgroundMusic.play();
        } else {
            System.err.println("Background music not found: GAME_MUSIC");
        }
    }

    /**
     * Dừng nhạc nền
     */
    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    /**
     * Điều chỉnh âm lượng nhạc nền (0.0 - 1.0)
     */
    public void setMusicVolume(double volume) {
        musicVolume = clamp(volume, 0.0, 1.0);
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(musicVolume);
        }
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    // ==================== ÂM THANH HIỆU ỨNG ====================

    public void playSfx(String id) {
        AudioClip clip = sfxClips.get(id);
        if (clip != null) {
            clip.setVolume(sfxVolume);
            clip.play();
        } else {
            System.err.println("SFX not found: " + id);
        }
    }

    public void setSfxVolume(double volume) {
        sfxVolume = clamp(volume, 0.0, 1.0);
    }

    public double getSfxVolume() {
        return sfxVolume;
    }

    // ==================== TIỆN ÍCH ====================

    private double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }
}

class AudioLoader {

    private static String getResourceUrl(String path) {
        URL resource = AudioLoader.class.getResource(path);
        if (resource == null) {
            throw new RuntimeException("ERROR: Audio resource not found at " + path);
        }
        return resource.toExternalForm();
    }

    public static AudioClip loadAudioClip(String fileName) {
        String fullFileName = fileName + ".wav";
        try {
            String resourceUrl = getResourceUrl("/sound/" + fullFileName);
            return new AudioClip(resourceUrl);
        } catch (RuntimeException e) {
            System.err.println("Failed to load audio clip '" + fullFileName + "': " + e.getMessage());
            return null;
        }
    }
}
