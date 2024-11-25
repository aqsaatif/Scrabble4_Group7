import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listens to user actions and acts accordingly
 * @author Basma Mohammed
 * @version 1, November 10, 2024
 *
 *@author Aqsa Atif, Yomna Ibrahim
 * @version 2, November 11, 2024
 */
public class ScrabbleController implements ActionListener {
    ScrabbleModel scrabbleModel;
    ScrabbleModelViewFrame view;

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
                // Get inputs from the view
                String word = view.getWord();
                int rowNum = view.getRow();
                int colNum = view.getColumn();
                boolean isHorizontal = view.getOrientation();


                String[] result = scrabbleModel.placeWord(word, rowNum, colNum, isHorizontal, currentPlayer);

                if (result[0].equals("false")) { //Word has not been placed on the board
                    JOptionPane.showMessageDialog(view, "Invalid word or placement!");
                } else { //Word has been placed on the board
                    scrabbleModel.currentPlayer.replaceTiles(result[1], word, scrabbleModel.getTileSet());
                    scrabbleModel.updatePlaceWord(word, rowNum, colNum, isHorizontal); //Update the GUI
                    scrabbleModel.changeTurn(); //Change the player's turns
                    JOptionPane.showMessageDialog(view, "Word placed successfully!");
                }
            }
        } else if (buttonPressed.equals("Pass Turn")) { //Pass turn button is pressed
            scrabbleModel.changeTurn();
        }
    }
}
