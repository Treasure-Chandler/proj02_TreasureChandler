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

 import java.io.*;               // mainly used for file reading stuff
 import java.util.Scanner;       // needed for the Scanner class
 
 public class ExamStatistics {
     // letter grade CONSTANT variables declaration
     public static final int LETTER_GRADE_A = 90;
     public static final int LETTER_GRADE_B = 80;
     public static final int LETTER_GRADE_C = 65;
     public static final int LETTER_GRADE_D = 50;
     public static final int LETTER_GRADE_F = 0;
 
     /**
      * 
      * @param args  entered values
      */
     public static void main(String[] args) throws IOException {
         // variables declaration
         int totalScores = 0, validScores = 0,
             aGrade = 0, bGrade = 0, cGrade = 0, dGrade = 0, fGrade = 0,
             minScore = Integer.MAX_VALUE, maxScore = Integer.MIN_VALUE, nextScore = 0;
         double sum = 0.0, average = 0.0, psd = 0.0;
         String outputMessage = "", inputFileName = "";
 
         // for console reading
         Scanner consoleReader = new Scanner(System.in), fileReader = null;
 
         // validateFileName is called in order to get the validated inputFileName
         inputFileName = validateFileName(consoleReader, inputFileName).nextLine();
 
         // open the file again (with the correct inputFileName) for data reading
         fileReader = new Scanner(new File(inputFileName));
 
         firstPass(fileReader, inputFileName, nextScore, totalScores, validScores, maxScore, minScore, aGrade, bGrade, cGrade, dGrade, fGrade, sum, average);
 
         // close and re-open fileReader in order to execute the second pass
         fileReader.close();
         fileReader = new Scanner(new File(inputFileName));
 
         secondPass(fileReader, validScores, maxScore, nextScore, psd);
 
         displayOutputMessage();
 
         fileReader.close();
         consoleReader.close();
 
     } // end of main()
 
     /**
      * this method simply validates the file name reading
      * 
      * @param consoleReader             reads the file name input
      * @param fileReader                a secondary Scanner object
      * @param inputFileName             the field for the user to input a file name
      * @return                          returns the file name the user inputted
      * @throws FileNotFoundException    thrown in the console if a file is not found
      */
     public static Scanner validateFileName(Scanner consoleReader, String inputFileName)
     throws FileNotFoundException {
         Scanner fileReader = null;
 
         /*
          * check if the file exists and repeat file name solicitation
          * until the name is accepted
          */
         while (fileReader == null) {
             // prompts user to enter the file name
             System.out.println("Enter your file name (ending in \".txt\"):");
             inputFileName = consoleReader.nextLine();
             /*
              * for files that will be solicited and recieved from the
              * console
              */
             File file0 = new File(inputFileName);
 
             /*
              * checks if the file actually exists. if it does, the program
              * states that was an acceptable file name and stops the loop.
              * otherwise, it will prompt the user to enter the file name again.
              */
             if (file0.exists()) {
                 System.out.println("\nFile name accepted!\n");
                 /*
                  * instantiates fileReader as a scanner (using file0
                  * as the input)
                  */
                 fileReader = new Scanner(file0);
             } else {
                 System.out.println("\nFile does not exist. Try again.\n");
             }
         }
 
         return fileReader;
 
     } // end of validateFileName()
 
     /**
      * once the file name is validated, a while loop is run in this method to
      * read, count, and sum the scores ("running total"). the loop contains
      * logic to determine the maxScore and minScore, and increments the
      * occurences of scores in the grade groups
      * 
      * incorrect inputs do not update any of the counter variables, nor the
      * sum, maxSxore, and minScore variables
      * 
      * @param fileReader                    placeholder param for fileReader
      * @param inputFileName                 placeholder param for inputFileName
      * @param nextScore                     placeholder param for nextScore
      * @param totalScores                   placeholder param for totalScores
      * @param validScores                   placeholder param for validScores
      * @param maxScore                      placeholder param for maxSxore
      * @param minScore                      placeholder param for minScore
      * @param sum                           placeholder param for sum
      * @param average                       placeholder param for avg
      * @param aGrade                        placeholder param for A
      * @param bGrade                        placeholder param for B
      * @param cGrade                        placeholder param for C
      * @param dGrade                        placeholder param for D
      * @param fGrade                        placeholder param for F
      * @throws FileNotFoundException        thrown in the console if a file is not found
      */
     public static void firstPass(Scanner fileReader, String inputFileName,
                                  int nextScore, int totalScores, int validScores,
                                  int maxScore, int minScore, int aGrade, int bGrade,
                                  int cGrade, int dGrade, int fGrade, double sum,
                                  double average) 
     throws FileNotFoundException {
         while (fileReader.hasNext()) {
             // processes all the scores in the file
             if (fileReader.hasNextInt()) {
                 nextScore = fileReader.nextInt();
                 totalScores++;
 
                 // validates the scores (in a range of 0-100)
                 if (nextScore >= LETTER_GRADE_F && nextScore <= 100) {
                     validScores++;
                     sum += nextScore;
 
                     // update the min and max scores
                     if (nextScore > maxScore) {
                         maxScore = nextScore;
                     }
                     if (nextScore < minScore) {
                         minScore = nextScore;
                     }
 
                     // increment the grades based on the ranges
                     if (nextScore >= LETTER_GRADE_A) {
                         aGrade++;
                     } else if (nextScore >= LETTER_GRADE_B) {
                         bGrade++;
                     } else if (nextScore >= cGrade) {
                         cGrade++;
                     } else if (nextScore >= dGrade) {
                         dGrade++;
                     } else {
                         fGrade++;
                     }
                 }
             } else {
                 // skip any invalid inputs
                 fileReader.next();
             }
         }
 
         // calculate the average value
         if (validScores > 0) {
             average = sum / validScores;
         }
         
     } // end of firstPass
 
     /*
      * a for loop is run that will make the summation for psd
      * in this method
      */
     public static void secondPass(Scanner fileReader, int validScores,
                                   int average, int nextScore, double psd) {
         // starts the process of calculating the psd
         for (int i = 0; i < validScores && fileReader.hasNext(); ) {
             if (fileReader.hasNextInt()) {
                 nextScore = fileReader.nextInt();
 
                 /*
                  * first, validate the score range (0-100) and then calculate
                  * the squared differences
                  */
                 if (nextScore >= LETTER_GRADE_F && nextScore <= 100) {
                     psd += Math.pow(nextScore - average, 2);
                     // only increment i when a valid score is processed
                     i++;
                 }
             } else {
                 // skip any invalid inputs
                 fileReader.next();
             }
         }
 
         // calculate the population standard deviation
         if (validScores > 0) {
             psd = Math.sqrt(psd / validScores);
         }
 
     } // end of secondPass
 
     public static void displayOutputMessage() {
         String outputMessage;
         int totalScores = 0, validScores = 0,
             aGrade = 0, bGrade = 0, cGrade = 0, dGrade = 0, fGrade = 0,
             minScore = Integer.MAX_VALUE, maxScore = Integer.MIN_VALUE, nextScore;
         double average = 0.0, psd = 0.0;
 
         outputMessage = "Exam Statistics" +
                         "\nTotal number of (given) scores: " + totalScores +
                         "\nTotal number of valid scores: " + validScores +
                         "\nConsidering only the valid given scores:\n" +
                         String.format("Average score (of valid scores:) " +
                                       "%.2f\n", average) +
                         String.format("Population standard deviation of " +
                                       " the valid scores: %.2f\n", psd) +
                         String.format("# of A, 90-100: %d %.2f%%\n",
                                       aGrade, (double) aGrade / validScores * 100) +
                         String.format("# of B, 80-89: %d %.2f%%\n",
                                       bGrade, (double) bGrade / validScores * 100) +
                         String.format("# of C, 65-79: %d %.2f%%\n",
                                       cGrade, (double) cGrade / validScores * 100) +
                         String.format("# of D, 50-64: %d %.2f%%\n",
                                       dGrade, (double) dGrade / validScores * 100) +
                         String.format("# of F, 00-49: %d %.2f%%\n",
                                       fGrade, (double) fGrade / validScores * 100) +
                         "Minimum score: " + minScore + "\n" +
                         "Maximum score: " + maxScore + "\n";
 
         System.out.println(outputMessage);
     }
 
 } // end of ExamStatistics