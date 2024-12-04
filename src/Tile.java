/**
 * This class sets up tile objects and a tileScore reference for each letter.
 *
 * @author Yomna Ibrahim
 * @version 1, October 8, 2024
 *
 * @author Yomna Ibrahim
 * @version 2, October 20, 2024
 */

import java.io.Serializable;
import java.util.*;

public class Tile implements Serializable {
    private String letter;
    private int value;

    private static Map<String, Integer> tileScores;

    public static final String BLANK_TILE = " ";

    /**
     * Constructor for a Tile object.
     *
     * @param letter The letter representation of the tile
     * @param value The integer value representation for the tile
     */
    public Tile(String letter, int value) {
        this.letter = letter;
        this.value = value;
        this.tileScores = new HashMap<String, Integer>();
        initTileScores();
    }
    /**
     * Constructor for a Tile object from another tile object.
     *
     * @param tile The tile to be used for the new object 
     */

    public Tile(Tile tile) {
        this.letter = tile.letter;
        this.value = tile.value;
        this.tileScores = new HashMap<String, Integer>();
        initTileScores();
    }

    /**
     * Initializes each tile score by letter.
     */
    private static void initTileScores(){
        tileScores.put("A",1);
        tileScores.put("B",3);
        tileScores.put("C",3);
        tileScores.put("D",2);
        tileScores.put("E",1);
        tileScores.put("F",4);
        tileScores.put("G",2);
        tileScores.put("H",4);
        tileScores.put("I",1);
        tileScores.put("J",8);
        tileScores.put("K",5);
        tileScores.put("L",1);
        tileScores.put("M",3);
        tileScores.put("N",1);
        tileScores.put("O",1);
        tileScores.put("P",3);
        tileScores.put("Q",10);
        tileScores.put("R",1);
        tileScores.put("S",1);
        tileScores.put("T",1);
        tileScores.put("U",1);
        tileScores.put("V",4);
        tileScores.put("W",4);
        tileScores.put("X",8);
        tileScores.put("Y",4);
        tileScores.put("Z",10);
        tileScores.put(BLANK_TILE, 0);
    }

    /**
     * Gets the hashmap with the predetermined tile scores.
     *
     * @return The hashmap with predetermined tile scores
     */
    public static Map<String,Integer> getTileScores(){
        return tileScores;
    }

    /**
     *Get the letter associated with the tile.
     *
     * @return Letter of the tile
     */
    public String getLetter() {
        return letter;
    }

    /**
     *Get the value associated with the tile.
     *
     * @return Value of the tile
     */
    public int getValue() {
        return value;
    }

    public boolean isBlank() {
        return letter.equals(BLANK_TILE);
    }

    public boolean setLetterValue(String letter, int value) {
        if (isBlank()) {
            this.letter = letter;
            this.value = value;
            return true;
        }
        return false;
    }


}
