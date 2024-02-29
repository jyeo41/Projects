/**
 * Joon Yeo
 * G00953081
 * CS310 - 005, Spring 2019
 * February 25, 2019
 * Project 1
 */

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

public class AnaData implements Visitable<AnaData> {

	private String key;											// the alphabetized string holding the key to the anagrams
	private GList<String> list = new GList<String>();			// this list will hold the corresponding anagrams according to the key
	private BufferedWriter outfile;								// visit will write to the output file
	
	public AnaData (String val, BufferedWriter outfile)			// constructor to give the key value to AnaData object
	{
		key = val;					// the anagram key
		this.outfile = outfile;		// buffered writer field in order to write to the output file
	}
	
	public void visit()			// method for checking the length of each GList at the current node
	{							
		if(list.listLength() > 1)	// if the length of the list is > 1, so if the anagram key has more than 1 word
		{
			try {
				outfile.write(list.toString() + "\n");	// write all the words found in the list to the output file

			} catch (IOException e) {
				System.out.println("Unable to write to output file.");
			}
		}
	}
	
	@Override
	public int compareTo(AnaData value)				// compareTo method in order to alphabetize correctly in the Binary Tree
	{
		return key.compareTo(value.key);
	}
	
	public static String cleanString(String word)
	{
		word = word.toLowerCase();					// changing string to all lowercase
		word = word.replaceAll("[^A-Za-z]", "");	// cleaning out punctuation
		char tempArray[] = word.toCharArray();		// putting the read string from the text file into a char array
		Arrays.sort(tempArray);						// sorting the characters to be alphabetical
		return new String(tempArray);				// return the completely cleaned string
	}
	
	public static String cleanStringOriginal(String word)
	{
		word = word.toLowerCase();					// changing string to all lowercase
		word = word.replaceAll("[^A-Za-z]", "");	// cleaning out punctuation
		char tempArray[] = word.toCharArray();		// putting the read string from the text file into a char array
		return new String(tempArray);				// return the original word without sorting, to be used later
	}
	
	@Override
	public String toString()	// toString method to print out the keys of AnaData
	{
		return "Key: " + key + "      list: " + list.toString();
	}
	
	public GList<String> getList()	// getter for list
	{
		return list;
	}
}
