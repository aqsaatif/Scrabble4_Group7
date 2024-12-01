import java.util.ArrayList;
import java.util.List;

public class GameState {

    private char[][] board;
    private int currentPlayerIndex;
    private List<Player> players = new ArrayList<>();

    public GameState(char[][] board, int currentPlayerIndex, List<Player> players) {
        this.board = board;
        this.currentPlayerIndex = currentPlayerIndex;
        this.players = players;
    }

    //getters
    public char[][] getBoard() {return board;}
    public List<Player> getPlayers() { return players; }
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }

}
