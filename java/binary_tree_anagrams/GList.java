/**
 * Joon Yeo
 * G00953081
 * CS310 - 005, Spring 2019
 * February 25, 2019
 * Project 1
 */

public class GList <T> 
{
	private GNode<T> head;
	private GNode<T> cursor;

	private class GNode<T>		// Generic Node class to use for the linked list
	{
		T data;					// data field to hold the words
		GNode<T> next;			// pointer field to be able to link up the linked list
		
		public GNode (T val)	// constructor to be able to create a node
		{
			data = val;
		}
	}
	
	public void addFirst(T val)		// adding the original word into the GList according to the key
	{
		GNode<T> p = new GNode<T>(val);		// create a new node with the corresponding data
		p.next = head;						// maintaining correct order of operations for linking the pointers
		head = p;							// now the new node is the head of the list
	}
	
	public void startTraversal()	// Starting the traversal of the linked list
	{
		cursor = head;
	}
	
	public boolean hasNext()		// used for the while gate to check until the end of the list
	{	
		return cursor != null;
	}
	
	public T getNext()
	{
		T returnval = cursor.data;
		cursor = cursor.next;
		return returnval;
	}
	
	public boolean duplicate (String original)		// method to check if the duplicate word already exists inside a key
	{												// if the word has already been added to the list, do not add to the list again
		startTraversal();
		while(hasNext())
		{
			if(cursor.data.equals(original))		// check to see if the data already exists
			{
				return true;
			}
			cursor = cursor.next;
		}
		return false;
	}
	
	public int listLength()							// a method to check the length of an Anagram list,
	{												// used with the visit() method in the AnaData class for writing to outfile
		int counter = 0;
		startTraversal();
		
		while(cursor != null)
		{
			counter++;
			cursor = cursor.next;
		}
		return counter;
	}
	
	@Override
	public String toString()						// a method to write the output to console to check if the BinaryTree
	{												// has the proper nodes and contents within it.
		startTraversal();							// used to write to the output file 
		String currentList = "";
		while(cursor != null)
		{
			currentList += cursor.data + " ";
			cursor = cursor.next;
		}
		return currentList;
		
	}

}
