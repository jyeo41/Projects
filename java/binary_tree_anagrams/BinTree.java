/**
 * Joon Yeo
 * G00953081
 * CS310 - 005, Spring 2019
 * February 25, 2019
 * Project 1
 */

public class BinTree <T extends Visitable<T>>
{
	private BinTreeNode<T> root;		// necessary root of the binary tree
	
	private class BinTreeNode<T>		// Nodes to hold the left and right children of the parent nodes
	{
		T data;
		BinTreeNode<T> leftChild;
		BinTreeNode<T> rightChild;
			
		public BinTreeNode(T val)		// constructor to create a Binary Tree Node
		{
			data = val;
		}
	}
	
	/*public T search(T target)
	{
		if(root == null)
		{
			return null;
		}
		
		return search(target, root);
	}
	
	private T search(T target, BinTreeNode<T> p)
	{
		if(p == null)
		{
			return null;
		}
		
		if (target.compareTo(p.data) == 0)
		{
			return p.data;
		}
		
		if (target.compareTo(p.data) < 0)
		{
			return search(target, p.leftChild);
		}
		
		return search(target, p.rightChild);
	}*/

	
	public void inorderTraverse(BinTreeNode<T> N)
	{
		if (N != null)
		{
			inorderTraverse(N.leftChild);				// go left first because that is the order of operations for in order traversal
			System.out.println(N.data.toString());
			N.data.visit();								// check the list at that node, if the length is > 1 then print to output file
			inorderTraverse(N.rightChild);				// next recursively check the right side
		}
	}
	
	public boolean insert(T target, String original, BinTreeNode<T> currentNode)
	{
		if(root == null)								// if the binary tree is completely empty
		{
			root = new BinTreeNode<T>(target);			// the first anagram key becomes the root of the tree
			AnaData temp = (AnaData) root.data;
			temp.getList().addFirst(original);
			return true;
		}
		
		else
		{
			BinTreeNode<T> searchNode = new BinTreeNode<T>(target);		// if the binary tree is not empty-
			if(target.compareTo(currentNode.data) == 0)					// and if the anagram key already exists-
			{
				AnaData temp = (AnaData) currentNode.data;				
				if(!temp.getList().duplicate(original))					// and if the word has NOT been added to that anagram list already-
				{
					temp.getList().addFirst(original);					// then add that word to the anagram list.
				}
				return true;
			}
			
			else if(target.compareTo(currentNode.data) < 0)				// if the anagram key precedes alphabetically to the current node-
			{
				if(currentNode.leftChild == null)						// and if that current node is a leaf-
				{
					currentNode.leftChild = searchNode;					// set the child of the current node to the focus node.
					AnaData temp = (AnaData) currentNode.leftChild.data;
					temp.getList().addFirst(original);					// add the word to the anagram list.
					return true;
				}
				else													// if the current node has children, then recursively call insert again -
				{														// to compare alphabetically to the children of the current node.
					insert(target, original, currentNode.leftChild);
				}
			}
			else														// if the anagram key alphabetically follows to the current node-
			{
				if(currentNode.rightChild == null)						// and if that current node is a leaf-
				{
					currentNode.rightChild = searchNode;				// the searchNode is now the child of the current node.
					AnaData temp = (AnaData) currentNode.rightChild.data;
					temp.getList().addFirst(original);					// add the word to the anagram list.
					return true;
				}
				else													// if the current node has children, then recursively call insert again -
				{														// but this time, to check the right side of the current node.
					insert(target, original, currentNode.rightChild);
				}
			}
		}
		return false;
	}
	
	public BinTreeNode<T> getRoot()	// getter to be able to access root from Anagram.java class
	{
		return root;
	}
}
