import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ScrabbleModelTest {
    private ScrabbleModel model;
    private Player player1;
    private Player player2;
    private Player player3;
    private List<Tile> tiles;
    private List<Tile> tiles2;
    private List<Tile> tiles3;
    private String word1 = "task";
    private String word2 = "kits";
    private char[][] board;
    int row = 1;
    int col = 1;
    int col2 = 4;
    private int[][] premiumSquares;
    private ScrabbleAI ai;

    public ScrabbleModelTest() {
    }

    @Before
    public void setUp() {
        this.model = new ScrabbleModel(2, 1);
        this.player1 = new Player("Player 1");
        this.player2 = new Player("Player 2");
        this.player3 = new Player("AI Player 1");
        this.player3.setIsAI(true);
        this.ai = new ScrabbleAI(this.model, new Dictionary());
        this.premiumSquares = new int[15][15];
        this.model.initializePremiumSquares();
        this.tiles = new ArrayList();
        this.tiles.add(new Tile("T", 1));
        this.tiles.add(new Tile("A", 1));
        this.tiles.add(new Tile("S", 1));
        this.tiles.add(new Tile("K", 5));
        this.tiles.add(new Tile("E", 1));
        this.tiles.add(new Tile("F", 4));
        this.tiles.add(new Tile("G", 2));
        this.tiles2 = new ArrayList();
        this.tiles2.add(new Tile("T", 1));
        this.tiles2.add(new Tile("I", 1));
        this.tiles2.add(new Tile("S", 1));
        this.tiles2.add(new Tile("K", 5));
        this.tiles2.add(new Tile("E", 1));
        this.tiles2.add(new Tile("F", 4));
        this.tiles2.add(new Tile("G", 2));
        this.tiles3 = new ArrayList();
        this.tiles3.add(new Tile("H", 4));
        this.tiles3.add(new Tile("A", 1));
        this.tiles3.add(new Tile("V", 4));
        this.tiles3.add(new Tile("E", 1));
        this.tiles3.add(new Tile("L", 1));
        this.tiles3.add(new Tile("E", 1));
        this.tiles3.add(new Tile("T", 1));
    }

    @Test
    public void testAINormal() {
        Assert.assertFalse(this.player1.isAI());
        Assert.assertFalse(this.player2.isAI());
        Assert.assertTrue(this.player3.isAI());
    }

    @Test
    public void testAIPlay() {
        ScrabbleModelViewFrame.NUMOFPLAYERS = 2;
        this.player3.addTiles(this.tiles3);
        this.player1.addTiles(this.tiles);
        this.model.changeTurn();
        Assert.assertTrue(this.model.checkEmptyBoard());
        ScrabbleAI var10000 = this.ai;
        ScrabbleAI.playBestMove(this.player3);
        Assert.assertFalse(this.model.checkEmptyBoard());
    }

    @Test
    public void testInitialEmptyBoard() {
        char[][] board = this.model.getBoard();

        for(int i = 0; i < 15; ++i) {
            for(int j = 0; j < 15; ++j) {
                Assert.assertEquals(32L, (long)board[i][j]);
            }
        }

    }

    @Test
    public void testPlaceWordHorizontalSuccess() {
        Player currentPlayer = this.model.getCurrentPlayer();
        currentPlayer.addTiles(this.tiles);
        String[] result = this.model.placeWord(this.word1, this.row, this.col, true, currentPlayer);
        this.model.currentPlayer.calculatePoints(this.word1, this.row, this.col, true, this.premiumSquares);
        Assert.assertEquals("true", result[0]);
        Assert.assertEquals((long)this.word1.charAt(0), (long)this.model.getBoard()[this.row - 1][this.col - 1]);
        Assert.assertEquals((long)this.word1.charAt(1), (long)this.model.getBoard()[this.row - 1][this.col]);
        Assert.assertEquals((long)this.word1.charAt(2), (long)this.model.getBoard()[this.row - 1][this.col + 1]);
        Assert.assertEquals((long)this.word1.charAt(3), (long)this.model.getBoard()[this.row - 1][this.col + 2]);
        Assert.assertEquals(8L, (long)currentPlayer.getScore());
    }

    @Test
    public void testPlaceWordVerticalSuccess() {
        Player currentPlayer = this.model.getCurrentPlayer();
        currentPlayer.addTiles(this.tiles2);
        String[] result = this.model.placeWord(this.word2, this.row, 4, false, currentPlayer);
        this.model.currentPlayer.calculatePoints(this.word2, this.row, 4, false, this.premiumSquares);
        Assert.assertEquals("true", result[0]);
        Assert.assertEquals((long)this.word2.charAt(0), (long)this.model.getBoard()[this.row - 1][this.col2 - 1]);
        Assert.assertEquals((long)this.word2.charAt(1), (long)this.model.getBoard()[this.row][this.col2 - 1]);
        Assert.assertEquals((long)this.word2.charAt(2), (long)this.model.getBoard()[this.row + 1][this.col2 - 1]);
        Assert.assertEquals((long)this.word2.charAt(3), (long)this.model.getBoard()[this.row + 2][this.col2 - 1]);
        Assert.assertEquals(8L, (long)currentPlayer.getScore());
    }

    @Test
    public void testFailedWordPlacement() {
        String word2 = "toolongword";
        int row2 = 1;
        int col2 = 13;
        this.player1.addTiles(this.tiles);
        String[] result = this.model.placeWord(word2, row2, col2, true, this.player1);
        Assert.assertEquals("false", result[0]);
    }

    @Test
    public void testFailedWordPlacementOutOfBounds() {
        int row2 = 20;
        int col2 = 22;
        this.player1.addTiles(this.tiles);
        String[] result = this.model.placeWord(this.word2, row2, col2, true, this.player1);
        Assert.assertEquals("false", result[0]);
    }

    @Test
    public void testFailedWordPlacementNoTiles() {
        String word = "zone";
        int row2 = 1;
        int col2 = 1;
        this.player1.addTiles(this.tiles);
        String[] result = this.model.placeWord(word, row2, col2, true, this.player1);
        Assert.assertEquals("false", result[0]);
    }

    @Test
    public void testPlaceWordPlayer2Fail() {
        Player currentPlayer = this.model.getCurrentPlayer();
        currentPlayer.addTiles(this.tiles2);
        String[] result = this.model.placeWord(this.word2, this.row, 4, false, currentPlayer);
        this.model.currentPlayer.calculatePoints(this.word2, this.row, 4, false, this.premiumSquares);
        Assert.assertEquals("true", result[0]);
        Assert.assertEquals(8L, (long)currentPlayer.getScore());
        currentPlayer = this.player2;
        this.player2.addTiles(this.tiles);
        String[] result2 = this.model.placeWord(this.word1, 1, 10, true, this.player2);
        Assert.assertEquals("false", result2[0]);
        Assert.assertEquals(0L, (long)currentPlayer.getScore());
    }

    @Test
    public void testTileRemoval() {
        this.player1.addTiles(this.tiles);
        this.model.placeWord(this.word1, 7, 7, true, this.player1);
        Assert.assertEquals(7L, (long)this.player1.getTiles().size());
    }
}