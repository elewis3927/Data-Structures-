package sortCompare;
import java.io.*;
import java.util.*;

/**
 * Sorts the data on-disk, by sorting the data in small chunks and then merging
 * the data into one larger chunk
 * 
 * @author Ethan Lewis
 * @date 9/22/2017
 */
public class OnDiskSort{	
	/**
	 * Creates a new sorter for sorting string data on disk.  The sorter operates by reading
	 * in maxSize worth of data elements (in this case, Strings) and then sorts them using
	 * the provided sorter.  It does this chunk by chunk for all of the data, at each stage
	 * writing the sorted data to temporary files in workingDirectory.  Finally, the sorted files
	 * are merged together (in pairs) until there is a single sorted file.  The final output of this
	 * sorting should be in outputFile
	 * 
	 * @param maxSize the maximum number of items to put in a chunk
	 * @param workingDirectory the directory where any temporary files created during sorting should be placed
	 * @param sorter the sorter to use to sort the chunks in memory
	 */
	// private int's used in methods below
	private int size;
	private Sorter sorter1; 
	
	
	
	public OnDiskSort(int maxSize, File workingDirectory, Sorter<String> sorter){
		// TODO: put some code here
		//Clear out the directory here, when restarting the program
		clearOutDirectory(workingDirectory, ".tempFile");
		
		// Declare the explicit sorter that will be used and a variable for maxSize
		sorter1 = sorter;
		size = maxSize;
		
		
		// create directory if it doesn't exist
		if( !workingDirectory.exists() ){
			workingDirectory.mkdir();
		}
	}
	
	/**
	 * Remove all files that that end with fileEnding in workingDirectory
	 * 
	 * If you name all of your temporary files with the same file ending, for example ".temp_sorted" 
	 * then it's easy to clean them up using this method
	 * 
	 * @param workingDirectory the directory to clear
	 * @param fileEnding clear only those files with fileEnding
	 */
	private void clearOutDirectory(File workingDirectory, String fileEnding){
		for( File file: workingDirectory.listFiles() ){
			if( file.getName().endsWith(fileEnding) ){
				file.delete();
			}
		}
	}
		
	/**
	 * Write the data in dataToWrite to outfile one String per line
	 * 
	 * @param outfile the output file
	 * @param dataToWrite the data to write out
	 */
	private void writeToDisk(File outfile, ArrayList<String> dataToWrite){
		try{
			PrintWriter out = new PrintWriter(new FileOutputStream(outfile));
			
			for( String s: dataToWrite ){
				out.println(s);
			}
			
			out.close();
		}catch(IOException e){
			throw new RuntimeException(e.toString());
		}
	}
	
	/**
	 * Copy data from fromFile to toFile
	 * 
	 * @param fromFile the file to be copied from
	 * @param toFile the destination file to be copied to
	 */
	private void copyFile(File fromFile, File toFile){
		try{
			BufferedReader in = new BufferedReader(new FileReader(fromFile));
			PrintWriter out = new PrintWriter(new FileOutputStream(toFile));
			
			String line = in.readLine();
			
			while( line != null ){
				out.println(line);
				line = in.readLine();
			}
			
			out.close();
			in.close();
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	
	/** 
	 * Sort the data in data using an on-disk version of sorting
	 * 
	 * @param dataReader an Iterator for the data to be sorted
	 * @param outputFile the destination for the final sorted data
	 */
	public void sort(Iterator<String> dataReader, File outputFile){
		// checked is an counter used to increment  # of words added to each file
		int checked = 0;
		// fileName is added to the string that names the temp files
		int fileName = 0;
		//moreChunks is an ArrayList of files that we add too after we sorted the 10 words
		ArrayList<File> moreChunks = new ArrayList<File>();
		
		// TODO: put some code here
	// Loops make sure there is another word in file
	while(dataReader.hasNext()){
		// chunk is the ArrayList of strings that has 10 words
		ArrayList<String> chunk = new ArrayList<String>();
			// while parameters make sure we don't go over size limit and dataReader has a next
			while(checked < size && dataReader.hasNext()){
				// add the word to chunk array list
				chunk.add(dataReader.next());
				// increment checked
				checked++;
			}
			//Once 10 strings have been added, created a new file, using fileName to name file
			File file = new File("sorting_run//"+fileName +".tempFile");
			//call sorter to sort the chunk of 10 words
			sorter1.sort(chunk);
			// writer the sorted chunk the the new file created
			writeToDisk(file, chunk);
			// add the file to arraylist of files call moreChunks
			moreChunks.add(file);
			// increment file name counter
			fileName++;
			// reset size variable for next use in for loop
			checked = 0;	
		}
		// finally call mergedFiles with moreChunks and outputFile
		mergeFiles(moreChunks, outputFile);
		
	}
	
	/**
	 * Merges all the Files in sortedFiles into one sorted file, whose destination is outputFile.
	 * 
	 * @pre All of the files in sortedFiles contained data that is sorted
	 * @param sortedFiles a list of files containing sorted data
	 * @param outputFile the destination file for the final sorted data
	 */
	protected void mergeFiles(ArrayList<File> sortedFiles, File outputFile){
		// TODO: put some code here
			// Create a new file merged 
			File merged = new File("sorting_run//merged.tempfile");
			//iterate through the list of sorted files, "moreChunks" from sort method to be specific
			for (int i = 1; i < sortedFiles.size(); i++){
				// have to check first case to give merge a value, which is why its last in parameter
				// of the merge call in the if statement
				if (i == 1){
					// can't forget about the 0th index, which is why is get(i -1)
					// then merge it with the first index to give merge a value (list of strings)
					merge(sortedFiles.get(i - 1), sortedFiles.get(i), merged);
				}else{
					// use recursion to have merged being merged with the sorted values
					// to become merged after the two files have been merged
					merge(merged, sortedFiles.get(i), merged);
				}
				
			}
			// copy the created merged file to the output file
			copyFile(merged, outputFile);
	}
	
	/**
	 * Given two files containing sorted strings, one string per line, merge them into
	 * one sorted file
	 * 
	 * @param file1 file containing sorted strings, one per line
	 * @param file2 file containing sorted strings, one per line
	 * @param outFile destination file for the results of merging the two files
	 */
	protected void merge(File file1, File file2, File outFile){
		// create a temporary Array List, if you don't, its impossible to write and read
		// from and too a file at the same time
		ArrayList<String> temp = new ArrayList<String>();
		// create to strings that hold the values of two strings at a time
		String word1;
		String word2;
		// Eclipse implemented the try method, make sure there's no RunTimeException
		try {
			// Create two buffered readers, one for each word being from each file
			BufferedReader buffer1 = new BufferedReader(new FileReader(file1));
			BufferedReader buffer2 = new BufferedReader(new FileReader(file2));
			// word 1 corresponds to buffer 1, and same for word2 -> buffer 2
			word1 = buffer1.readLine();
			word2 = buffer2.readLine();
			
			// makes sure that the words are not null
			while (!(word1 == null) && !(word2 == null)){
				// use compareTo to compare strings
				// if word1 is "lower" than word2, add it to the temporary Array List
				if (word1.compareTo(word2) < 1){
					// adds word1 to temp
					temp.add(word1);	
					// get the next value for word1
					word1 = buffer1.readLine();
				// else if word2 is "lower" add it to temp
				}else{
					// adds word2 to temp
					temp.add(word2);
					// gets the next string for word2
					word2 = buffer2.readLine();
				}
			}
			// once word2 or word1 if null, the method will hit these statements
			// if word1 == null, then word too will be added until it's value is null
			if (word1 == null){
				// making sure word2 isn't null
				while (word2 != null){
						// if its not, add it to temp
						temp.add(word2);
						// get the next vale for word2
						word2 = buffer2.readLine();
					}
				// if word2 is null, then do the same for word1
				}else{
					// while word1 isn't null
					while (word1 != null){
						// add it to the temporary array list
						temp.add(word1);
						// get the next string for word1
						word1 = buffer1.readLine();
					}
					
				}
		
		// close both buffers to open print writers
		buffer1.close();
		buffer2.close();
		
		// Must declare print writer after buffer is close
		// That is why accumulator was made 
		// Create a new PrintWriter called writer that writes to the outFile
		PrintWriter writer = new PrintWriter(new FileWriter(outFile));
		// iterate through temp and add write the strings to the outFile
		for (int i = 0; i < temp.size(); i++){
			writer.println(temp.get(i));
		}
		// close the writer
		writer.close();
		// Close try and catch method with IOException 
		} catch (IOException e) {
			throw new RuntimeException (e);
		}
		// TODO: put some code here
		
	}
	
	/**
	 * Create a sorter that does a merge sort in memory
	 * Create a diskSorter to do external merges
	 * Use subdirectory "sorting_run" of your project as the working directory
	 * Create a word scanner to read King's "I have a dream" speech.
	 * Sort all the words of the speech and put them in file data.sorted
	 * @param args -- not used!
	 */
	public static void main(String[] args){
		MergeSort<String> sorter = new MergeSort<String>();
		OnDiskSort diskSorter = new OnDiskSort(10, new File("sorting_run"), 
				sorter);
		
		WordScanner scanner = new WordScanner(new File("sorting_run//Ihaveadream.txt"));
		
		System.out.println("running");		
		diskSorter.sort(scanner, new File("sorting_run//data.sorted"));
		System.out.println("done");
	}

}
