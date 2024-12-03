/**
 * The modelTest class
 * @author Lujain Jdue
 * @version 1, November 10, 2024
 *
 * @author Yomna Ibrahim
 * @version 2, November 24, 2024
 *
 * @author Lujain Jdue 
 * @version 3, December 2, 2024
 */

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ScrabbleModelTest {
    private ScrabbleModel model;
    private Player player1;
    private Player player2;
    private Player player3;
    private List<Tile> tiles, tiles2, tiles3;
    List<Player> players = new ArrayList<>();
    private String word1 = "task";
    private String word2 = "kits";

    private char[][] board;

    int row = 1;
    int col = 1;
    int col2 = 4;

    private int[][] premiumSquares;

    private ScrabbleAI ai;

    /**
     * Setup common variables and objects to be used among different test cases
     */
    @Before
    public void setUp(){
        model = new ScrabbleModel(2,1);
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");
        player3 = new Player("AI Player 1");
        player3.setIsAI(true);

        players.add(player1);
        players.add(player2);
        players.add(player3);

        ai = new ScrabbleAI(model, new Dictionary());

        premiumSquares = new int[ScrabbleModel.SIZE][ScrabbleModel.SIZE];
        model.initializePremiumSquares();

        tiles = new ArrayList<>();
        tiles.add(new Tile("T", 1));
        tiles.add(new Tile("A", 1));
        tiles.add(new Tile("S", 1));
        tiles.add(new Tile("K", 5));
        tiles.add(new Tile("E", 1));
        tiles.add(new Tile("F", 4));
        tiles.add(new Tile("G", 2));

        tiles2 = new ArrayList<>();
        tiles2.add(new Tile("T", 1));
        tiles2.add(new Tile("I", 1));
        tiles2.add(new Tile("S", 1));
        tiles2.add(new Tile("K", 5));
        tiles2.add(new Tile("E", 1));
        tiles2.add(new Tile("F", 4));
        tiles2.add(new Tile("G", 2));

        tiles3 = new ArrayList<>();
        tiles3.add(new Tile("H", 4));
        tiles3.add(new Tile("A", 1));
        tiles3.add(new Tile("V", 4));
        tiles3.add(new Tile("E", 1));
        tiles3.add(new Tile("L", 1));
        tiles3.add(new Tile("E", 1));
        tiles3.add(new Tile("T", 1));
    }

    /**
     * test that AI players are AI and normal players are normal
     */
    @Test
    public void testAINormal(){
        assertFalse(player1.isAI());
        assertFalse(player2.isAI());
        assertTrue(player3.isAI());

    }

    /**
     * test that AI playing on empty board
     */
    @Test
    public void testAIPlay(){
        ScrabbleModelViewFrame.NUMOFPLAYERS = 2;
        player3.addTiles(tiles3);
        player1.addTiles(tiles);
        model.changeTurn();

        assertTrue(model.checkEmptyBoard());

        ai.playBestMove(player3);

        assertFalse(model.checkEmptyBoard()); // board no longer empty, AI was able to place a word

    }


    /**
     * test that the initial setup of the board is empty and ready to play on
     */
    @Test
    public void testInitialEmptyBoard(){
        char[][] board = model.getBoard();

        //check that every cell is initially empty
        for(int i = 0; i < ScrabbleModel.SIZE; i++){
            for (int j =0; j < ScrabbleModel.SIZE; j++){
                assertEquals(' ', board[i][j]);
            }
        }

    }
    /**
     * test that the word can be placed horizontally successfully
     */
    @Test
    public void testPlaceWordHorizontalSuccess(){
        Player currentPlayer = model.getCurrentPlayer();
        //mock that the player has the necessary letters in hand
        currentPlayer.addTiles(tiles);
        String[] result = model.placeWord(word1, row, col, true, currentPlayer);
        model.currentPlayer.calculatePoints(word1,row,col, true, premiumSquares);

        assertEquals("true", result[0]);
        assertEquals(word1.charAt(0), model.getBoard()[row-1][col-1]);
        assertEquals(word1.charAt(1), model.getBoard()[row-1][col]);
        assertEquals(word1.charAt(2), model.getBoard()[row-1][col+1]);
        assertEquals(word1.charAt(3), model.getBoard()[row-1][col+2]);

        assertEquals(8, currentPlayer.getScore());

    }

    /**
     * test that teh word can be placed vertically successfully
     */
    @Test
    public void testPlaceWordVerticalSuccess(){
        Player currentPlayer = model.getCurrentPlayer();
        currentPlayer.addTiles(tiles2);
        String [] result = model.placeWord(word2, row, 4, false, currentPlayer);
        model.currentPlayer.calculatePoints(word2, row, 4, false, premiumSquares);

        assertEquals("true", result[0]);
        assertEquals(word2.charAt(0), model.getBoard()[row-1][col2-1]);
        assertEquals(word2.charAt(1), model.getBoard()[row][col2-1]);
        assertEquals(word2.charAt(2), model.getBoard()[row+1][col2-1]);
        assertEquals(word2.charAt(3), model.getBoard()[row+2][col2-1]);

        assertEquals(8, currentPlayer.getScore());
    }

    /**
     * test an invalid word input to check that it will not be placed on board
     */
    @Test
    public void testFailedWordPlacement(){
        String word2 = "toolongword"; //not in dictionary
        int row2 = 1;
        int col2 = 13;

        player1.addTiles(tiles);
        String[] result = model.placeWord(word2, row2, col2, true, player1);

        assertEquals("false", result[0]);

    }

    /**
     * test that player can only place word within bounds of SIZE = 15
     */
    @Test
    public void testFailedWordPlacementOutOfBounds(){
        int row2 = 20;
        int col2 = 22;

        player1.addTiles(tiles);
        String[] result = model.placeWord(word2, row2, col2, true, player1);

        assertEquals("false", result[0]);

    }

    /**
     * test that player cannot place word if the player does not have the tiles for that word
     */
    @Test
    public void testFailedWordPlacementNoTiles(){
        String word = "zone";
        int row2 = 1;
        int col2 = 1;

        player1.addTiles(tiles);
        String[] result = model.placeWord(word, row2, col2, true, player1);

        assertEquals("false", result[0]);

    }
    /**
     * test the placement of player2 word that it fails if it is not connected to player1 word
     */

    @Test
    public void testPlaceWordPlayer2Fail(){
        Player currentPlayer = model.getCurrentPlayer();
        currentPlayer.addTiles(tiles2);
        String [] result = model.placeWord(word2, row, 4, false, currentPlayer);
        model.currentPlayer.calculatePoints(word2, row, 4, false, premiumSquares);

        assertEquals("true", result[0]);
        assertEquals(8, currentPlayer.getScore());

        currentPlayer = player2;
        player2.addTiles(tiles);
        String [] result2 = model.placeWord(word1, 1, 10, true, player2);

        assertEquals("false", result2[0]);
        assertEquals(0, currentPlayer.getScore());
    }

    /**
     * test that the tiles used are removed and replaced with new tiles
     */
    @Test
    public void testTileRemoval(){
        player1.addTiles(tiles);
        model.placeWord(word1, 7, 7, true, player1);
        assertEquals(7, player1.getTiles().size());
    }
    
    /**
     * test that the undo/redo functionality is working
     */
    @Test
    public void testUndoRedo() {
        player1.addTiles(tiles);
        model.placeWord(word1, row, col, true, player1);
        board = model.getBoard();

        model.pushRedo(board, 0, players);
        GameState gs = model.popUndo(); //undo the last action
        model.updateUndoRedo(gs.getBoard(), gs.getPlayers());

        assertEquals(' ', model.getBoard()[row - 1][col - 1]);

        model.pushUndo(board, 0, players); //redo the action
        GameState gs2 = model.popRedo();
        model.updateUndoRedo(gs.getBoard(), gs2.getPlayers());

        assertEquals(word1.charAt(0), model.getBoard()[row - 1][col - 1]); //ensure the word is redone
    }

    /**
     * test that the save/load functionality is working
     */
    @Test
    public void testSaveLoad(){
        player1.addTiles(tiles);
        model.placeWord(word1, row, col, true, player1);

        //save the game state
        model.saveGame("testSave.json");

        //create new model and load the saved game
        ScrabbleModel newModel = new ScrabbleModel(2, 1);
        newModel.loadGame("testSave.json");

        assertEquals(word1.charAt(0), newModel.getBoard()[row-1][col-1]); //ensure the word that was saved is loaded correctly
    }

    /**
     * test that custom boards are working properly
     */
    @Test
    public void testCustomBoard(){
       //load the premium square from the XML file
        model.loadPremiumSquaresFromXML("premiumSquaresXML.txt");
        //check that specific premium squares are loaded correctly
        assertEquals(5, model.getPremiumSquares()[0][0]);
        assertEquals(2, model.getPremiumSquares()[0][3]);
        assertEquals(4, model.getPremiumSquares()[1][1]);
        assertEquals(3, model.getPremiumSquares()[1][5]);
    }

}
