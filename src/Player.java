/**
 * The player class
 * @author Lujain Jdue
 * @version 1, October 8, 2024
 *
 * @author Aqsa Atif, Yomna Ibrahim
 * @version 2, October 17, 2024
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    private String name;
    private int score;
    private List<Tile> tiles;
    private boolean isAI;

    /**
     * Constructs a new player
     * @param name Name of the player
     */
    public Player(String name){
        this.name = name;
        this.score = 0;
        this.tiles = new ArrayList<>();
        this.isAI = false;
    }

    public boolean isAI() {
        return isAI;
    }

    public void setIsAI(boolean isAI) {
        this.isAI = isAI;
    }

    /**
     * Updates the current score of the player
     * @param points The points the player should acquire
     */
    public void updateScore (int points){
        this.score += points;
    }

    /**
     * Get the name of the current player
     * @return The name of the player
     */
    public String getName(){
        return name;
    }

    /**
     * Get the score of the current player
     * @return the score of the current player
     */
    public int getScore(){
        return score;
    }

    /**
     * Get the list of tiles for the player
     * @return the List of tiles
     */
    public ArrayList<Tile> getTiles(){
        return (ArrayList<Tile>) tiles;
    }

    /**
     * Get random tile from the available tileset
     * @param tileSet The available tileSet to choose from
     * @return random tile from the available tileSet
     */
    public Tile getRandomTile(ArrayList<Tile> tileSet){
        Random rand = new Random();
        return tileSet.get(rand.nextInt(tileSet.size()));
    }


    /**
     * Gives player (n) set of tiles.
     *
     * @param tileSet The available tile set to draw from
     * @param n The number of tiles to draw
     */

    public void getHand(ArrayList<Tile> tileSet, int n){
        Tile myTile;
        for (int i =0; i< n; i++){
            myTile = getRandomTile(tileSet);
            tiles.add(myTile);
            tileSet.remove(myTile);
        }
    }

    /**
     * Prints the tiles in the player's hand.
     *
     * @return String representation of the player's tiles
     */
    public String printTiles(){
        int i = 0;
        String s = "[";
        for (Tile t: tiles){
            if(i++ == tiles.size() - 1){ //Last tile
                s += t.getLetter() + "(" + t.getValue() + ")";
                break;
            }
            s += t.getLetter() + "(" + t.getValue() + "), ";

        }
        s += "]";
        return s;
    }

    /**
     * Replace the players tiles after they play their turn
     *
     * @param existing The string representation of tiles already on the board
     * @param word The string representation of the word played
     * @param tileSet The available tileSet to draw from
     */
    public void replaceTiles(String existing, String word, ArrayList<Tile> tileSet){
        for (int i =0 ; i< existing.length(); i++){
            char myChar = existing.charAt(i);
            word = word.replaceFirst(Character.toString(myChar),"");

        }

        word = word.toUpperCase();

        for (int i = 0; i<word.length(); i++){
            String myChar = Character.toString(word.charAt(i));
            boolean removed = tiles.removeIf(t -> myChar.equals(t.getLetter()));
        }

        while(tiles.size()<7) {
            getHand(tileSet, 1); //replace missing tiles
        }
    }

    /**
     * Checks if the player has the required tiles in their hand
     *
     * @param existing The string representation of tiles already on the board
     * @param word The string representation of the word playe
     * @return True if the player has the tiles required, false if not
     */

    public boolean checkInHand(String existing, String word, List<Tile> tileSet){
        ArrayList<Tile> myHand = (ArrayList<Tile>) tiles;
        String strInHand = "";

        for (Tile t: myHand){
            strInHand += t.getLetter();
        }

        //remove the existing letters from the board from the word
        for (int i =0 ; i< existing.length(); i++){
            char myChar = existing.charAt(i);
            word = word.replaceFirst(Character.toString(myChar),"");

        }

        strInHand = strInHand.toLowerCase();

        for (int i =0 ; i< strInHand.length(); i++){
            char myChar = strInHand.charAt(i);
            word = word.replaceFirst(Character.toString(myChar),"");
        }

        //If using a blank tile
        if (word.length() == 1 && strInHand.contains(" ")){
            word = word.toUpperCase();
            int value = Tile.getTileScores().get(String.valueOf(word.charAt(0))); //point value of the tile that the blank tile will become
            tiles.add(new Tile(String.valueOf(word.charAt(0)), value)); //add the tile that the blank tile will become
            boolean removed = tiles.removeIf(t -> Tile.BLANK_TILE.equals(t.getLetter())); //remove blank tile from your hand
            word = "";
        }

        return word.isEmpty();

    }

    /**
     * Used to calculate the player's points after they place a word on the board. The location of the word is taken into account for premium squares
     * @param word the word that is placed
     * @param row the row that the word is placed
     * @param col the column that the word is placed
     * @param isHorizontal the orientation of the word
     * @param premiumSquares the list of premium squares on the board
     */
    public void calculatePoints(String word, int row, int col, boolean isHorizontal, int[][] premiumSquares){
        int wordMultiplier = 1;
        int wordScore = 0;

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            int letterScore = Tile.getTileScores().get(String.valueOf(letter).toUpperCase());

            int currentRow = row + (isHorizontal ? -1 : i-1);
            int currentCol = col + (isHorizontal ? i-1 : -1);

            // Apply premium square effects
            if (premiumSquares[currentRow][currentCol] == 2) { // Double letter score
                letterScore *= 2;
            } else if (premiumSquares[currentRow][currentCol] == 3) { // Triple letter score
                letterScore *= 3;
            } else if (premiumSquares[currentRow][currentCol] == 4) { // Double word score
                wordMultiplier *= 2;
            } else if (premiumSquares[currentRow][currentCol] == 5) { // Triple word score
                wordMultiplier *= 3;
            }

            wordScore += letterScore;
        }

        score += wordScore * wordMultiplier;
    }

    /**
     * Used to manually add tiles to player, mainly for test cases purposes
     * @param tiles, the list of tiles to be added to the player's hand
     */
    public void addTiles(List<Tile> tiles) {
        for (Tile t: tiles){
            this.tiles.add(t);
        }
    }
}
