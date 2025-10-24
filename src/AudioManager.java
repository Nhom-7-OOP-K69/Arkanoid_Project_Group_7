import javafx.scene.media.AudioClip;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private static volatile AudioManager instance;
    private Map<String, AudioClip> sfxClips = new HashMap<>();

    // Không cần volatile nếu bạn dùng phương pháp khởi tạo Eagerly, nhưng hiện tại là OK
    private static final AudioLoader loader = new AudioLoader();

    private AudioManager() {
        loadSfx();
    }

    private void loadSfx() {
        sfxClips.put("SHOT", loader.loadAudioClip("laser_shot"));
        sfxClips.put("COLLISION", loader.loadAudioClip("collision"));
        sfxClips.put("GET_ITEM", loader.loadAudioClip("get_item"));
        sfxClips.put("LEVEL_UP",loader.loadAudioClip("level_up"));
        sfxClips.put("BREAK",loader.loadAudioClip("break"));
        sfxClips.put("GAME_MUSIC",loader.loadAudioClip("game_music"));
    }

    public AudioClip getAudioClip(String id) {
        return sfxClips.get(id);
    }

    public void playSfx(String id) {
        AudioClip clip = sfxClips.get(id);
        if (clip != null) {
            clip.play();
        } else {

            System.err.println("Error: Attempted to play non-existent SFX ID: " + id);
        }
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
}

// ----------------------------------------------------------------------------------------------------

class AudioLoader {

    private static String getResourceUrl(String path) {

        URL resource = AudioLoader.class.getResource(path);

        if (resource == null) {

            throw new RuntimeException("ERROR: Audio resource file not found at classpath: " + path);
        }

        return resource.toExternalForm();
    }

    public static AudioClip loadAudioClip(String fileName) {

        String fullFileName = fileName + ".wav";

        try {
            String resourceUrl = getResourceUrl("/sound/" + fullFileName);
            return new AudioClip(resourceUrl);

        } catch (RuntimeException e) {
            // In chi tiết lỗi khi tải file thất bại
            System.err.println("Failed to load audio clip '" + fullFileName + "'. Details: " + e.getMessage());
            return null;
        }
    }
}