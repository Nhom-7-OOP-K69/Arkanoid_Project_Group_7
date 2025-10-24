/**
 * Lớp Score đóng gói toàn bộ logic liên quan đến việc quản lý điểm số
 * của người chơi, bao gồm điểm hiện tại, điểm cao nhất và điểm thưởng combo.
 */
public class Score {
    private int currentScore;
    private int highScore;
    private int scorePlus; // Điểm thưởng tăng dần cho mỗi viên gạch vỡ liên tiếp

    /**
     * Constructor khởi tạo đối tượng Score.
     * Nó sẽ tự động tải điểm cao nhất đã được lưu từ file.
     */
    public Score() {
        this.currentScore = 0;
        this.highScore = ScoreManager.loadHighScore(); // Tải điểm cao khi khởi tạo
        this.scorePlus = 10; // Điểm thưởng ban đầu
    }

    /**
     * Lấy điểm số của lượt chơi hiện tại.
     * (Đổi tên từ getCurrentScore để tương thích với GameManager)
     * @return Điểm hiện tại.
     */
    public int getScore() {
        return currentScore;
    }

    /**
     * Lấy điểm cao nhất đã được lưu.
     * @return Điểm cao nhất.
     */
    public int getHighScore() {
        return highScore;
    }

    /**
     * Cộng điểm thưởng vào điểm số hiện tại và tăng điểm thưởng cho lần sau.
     * (Thay thế cho phương thức add, để tương thích với GameManager)
     */
    public void updateScore() {
        this.currentScore += this.scorePlus;
        this.scorePlus += 10; // Tăng điểm thưởng cho combo tiếp theo
    }

    /**
     * Đặt lại điểm số của lượt chơi hiện tại về 0 và reset điểm thưởng.
     * (Đổi tên từ reset để tương thích với GameManager)
     */
    public void resetScore() {
        this.currentScore = 0;
        this.scorePlus = 10; // Reset điểm thưởng về mức ban đầu
    }

    /**
     * Reset điểm thưởng về giá trị ban đầu, thường là khi bóng chạm vào paddle.
     * (Phương thức mới để tương thích với GameManager)
     */
    public void resetScorePlus() {
        this.scorePlus = 10;
    }


    /**
     * Kiểm tra xem điểm hiện tại có cao hơn điểm cao nhất không.
     * Nếu có, cập nhật điểm cao nhất và lưu vào file.
     * @return true nếu một kỷ lục điểm cao mới được thiết lập, ngược lại là false.
     */
    public boolean checkAndSaveHighScore() {
        if (currentScore > highScore) {
            highScore = currentScore;
            ScoreManager.saveHighScore(highScore);
            return true; // Báo hiệu có điểm cao mới
        }
        return false; // Không có điểm cao mới
    }
}

