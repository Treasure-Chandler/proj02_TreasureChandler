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
 
     // simple boolean to check if this is the first file being processed
     private static boolean checkExamStatFile = false;
 
     /**
      * 
      * @param args entered values
      */
     // this line is used to surpress any warnings for unused variables
     @SuppressWarnings("unused")
     public static void main(String[] args) throws IOException {
         // variables declaration
         int totalScores = 0, validScores = 0,
             aGrade = 0, bGrade = 0, cGrade = 0, dGrade = 0, fGrade = 0;
         int minScore = Integer.MAX_VALUE, maxScore = Integer.MIN_VALUE;
         double sum = 0, average = 0, psd = 0;
         String outputMessage, inputFileName;
 
         // for console reading
         Scanner consoleReader = new Scanner(System.in);
 
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
 
         Scanner fileScanner = null;
         // checks if the file the user inputted exists
         if (fileInput.exists()) {
             /*
              * instantiates fileScanner as a scanner (using fileInput
              * as the input)
              */
             fileScanner = new Scanner(fileInput);
             while (fileScanner.hasNextLine()) {
                 String readLine = fileScanner.nextLine();
                 Scanner readLineScanner = new Scanner(readLine);
                 while (readLineScanner.hasNext()) {
                     // processes all the scores in the file
                     if (readLineScanner.hasNextInt()) {
                         int nextScore = readLineScanner.nextInt();
 
                         // checks for any valid scores
                         if (nextScore >= 0 && nextScore <= 100) {
                             totalScores++;
                             validScores++;
                             sum += nextScore;
 
                             // update the min and max scores
                             if (nextScore < minScore) {
                                 minScore = nextScore;
                             }
                             if (nextScore > maxScore) {
                                 maxScore = nextScore;
                             }
 
                             // update the grade computations
                             if (nextScore >= LETTER_GRADE_A) {
                                 aGrade++;
                             } else if (nextScore >= LETTER_GRADE_B) {
                                 bGrade++;
                             } else if (nextScore >= LETTER_GRADE_C) {
                                 cGrade++;
                             } else if (nextScore >= LETTER_GRADE_D) {
                                 dGrade++;
                             } else {
                                 fGrade++;
                             }
                         }
                     } else {
                         // skip any invalid inputs
                         String nonInt = readLineScanner.next();
                     }
                 }
                 readLineScanner.close();
             }
             fileScanner.close();
         }
 
         /*
          * calculating the average and population standard deviation (if there
          * are valid scores)
          */
         if (validScores > 0) {
             average = sum / validScores;
 
             // re-opening fileReader in order to execute the second pass
             fileScanner = new Scanner(new File(inputFileName));
 
             // executing secondPass, then closing fileReader again
             // starts the process of calculating the psd
             while (fileScanner.hasNextLine()) {
                 String line = fileScanner.nextLine();
                 Scanner lineScanner = new Scanner(line);
                 while (lineScanner.hasNext()) {
                     if (lineScanner.hasNextInt()) {
                         int nextScore = lineScanner.nextInt();
                         if (nextScore >= 0 && nextScore <= 100) {
                             psd += Math.pow(nextScore - average, 2);
                         }
                     }
                 }
                 lineScanner.close();
             }
             // calculate the population standard deviation
             psd = Math.sqrt(psd / validScores);
             fileScanner.close();
         }
 
         // compose the outpust message to be displayed in the console
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
 
         System.out.println(outputMessage);
 
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
                 fileOutput = inputFileName.replace(".txt", "ExamStatFile.txt");
             }
             
             PrintWriter fileWriter = new PrintWriter(new FileWriter(fileOutput));
             fileWriter.println(outputMessage);
             fileWriter.close();
         }
 
     } // end of main()
 
 } // end of ExamStatistics