import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ScoreManager {
    private static final String SCORE_FILE_PATH = "Score.txt";

    public static int loadHighScore() {
        File scoreFile = new File(SCORE_FILE_PATH);
        if (!scoreFile.exists()) {
            return 0;
        }

        try (Scanner scanner = new Scanner(scoreFile)) {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Lỗi: Không tìm thấy file điểm số khi đang tải!");
        }
        return 0;
    }

    public static void saveHighScore(int highScore) {
        try (FileWriter writer = new FileWriter(SCORE_FILE_PATH, false)) {
            writer.write(String.valueOf(highScore));
        } catch (IOException e) {
            System.err.println("Lỗi: Không thể ghi điểm cao vào file!");
            e.printStackTrace();
        }
    }
}