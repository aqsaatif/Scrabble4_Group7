/**
 * The event class for the controller 
 * @author Lujain Jdue
 * @version 1, November 7, 2024
 *
 * author Aqsa Atif
 * @version 2, November 11, 2024
 */
import java.util.EventObject;
import java.util.List;

public class ScrabbleEvent extends EventObject {
    char[][] board;
    Player currentPlayer;
    private List<Player> players;

    /**
     *Class constructor
     *@param model ScrabbelModel class
     *@param board char[][] for the game
     * currPlayer the currentplayer for the game
     */
    public ScrabbleEvent(ScrabbleModel model, char[][] board, Player currPlayer, List<Player> players) {
        super(model);
        this.board= board;
        currentPlayer = currPlayer;
        this.players = players;

    }

    /**
     * Method to get the current player
     * @return the current player
     */
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * Method to get the scrabble board
     * @return the scrabble board
     */
    public char[][] getBoard(){
        return board;
    }

    public List<Player> getPlayers() {
        return players;
    }
}