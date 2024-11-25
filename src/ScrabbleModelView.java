/**
 * This interface outlines methods used for handling the updates from Scrabble events.

 * @author: Aqsa Atif
 * @version 1 November 10, 2024
 */

public interface ScrabbleModelView {
    public void handleScrabbleStatusUpdate(ScrabbleEvent e);
    public void handleScrabblePassTurnUpdate(ScrabbleEvent e);
}