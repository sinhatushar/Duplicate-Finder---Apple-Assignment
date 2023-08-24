import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *  External Merge Sort Implementation.
 *  Can sort large files which don't fit into memory by
 *  breaking them into chunks of file and sorting them
 *  individually and thereafter merging them and storing
 *  them on disk.
 */
public class ExternalMergeSort {

    private static final Logger logger = Logger.getLogger("main.java");

    private static long maximumMemoryAvailableForThisProgram;
    private static long sizeOfOneChunkInBytes;
    private static int numberOfCalls = 0;
    private static File inputFile;

    /**
     * find available memory
     *
     * @return {@link long}
     */
    private static long findAvailableMemory(){
        System.gc();
        Runtime runtime = Runtime.getRuntime();

        long availableMemoryOnSystem = runtime.maxMemory() - (runtime.totalMemory() - runtime.freeMemory());
        logger.info("Memory available on this system is : " + availableMemoryOnSystem + " bytes");

        return availableMemoryOnSystem;
    }


    /**
     * find size of one chunk in bytes
     *
     * @param sizeOfGivenFile sizeOfGivenFile
     * @return {@link long}
     */
    private static long findSizeOfOneChunkInBytes(long sizeOfGivenFile){

        //We are using maximum 50% of available memory for this program for sorting one chunk.
        //We are leaving 50% of available memory for other work that the program needs to do
        long maximumMemoryAllowedForSortingAChunk = (int) ((maximumMemoryAvailableForThisProgram)/2);

        //While sizeOfOneChunk is > maximumMemoryAllowedForSortingAChunk, increase number of files.
        int numberOfTemporaryFiles = 1;
        while(sizeOfGivenFile/numberOfTemporaryFiles > maximumMemoryAllowedForSortingAChunk) {
            numberOfTemporaryFiles++;
        }

        sizeOfOneChunkInBytes = sizeOfGivenFile/numberOfTemporaryFiles;

        logger.info("Maximum amount of memory made available to this program is : " + maximumMemoryAvailableForThisProgram + " bytes");
        logger.info("Maximum amount of memory to be used to sort one chunk for " + inputFile.getName() + " is : " + maximumMemoryAllowedForSortingAChunk + " bytes");
        logger.info("Number of chunks to be created for " + inputFile.getName() + " is : " + numberOfTemporaryFiles);
        logger.info("Size of one chunk in bytes for " + inputFile.getName() + " is : " + sizeOfOneChunkInBytes + " bytes");

        return sizeOfOneChunkInBytes;
    }


    /**
     * sort and save chunk
     *
     * @param chunkContent chunkContent
     * @return {@link File}
     * @see File
     * @throws IOException java.io. i o exception
     */
    public static File sortAndSaveChunk(List<String> chunkContent) throws IOException {

        numberOfCalls++;

        // Sort the numbers this chunk using parallel streams to make sorting faster
        chunkContent = chunkContent.parallelStream().sorted(Comparator.naturalOrder()).collect(Collectors.toCollection(ArrayList<String>::new));
//        logger.info(chunkContent.toString());

        File currentChunk = new File(System.getProperty("user.dir") + "/files/sortedInputFiles/sortedChunks/C" + numberOfCalls + "_" + inputFile.getName());
        currentChunk.createNewFile();

        int numCount = 0;
        OutputStream out = new FileOutputStream(currentChunk);
        try (BufferedWriter fbw = new BufferedWriter(new OutputStreamWriter(out)))
        {
            Iterator<String> iterator = chunkContent.iterator();

            // Only for zeroth index as this has no previous line to compare
            String previousLine = null;
            if (iterator.hasNext()) {
                previousLine = iterator.next();
                fbw.write(previousLine);
                numCount++;
                fbw.newLine();
            }

            // Iterate from the first index till end and remove duplicates in this chunk
            while (iterator.hasNext()) {
                String currentLine = iterator.next();

                if (currentLine.compareTo(previousLine) != 0) {
                    fbw.write(currentLine);
                    fbw.newLine();
                    numCount++;
                    previousLine = currentLine;
                }
            }
        }

        String chunkName = "C" + numberOfCalls + "_" + inputFile.getName();
        logger.info("One chunk for " + inputFile.getName() + " named " + chunkName + " has been sorted and saved on disk");
        logger.info("The number of numbers in this chunk named " + chunkName + " is : " + numCount);
        logger.info("The size of this chunk named " + chunkName + " is : " + currentChunk.length() + " bytes");

        return currentChunk;
    }


    /**
     * divide in chunk and sort
     *
     * @param fbr fbr
     * @return {@link List}
     * @see List
     * @see File
     * @throws IOException java.io. i o exception
     */
    private static List<File> divideInChunksAndSort(BufferedReader fbr) throws IOException {

        // List of chunks the input file is divided in
        List<File> chunks = new ArrayList<>();

        try {
            List<String> currentChunkContent = new ArrayList<>();
            String currentLine = "";
            try {
                while (currentLine != null) {
                    long currentChunkSize = 0; // in bytes

                    while ((currentChunkSize < sizeOfOneChunkInBytes) && ((currentLine = fbr.readLine()) != null)) {
                        currentChunkContent.add(currentLine);
                        currentChunkSize += (1+currentLine.length());     // Approximation for the amount of size required to store a string on disk.
                    }

                    if(currentChunkContent.size() > 0){
                        chunks.add(sortAndSaveChunk(currentChunkContent));
                        currentChunkContent.clear();
//                        logger.info("The number of numbers in current chunk for " + file.getName() + " is : " + currentChunkContent.size());
                    }
                }
            } catch (EOFException oef) {
                if (currentChunkContent.size() > 0) {
                    chunks.add(sortAndSaveChunk(currentChunkContent));

//                    logger.info("The number of numbers in current chunk for " + file.getName() + " is : " + currentChunkContent.size());

                    currentChunkContent.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            fbr.close();
        }
        return chunks;
    }


    /**
     * merge sorted chunks
     *
     * @param chunks chunks
     * @param outputFile outputFile
     * @throws IOException java.io. i o exception
     */
    private static void mergeSortedChunks(List<File> chunks, File outputFile) throws IOException {

//        System.out.println(outputFile.getName());

        //A list of all the chunks is created here.
        ArrayList<BufferedReaderPro> brps = new ArrayList<>();
        for (File f : chunks) {
            BufferedReaderPro brp = new BufferedReaderPro(new BufferedReader(new InputStreamReader(new FileInputStream(f))));

            brps.add(brp);
        }

//        System.out.println("brps size is " + brps.size());

        BufferedWriter fbw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outputFile, true)));

        PriorityQueue<BufferedReaderPro> priorityQueue = new PriorityQueue<>(
                Comparator.comparingLong((BufferedReaderPro brp) -> Long.parseLong(brp.peek()))
        );

        for (BufferedReaderPro brp : brps) {
            if (!brp.empty()) {
                priorityQueue.add(brp);
            }
        }
//        System.out.println(priorityQueue.size());

        int numCount = 0;
        try{
            String previousLine = null;
            if (priorityQueue.size() > 0) {

                BufferedReaderPro brp = priorityQueue.poll();
                previousLine = brp.poll();

//                System.out.println(previousLine);
                fbw.write(previousLine);
                fbw.newLine();
                numCount++;

                if (brp.empty()) {
                    brp.close();
                } else {
                    priorityQueue.add(brp); // add it back
                }

            }
            while (priorityQueue.size() > 0) {

                BufferedReaderPro brp = priorityQueue.poll();
                String currentLine = brp.poll();

//                System.out.println(currentLine);

                // Skipping duplicate lines
                if (currentLine.compareTo(previousLine) != 0) {
                    fbw.write(currentLine);
                    numCount++;
                    fbw.newLine();
                    previousLine = currentLine;
                }
                if (brp.empty()) {
                    brp.close();
                }
                else {
                    priorityQueue.add(brp); // add it back
                }
            }
        } finally {
            for (BufferedReaderPro brp : priorityQueue) {
                brp.close();
            }
        }
        fbw.close();

        logger.info("The duplicates in " + inputFile.getName() + " have been removed and the file has been sorted and stored in /files/sortedOutput/" + inputFile.getName());
        logger.info("The number of numbers after removing duplicates is " + numCount);
    }


    /**
     * External Merge Sort
     *
     * @param inputFilePath inputFilePath
     * @throws IOException java.io. i o exception
     */
    public static void sort(String inputFilePath) throws IOException {

        logger.info("-------------------------------------------------------------");
        inputFile     = new File(inputFilePath);
        numberOfCalls = 0;
        long availableMemoryInSystem = findAvailableMemory();

        // We don't let the program use more than 128MB(by default) of memory even if it is available. This is done for demo purposes.
        maximumMemoryAvailableForThisProgram = Math.min(availableMemoryInSystem, Util.maxMemoryToBeUsedByThisProgram );

//        logger.info("The memory that is made available for this program is : " + maximumMemoryAvailableForThisProgram + " bytes");


        long sizeOfGivenFile    = inputFile.length();
        // Finding size of each chunk based on number of files we are dividing them into
        sizeOfOneChunkInBytes   = findSizeOfOneChunkInBytes(sizeOfGivenFile);


        String outputFilePath   = System.getProperty("user.dir") + "/files/sortedInputFiles/sortedOutput/" + inputFile.getName();
        File outputFile         = new File(outputFilePath);


        // Dividing the huge file into chunks, sorting and saving the chunks into different files in /files/sortedInputFiles/sortedChunks
        BufferedReader fbr      = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
        List<File> chunks       = divideInChunksAndSort(fbr);

        logger.info("Sorted chunks created and saved for " + inputFile.getName());


        // Merging all the sorted chunks
        mergeSortedChunks(chunks, outputFile);

        logger.info("-------------------------------------------------------------");
    }
}