public class Score {
    private int score;
    private int scorePlus;

    public Score() {
        score = 0;
        scorePlus = 0;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void updateScore() {
        score += scorePlus + GameConstants.SCORE_PLUS;
        scorePlus += 5;
    }

    public void resetScorePlus() {
        scorePlus = 0;
    }

    public void resetScore() {
        resetScorePlus();
        score = 0;
    }
}
