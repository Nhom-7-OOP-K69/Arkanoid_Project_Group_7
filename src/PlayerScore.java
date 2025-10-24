// File: PlayerScore.java (Tạo một tệp mới)
import java.io.Serializable; // Để có thể lưu trữ đối tượng nếu cần

public class PlayerScore implements Comparable<PlayerScore>, Serializable {
    private String name;
    private int score;

    public PlayerScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    // So sánh để sắp xếp theo điểm số giảm dần
    @Override
    public int compareTo(PlayerScore other) {
        return Integer.compare(other.score, this.score); // Giảm dần
    }

    @Override
    public String toString() {
        return name + ": " + score;
    }
}