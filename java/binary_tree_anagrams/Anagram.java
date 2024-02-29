/**
 * Joon Yeo
 * G00953081
 * CS310 - 005, Spring 2019
 * February 25, 2019
 * Project 1
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Anagram {

	public static void main(String[] args) {
		
		
		Scanner input = null;
		FileReader fr = null;
		BufferedReader br = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		try
		{
			fr = new FileReader(args[0]);		// FileReader in order to read the input file from command line argument
			br = new BufferedReader(fr);		
			fw = new FileWriter(args[1]);		// FileWriter in order to write to the output file given from the command line
			bw = new BufferedWriter(fw);		// BufferedWriter needed to write to file from the visit() method within AnaData

			input = new Scanner(br);
			
		}
		catch (IOException e)	// throws an exception if the file cannot be opened and then exits the program
		{
			System.out.println("Unable to open file: " + args[0]);
		}
		
		
		BinTree<AnaData> theBinaryTree = new BinTree<AnaData>();	// creating an instance of a binary tree to start adding nodes
		
		while(input.hasNextLine())									// Will read the file until the end of the file is reached
		{
			String initial = input.next();							// reading the file word by word
			String original = AnaData.cleanStringOriginal(initial);	// second clean to retain the original word to add into the GList of that specific anagram key
			String clean = AnaData.cleanString(initial);			// calling clean string method to change letters to lowercase and remove punctuation
			AnaData currentkey = new AnaData(clean, bw);			// creating an instance of AnaData using the key from original word
			theBinaryTree.insert(currentkey, original, theBinaryTree.getRoot());		// inserting all the key-nodes into the binary tree 
		}
		
		theBinaryTree.inorderTraverse(theBinaryTree.getRoot());		// calling the traversal method of the binary tree and writing to the output file

		try {
			br.close();		// closing files
			bw.close();		
		} catch (IOException e) {
			System.out.println("Unable to close files.");
		}
		
	}

}
