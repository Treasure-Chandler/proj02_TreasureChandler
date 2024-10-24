/*
 * Treasure Chandler
 * CS 16000-01 – 02/03, Fall Semester 2024
 * Project 2: Examination Statistics
 *
 * Description:
 * The purpose of this class is to process large
 * amounts of data to obtain statistical information.
 * The data can be manipulated to calculate different aspects
 * of it, using the average, median, and the population
 * standard deviation
 */

import java.io.*; // mainly used for file reading stuff
import java.util.Scanner; // needed for the Scanner class

public class ExamStatistics {
    // letter grade CONSTANT variables declaration
    public static final int LETTER_GRADE_A = 90;
    public static final int LETTER_GRADE_B = 80;
    public static final int LETTER_GRADE_C = 65;
    public static final int LETTER_GRADE_D = 50;
    public static final int LETTER_GRADE_F = 0;

    // simple boolean to check if this is the first file being processed
    private static boolean checkExamStatFile = false;

    /**
     * 
     * @param args              entered values
     * @throws IOException      if any IO exception has occured
     */
    // this line is used to surpress any warnings for unused variables
    public static void main(String[] args) throws IOException {
        // variables declaration and method calling
        File fileInput = checkFileFromUser();
        String outputMessage = processScores(fileInput);

        // writing a new .txt file based on the file that was read
        displayOutputToFile(fileInput, outputMessage);

        // displaying the outputMessage in the console
        System.out.println(outputMessage);

    } // end of main()

    /**
     * mainly handles file input and determines if the file exists
     * 
     * @return      the file name the user inputs
     */
    private static File checkFileFromUser() {
        // variables declaration
        // for console reading
        Scanner consoleReader = new Scanner(System.in);
        String inputFileName;

        // get the validated inputFileName
        File fileInput;

        /*
         * check if the file exists in a loop and repeat file name
         * solicitation until the name is accepted
         */
        while (true) {
            // prompts user to enter the file name
            System.out.println("Enter the file name of " +
                    "the score statistics you would " +
                    "like to see (ending in \".txt\"):");
            inputFileName = consoleReader.nextLine();
            /*
             * for files that will be solicited and recieved from the
             * console
             */
            fileInput = new File(inputFileName);
            /*
             * checks if the file actually exists. if it does, the program
             * states that was an acceptable file name and stops the loop.
             * otherwise, it will prompt the user to enter the file name again.
             */
            if (fileInput.exists() && fileInput.isFile()) {
                System.out.println("\nFile name accepted!\n");
                break;
            } else {
                System.out.println("\nFile does not exist. Try again.\n");
            }
        }
        consoleReader.close();

        return fileInput;

    } // end of checkFileFromUser()

    /**
     * reads and processes the file, calculates scores, and calls two
     * helper methods for calculations
     * 
     * @param fileInput         the file name the user has inputted
     * @return                  returns the output eventually displayed in the console
     * @throws IOException      if any IO exception has occured
     */
    private static String processScores(File fileInput) throws IOException {
        // variables declaration
        int totalScores = 0, validScores = 0,
                aGrade = 0, bGrade = 0, cGrade = 0, dGrade = 0, fGrade = 0;
        int minScore = Integer.MAX_VALUE, maxScore = Integer.MIN_VALUE;
        double sum = 0, average = 0, psd = 0;
        /*
         * instantiates fileScanner as a scanner (using fileInput
         * as the input)
         */
        Scanner fileScanner = new Scanner(fileInput);

        while (fileScanner.hasNextLine()) {
            String readLine = fileScanner.nextLine();
            Scanner readLineScanner = new Scanner(readLine);
            while (readLineScanner.hasNext()) {
                // processes all the scores in the file
                if (readLineScanner.hasNextInt()) {
                    int nextScore = readLineScanner.nextInt();

                    /*
                     * increment the total (given scores) for every
                     * read score
                     */
                    totalScores++;

                    // checks for any valid scores
                    if (nextScore >= 0 && nextScore <= 100) {
                        validScores++;
                        sum += nextScore;

                        /*
                         * update the min and max scores, along
                         * with updating the grade computations
                         */
                        if (nextScore < minScore)
                            minScore = nextScore;
                        if (nextScore > maxScore)
                            maxScore = nextScore;
                        if (nextScore >= LETTER_GRADE_A)
                            aGrade++;
                        else if (nextScore >= LETTER_GRADE_B)
                            bGrade++;
                        else if (nextScore >= LETTER_GRADE_C)
                            cGrade++;
                        else if (nextScore >= LETTER_GRADE_D)
                            dGrade++;
                        else
                            fGrade++;
                    }
                } else {
                    // skip any invalid inputs
                    readLineScanner.next();
                    // counts invalid scores as a part of given scores
                    totalScores++;
                }
            }
            readLineScanner.close();
        }
        fileScanner.close();

        /*
         * calculating the average and population standatrd deviation (
         * if there are valid scores)
         */
        if (validScores > 0) {
            average = calculateAverage(sum, validScores);
            psd = calculatePSD(fileInput, average, validScores);
        }

        return displayOutput(totalScores, validScores, average, psd,
                            aGrade, bGrade, cGrade, dGrade, fGrade, 
                            minScore, maxScore);

    } // end of processScores()

    /**
     * simply calculates the average
     * 
     * @param sum               sum of the individial scores
     * @param validScores       total number of (valid) scores
     * @return                  returns the average
     */
    private static double calculateAverage(double sum, int validScores) {
        return sum / validScores;

    } // end of calculateAverage

    /**
     * simply calculats the population standard deviation (psd)
     * 
     * @param fileinput         the file name the user has inputted
     * @param average           the average previously calculated
     * @param validScores       total number of (valid) scores
     * @return                  returns the square root of the psd
     * @throws IOException      if any IO exception has occured
     */
    private static double calculatePSD(File fileinput, double average,
                                        int validScores)
    throws IOException {
        // variables declaration
        double psd = 0;
        Scanner fileScanner = new Scanner(fileinput);

        /*
         * redoing the while-loop, but excluding the score determination
         * and only using it to calculate the PSD
         */
        while (fileScanner.hasNextLine()) {
            String readLine = fileScanner.nextLine();
            Scanner readLineScanner = new Scanner(readLine);
            while (readLineScanner.hasNext()) {
                if (readLineScanner.hasNextInt()) {
                    int nextScore = readLineScanner.nextInt();
                    if (nextScore >= 0 && nextScore <= 100) {
                        psd += Math.pow(nextScore - average, 2);
                    }
                }
            }
            readLineScanner.close();
        }
        fileScanner.close();
        return Math.sqrt(psd / validScores);

    } // end of calculatePSD()

    /**
     * displays the output to the console
     * 
     * @param totalScores       total scores
     * @param validScores       valid scores
     * @param average           calculated average
     * @param psd               calculated psd
     * @param aGrade            incremented A
     * @param bGrade            incremented B
     * @param cGrade            incremented C
     * @param dGrade            incremented D
     * @param fGrade            incremented F
     * @param minScore          lowest score found in the file
     * @param maxScore          highest score found in the file
     * @return                  returns the output message to be displayed
     */
    private static String displayOutput(int totalScores, int validScores, double average,
                                        double psd, int aGrade, int bGrade, int cGrade,
                                        int dGrade, int fGrade, int minScore, int maxScore) {
        // compose the output message to be displayed in the console
        String outputMessage;
        outputMessage = "Exam Statistics\n\n";
        outputMessage += "Total number of (given) scores: " + totalScores + "\n";
        outputMessage += "Total number of valid scores: " + validScores + "\n\n";
        outputMessage += "Considering only the given valid scores:\n";
        outputMessage += String.format("Average score (of valid scores): %.2f\n", average);
        outputMessage += String.format("Population standard deviation of the valid " +
                                        "scores: %.2f\n\n", psd);
        outputMessage += String.format("# of A, 85-100: \t%d \t\t%.2f%%\n", aGrade,
                                        (aGrade * 100.0 / validScores));
        outputMessage += String.format("# of B, 75--84: \t%d \t\t%.2f%%\n", bGrade,
                                        (bGrade * 100.0 / validScores));
        outputMessage += String.format("# of C, 65--74: \t%d \t\t%.2f%%\n", cGrade,
                                        (cGrade * 100.0 / validScores));
        outputMessage += String.format("# of D, 50--64: \t%d \t\t%.2f%%\n", dGrade,
                                        (dGrade * 100.0 / validScores));
        outputMessage += String.format("# of F, 00--49: \t%d \t\t%.2f%%\n", fGrade,
                                        (fGrade * 100.0 / validScores));
        outputMessage += "\nMinimum score: " + (validScores > 0 ? minScore : "N/A") + "\n";
        outputMessage += "Maximum score: " + (validScores > 0 ? maxScore : "N/A") + "\n";

        return outputMessage;

    } // return displayOutput()

    /**
     * "copy pastes" the message in the console to a new .txt file
     * 
     * @param fileInput         the file name the user has inputted
     * @param outputMessage     message that is in the console
     * @throws IOException      if any IO exception has occured
     */
    private static void displayOutputToFile(File fileInput, String outputMessage)
    throws IOException {
        // the beginning of file creation logic
        File examStatFile = new File("ExamStatFile.txt");

        /*
         * if this is the first time generating a .txt file (preferably using
         * to_test.txt), the .txt file will be named "ExamStatFile.txt"
         */
        if (!checkExamStatFile && !examStatFile.exists()) {
            // "copy-paste the output to a new .txt file"
            PrintWriter txtWriter = new PrintWriter(new FileWriter(examStatFile));
            txtWriter.println(outputMessage);
            txtWriter.close();
            checkExamStatFile = true;
        } else {
            // handle the special case for "examScores.txt"
            String fileOutput;
            if (fileInput.getName().equals("exam_scores.txt")) {
                fileOutput = "exam_scoresStatFile.txt";
            } else {
                /*
                 * subsequent files will use this standard naming convention for other
                 * files
                 */
                fileOutput = fileInput.getName().replace(".txt", "ExamStatFile.txt");
            }

            PrintWriter fileWriter = new PrintWriter(new FileWriter(fileOutput));
            fileWriter.println(outputMessage);
            fileWriter.close();
        }

    } // end of displayOutputToFile()

} // end of ExamStatistics