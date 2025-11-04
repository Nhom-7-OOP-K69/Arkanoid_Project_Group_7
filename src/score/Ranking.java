package score;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ranking {
    private static List<PlayerScore> rankScores = new ArrayList<>();
    private static final int MAX_RANKING = 5;
    private static final String RANKING_FILE = "ranking.txt";

    // Hàm loadScore() đã sửa: Kỳ vọng định dạng "NAME:SCORE"
    public static void loadScore() throws IOException {
        rankScores = new ArrayList<>();

        try (FileReader f = new FileReader(RANKING_FILE);
             BufferedReader reader = new BufferedReader(f)) {
            String line;

            while ((line = reader.readLine()) != null) {
                String cleanedLine = line.trim();

                if (!cleanedLine.isEmpty()) {
                    // SỬ DỤNG split(":") để tách NAME và SCORE
                    String[] parts = cleanedLine.split(":");

                    if (parts.length == 2) {
                        try {
                            String name = parts[0].trim();
                            int score = Integer.parseInt(parts[1].trim());
                            rankScores.add(new PlayerScore(name, score));
                        } catch (NumberFormatException e) {
                            System.err.println("Cảnh báo: Lỗi định dạng số. Bỏ qua dòng: " + line);
                        }
                    } else {
                        System.err.println("Cảnh báo: Lỗi định dạng dòng (Thiếu dấu hai chấm ':'). Bỏ qua: " + line);
                    }
                }
            }
        } catch (IOException e) {
            // Đây là lỗi phổ biến nếu file chưa tồn tại.
            System.out.println("Cảnh báo: Không tìm thấy file xếp hạng, tạo bảng xếp hạng mới.");
        }
    }

    // Hàm saveScore() (Không đổi, đã đúng định dạng "NAME:SCORE")
    public static void saveScore(String playerName, int newScore) throws IOException {
        loadScore();

        rankScores.add(new PlayerScore(playerName, newScore));
        Collections.sort(rankScores);

        if (rankScores.size() > MAX_RANKING) {
            rankScores = rankScores.subList(0, MAX_RANKING);
        }

        try (FileWriter f = new FileWriter(RANKING_FILE);
             BufferedWriter writer = new BufferedWriter(f)) {

            for (PlayerScore ps : rankScores) {
                // ĐỊNH DẠNG: NAME:SCORE
                writer.write(ps.getName() + ":" + ps.getScore() + "\n");
            }
            System.out.println("Đã lưu điểm mới: " + playerName + " - " + newScore + ". Bảng xếp hạng: " + rankScores.size() + " mục.");
        } catch (IOException e) {
            System.err.println("Lỗi I/O khi ghi file: " + e.getMessage());
        }
    }

    public static List<PlayerScore> getRankScores() throws IOException {
        if (rankScores.isEmpty()) {
            loadScore();
        }
        return new ArrayList<>(rankScores);
    }
}
