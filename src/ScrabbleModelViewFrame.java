import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This class handles all GUI activity of the game including the Scrabble board and player tiles
 *
 * @author: Aqsa Atif
 * @version 1 November 10, 2024
 *
 * @author Yomna Ibrahim
 * @version 2 November 11, 2024
 *
 *  @author Basma Mohammed
 *  @version 3 November 23, 2024
 *
 * @author Aqsa Atif
 * @version 4 November 29, 2024
 */

public class ScrabbleModelViewFrame extends JFrame implements ScrabbleModelView {

    public static int NUMOFPLAYERS = 0;
    private JButton[][] boardButtons;
    private JLabel currentPlayerLabel;

    private JLabel wordToPlace;
    JLabel row;
    JLabel col;
    JLabel orientation;

    private JButton[] playerTiles;
    private JLabel player1Points;
    private JLabel player2Points;
    private JLabel player3Points;
    private JLabel player4Points;
    private ScrabbleModel model;
    private ScrabbleController controller;
    private Dictionary dictionary;

    public int numOfRealPlayers = 0;
    public int numOfAiPlayers = 0;

    private ScrabbleAI ai;

    private final Color DLSCOLOR = new Color(255, 219, 88);
    private final Color TLSCOLOR = new Color(137, 207, 240);
    private final Color DWSCOLOR = new Color(227, 185, 255);
    private final Color TWSCOLOR = Color.PINK;


    public ScrabbleModelViewFrame() {
        super("Scrabble Game"); //Create JFrame

        this.dictionary = new Dictionary();
        setNumOfPlayers();

        model = new ScrabbleModel(numOfRealPlayers, numOfAiPlayers);
        model.addScrabbleView(this); //register view to the model

        ai = new ScrabbleAI(model, dictionary); //set up the ai
        controller = new ScrabbleController(model, this);
        setLayout(new BorderLayout());

        // Board panel
        JPanel boardPanel = new JPanel(new GridLayout(ScrabbleModel.SIZE, ScrabbleModel.SIZE));
        boardButtons = new JButton[ScrabbleModel.SIZE][ScrabbleModel.SIZE];
        for (int i = 0; i < ScrabbleModel.SIZE; i++) {
            for (int j = 0; j < ScrabbleModel.SIZE; j++) {
                JButton button = new JButton(" "); //Initialize buttons
                //button.setEnabled(false);
                boardButtons[i][j] = button;
                boardButtons[i][j].setActionCommand(i + " " + j);
                boardButtons[i][j].addActionListener(controller);
                setButtonColor(i, j);
                button.setOpaque(true);
                boardPanel.add(boardButtons[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        //Set up GUI
        setMenus();
        setPlayerPanel();
        setPlayerOptionPanel();
        setLegendPanel();

        //setPremiumSquares();

        pack();
        setSize(1000,900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    /**
     * The menu bars and items set up
     */
    private void setMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");

        JMenuItem undoMenuItem = new JMenuItem("Undo");
        JMenuItem redoMenuItem = new JMenuItem("Redo");
        JMenuItem saveMenuItem = new JMenuItem("Save Game");
        JMenuItem loadMenuItem = new JMenuItem("Load Game");

        undoMenuItem.addActionListener(controller);
        redoMenuItem.addActionListener(controller);
        saveMenuItem.addActionListener(controller);
        loadMenuItem.addActionListener(controller);

        fileMenu.add(loadMenuItem);
        fileMenu.add(saveMenuItem);
        editMenu.add(undoMenuItem);
        editMenu.add(redoMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Information to let the user know what the coloured squares mean
     */
    public void setLegendPanel(){
        //Panel to describe what each coloured square means
        JPanel legendPanel = new JPanel(new GridLayout(4,1));
        legendPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

        JLabel DLS = new JLabel("Double Letter Square");
        DLS.setForeground(DLSCOLOR);
        DLS.setBorder(BorderFactory.createLineBorder(DLSCOLOR, 4));
        legendPanel.add(DLS);

        JLabel TLS = new JLabel("Triple Letter Square");
        TLS.setForeground(TLSCOLOR);
        TLS.setBorder(BorderFactory.createLineBorder(TLSCOLOR, 4));
        legendPanel.add(TLS);

        JLabel DWS = new JLabel("Double Word Square");
        DWS.setForeground(DWSCOLOR);
        DWS.setBorder(BorderFactory.createLineBorder(DWSCOLOR, 4));
        legendPanel.add(DWS);

        JLabel TWS = new JLabel("Triple Word Square");
        TWS.setForeground(TWSCOLOR);
        TWS.setBorder(BorderFactory.createLineBorder(TWSCOLOR, 4));
        legendPanel.add(TWS);

        add(legendPanel, BorderLayout.EAST);
    }

    /**
     * GUI to let the user place a word
     */
    public void setPlayerOptionPanel(){
        JPanel playerOptionPanel = new JPanel(new GridLayout(1,2));

        JPanel inputPanel = new JPanel(new GridLayout(2,1));
        wordToPlace = new JLabel("", SwingConstants.CENTER);
        inputPanel.add(wordToPlace);

        JPanel tilePanel = new JPanel(new GridLayout(1,7));
        playerTiles = new JButton[7];
        ArrayList<Tile> playerHand = model.getCurrentPlayer().getTiles();
        for (int i = 0; i < 7; i++){
            JButton tile = new JButton(playerHand.get(i).getLetter() + " (" + playerHand.get(i).getValue() + ")");
            playerTiles[i] = tile;
            playerTiles[i].setActionCommand(playerHand.get(i).getLetter());
            playerTiles[i].addActionListener(controller);
            tilePanel.add(playerTiles[i]);
        }

        inputPanel.add(tilePanel);

        playerOptionPanel.add(inputPanel);

        JPanel sidePanel = new JPanel(new GridLayout(2,1));

        JPanel infoPanel = new JPanel(new GridLayout(1, 3));
        row = new JLabel ("Row: ");
        col = new JLabel("Col: ");
        orientation = new JLabel("Orientation: ");
        infoPanel.add(row);
        infoPanel.add(col);
        infoPanel.add(orientation);
        playerOptionPanel.add(infoPanel);

        sidePanel.add(infoPanel);


        //Panel for place word and pass turn buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2));

        // Add a button to place the word
        JButton placeWordButton = new JButton("Place Word");
        placeWordButton.setActionCommand("Place Word");
        placeWordButton.addActionListener(controller); // Trigger the controller when clicked
        buttonPanel.add(placeWordButton);

        //Add a button to pass your turn
        JButton passTurnButton = new JButton("Pass Turn");
        passTurnButton.setActionCommand("Pass Turn");
        passTurnButton.addActionListener(controller);
        buttonPanel.add(passTurnButton);

        JButton horizontalButton = new JButton("Horizontal");
        horizontalButton.setActionCommand("Horizontal");
        horizontalButton.addActionListener(controller);
        buttonPanel.add(horizontalButton);

        JButton verticalButton = new JButton("Vertical");
        verticalButton.setActionCommand("Vertical");
        verticalButton.addActionListener(controller);
        buttonPanel.add(verticalButton);

        sidePanel.add(buttonPanel);
        playerOptionPanel.add(sidePanel);

        add(playerOptionPanel, BorderLayout.SOUTH);
    }

    /**
     * GUI to display the current player and all players' points
     */
    public void setPlayerPanel(){
        //Add labels for the current player and their tiles
        JPanel playerPanel = new JPanel(new GridLayout(2, 1));
        currentPlayerLabel = new JLabel("Current Player: " + model.getCurrentPlayer().getName(), SwingConstants.CENTER);
        playerPanel.add(currentPlayerLabel);

        //Add labels for the players' points
        JPanel pointPanel = new JPanel(new GridLayout(1,4));
        player1Points = new JLabel("Player 1 Points: " + 0);
        player2Points = new JLabel("Player 2 Points: 0");
        player3Points = new JLabel("Player 3 Points: 0");
        player4Points = new JLabel("Player 4 Points: 0");
        pointPanel.add(player1Points);
        pointPanel.add(player2Points);
        pointPanel.add(player3Points);
        pointPanel.add(player4Points);
        playerPanel.add(pointPanel);

        add(playerPanel, BorderLayout.NORTH);
    }

    /**
     * Used to set the number of users that will be playing Scrabble and how many of them will be AI
     */
    private void setNumOfPlayers(){
        int totalPlayers = -1;

        while (totalPlayers < 2 || totalPlayers > 4){
            try {
                totalPlayers = Integer.parseInt(
                        JOptionPane.showInputDialog(null, "Enter the total number of players (2-4):")
                );
                if (totalPlayers < 2 || totalPlayers > 4) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number between 2 and 4.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.");
            }
        }

        int aiPlayers = -1;

        while (aiPlayers < 0 || aiPlayers >= totalPlayers){
            try {
                aiPlayers = Integer.parseInt(
                        JOptionPane.showInputDialog(null, "Enter the number of AI players (0-" + (totalPlayers - 1) + "):")
                );
                if (aiPlayers < 0 || aiPlayers >= totalPlayers) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Invalid input. Please ensure there is at least one real player."
                    );
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.");
            }
        }


        numOfRealPlayers = totalPlayers - aiPlayers;
        numOfAiPlayers = aiPlayers;
        NUMOFPLAYERS = numOfAiPlayers + numOfRealPlayers;
    }

    /**
     * Getter method for the word that is being placed on the board
     * @return the word being placed on the board
     */
    public String getWordToPlaced(){
        return wordToPlace.getText();
    }

    /**
     * Setter method for the word being placed on the board
     * @param s the String that will be placed on the board
     */
    public void setWordToPlace(String s){
        wordToPlace.setText(s);
    }

    /**
     * Check the text of a cell on the Scrabble board
     * @param row the row of the cell
     * @param col the column of the cell
     * @return the text of the cell
     */
    public String checkCellBlank(int row, int col){
        return boardButtons[row][col].getText();
    }

    /**
     * Check that a word is valid in the dictionary
     * @param word the word we are checking for
     * @return true if the word is valid, false otherwise
     */
    public boolean checkValidWord(String word){
        return dictionary.isValidWord(word);
    }

    /**
     * Let the user know what their selection of row and column is for placing a word on the board
     * @param row the row that the user is setting
     * @param col the column that the user is setting
     */
    public void setRowCol(int row, int col){
        this.row.setText("Row: " + row);
        this.col.setText("Col: " + col);
    }

    /**
     * Clearing the text of the row and column labels (i.e. the user has not made a selection for a row and column)
     */
    public void clearRowCol(){
        row.setText("Row: ");
        col.setText("Col: ");
    }

    /**
     * Let the user know what their selection of orientation (horizontal or vertical) is for placing a word on the board
     * @param orient the orientation the user is setting
     */
    public void setOrientation(String orient){
        this.orientation.setText("Orientation: " + orient);
    }

    /**
     * Clearing the text of the orientation label (i.e. the user has not made a selection for the orientation)
     */
    public void clearOrientation(){
        orientation.setText("Orientation: ");
    }

    /**
     * Set the button color of premium squares
     * @param row the row of the premium square
     * @param col the column of the premium square
     */
    private void setButtonColor(int row, int col) {
        int premiumType = model.getPremiumSquares()[row][col];

        switch (premiumType) {
            case 2: // Double letter
                boardButtons[row][col].setBackground(DLSCOLOR);
                break;
            case 3: // Triple letter
                boardButtons[row][col].setBackground(TLSCOLOR);
                break;
            case 4: // Double word
                boardButtons[row][col].setBackground(DWSCOLOR);
                break;
            case 5: // Triple word
                boardButtons[row][col].setBackground(TWSCOLOR);
                break;
            default: // Regular square
                boardButtons[row][col].setBackground(Color.WHITE);
                break;
        }
    }

    /**
     * Method used to update the GUI when a player places a word
     * @param e EventObject used to get the board and current player information
     */
    @Override
    public void handleScrabbleStatusUpdate(ScrabbleEvent e) {
        char[][] board = e.getBoard();
        // Update the board buttons based on the new board state
        for (int i = 0; i < ScrabbleModel.SIZE; i++) {
            for (int j = 0; j < ScrabbleModel.SIZE; j++) {
                boardButtons[i][j].setText(String.valueOf(board[i][j]));
            }
        }

        // Update player info
        handleScrabblePassTurnUpdate(e);

        switch(model.getPlayers().indexOf(e.getCurrentPlayer())){
            case 0:
                player1Points.setText("Player 1 Points: " + e.getCurrentPlayer().getScore());
                break;
            case 1:
                player2Points.setText(e.getCurrentPlayer().getName() + " Points: " + e.getCurrentPlayer().getScore());
                break;
            case 2:
                player3Points.setText(e.getCurrentPlayer().getName()+" Points: "  + e.getCurrentPlayer().getScore());
                break;
            default:
                player4Points.setText(e.getCurrentPlayer().getName()+" Points: "  + e.getCurrentPlayer().getScore());
                break;

        }

    }

    /**
     * Method used to update the GUI when a player passes their turn
     * @param e EventObject used to get the current player information
     */
    @Override
    public void handleScrabblePassTurnUpdate(ScrabbleEvent e){
        //Update the labels for the current player and their tiles
        currentPlayerLabel.setText("Current Player: " + e.getCurrentPlayer().getName());
        //playerTilesLabel.setText("Tiles: " + e.getCurrentPlayer().printTiles());

        wordToPlace.setText("");
        clearRowCol();
        clearOrientation();

        ArrayList<Tile> playerHand = model.getCurrentPlayer().getTiles();

        for (int i = 0; i < 7; i++){
            playerTiles[i].setText(playerHand.get(i).getLetter() + " (" + playerHand.get(i).getValue() + ")");
            playerTiles[i].setActionCommand(playerHand.get(i).getLetter());

            if (model.currentPlayer.isAI()){
                playerTiles[i].setEnabled(false);
            } else{
                playerTiles[i].setEnabled(true);
            }
        }
    }

    /**
     * Method used to get the word the user wants to place on the board
     * @return the word the user wants to place
     */
    public String getWord(){
        String word = JOptionPane.showInputDialog(null, "Enter the word to place:");

        word = word.toLowerCase(); //for consistency


        if (word == null || word.isEmpty() || !dictionary.isValidWord(word)) {
            JOptionPane.showMessageDialog(null, "Invalid word. Try again.");
            word = getWord(); //allow user to continue entering a word
        }

        return word;
    }

    /**
     * Method used to get the row on the board where the word will be placed
     * @return the row number
     */
    public int getRow(){
        int row = -1;
        try {
            row = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the starting row (1-" + (ScrabbleModel.SIZE) + "):"));

        } catch (NumberFormatException ex) { //Number is not entered
            JOptionPane.showMessageDialog(null, "Invalid row input. Try again.");
            row = getRow(); //allow user to continue entering a row number
        }

        if (row <= 0 || row > ScrabbleModel.SIZE) { //out of bounds
            JOptionPane.showMessageDialog(null, "Row out of bounds. Try again.");
            row = getRow(); //allow user to continue entering a row number
        }

        return row;
    }

    /**
     * Method used to get the column on the board where the word will be placed
     * @return the column number
     */
    public int getColumn(){
        int col = -1;
        try {
            col = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the starting column (1-" + (ScrabbleModel.SIZE) + "):"));
        } catch (NumberFormatException ex) { //Number is not entered
            JOptionPane.showMessageDialog(null, "Invalid column input. Try again.");
            col = getColumn(); //Allow user to continue entering a column
        }

        if (col <= 0 || col > ScrabbleModel.SIZE) { //Out of bounds
            JOptionPane.showMessageDialog(null, "Column out of bounds. Try again.");
            col = getColumn(); //Allow user to continue entering a column
        }

        return col;
    }

    /**
     * Method used to get the orientation, horizontal (H) or vertical (V), of the word placement
     * @return true if H, false if V
     */
    public boolean getOrientation(){
        // Ask for orientation
        String orientation = JOptionPane.showInputDialog(null, "Enter orientation (H for horizontal, V for vertical):");

        boolean isHorizontal = true; // Default orientation
        if (orientation != null && (orientation.equalsIgnoreCase("H") || orientation.equalsIgnoreCase("V"))) {
            isHorizontal = orientation.equalsIgnoreCase("H");
        } else {
            JOptionPane.showMessageDialog(null, "Invalid orientation. Try again.");
            isHorizontal = getOrientation(); //Allow user to continue entering an orientation
        }

        return isHorizontal;
    }

    /**
     * @return number of real players
     */
    public int getNumOfRealPlayers() {
        return numOfRealPlayers;
    }

    /**
     * @return number of AI players
     */
    public int getNumOfAiPlayers() {
        return numOfAiPlayers;
    }

    public static void main(String[] args) {
        new ScrabbleModelViewFrame();
    }


}
