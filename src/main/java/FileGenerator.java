import java.io.*;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.LongStream;

/**
 *  file generator
 */
public class FileGenerator {

    private static final Logger logger = Logger.getLogger("main.java");

    /**
     * generate files with random numbers
     *
     * @param numberOfNumbers numberOfNumbers
     * @param minimum minimum
     * @param maximum maximum
     * @param path path
     */
    public static void generateFilesWithRandomNumbers (long numberOfNumbers, long minimum, long maximum, String path) throws FileNotFoundException {

//        System.out.println(numberOfNumbers + path + minimum + maximum);
        Random random    = new Random();
        LongStream longs = random.longs(numberOfNumbers, minimum, maximum);


        BufferedWriter fbw = new BufferedWriter(
                                new OutputStreamWriter(
                                    new FileOutputStream(path, true)));

        longs.forEach(num -> {
            try {
                fbw.write(Long.toString(num));
                fbw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        logger.info("File with " + numberOfNumbers + " random numbers between " + minimum + " - " + (maximum-1) + " created at following path : " + path);
    }
}