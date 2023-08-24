import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 *  Utility class
 */
public class Util
{
    private static final Logger logger = Logger.getLogger("main.java");

    //Change this number to change the amount of memory available for sorting
    public static long maxMemoryToBeUsedByThisProgram = 128*1024*1024;

    /**
     * generate files with random numbers
     *
     * @param path path
     * @throws IOException java.io. i o exception
     */
    public static void generateFilesWithRandomNumbers(String path) throws IOException {

        // Generates file with size of approximately 1GB
        // Each long of the order of 1e18 takes around 20 bytes of storage
        // Change value of numberOfNumbers to change the size of the file generated
        long numberOfNumbers = 50_000_000;
        long minimumNumber   = 9_000_000_000_000_000_000L;
        long maximumNumber   = 9_000_000_000_090_000_000L;
        FileGenerator.generateFilesWithRandomNumbers(numberOfNumbers, minimumNumber, maximumNumber, path);
    }

    /**
     * clean directories
     *
     * @throws IOException java.io. i o exception
     */
    public static void cleanDirectories() throws IOException {
        FileUtils.cleanDirectory(new File(System.getProperty("user.dir") + "/files/input"));
        FileUtils.cleanDirectory(new File(System.getProperty("user.dir") + "/files/duplicates"));
        FileUtils.cleanDirectory(new File(System.getProperty("user.dir") + "/files/sortedInputFiles/sortedChunks"));
        FileUtils.cleanDirectory(new File(System.getProperty("user.dir") + "/files/sortedInputFiles/sortedOutput"));
        FileUtils.cleanDirectory(new File(System.getProperty("user.dir") + "/files/duplicates"));
        logger.info("All the input/output for previous runs have been cleared");
    }
}