import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.File;

/**
 * The logic behind the Scrabble game
 * @author Yomna Ibrahim
 * @version 1, November 10, 2024
 *
 * @author Aqsa Atif
 * @version 2, November 11, 2024
 *
 *  @author Basma Mohammed
 *  @version 3 November 23, 2024
 *
 * @author Aqsa Atif
 * @version 4, November 26, 2024
 */
public class ScrabbleModel {

    public static final int SIZE = 15;

    private List<ScrabbleModelView> views;

    private char[][] board;
    private  List<Player> players = new ArrayList<>();
    private static List<Tile> tileSet;

    private int[][] premiumSquares;
    String [] check = {"",""}; //Index 0 indicates if word has been placed or not, index 1 if a letter is already on the board

    Player currentPlayer;

    private int currentPlayerIndex = 0;

    private Stack undoStack;

    private Stack redoStack;

    public ScrabbleModel(int numOfRealPlayers, int numOfAiPlayers) {
        board = new char[SIZE][SIZE];
        for (int i =0; i< SIZE; i++){
            for (int j = 0; j<SIZE; j++){
                board[i][j] = ' ';
            }
        }
        this.views = new ArrayList<ScrabbleModelView>();

        // Create real players
        for (int i = 1; i <= numOfRealPlayers; i++) {
            players.add(new Player("Player " + i));
        }

        // Create AI players
        for (int i = 1; i <= numOfAiPlayers; i++) {
            Player aiPlayer = new Player("AI Player " + i);
            aiPlayer.setIsAI(true); // Flag this player as AI
            players.add(aiPlayer);
        }

        currentPlayer = players.get(0);

        //initialize tileSet
        this.tileSet = initializeTileSet();

        for(Player p: players){
            p.getHand((ArrayList) tileSet,7);
        }

        premiumSquares = new int[SIZE][SIZE];
        //initializePremiumSquares();

        loadPremiumSquaresFromXML("premiumSquaresXML.txt");

    }

    /**
     * Create customized premium squares
     * @param filePath the name of the file that contain the location of premium squares in XML
     */
    public void loadPremiumSquaresFromXML(String filePath) {
        try {
            // Parse the XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(filePath));

            // Normalize the document to remove unnecessary whitespaces
            doc.getDocumentElement().normalize();

            // Get all the <row> elements
            NodeList rowList = doc.getElementsByTagName("row");

            for (int i = 0; i < rowList.getLength(); i++) {
                Node rowNode = rowList.item(i);

                if (rowNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element rowElement = (Element) rowNode;

                    // Get the row number
                    int rowNumber = Integer.parseInt(rowElement.getAttribute("number"));

                    // Get all <square> elements within this row
                    NodeList squareList = rowElement.getElementsByTagName("square");

                    for (int j = 0; j < squareList.getLength(); j++) {
                        Node squareNode = squareList.item(j);

                        if (squareNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element squareElement = (Element) squareNode;

                            // Extract the column and type attributes
                            int colNumber = Integer.parseInt(squareElement.getAttribute("col"));
                            String type = squareElement.getAttribute("type");

                            //Set the premium square value accordingly
                            if (type.equals("DLS")){
                                premiumSquares[rowNumber][colNumber] = 2;
                            } else if (type.equals("TLS")){
                                premiumSquares[rowNumber][colNumber] = 3;
                            } else if (type.equals("DWS")){
                                premiumSquares[rowNumber][colNumber] = 4;
                            } else if (type.equals("TWS")){
                                premiumSquares[rowNumber][colNumber] = 5;
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the premium squares on the board
     */
    private void initializePremiumSquares() {
        // Double letter squares
        premiumSquares[0][3] = 2; premiumSquares[0][11] = 2;
        premiumSquares[3][0] = 2; premiumSquares[3][7] = 2; premiumSquares[3][14] = 2;
        premiumSquares[11][0] = 2; premiumSquares[11][7] = 2; premiumSquares[11][14] = 2;
        premiumSquares[14][3] = 2; premiumSquares[14][11] = 2;
        premiumSquares[6][2] = 2; premiumSquares[6][6] = 2; premiumSquares[6][8] = 2; premiumSquares[6][12] = 2;
        premiumSquares[8][2] = 2; premiumSquares[8][6] = 2; premiumSquares[8][8] = 2; premiumSquares[8][12] = 2;
        premiumSquares[2][6] = 2; premiumSquares[2][8] = 2;
        premiumSquares[7][3] = 2; premiumSquares[7][11] = 2;
        premiumSquares[12][6] = 2; premiumSquares[12][8] = 2;

        // Triple letter squares
        premiumSquares[1][5] = 3; premiumSquares[1][9] = 3;
        premiumSquares[5][1] = 3; premiumSquares[5][5] = 3; premiumSquares[5][9] = 3; premiumSquares[5][13] = 3;
        premiumSquares[9][1] = 3; premiumSquares[9][5] = 3; premiumSquares[9][9] = 3; premiumSquares[9][13] = 3;
        premiumSquares[13][5] = 3; premiumSquares[13][9] = 3;

        // Double word squares
        int j = 1;
        for (int i = 1; i < 5; i++){
            premiumSquares[i][j] = 4;
            j++;
        }

        j = 10;
        for (int i = 10; i < SIZE; i++){
            premiumSquares[i][j] = 4;
            j++;
        }

        j = 13;
        for (int i = 1; i < 5; i++){
            premiumSquares[i][j] = 4;
            premiumSquares[j][i] = 4;
            j--;
        }
        premiumSquares[7][7] = 4;

        premiumSquares[0][0] = 4; premiumSquares[0][14] = 4;
        premiumSquares[14][0] = 4; premiumSquares[14][14] = 4;

        // Triple word squares
        premiumSquares[0][0] = 5; premiumSquares[0][7] = 5; premiumSquares[0][14] = 5;
        premiumSquares[7][0] = 5; premiumSquares[7][14] = 5;
        premiumSquares[14][0] = 5; premiumSquares[14][7] = 5; premiumSquares[14][14] = 5;
    }

    /**
     * Getter method for the list of premium squares
     * @return the premium squares
     */
    public int[][] getPremiumSquares() {
        return premiumSquares;
    }


    /**
     * Method to register the view to the model
     * @param view to be registered
     */
    public void addScrabbleView (ScrabbleModelView view){this.views.add(view);}

    /**
     * Method to unsubscribe view from the model
     * @param view the view to be unsubscribed
     */
    public void removeScrabbleView (ScrabbleModelView view){this.views.remove(view);}

    /**
     * Method to return the current player
     * @return the current player
     */
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * Method to return the players list
     * @return the players list
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Method to return the scrabble board
     * @return the scrabble board
     */
    public char[][] getBoard(){
        return board;
    }

    /**
     * Method to change the current player
     */
    public void changeTurn(){
        currentPlayerIndex = (currentPlayerIndex + 1) % ScrabbleModelViewFrame.NUMOFPLAYERS;
        currentPlayer = players.get(currentPlayerIndex);

        for (ScrabbleModelView view: views){ //Update the views
            view.handleScrabblePassTurnUpdate(new ScrabbleEvent(this, board, currentPlayer));
        }
    }

    /**
     * Initializes the tile set used in the game.
     *
     * @return An ArrayList of Tile objects representing the complete tile set.
     */
    public ArrayList<Tile> initializeTileSet(){
        ArrayList<Tile> tileSet = new ArrayList<>();

        tileSet.add(new Tile("K", 5));
        tileSet.add(new Tile("J", 8));
        tileSet.add(new Tile("X", 8));
        tileSet.add(new Tile("Q", 10));
        tileSet.add(new Tile("Z", 10));

        for(int i = 0; i<2; i++){
            tileSet.add(new Tile(Tile.BLANK_TILE, 0));
            tileSet.add(new Tile("B", 3));
            tileSet.add(new Tile("C", 3));
            tileSet.add(new Tile("M", 3));
            tileSet.add(new Tile("P", 3));
            tileSet.add(new Tile("F", 4));
            tileSet.add(new Tile("H", 4));
            tileSet.add(new Tile("V", 4));
            tileSet.add(new Tile("W", 4));
            tileSet.add(new Tile("Y", 4));
        }

        for(int i = 0; i<3; i++){
            tileSet.add(new Tile("G", 2));
        }

        for(int i = 0; i<4; i++){
            tileSet.add(new Tile("L", 1));
            tileSet.add(new Tile("S", 1));
            tileSet.add(new Tile("U", 1));
            tileSet.add(new Tile("D", 2));
        }

        for(int i = 0; i<6; i++){
            tileSet.add(new Tile("N", 1));
            tileSet.add(new Tile("R", 1));
            tileSet.add(new Tile("T", 1));
        }

        for(int i = 0; i<8; i++){
            tileSet.add(new Tile("O", 1));
        }

        for(int i = 0; i<9; i++){
            tileSet.add(new Tile("A", 1));
            tileSet.add(new Tile("I", 1));
        }

        for(int i = 0; i<12; i++){
            tileSet.add(new Tile("E", 1));
        }

        return tileSet;
    }

    /**
     * Method used to check if the scrabble board is empty
     * @return true if the board is empty and false otherwise
     */
    public boolean checkEmptyBoard(){
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                if (board[i][j] != ' '){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This function is used to place a word that the user enters on the 15 x 15 Scrabble board.
     * @param word the word to be placed on the board
     * @param row the row that the word is being placed on (from 1 to 15)
     * @param col the column that the word is being placed on (from 1 to 15)
     * @param isHorizontal true if the placement of the word is horizontal and F is placement is vertical
     * @param currentPlayer The current player
     * @return the first index indicates if the word has been placed or not ("true" if yes, "false" if no), the second index is used to indicate if there was already a letter on the board that the word used ("" if no letter, or the letter on the board that was used)
     */
    public String[] placeWord(String word, int row, int col, boolean isHorizontal, Player currentPlayer) {

        //String [] check = {"",""}; //Index 0 indicates if word has been placed or not, index 1 if a letter is already on the board

        boolean validLocation = false; //Needed to check for connecting words

        if (isHorizontal) { //Placement of word is horizontal
            if (col + word.length() <= SIZE){ //Check that the word will fit on the board

                for (int i = 0; i < word.length(); i++) { //Loop through all cells where the word will be put
                    if (!String.valueOf(board[row - 1][col + i - 1]).equals(" ")){ //If cell is not blank
                        if (word.charAt(i) != board[row - 1][col + i - 1]) { //If cells does not contain the same letter as the word
                            check[0] = "false";
                            return check;
                        } else{
                            validLocation = true;
                        }
                        //Store the letter that is used that was already on the board
                        check[1] += String.valueOf(board[row - 1][col + i - 1]);
                    }
                }
                boolean inHand = currentPlayer.checkInHand(check[1],word, tileSet); //Check that the player has all the needed tiles
                boolean empty = checkEmptyBoard();

                if(inHand && validLocation|| inHand && empty) {
                    for (int i = 0; i < word.length(); i++) { //Place the word on the board
                        board[row - 1][col + i - 1] = word.charAt(i); //Print word in correct position
                    }

                } else{ //Word cannot be placed on the board
                    check[0] = "false";
                    return check;
                }
            }
            else{ //Word does not fit on board
                check[0] = "false";
                return check;
            }

        } else { //Placement of word is vertical
            if (row + word.length() <= SIZE) { //Check that the word will fit on the board

                for (int i = 0; i < word.length(); i++) { //Loop through all cells where the word will be put
                    if (!String.valueOf(board[row + i - 1][col - 1]).equals(" ")) { //If the cell is not blank
                        if (word.charAt(i) != board[row + i - 1][col - 1]) { //If cell does not contain the same letter as the word
                            check[0] = "false";
                        } else {
                            validLocation = true;
                        }
                        //Store the letter that is used that was already on the board
                        check[1] += String.valueOf(board[row + i - 1][col - 1]);
                    }
                }

                boolean inHand = currentPlayer.checkInHand(check[1], word, tileSet); //Check that the player has all the needed tiles
                boolean empty = checkEmptyBoard();

                if (inHand && validLocation || inHand && empty) {
                    for (int i = 0; i < word.length(); i++) {
                        board[row + i - 1][col - 1] = word.charAt(i); //Print word in correct position
                    }
                } else{ //Word cannot be placed on the board
                    check[0] = "false";
                    return check;
                }

            } else { //Word does not fit on board
                check[0] = "false";
                return check;
            }

        }
        check[0] = "true";

        return check;
    }

    /**
     * Method used to update the view after a word has been placed
     * @param word The word that's placed on the board
     */
    public void updatePlaceWord(String word, int row, int column, boolean isHorizontal){
        currentPlayer.calculatePoints(word, row, column, isHorizontal, premiumSquares); //Get the updated player points
        ScrabbleEvent event = new ScrabbleEvent(this, board, getCurrentPlayer());
        for (ScrabbleModelView view : views) {
            view.handleScrabbleStatusUpdate(event);
        }
    }

    /**
     * Getter method for the tileset
     * @return the tileset
     */
    public ArrayList<Tile> getTileSet() {
        return (ArrayList<Tile>) tileSet;
    }


    public String[] checkWord(String word, int row, int col, boolean isHorizontal, Player currentPlayer) {

        //String [] check = {"",""}; //Index 0 indicates if word has been placed or not, index 1 if a letter is already on the board

        boolean validLocation = false; //Needed to check for connecting words

        if (isHorizontal) { //Placement of word is horizontal
            if (col + word.length() <= SIZE){ //Check that the word will fit on the board

                for (int i = 0; i < word.length(); i++) { //Loop through all cells where the word will be put
                    if (!String.valueOf(board[row - 1][col + i - 1]).equals(" ")){ //If cell is not blank
                        if (word.charAt(i) != board[row - 1][col + i - 1]) { //If cells does not contain the same letter as the word
                            check[0] = "false";
                        } else{
                            validLocation = true;
                        }
                        //Store the letter that is used that was already on the board
                        check[1] += String.valueOf(board[row - 1][col + i - 1]);
                    }
                }
                boolean inHand = currentPlayer.checkInHand(check[1],word, tileSet); //Check that the player has all the needed tiles
                boolean empty = checkEmptyBoard();

                if(inHand && validLocation|| inHand && empty) {

                } else{ //Word cannot be placed on the board
                    check[0] = "false";
                    return check;
                }
            }
            else{ //Word does not fit on board
                check[0] = "false";
                return check;
            }

        } else { //Placement of word is vertical
            if (row + word.length() <= SIZE) { //Check that the word will fit on the board

                for (int i = 0; i < word.length(); i++) { //Loop through all cells where the word will be put
                    if (!String.valueOf(board[row + i - 1][col - 1]).equals(" ")) { //If the cell is not blank
                        if (word.charAt(i) != board[row + i - 1][col - 1]) { //If cell does not contain the same letter as the word
                            check[0] = "false";
                        } else {
                            validLocation = true;
                        }
                        //Store the letter that is used that was already on the board
                        check[1] += String.valueOf(board[row + i - 1][col - 1]);
                    }
                }

                boolean inHand = currentPlayer.checkInHand(check[1], word, tileSet); //Check that the player has all the needed tiles
                boolean empty = checkEmptyBoard();

                if (inHand && validLocation || inHand && empty) {

                } else{ //Word cannot be placed on the board
                    check[0] = "false";
                    return check;
                }

            } else { //Word does not fit on board
                check[0] = "false";
                return check;
            }

        }
        check[0] = "true";

        return check;
    }

    /**
     * Method used to push onto the undo stack
     * @param board the current board
     * @param currentPlayerIndex the current player index before incrementing
     * @param scores the current scores of all players
     */
    public void pushUndo(char[][] board, int currentPlayerIndex, List<Integer> scores){
        //save the game state
        GameState gs = new GameState(board, currentPlayerIndex, scores);
        undoStack.push(gs);

    }

    /**
     * Method used to push onto the redo stack
     * @param board the current board
     * @param currentPlayerIndex the current player index before incrementing
     * @param scores the current scores of all players
     */
    public void pushRedo(char[][] board, int currentPlayerIndex, List<Integer> scores){
        //save the game state
        GameState gs = new GameState(board, currentPlayerIndex, scores);
        redoStack.push(gs);
    }

    /**
     * Method used to pop from the undo stack
     */
    public void popUndo(){

        GameState gs = (GameState) undoStack.pop();

        this.board = gs.getBoard();
        this.currentPlayerIndex = gs.getCurrentPlayerIndex();
        //scores
    }
}


