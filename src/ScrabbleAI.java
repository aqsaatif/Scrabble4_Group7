/**
 * The ScrabbleAI class
 * @author Lujain Jdue
 * @version 1, November 12, 2024
 *
 */
import javax.swing.*;
import java.util.*;

public class ScrabbleAI{

    private static ScrabbleModel model;
    private static Dictionary dictionary;
    static List<Tile> tiles = new ArrayList<>();
    /**
     * constructs a new ScrabbleAI
     *@param model scrabbleModel
     *@param dictionary for the valid words
     */
    public ScrabbleAI(ScrabbleModel model, Dictionary dictionary){
        this.model = model;
        this.dictionary = dictionary;
    }

    /**
     * Determines the best possible move and makes it
     *@param aiPlayer which is a player
     */
     public static void playBestMove(Player aiPlayer){
        Move bestMove = null;
        int highestScore = 0;
        char[][] myBoard = model.getBoard();
        Random random = new Random();
        String existBest = "";

        if (model.checkEmptyBoard()){ //if board is empty play randomly in the center
            List<String> possibleWords = generateWords(aiPlayer.getTiles());
            String word = "";
            for (String possibleWord : possibleWords) {
                if (possibleWord.length() > word.length()) {
                    word = possibleWord;
                }
            }
            model.placeWord(word,8,8,true, aiPlayer);
            model.currentPlayer.replaceTiles("", word, model.getTileSet());
            model.updatePlaceWord(word, 8, 8, true); //Update the GUI
            model.changeTurn(); //Change the player's turns
            return;
        }
        //generate all words from the tiles starting with that letter

        List<String> possibleWords = generateWords(aiPlayer.getTiles());

        //iterate over all possible starting positions
        for (int row =1; row < ScrabbleModel.SIZE; row++){
            for(int col = 1; col < ScrabbleModel.SIZE; col++){
                //try both vertical and horizontal placements
                for (boolean isHorizontal: new boolean[]{true, false}){


                    for (String word: possibleWords){
                        //check if the word is a valid move
                        String[] check = model.checkWord(word, row, col, isHorizontal, aiPlayer);
                        if (dictionary.isValidWord(word) && check[0].equals("true")){
                            if (bestMove == null || word.length() > bestMove.word.length()) {
                                bestMove = new Move(word, row, col, isHorizontal);

                            }

                        }
                    }
                }
            }
        }
        if (bestMove != null){
            model.placeWord(bestMove.word, bestMove.row, bestMove.col, bestMove.isHorizontal, aiPlayer);
            model.currentPlayer.replaceTiles(existBest, bestMove.word, model.getTileSet());
            model.updatePlaceWord(bestMove.word, bestMove.row, bestMove.col, bestMove.isHorizontal); //Update the GUI
            model.changeTurn(); //Change the player's turns
            System.out.println("AI played: " + bestMove.word );
        }else {
            //System.out.println("AI passes turn - no legal moves");
            JOptionPane.showMessageDialog(null, "AI passes turn");
            model.changeTurn();
        }

    }
    /**
     * generates a list of strings based on the tiles of the player
     *@param tiles in the player's hand
     */
    private static List<String> generateWords(List<Tile> tiles){
        List<String> words = new ArrayList<>();
        for (int i = 0; i <= tiles.size(); i++){
            for (int j = i+1; j<=tiles.size(); j++){
                String word = getWord(tiles, i, j);
                System.out.println("Generated word: " + word);
                if(dictionary.isValidWord(word)){
                    words.add(word);
                }
            }
        }
        return words;
    }
    /**
     * Get the word generated
     * @param tiles in the player's had
     * @param start int
     * @param end int
     */
    private static String getWord(List<Tile> tiles, int start,int end){
        StringBuilder word = new StringBuilder();
        for (int i = start; i<end; i++){
            word.append(tiles.get(i).getLetter());
        }

        return word.toString().toLowerCase();
    }

    /**
     * calculates the points for the potential word
     * @param word String
     * @param tiles list of tiles for the player
     * @return score the value of the word
     */
    private static int calculateScore(String word, List<Tile> tiles){
        int score = 0;
        for (char letter: word.toCharArray()){
            for(Tile tile: tiles){
                if(tile.getLetter().equalsIgnoreCase(String.valueOf(letter))){
                    score += tile.getValue();
                    break;
                }
            }
        }
        return score;
    }

    /*

     * main to check the functionality of methods
     *@param args String[]
     *
    public static void main(String[] args){
        int numOfRealPlayers = 1;
        int numOfAiPlayers = 1;

        ScrabbleModel model = new ScrabbleModel(numOfRealPlayers, numOfAiPlayers);
        Dictionary dictionary = new Dictionary();
        Player aiPlayer = new Player("aiplayer");
        ScrabbleAI ai = new ScrabbleAI(model, dictionary);


        tiles.add(new Tile("c", 1));
        tiles.add(new Tile("a", 1));
        tiles.add(new Tile("t", 1));
        aiPlayer.addTiles(tiles);

        ai.playBestMove(aiPlayer);

        System.out.println("tiles in players hand");
        for(Tile tile: tiles){
            System.out.println(tile.getLetter() + " ");
        }

        List<String> generateWords = ai.generateWords(tiles);
        System.out.println(generateWords);

        System.out.println("Generated words from the tiles: ");
        for(String word: generateWords){
            System.out.println(word);
            int score = calculateScore(word, tiles);
            System.out.println("The score for the word: " + word + "  "+ score);
        }
        */

}
