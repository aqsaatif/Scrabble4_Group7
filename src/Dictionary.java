import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
/**
 * This class reads all valid words from a text file that may be used in the game
 * @author: Aqsa Atif
 */
public class Dictionary {
    final private Set<String> words;

    /**
     * Initialize the hashset by loading all the words from the text file.
     */
    public Dictionary() {
        words = new HashSet<>();
        loadWords("scrabble_dictionary.txt");
    }

    /**
     * Check to see if a word is valid
     * @param word the word we are checking for
     * @return boolean true if the word is valid, false if it is not
     */
    public boolean isValidWord(String word) {
        return words.contains(word.toUpperCase());
    }

    /**
     * Load all words from the text file into a HashSet.
     * @param filePath the name of the text file we are loading from
     */
    public void loadWords(String filePath){
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine().trim().toUpperCase(); // Ensure uppercase and trimmed
                words.add(data); // Add each word to the set
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
    }
}