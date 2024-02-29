
public class Sparse {
	private Node head;		// points to matrix head, always non-null
	private int node_count;	// count of nodes in matrix not counting header nodes or matrix header
	private int row_max = 0;
	private int col_max = 0;

	
	private class Node		// private class to be able to create a new node to insert into matrix
	{
		Integer data;		// data field containing an integer represented in the matrix
		int row;			// data field for representing the row for the integer
		int col;			// data field for representing the column for the integer
		Node left;			
		Node right;			// set of pointers to be able to traverse the matrix
		Node up;
		Node down;
		
		public Node (Integer data, int row, int col)	// constructor class for creating a new node
		{
			this.data = data;							// setting appropriate data fields for incoming parameters
			this.row = row;
			this.col = col;
			
			if(row_max < row)		// setting the max row dimension if the matrix ever expands
			{
				row_max = row;
			}
			
			if(col_max < col)		// setting the max column dimension if the matrix ever expands
			{
				col_max = col;
			}
		}
	}
	
	public Sparse()			// constructor class for initializing a new sparse matrix
	{
		this.head = new Node(null, -1, -1);	// setting the head of the matrix header with no data field
		this.node_count = 0;				// setting node count to 0 since no nodes will have been inserted yet
		head.up = head;						
		head.down = head;					// making all the pointers point to itself since no nodes have been inserted yet
		head.left = head;
		head.right = head;

	}
	
	
	
	
	
	public boolean insert(Integer value, int row, int col)
	{
		if(this.node_count == 0)
		{
			Node new_node = new Node(value, row, col);		// creating new node

			Node row_header = new Node(null, row, -1);		// creating new row header because its an empty matrix
			head.down = row_header;							// setting down head pointer to new row header
			head.up = row_header;							// setting up head pointer to new row header because circular to loop around
			row_header.up = head;							// setting new row header up pointer to head
			row_header.down = head;							// setting new row header down pointer to head because circular to loop around
			
			Node col_header = new Node(null, -1, col);		// creating new col header because its an empty matrix
			head.right = col_header;						// setting right head pointer to new col header
			head.left = col_header;							// setting left head pointer to new col header to loop back around
			col_header.left = head;							// setting new col header left pointer to head
			col_header.right = head;						// setting new col header right pointer to head to loop back around
			
			new_node.up = col_header;						// linking the pointers of new node and column/row headers
			new_node.down = col_header;
			new_node.left = row_header;
			new_node.right = row_header;
			
			col_header.up = new_node;
			col_header.down = new_node;
			
			row_header.left = new_node;
			row_header.right = new_node;
			
			this.node_count++;								// increase node count in the matrix by 1
			
			//System.out.println("Created first node and made first row and first col headers.\n");
			return true;
		}
		
		else
		{
			//System.out.println("Node Count is > 1.\n");
			Node new_node_two = new Node(value, row, col);		// creating new node
			this.node_count++;									// increase node count in the matrix by 1

			Node current_row = head;							// pointer to keep track of row
			Node current_col = head;							// pointer to keep track of column
			Node temp_row = null;								// pointer node to keep track of row header
			Node temp_col = null;								// pointer node to keep track of col header
			
			
			// while gate to traverse rows
			while(current_row.down != head && current_row.down.row <= new_node_two.row)	
			{
				current_row = current_row.down;
			}
						
			if(current_row.row != new_node_two.row)					// if the current row is not the row i need
			{
				//System.out.println("Creating new Row Header.\n");
				Node new_row_header = new Node(null, row, -1);		// create new row header and link to existing row header LL
				
				new_row_header.up = current_row;
				new_row_header.down = current_row.down;
				new_row_header.left = new_row_header;				// making pointers equal itself to avoid null exception
				new_row_header.right = new_row_header;				// making pointers equal itself to avoid null exception
				current_row.down.up = new_row_header;
				current_row.down = new_row_header;
				
				current_row = new_row_header;
				temp_row = new_row_header;
			}
			
			else
			{
				//System.out.println("Current row IS the row I need.\n");
				// if the current row IS the row I need, then traverse that row and find the node before the column i need
				temp_row = current_row;
				while(current_row.right != temp_row && current_row.right.col <= new_node_two.col)
				{
					current_row = current_row.right;
				//	System.out.println("Current Row traversed right once.\n");
				}
			}
			
			// while gate to traverse columns
			while(current_col.right != head && current_col.right.col <= new_node_two.col)
			{
				current_col = current_col.right;
			//	System.out.println("Traversing columns.\n");
			}
			
			if(current_col.col != new_node_two.col)					// if the current col is NOT the col I need
			{
				//System.out.println("Current col is NOT the column I need.\n");
				Node new_col_header = new Node(null, -1, col);		// create new col header and link to existing col header LL
				
				new_col_header.left = current_col;
				new_col_header.right = current_col.right;
				new_col_header.up = new_col_header;					// making pointers equal itself to avoid null exception
				new_col_header.down = new_col_header;				// making pointers equal itself to avoid null exception
				current_col.right.left = new_col_header;
				current_col.right = new_col_header;
				
				current_col = new_col_header;
				temp_col = new_col_header;
				//System.out.println("New column header was created.\n");
			}
			else		// if the current col IS the column I need then traverse down to the correct row
			{
				temp_col = current_col;
				while(current_col.down != temp_col && current_col.down.row <= new_node_two.row)
				{
					current_col = current_col.down;
				}
			}
			
			//link new node to the appropriate row and column in the matrix
			
			// case if the node is being linked when it is at an edge column but not an edge row
			if(new_node_two.col == col_max && new_node_two.row < row_max)
			{
				new_node_two.left = current_row;
				new_node_two.up = current_col;
				new_node_two.down = current_col.down;
				new_node_two.right = temp_row;
				current_col.down.up = new_node_two;
				current_col.down = new_node_two;
				temp_col.up = new_node_two;
				current_row.right = new_node_two;
			}
			
			// case if the node is being linked when it is at an edge row but not an edge column
			else if(new_node_two.row == row_max && new_node_two.col < col_max)
			{
				new_node_two.right = current_row.right;
				new_node_two.left = current_row;
				new_node_two.up = current_col;
				new_node_two.down = temp_col;
				current_row.right.left = new_node_two;
				current_row.right = new_node_two;
				temp_col.up = new_node_two;
			}
			
			// case if the node is being linked to the bottom right corner of the matrix
			else if(new_node_two.row == row_max && new_node_two.col == col_max)
			{
				current_row.right = new_node_two;
				current_col.down = new_node_two;
				new_node_two.left = current_row;
				new_node_two.up = current_col;
				new_node_two.down = temp_col;
				temp_col.up = new_node_two;
				new_node_two.right = temp_row;
				temp_row.left = new_node_two;
			}
			
			// every other case where the node is being linked in the middle of the matrix
			else
			{
				current_row.right.left = new_node_two;
				current_col.down.up = new_node_two;
				new_node_two.down = current_col.down;
				new_node_two.right = current_row.right;
				new_node_two.left = current_row;
				new_node_two.up = current_col;
				current_col.down = new_node_two;
				current_row.right = new_node_two;
			}
			return true;
		}
	}
	
	
	
	
	
	public static void simplePrint(Sparse m)
	{
		if(m.node_count == 0)		// edge case if there are absolutely no nodes in the matrix
		{
			System.out.println("\nThis is an empty matrix.\n\n");
		}
		
		else
		{
			Node current_row = m.head;
			Node temp_row = m.head;
			
			while(temp_row.down != m.head)		// iterate through all the rows of the matrix' rows
			{
				
				if(temp_row.down.row - temp_row.row > 1)		// if there are any skipped rows, print out the according number of *'s
				{
					int print_number = (temp_row.down.row - temp_row.row) - 1;
					for(int i = 0; i < print_number; i++)
					{
						for(int j = 0; j <= m.col_max; j++)
						{
							System.out.printf("%6c", '*');
						}
						System.out.print("\n");
					}
				}
				
				temp_row = temp_row.down;		// traversing down the list of rows
				current_row = temp_row;			// needed to keep track of current node when iterating through the row
				int counter = 0;
				
				// print out current row
				while(current_row.right != temp_row)
				{
					
					// prints out according *'s if there are any skipped columns
					if(current_row.right.col - current_row.col > 1)
					{
						int print_number = (current_row.right.col - current_row.col) - 1;
						
						for(int i = 0; i < print_number; i++)
						{
							System.out.printf("%6c", '*');			// print out according number of *'s if there are skipped columns
						}
					}

					if(current_row.right.data != null)					// if there is a space between the current row node and the next row node
					{
						System.out.printf("%6d", current_row.right.data);	// print out the data and continue traversing
					}
					counter = current_row.right.col;
					current_row = current_row.right;
				}
				for(int i = 0; i < (m.col_max - counter); i++)			// used to print out remaining *'s if there are no more nodes in that row
				{														// but there are columns that still need to be accounted for
					System.out.printf("%6c", '*');
				}
				
				System.out.print("\n");
				
			}
			System.out.print("\n");
		}		
	}
	

	
	
	
	// method to delete a node in the sparse matrix
	public boolean delete(int row, int col)
	{
		Node current_row = this.head;		// keeps track of current node in the row
		Node temp_row = this.head;			// keeps track of which row header I am at
		Node temp_col = this.head;			// keeps track of which col header I am at
		Node delete_node = null;			// 
		int counter = 0;
		
		while(current_row.down != head && current_row.row != row)	// traversing down the list of rows until I find the appropriate row
		{
			current_row = current_row.down;						
		}
		
		temp_row = current_row;		// setting temp_row so I know if I reach the end of the row
		
		while (temp_col.right != head && temp_col.col != col)
		{
		    temp_col = temp_col.right;
		}
		
		while(current_row.right != temp_row)		// traversing the current row 
		{
			if(current_row.right.col == col && current_row.right.row == row)	// if the current node I am at IS the target node
			{																	// then return true
				delete_node = current_row.right;
				
				// doing the correct linking and unlinking
				delete_node.up.down = delete_node.down;	
				delete_node.down.up = delete_node.up;
				delete_node.up = null;
				delete_node.down = null;
				delete_node.left.right = delete_node.right;
				delete_node.right.left = delete_node.left;
				delete_node.left = null;
				delete_node.right = null;
				
				// linking and unlinking the proper row headers if that row is empty
				if(temp_row.right == temp_row && temp_row.left == temp_row)
				{
					temp_row.up.down = temp_row.down;
					temp_row.down.up = temp_row.up;
					temp_row.up = null;
					temp_row.down = null;
				}
				
				// linking and unlinking the proper col headers if that column is empty
				if(temp_col.down == temp_col && temp_col.up == temp_col)
				{
					counter = temp_col.left.col;			// needed to keep track of the previous existing column
					temp_col.left.right = temp_col.right;
					temp_col.right.left = temp_col.left;
					temp_col.left = null;
					temp_col.right = null;
					
				}
				if(temp_col.col ==col_max)
				{
					col_max = counter;		// if there is a difference of more than 1 between the final column and previous column
											// then we need to delete the unnecessary headers as well utilizing the new col_max
				}
				
				this.node_count--;			// reducing node count by 1 because we are deleting a single node
				return true;
			}
			
			current_row = current_row.right;	// otherwise keep going through the row
		}

										// if I reach the end of the matrix without having found the node
		return false;					// return false
	}
	
	
	
	
	
	public static Sparse add(Sparse matrix_A, Sparse matrix_B)
	{
		Sparse matrix_result = new Sparse();	// creating a clean matrix to add into
		matrix_result = matrixCopy(matrix_A);	// calling a copy method so editing matrix_result does NOT change matrix_A
		Node current_row = matrix_B.head;		// keeps track of current node in the row
		Node temp_row = matrix_B.head;			// keeps track of which row header you are at
			
		System.out.println("Adding Matrices.\n");
		while(temp_row.down != matrix_B.head)		// iterate through all the rows of matrix B rows
		{
			temp_row = temp_row.down;
			current_row = temp_row;
			
			
			while(current_row.right != temp_row)	// iterate through each node of the row
			{
				current_row = current_row.right;
				
				if(matrix_result.hasValue(current_row.row, current_row.col) == false)			// if the result matrix im adding into does not have an -
				{																				// existing at the same row and column as matrix B -
					matrix_result.insert(current_row.data, current_row.row, current_row.col);	// then insert that node directly into the result matrix
				}
				
				else	// if the result matrix DOES have an existing node at the same row and column as matrix B
				{
					int current_matrix_data = current_row.data;			
					int result_matrix_data = matrix_result.getValue(current_row.row, current_row.col);
					int new_data = current_matrix_data + result_matrix_data;			// perform the necessary addition to add the matrices
						
					if(new_data == 0)		// however if the resulting subtraction results in 0
					{
						matrix_result.delete(current_row.row, current_row.col);	// delete that node
					}
					else		// else just change the value at that row and column to the appropriate data value
					{
						matrix_result.changeValue(current_row.row, current_row.col, new_data);	// change the value in the result matrix to have the new added value
					}

				}
			}
		}

		return matrix_result;
	}
	
	
	
	
	public static Sparse subtract(Sparse matrix_A, Sparse matrix_B)
	{
		Sparse matrix_result = new Sparse();	// creating a clean matrix to add into
		matrix_result = matrixCopy(matrix_A);	// calling copy method so editing matrix_result does NOT edit matrix_A
		Node current_row = matrix_B.head;		// keeps track of current node in the row
		Node temp_row = matrix_B.head;			// keeps track of which row header you are at
		
		System.out.println("Subtracting Matrices.\n");
		while(temp_row.down != matrix_B.head)		// iterate through all the rows of matrix B rows
		{
			temp_row = temp_row.down;
			current_row = temp_row;
			
			
			while(current_row.right != temp_row)	// iterate through each node of the row
			{
				current_row = current_row.right;
				if(matrix_result.hasValue(current_row.row, current_row.col) == false)			// if the result matrix im subtracting into does not have an -
				{																				// existing at the same row and column as matrix B -
					matrix_result.insert(-current_row.data, current_row.row, current_row.col);	// then insert that node directly into the result matrix
				}
				
				else	// if the result matrix DOES have an existing node at the same row and column as matrix B
				{
					int current_matrix_data = current_row.data;								
					int result_matrix_data = matrix_result.getValue(current_row.row, current_row.col);
					int new_data = result_matrix_data - current_matrix_data;	// perform the necessary subtraction to the matrices
					
					if(new_data == 0)		// however if the resulting subtraction results in 0
					{
						matrix_result.delete(current_row.row, current_row.col);	// delete that node
					}
					else		// else just change the value at that row and column to the appropriate data value
					{
						matrix_result.changeValue(current_row.row, current_row.col, new_data);
					}
					

				}
			}
		}
		return matrix_result;
	}
	
	
	
	// helper method to be able to perform add and subtract method correctly
	private static Sparse matrixCopy(Sparse inc_matrix)
	{
		Sparse copied_matrix = new Sparse();
		Node current_row = inc_matrix.head;
		Node temp_row = inc_matrix.head;
		
		while(temp_row.down != inc_matrix.head)	// iterate through all the rows of the incoming matrix
		{
			temp_row = temp_row.down;
			current_row = temp_row;
			
			while(current_row.right != temp_row)	// iterate through current row 
			{
				current_row = current_row.right;
				copied_matrix.insert(current_row.data, current_row.row, current_row.col);	// insert every node from this matrix into a temporary "copy matrix
			}
		}
		
		return copied_matrix;	// this matrix will have the same exact values as the incoming matrix, however, it will be two different instances of objects
	}
	
	
	
	private void changeValue(int row, int col, int value)
	{
		Node current_row = this.head;		// keeps track of current node in the row
		Node temp_row = this.head;			// keeps track of which row header you are at
		
		while(current_row.down != this.head && current_row.row != row)	// traversing down the list of rows until I find the appropriate row
		{
			current_row = current_row.down;						
		}
		
		temp_row = current_row;		// setting temp_row so I know if I reach the end of the row
		
		while(current_row.right != temp_row)		// traversing the current row 
		{
			if(current_row.right.col == col && current_row.right.row == row)	// if the current node I am at IS the correct node
			{																	// change the value of the node from the add method
				current_row.right.data = value;
			}
			current_row = current_row.right;					// otherwise keep going through the row
		}

	}
	
	
	
	
	
	public int getValue(int row, int col)
	{
		Node current_row = this.head;		// keeps track of current node in the row
		Node temp_row = this.head;			// keeps track of which row header you are at
		
		while(current_row.down != head && current_row.row != row)	// traversing down the list of rows until I find the appropriate row
		{
			current_row = current_row.down;						
		}
		
		temp_row = current_row;		// setting temp_row so I know if I reach the end of the row
		
		while(current_row.right != temp_row)		// traversing the current row 
		{
			if(current_row.right.col == col && current_row.right.row == row)	// if the current node I am at IS the target node
			{																	// then return true
				//System.out.printf("\nThe data is: %d", current_row.right.data);		
				return current_row.right.data;
			}
			current_row = current_row.right;						// otherwise keep going through the row
		}

		System.out.println("\nUnable to retrive the desired data.");	// if I reach the end of the matrix without having found the node
		return 0;														// return 0
	}
	
	
	
	
	public boolean hasValue(int row, int col)
	{
		Node current_row = this.head;		// keeps track of current node in the row
		Node temp_row = this.head;			// keeps track of which row header you are at
		
		while(current_row.down != head && current_row.row != row)	// traversing down the list of rows until I find the appropriate row
		{
			current_row = current_row.down;						
		}
		
		temp_row = current_row;		// setting temp_row so I know if I reach the end of the row
		
		while(current_row.right != temp_row)		// traversing the current row 
		{
			if(current_row.right.col == col && current_row.right.row == row)	// if the current node I am at IS the target node
			{																	// then return true
			//	System.out.println("\nFound the target node!");		
				return true;
			}
			current_row = current_row.right;						// otherwise keep going through the row
		}

		//System.out.println("\nUnable to find the target node.");	// if I reach the end of the matrix without having found the node
		return false;												// return false
	}
	
	
	
	
	// method to return the node count and print out the value
	public int getNodeCount()
	{
		System.out.print("\n");
		System.out.printf("Node Count: %d", this.node_count);
		return this.node_count;
	}
	
	// getter to be able to check for dimensions error
	public int getRow_max() 
	{
		return row_max;
	}

	// getter to be able to check for dimensions error
	public int getCol_max() 
	{
		return col_max;
	}

	
}
