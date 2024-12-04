/**
 * The GameState class
 * @author Yomna Ibrahim, Basma Mohamed
 * @version 1, December 4
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {

    private char[][] board;
    private int currentPlayerIndex;
    private List<Player> players = new ArrayList<>();

    /**
     * Constructs a new Game State
     * @param board Board of the game 
     * @param currentPlayerIndex The index of the current player 
     * @param players The list of the current players 
     */
    public GameState(char[][] board, int currentPlayerIndex, List<Player> players) {
        this.board = board;
        this.currentPlayerIndex = currentPlayerIndex;
        this.players = players;
    }

    //getters
    /**
     * Gets the board in the Game State 
     * @return Board of the saved game state 
     */
    public char[][] getBoard() {return board;}
    
    /**
     * Gets the players in the Game State 
     * @return players of the saved game state 
     */
    public List<Player> getPlayers() { return players; }

    /**
     * Gets the current player index in the Game State 
     * @return Current player index of the saved game state 
     */
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }

}
