import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listens to user actions and acts accordingly
 * @author Basma Mohammed
 * @version 1, November 10, 2024
 *
 * @author Aqsa Atif, Yomna Ibrahim
 * @version 2, November 11, 2024
 *
 * @author Aqsa Atif
 * @version 4 November 29, 2024
 */
public class ScrabbleController implements ActionListener {
    ScrabbleModel scrabbleModel;
    ScrabbleModelViewFrame view;
    int rowNum;
    int colNum;
    boolean isHorizontal;
    boolean orientation = false;
    String word;

    public ScrabbleController(ScrabbleModel scrabbleModel, ScrabbleModelViewFrame view) {
        this.scrabbleModel = scrabbleModel;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();

        if (buttonPressed.equals("Place Word")) { //Place word button is pressed

            Player currentPlayer = scrabbleModel.getCurrentPlayer();
            if (currentPlayer.isAI()){
                ScrabbleAI.playBestMove(currentPlayer);
            }

            else {

                if (rowNum > 0 && colNum > 0 && !word.isEmpty() && orientation && view.checkValidWord(word)){ //Check that everything has been inputted and its valid
                    String[] result = scrabbleModel.placeWord(word, rowNum, colNum, isHorizontal, currentPlayer);

                    if (result[0].equals("false")) { //Word has not been placed on the board
                        JOptionPane.showMessageDialog(view, "Invalid word or placement!");
                        view.setWordToPlace("");
                        view.clearOrientation();
                        view.clearRowCol();
                    } else { //Word has been placed on the board

                        scrabbleModel.currentPlayer.replaceTiles(result[1], word, scrabbleModel.getTileSet());
                        scrabbleModel.updatePlaceWord(word, rowNum, colNum, isHorizontal); //Update the GUI
                        scrabbleModel.changeTurn(); //Change the player's turns
                        JOptionPane.showMessageDialog(view, "Word placed successfully!");
                    }
                } else {
                    JOptionPane.showMessageDialog(view, "Please enter a valid word, location, and/or orientation");
                    view.setWordToPlace("");
                    view.clearOrientation();
                    view.clearRowCol();
                }
                //reset the input
                rowNum = -1;
                colNum = -1;
                word = "";
                orientation = false;
            }
        } else if (buttonPressed.equals("Pass Turn")) { //Pass turn button is pressed
            scrabbleModel.pushUndo(scrabbleModel.getBoard(), scrabbleModel.getCurrentPlayerIndex(), scrabbleModel.getPlayers());
            scrabbleModel.changeTurn();


        } else if (buttonPressed.equals("Horizontal")){
            isHorizontal = true;
            orientation = true; //an orientation has been picked
            view.setOrientation("H"); //letting the user know
        } else if (buttonPressed.equals("Vertical")){
            isHorizontal = false;
            orientation = true; //an orientation has been picked
            view.setOrientation("V"); //letting the user know
        } else if (buttonPressed.length() == 1){ //tile button is pressed
            word = view.getWordToPlaced();

            if (buttonPressed.equals(" ")){ //blank tile
                String letter = JOptionPane.showInputDialog(null, "Enter the letter you would like the blank tile to be:");
                letter = letter.toLowerCase(); //for consistency

                if (letter == null || word.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Invalid. Try again.");
                } else {
                    word += letter.toLowerCase(); //update the word being placed with the new letter
                }
            } else{
                word += buttonPressed.toLowerCase(); //update the word being placed with the new letter
            }

            view.setWordToPlace(word); //inform user

        } else if(buttonPressed.matches("\\d+ \\d+")){ //cell of the Scrabble board is pressed
            String[] position = e.getActionCommand().split(" ");
            String letter = view.checkCellBlank(Integer.parseInt(position[0]),Integer.parseInt(position[1])); //check if using a word already on the board

            if (!letter.equals(" ")){ //using a letter already on the board
                word += letter; //update the word being placed with the new letter
                view.setWordToPlace(word);
            }

            //Store the position
            rowNum = Integer.parseInt(position[0]) + 1;
            colNum = Integer.parseInt(position[1]) + 1;

            view.setRowCol(rowNum, colNum); //inform user

        }else if (buttonPressed.equals("Undo")){
            scrabbleModel.pushRedo(scrabbleModel.getBoard(),scrabbleModel.getCurrentPlayerIndex(),scrabbleModel.getPlayers());
            GameState gs = scrabbleModel.popUndo();
            scrabbleModel.updateUndoRedo(gs.getBoard(),gs.getPlayers());

        }else if (buttonPressed.equals("Redo")){
            scrabbleModel.pushUndo(scrabbleModel.getBoard(),scrabbleModel.getCurrentPlayerIndex(),scrabbleModel.getPlayers());
            GameState gs = scrabbleModel.popRedo();
            scrabbleModel.updateUndoRedo(gs.getBoard(),gs.getPlayers());

        }else if (buttonPressed.equals("Save Game")){
            System.out.println("save game");
        }else if (buttonPressed.equals("Load Game")){
            System.out.println("load game");
        }
    }
}
