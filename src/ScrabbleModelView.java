/**
 * This interface outlines methods used for handling the updates from Scrabble events.

 * @author: Aqsa Atif
 * @version 1 November 10, 2024
 */

public interface ScrabbleModelView {
    void handleScrabbleStatusUpdate(ScrabbleEvent e);
    void handleScrabblePassTurnUpdate(ScrabbleEvent e);
    void handleScrabbleUndoRedoUpdate(ScrabbleEvent e);
    void handleScrabbleLoadUpdate(ScrabbleEvent e);
}