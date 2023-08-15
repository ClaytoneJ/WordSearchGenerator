//Clayton Johnson
//CS 145 WordSearch assignment
//7/24/2023
//This program generates a word search puzzle and solution to that puzzle
import java.util.*;

public class WordSearchGenerator {
    private static final int MAX_TRIES_PER_WORD = 100;
    private static final char EMPTY_CELL = '-';

    private char[][] wordSearch;
    private char[][] solution;
    private int size;
    
    //Constructor to initialize the WordSearchGenerator with a given size
    public WordSearchGenerator(int size) {
        this.size = size;
        wordSearch = new char[size][size];
        solution = new char[size][size];
        initializeWordSearch(size);
    }

    //Initializes the 2-d arrays that hold the word puzzles with '-'
    private void initializeWordSearch(int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                wordSearch[i][j] = EMPTY_CELL;
                solution[i][j] = EMPTY_CELL;
            }
        }
    }

    //returns a random upper case letter
    private static char getRandomChar() {
        return (char)(65 + (int)(Math.random() * 26));
    }

    //Method to generate word search grid
    public void generate(Scanner scanner, List < String > words) {
        for (String word: words) {
            placeWord(word);
        }
        print();
    }

    //Helper method to place individual words in the puzzle
    private void placeWord(String word) {
        Random random = new Random();
        int wordLength = word.length();
        int tryCount = 0;
        //Trys to put words into puzzle at random directions
        while (tryCount < MAX_TRIES_PER_WORD) {
            tryCount++;
            int direction = random.nextInt(8);
            int row = random.nextInt(size);
            int col = random.nextInt(size);

            int dRow = 0;
            int dCol = 0;
            //Calculates row/collum increments based on random direction
            switch (direction) {
                case 0:
                    dRow = -1;
                    break; //up
                case 1:
                    dRow = 1;
                    break; //Down
                case 2:
                    dCol = -1;
                    break; //Left
                case 3:
                    dCol = 1;
                    break; //right
                case 4:
                    dRow = -1;
                    dCol = -1;
                    break; //Up-left
                case 5:
                    dRow = -1;
                    dCol = 1;
                    break; //Up-right
                case 6:
                    dRow = 1;
                    dCol = -1;
                    break; //Down-left
                case 7:
                    dRow = 1;
                    dCol = 1;
                    break; //Down-right
            }

            //Checks if word can be fit that way
            if (isValidPosition(row, col, dRow * (wordLength - 1), dCol * (wordLength - 1))) {
                boolean wordFits = true;
                //Makes sure the word won't overlap with placed word
                for (int i = 0; i < wordLength; i++) {
                    if (wordSearch[row + i * dRow][col + i * dCol] != EMPTY_CELL &&
                        wordSearch[row + i * dRow][col + i * dCol] != word.charAt(i)) {
                        wordFits = false;
                        break;
                    }
                }
                if (wordFits) {
                    //Places word into solution and puzzle grid
                    for (int i = 0; i < wordLength; i++) {
                        wordSearch[row + i * dRow][col + i * dCol] = word.charAt(i);
                        solution[row + i * dRow][col + i * dCol] = word.charAt(i);
                    }
                    return;
                }
            }
        }
        //If word cant be placed
        System.out.println("Warning: Could not place the word: " + word);
    }

    //Checks if position is valid in the grid
    private boolean isValidPosition(int row, int col, int dRow, int dCol) {
        int lastRow = row + dRow;
        int lastCol = col + dCol;
        return lastRow >= 0 && lastRow < size && lastCol >= 0 && lastCol < size;
    }

    //Prints out generated word search
    public void print() {
        //Fills puzzle with the correct characters(word or random)
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (wordSearch[i][j] == EMPTY_CELL) {
                    wordSearch[i][j] = getRandomChar();
                }
                System.out.print(wordSearch[i][j] + " ");
            }
            System.out.println();
        }
    }

    //Prints out solution grid
    public void showSolution() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(solution[i][j] + " ");
            }
            System.out.println();
        }
    }

    //Prints introduction
    public static void printIntro() {
        System.out.println("Welcome to Word Search Generator!");
        System.out.println("You can generate a word search and");
        System.out.println("find the hidden words in it. If you need help");
        System.out.println("you can choose to show the solution.\n");
    }

    //Main method, used to interact with user
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        printIntro();
        char choice;
        WordSearchGenerator generator = null;

        do {
            System.out.println("Menu:");
            System.out.println("  (g) - Generate new word search puzzle");
            System.out.println("  (p) - Print the word search");
            System.out.println("  (s) - Show the solution");
            System.out.println("  (q) - Quit");

            choice = scanner.next().charAt(0);
            scanner.nextLine();

            switch (choice) {
                //Generate word search choice
                case 'g':
                    System.out.print("Enter the number of words: ");
                    int numWords = scanner.nextInt();
                    scanner.nextLine();

                    List <String> words = new ArrayList <>();
                    int longestWordLen = 0;
                    //for loop that collects words
                    for (int i = 0; i < numWords; i++) {
                        System.out.print("Enter word " + (i + 1) + ": ");
                        String word = scanner.nextLine().toUpperCase();

                        words.add(word);
                        int wordLen = word.length();
                        if (wordLen > longestWordLen) {
                            longestWordLen = wordLen;
                        }
                    }
                    //Calculate size of grid based on number of words
                    //and the longest word
                    int puzzleSize = numWords + longestWordLen + 2;
                    generator = new WordSearchGenerator(puzzleSize);
                    generator.generate(scanner, words);
                    break;
                //Print word search choice
                case 'p':
                    if (generator != null) {
                        generator.print();
                    } else {
                        System.out.println("Please generate a word search first");
                    }
                    break;
                //Print solution choice
                case 's':
                    if (generator != null) {
                        generator.showSolution();
                    } else {
                        System.out.println("Please generate a word search first");
                    }
                    break;
                //quit program
                case 'q':
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 'q');
        scanner.close();
    }
}