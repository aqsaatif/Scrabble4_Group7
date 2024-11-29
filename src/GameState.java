import java.util.ArrayList;
import java.util.List;

public class GameState {

    private char[][] board;
    private int currentPlayerIndex;
    private List<Integer> scores = new ArrayList<>();

    public GameState(char[][] board, int currentPlayerIndex, List<Integer> scores) {
        this.board = board;
        this.currentPlayerIndex = currentPlayerIndex;
        this.scores = scores;
    }

    //getters
    public char[][] getBoard() {return board;}
    public List<Integer> getScores() { return scores; }
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }

}
