import java.io.IOException;
import java.util.logging.Logger;

/**
 *  find duplicates in two large files which don't fit into memory
 */
public class FindDuplicates {

    private static final Logger logger = Logger.getLogger("main.java");

    /**
     * @param args command line arguments
     * @throws IOException java.io. i o exception
     */
    public static void main(String[] args) throws IOException {

        //Cleaning the output for previous runs
        Util.cleanDirectories();

        // Path of input files
        String f1Path = System.getProperty("user.dir") + "/files/input/file1.txt";
        String f2Path = System.getProperty("user.dir") + "/files/input/file2.txt";

        // Generating input files
        Util.generateFilesWithRandomNumbers(f1Path);
        Util.generateFilesWithRandomNumbers(f2Path);

        //Sort both the files using external merge sort and save them in /files/sortedInputFiles/sortedOutput
        ExternalMergeSort.sort(f1Path);
        ExternalMergeSort.sort(f2Path);

        // Path of input files
        String f1SortedPath = System.getProperty("user.dir") + "/files/sortedInputFiles/sortedOutput/file1.txt";
        String f2SortedPath = System.getProperty("user.dir") + "/files/sortedInputFiles/sortedOutput/file2.txt";

        //Fine duplicates using merge step of merge sort and save them in /files/duplicates/duplicates.txt
        TwoWayDuplicateFinder.findDuplicates(f1SortedPath, f2SortedPath);

        logger.info("Program Finished Execution");
    }
}