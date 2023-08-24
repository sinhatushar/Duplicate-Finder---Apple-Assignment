# Duplicate Finder For 2 Very Large Files

## Requirements :

>
> 1. Java 8 +
> 2. Maven
>

## Execution Steps :

>
>1. Import Maven Dependencies. Run FindDuplicates.
>
>2. The program uses BufferedReader and BufferedWriter to read and write one
>line at a time from the large files.
>
>3. As the first step, the program by default generates 2 files each with 5e7 random numbers between 9_000_000_000_000_000_000L and 9_000_000_000_090_000_000L. The number of numbers and the maximum number and minimum number generated can be changed in the Util class. The files are generated in /files/input/ and are named as file1.txt and file2.txt . 
>
>4. The program assumes 128MB of memory available for sorting purposes. The amount of memory that can be used by the program can also be changed from Util class.
>
>5. The program creates chunks of each file and sorts them separately. These are stored in /files/sortedInputFiles/sortedChunks/. The number of chunks depends upon the size of the generated files and the amount of memory available for sorting.
>
>6. In the next step, the chunks for one file are merged using a heap. The algorithm is same as merging K sorted lists. Duplicate numbers in the file are also removed and the output is stored in 
/files/sortedInputFiles/sortedOutput/. The ouput is stored in single file. 
>
>7. The program finally finds duplicates in file1 and file2 using the merge step of merge sort and stores it in /files/duplicates/duplicates.txt

