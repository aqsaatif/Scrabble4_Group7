/**
 * The Move class
 * @author Lujain Jdue
 * @version 1, November 12, 2024
 */
public class Move {
    String word;
    int row;
    int col;
    boolean isHorizontal;

    /**
     * Move object for AI player
     *@param word string for word generated
     *@param int row to place word
     *@param int col
     *@param isHorizontal boolean, true if horizontal, false otherwise
     */
    Move(String word, int row, int col, boolean isHorizontal){
        this.word = word;
        this.row = row;
        this.col = col;
        this.isHorizontal = isHorizontal;
    }
}
