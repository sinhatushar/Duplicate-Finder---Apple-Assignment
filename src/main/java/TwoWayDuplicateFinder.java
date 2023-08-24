import java.io.*;
import java.util.logging.Logger;

/**
 *  Finds duplicates in 2 large sorted files
 */
public class TwoWayDuplicateFinder {

    private static final Logger logger = Logger.getLogger("main.java");

    /**
     * find duplicates
     *
     * @param file1SortedPath file1SortedPath
     * @param file2SortedPath file2SortedPath
     * @throws IOException java.io. i o exception
     */
    public static void findDuplicates(String file1SortedPath, String file2SortedPath) throws IOException {

        BufferedReaderPro brpForFile1 = new BufferedReaderPro(
                                            new BufferedReader(
                                                new InputStreamReader(
                                                    new FileInputStream(file1SortedPath))));

        BufferedReaderPro brpForFile2 = new BufferedReaderPro(
                                            new BufferedReader(
                                                new InputStreamReader(
                                                    new FileInputStream(file2SortedPath))));

        String duplicateFilePath   = System.getProperty("user.dir") + "/files/duplicates/" + "duplicates.txt";
        File duplicateFile         = new File(duplicateFilePath);

        BufferedWriter fbw = new BufferedWriter(
                                new OutputStreamWriter(
                                    new FileOutputStream(duplicateFile, true)));

        long duplicateCount = 0;
        while (!brpForFile1.empty() && !brpForFile2.empty()){

            Long currentLineInFile1 = Long.parseLong(brpForFile1.peek());
            Long currentLineInFile2 = Long.parseLong(brpForFile2.peek());

            if(currentLineInFile1.equals(currentLineInFile2)){
                fbw.write(currentLineInFile1.toString());
                duplicateCount++;
                fbw.newLine();
                brpForFile1.poll();
                brpForFile2.poll();
            }
            else if(currentLineInFile1 < currentLineInFile2){
                brpForFile1.poll();
            }
            else {
                brpForFile2.poll();
            }
        }

        fbw.close();
        brpForFile1.close();
        brpForFile2.close();

        logger.info("The number of duplicates in file1 and file2 is : " + duplicateCount);
        logger.info("Duplicates found and stored in /files/duplicates/duplicates.txt");
    }
}