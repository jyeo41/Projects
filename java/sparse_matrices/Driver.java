import java.util.InputMismatchException;
import java.util.Scanner;

public class Driver
{
	public static void main(String[] args)
	{
		
		int user_choice = -1;						// integer to hold the user integer choice to be able to utilize the menu function
		boolean exit = false;						// exit condition for when it triggers to end the program
		Driver driver = new Driver();				// instance of driver in order to use the methods within the Driver class
		Scanner in = new Scanner(System.in);		// Scanner to read the user input
		Sparse matrix_a = new Sparse();				// an instance of Matrix A
		Sparse matrix_b = new Sparse();				// an instance of Matrix B
		Sparse matrix_c = new Sparse();				// an instance of Matrix C
		Sparse matrix_arithmetic = new Sparse();	// matrix to print the results of adding and subtracting
		driver.header();							// printing out a header for menu formatting
		
		while (exit != true)		// while loop condition to make sure menu keeps running until user wants to quit the program
		{
			while(user_choice < 0 || user_choice > 5)		// while loop to keep getting the user input until a valid choice is made
			{
				try			// try block to get the user input
				{
					driver.menuMain();
					System.out.println("\nPlease Choose A Valid Menu Option:");
					user_choice = in.nextInt();			// my menu uses integers, so the user must input a valid integer

					
					if(user_choice < 0 || user_choice > 5)
					{
						System.out.println("Please choose a number between 0 and 5.\n");
					}
				}
				catch (InputMismatchException e)	// if a non-integer is selected, throws out an error message and the user is requested to try again
				{
					System.err.println("Not a valid integer choice. Please try again!");
					in.next();						// extremely important line to "flush" the Scanner so it doesn't infinite loop while stuck on a non-int value
				}
			}
			
			switch(user_choice)						// the switch function of the menu, have all the cases laid out
			{
				// exiting the program completely
				case 0 :
				{
					exit = true;
					System.out.println("You will now exit the program!\n");
					break;
				}
				
				// inserting a node. User must choose which matrix to insert into, the row col and data field of the node they want to insert into the matrix
				case 1 : 
				{
					System.out.println("Which matrix would you like to insert to?\n");
					int case1_choice = -1;
					int matrix_row = -1;
					int matrix_col = -1;
					int user_data = 0;
					
					while(case1_choice < 1 || case1_choice > 3)		// while loop to get user input for which matrix they want to insert into
					{
						try			// try block to get the user input
						{
							driver.menuMatrices();
							System.out.println("\nPlease Choose A Valid Menu Option:");
							case1_choice = in.nextInt();			

							
							if(case1_choice < 1 || case1_choice > 3)	// throw a message if they do not input a valid matrix choice
							{
								System.out.println("Please choose a number between 1 and 3.\n");
							}
						}
						catch (InputMismatchException e)	// if a non-integer is selected, throws out an error message and the user is requested to try again
						{
							System.err.println("Not a valid integer choice. Please try again!");
							in.next();						// extremely important line to "flush" the Scanner so it doesn't infinite loop while stuck on a non-int value
						}
					}
					
					while(matrix_row < 0 || matrix_col < 0)		// while loop to get user input for row and column they want to insert to
					{
						try	// try block to get the user input
						{
							System.out.println("\nChoose what row to insert (Number must be >= 0):");
							matrix_row = in.nextInt();			
							
							System.out.println("\nChoose what column to insert (Number must be >= 0):");
							matrix_col = in.nextInt();
							
							if(matrix_row < 0 || matrix_col < 0)		// if the user does not input valid row or column number, throw a message to them
							{
								System.out.println("\n\nInput a valid row and column number.\n");
							}
							
						}
						catch (InputMismatchException e)	// if a non-integer is selected, throws out an error message and the user is requested to try again
						{
							System.err.println("Not a valid integer choice. Please try again!");
							in.next();						// extremely important line to "flush" the Scanner so it doesn't infinite loop while stuck on a non-int value
						}
					}
					
					while(user_data == 0)		// while loop to get user input for the data field of the node they want to insert
					{
						try	// try block to get the user input
						{
							System.out.println("\nEnter a non-0 (zero) integer for the data.");
							user_data = in.nextInt();
						
							if(user_data == 0)		// data field can be any non-zero integer, if they input 0 then keep asking them to put a valid non-zero integer
							{
								System.out.println("\n\nPlease input a valid non-0 (zero) integer.\n");
							}
							
						}
						catch (InputMismatchException e)	// if a non-integer is selected, throws out an error message and the user is requested to try again
						{
							System.err.println("Not a valid integer choice. Please try again!");
							in.next();						// extremely important line to "flush" the Scanner so it doesn't infinite loop while stuck on a non-int value
						}
					}
					
					if(case1_choice == 1) // insert into Matrix A
					{
						matrix_a.insert(user_data, matrix_row, matrix_col);
					}
					
					if(case1_choice == 2) // insert into Matrix B
					{
						matrix_b.insert(user_data, matrix_row, matrix_col);
					}
					
					if(case1_choice == 3) // insert into Matrix C
					{
						matrix_c.insert(user_data, matrix_row, matrix_col);
					}
					
					user_choice = -1;
					break;
				}
				
				// deleting a node. User must choose which matrix to delete from, the row col of the node they want to delete from the matrix
				case 2 :
				{
					System.out.println("Which matrix would you like to delete a node from?\n");
					int case2_choice = -1;	// user input for matrix choice
					int matrix_row = -1;	// user input for row choice
					int matrix_col = -1;	// user input for column choice
					
					while(case2_choice < 1 || case2_choice > 3)		// while loop to get user input for which matrix they want to delete from
					{
						try			// try block to get the user input
						{
							driver.menuMatrices();
							System.out.println("\nPlease Choose A Valid Menu Option:");
							case2_choice = in.nextInt();			

							
							if(case2_choice < 1 || case2_choice > 3)	// throw a message if they do not input a valid matrix choice
							{
								System.out.println("Please choose a number between 1 and 3.\n");
							}
						}
						catch (InputMismatchException e)	// if a non-integer is selected, throws out an error message and the user is requested to try again
						{
							System.err.println("Not a valid integer choice. Please try again!");
							in.next();						// extremely important line to "flush" the Scanner so it doesn't infinite loop while stuck on a non-int value
						}
					}
					
					while(matrix_row < 0 || matrix_col < 0)		// while loop to get user input for row and column they want to delete from
					{
						try	// try block to get the user input
						{
							System.out.println("\nChoose what row to insert (Number must be >= 0):");
							matrix_row = in.nextInt();			
							
							System.out.println("\nChoose what column to insert (Number must be >= 0):");
							matrix_col = in.nextInt();
							
							if(matrix_row < 0 || matrix_col < 0)		// if the user does not input valid row or column number, throw a message to them
							{
								System.out.println("\n\nInput a valid row and column number.\n");
							}
							
						}
						catch (InputMismatchException e)	// if a non-integer is selected, throws out an error message and the user is requested to try again
						{
							System.err.println("Not a valid integer choice. Please try again!");
							in.next();						// extremely important line to "flush" the Scanner so it doesn't infinite loop while stuck on a non-int value
						}
					}
					
					if(case2_choice == 1) // delete the proper node from Matrix A
					{
						matrix_a.delete(matrix_row, matrix_col);
						System.out.println("You have deleted a node at--");
						System.out.printf("Row: %d\n", matrix_row);
						System.out.printf("Col: %d\n", matrix_col);
						System.out.println("\nMatrix A:");
						Sparse.simplePrint(matrix_a);
						
					}
					
					else if(case2_choice == 2) // delete the proper node from Matrix B
					{
						matrix_b.delete(matrix_row, matrix_col);
						System.out.println("You have deleted a node at--");
						System.out.printf("Row: %d\n", matrix_row);
						System.out.printf("Col: %d\n", matrix_col);
						System.out.println("\nMatrix C:");
						Sparse.simplePrint(matrix_b);
					}
					
					else /// delete the proper node from Matrix C
					{
						matrix_c.delete(matrix_row, matrix_col);
						System.out.println("You have deleted a node at--");
						System.out.printf("Row: %d\n", matrix_row);
						System.out.printf("Col: %d\n", matrix_col);
						System.out.println("\nMatrix C:");
						Sparse.simplePrint(matrix_c);
					}
					
					user_choice = -1;
					break;
				}
		
				// add two matrices, check to make sure the dimensions of the matrices are the same
				case 3 :
				{
					int case3_first_matrix = -1;
					int case3_second_matrix = -1;
					
					while((case3_first_matrix < 1 || case3_first_matrix > 3) || (case3_second_matrix < 1 || case3_second_matrix > 3))
					{
						try		// try block to get the user input
						{
							driver.menuMatrices();
							System.out.println("\nFirst Matrix Choice to Add:");
							case3_first_matrix = in.nextInt();
							System.out.println("\nSecond Matrix Choice to Add:");
							case3_second_matrix = in.nextInt();
							
							if((case3_first_matrix < 1 || case3_first_matrix > 3) || (case3_second_matrix < 1 || case3_second_matrix > 3))	// throw a message if they do not input a valid matrix choice
							{
								System.out.println("Please choose a number between 1 and 3 for both matrix decisions.\n");
							}
						}
						catch (InputMismatchException e)	// if a non-integer is selected, throws out an error message and the user is requested to try again
						{
							System.err.println("Not a valid integer choice. Please try again!");
							in.next();						// extremely important line to "flush" the Scanner so it doesn't infinite loop while stuck on a non-int value
						}
					}
					
					// adding A + B or B + A they are the same thing
					if((case3_first_matrix == 1 && case3_second_matrix == 2) || (case3_first_matrix == 2 && case3_second_matrix == 1))
					{
						if((matrix_a.getRow_max() != matrix_b.getRow_max()) || (matrix_a.getCol_max() != matrix_b.getCol_max()))
						{
							System.err.print("Unable to add matrices of different dimensions...\n\n");
						}
						else
						{
							System.out.println("\nAdding your chosen matrices!");
							matrix_arithmetic = Sparse.add(matrix_a, matrix_b);
							System.out.println("Matrix A:");
							Sparse.simplePrint(matrix_a);
							System.out.println("Matrix B:");
							Sparse.simplePrint(matrix_b);
							System.out.println("Matrix Result:");
							Sparse.simplePrint(matrix_arithmetic);
						}
					}
					// adding B + C or C + B
					else if ((case3_first_matrix == 2 && case3_second_matrix == 3) || (case3_first_matrix == 3 && case3_second_matrix == 2))
					{
						if((matrix_b.getRow_max() != matrix_c.getRow_max()) || (matrix_b.getCol_max() != matrix_c.getCol_max()))
						{
							System.err.print("Unable to add matrices of different dimensions...\n\n");
						}
						else
						{
							System.out.println("\nAdding your chosen matrices!");
							matrix_arithmetic = Sparse.add(matrix_b, matrix_c);
							System.out.println("Matrix B:");
							Sparse.simplePrint(matrix_b);
							System.out.println("Matrix C:");
							Sparse.simplePrint(matrix_c);
							System.out.println("Matrix Result:");
							Sparse.simplePrint(matrix_arithmetic);
						}
					}
					// adding A + C or C + A
					else
					{
						if((matrix_a.getRow_max() != matrix_c.getRow_max()) || (matrix_a.getCol_max() != matrix_c.getCol_max()))
						{
							System.err.print("Unable to add matrices of different dimensions...\n\n");
						}
						else
						{
							System.out.println("\nAdding your chosen matrices!");
							matrix_arithmetic = Sparse.add(matrix_a, matrix_c);
							System.out.println("Matrix A:");
							Sparse.simplePrint(matrix_a);
							System.out.println("Matrix C:");
							Sparse.simplePrint(matrix_c);
							System.out.println("Matrix Result:");
							Sparse.simplePrint(matrix_arithmetic);
						}
					}
					
					user_choice = -1;
					break;
				}
				
				// subtract two matrices, check to make sure the dimensions of the matrices are the same
				case 4 :
				{

					int case4_first_matrix = -1;
					int case4_second_matrix = -1;
					
					// while gate to keep pinging the user to choose two valid matrices
					while((case4_first_matrix < 1 || case4_first_matrix > 3) || (case4_second_matrix < 1 || case4_second_matrix > 3))
					{
						try		// try block to get the user input
						{
							driver.menuMatrices();
							System.out.println("\nFirst Matrix Choice to Subtract:");
							case4_first_matrix = in.nextInt();
							System.out.println("\nSecond Matrix Choice to Subract:");
							case4_second_matrix = in.nextInt();
							
							if((case4_first_matrix < 1 || case4_first_matrix > 3) || (case4_second_matrix < 1 || case4_second_matrix > 3))	// throw a message if they do not input a valid matrix choice
							{
								System.out.println("Please choose a number between 1 and 3 for both matrix decisions.\n");
							}
						}
						catch (InputMismatchException e)	// if a non-integer is selected, throws out an error message and the user is requested to try again
						{
							System.err.println("Not a valid integer choice. Please try again!");
							in.next();						// extremely important line to "flush" the Scanner so it doesn't infinite loop while stuck on a non-int value
						}
					}
					
					// Subtracting A - B
					if(case4_first_matrix == 1 && case4_second_matrix == 2)
					{
						if((matrix_a.getRow_max() != matrix_b.getRow_max()) || (matrix_a.getCol_max() != matrix_b.getCol_max()))
						{
							System.err.print("Unable to add matrices of different dimensions...\n\n");
						}
						else
						{
							System.out.println("\nSubtracting your chosen matrices!");
							matrix_arithmetic = Sparse.subtract(matrix_a, matrix_b);
							System.out.println("Matrix A:");
							Sparse.simplePrint(matrix_a);
							System.out.println("Matrix B:");
							Sparse.simplePrint(matrix_b);
							System.out.println("Matrix Result:");
							Sparse.simplePrint(matrix_arithmetic);
						}
					}
					// Subtracting B - A
					else if (case4_first_matrix == 2 && case4_second_matrix == 1)
					{
						if((matrix_a.getRow_max() != matrix_b.getRow_max()) || (matrix_a.getCol_max() != matrix_b.getCol_max()))
						{
							System.err.print("Unable to subtract matrices of different dimensions...\n\n");
						}
						else
						{
							System.out.println("\nSubtracting your chosen matrices!");
							matrix_arithmetic = Sparse.subtract(matrix_b, matrix_a);
							System.out.println("Matrix B:");
							Sparse.simplePrint(matrix_b);
							System.out.println("Matrix A:");
							Sparse.simplePrint(matrix_a);
							System.out.println("Matrix Result:");
							Sparse.simplePrint(matrix_arithmetic);
						}
					}
					// Subtracting B - C
					else if (case4_first_matrix == 2 && case4_second_matrix == 3)
					{
						if((matrix_b.getRow_max() != matrix_c.getRow_max()) || (matrix_b.getCol_max() != matrix_c.getCol_max()))
						{
							System.err.print("Unable to subtract matrices of different dimensions...\n\n");
						}
						else
						{
							System.out.println("\nSubtracting your chosen matrices!");
							matrix_arithmetic = Sparse.subtract(matrix_b, matrix_c);
							System.out.println("Matrix B:");
							Sparse.simplePrint(matrix_b);
							System.out.println("Matrix C:");
							Sparse.simplePrint(matrix_c);
							System.out.println("Matrix Result:");
							Sparse.simplePrint(matrix_arithmetic);
						}
					}
					
					// Subtracting C - B
					else if (case4_first_matrix == 3 && case4_second_matrix == 2)
					{
						if((matrix_b.getRow_max() != matrix_c.getRow_max()) || (matrix_b.getCol_max() != matrix_c.getCol_max()))
						{
							System.err.print("Unable to subtract matrices of different dimensions...\n\n");
						}
						else
						{
							System.out.println("\nSubtracting your chosen matrices!");
							matrix_arithmetic = Sparse.subtract(matrix_c, matrix_b);
							System.out.println("Matrix C:");
							Sparse.simplePrint(matrix_c);
							System.out.println("Matrix B:");
							Sparse.simplePrint(matrix_b);
							System.out.println("Matrix Result:");
							Sparse.simplePrint(matrix_arithmetic);
						}
					}
					// Subtracting A - C
					else if (case4_first_matrix == 1 && case4_second_matrix == 3)
					{
						if((matrix_a.getRow_max() != matrix_c.getRow_max()) || (matrix_a.getCol_max() != matrix_c.getCol_max()))
						{
							System.err.print("Unable to subtract matrices of different dimensions...\n\n");
						}
						else
						{
							System.out.println("\nSubtracting your chosen matrices!");
							matrix_arithmetic = Sparse.subtract(matrix_a, matrix_c);
							System.out.println("Matrix A:");
							Sparse.simplePrint(matrix_a);
							System.out.println("Matrix C:");
							Sparse.simplePrint(matrix_c);
							System.out.println("Matrix Result:");
							Sparse.simplePrint(matrix_arithmetic);
						}
					}
					// Subtracting C - A
					else
					{
						if((matrix_a.getRow_max() != matrix_c.getRow_max()) || (matrix_a.getCol_max() != matrix_c.getCol_max()))
						{
							System.err.print("Unable to subtract matrices of different dimensions...\n\n");
						}
						else
						{
							System.out.println("\nSubtracting your chosen matrices!");
							matrix_arithmetic = Sparse.subtract(matrix_c, matrix_a);
							System.out.println("Matrix C:");
							Sparse.simplePrint(matrix_c);
							System.out.println("Matrix A:");
							Sparse.simplePrint(matrix_a);
							System.out.println("Matrix Result:");
							Sparse.simplePrint(matrix_arithmetic);
						}
					}
					
					user_choice = -1;
					break;
				}
				
				
				
				// print choice to look at the state of all the matrices
				case 5 :
				{
					System.out.println("\n\n||Printing your matrices||\n");
					System.out.println("Matrix A:");
					Sparse.simplePrint(matrix_a);
					
					System.out.println("Matrix B:");
					Sparse.simplePrint(matrix_b);
					
					System.out.println("Matrix C:");
					Sparse.simplePrint(matrix_c);
					
					user_choice = -1;
					break;
				}
			}
		}
		
		in.close();

	}
	
		private void menuMatrices()		// method to guide user for choosing matrix options when using the menu
		{
			System.out.print("\n");
			System.out.println("Matrix A:            Press '1'");
			System.out.println("Matrix B:            Press '2'");
			System.out.println("Matrix C:            Press '3'");		
		}
		
		private void header()
		{
			System.out.println("+-------------------------------------|");
			System.out.println("|        Joon Yeo Sparse Matrix       |");
			System.out.println("|            Menu Console             |");
			System.out.println("+-------------------------------------+");
		}
		
		private void menuMain()			// menu method for all the possible options in the menu
		{
			System.out.print("\n");
			System.out.println("Insert an Element:            Press '1'");
			System.out.println("Delete an Element:            Press '2'");
			System.out.println("Add Two Matrices:             Press '3'");
			System.out.println("Subtract Two Matrices:        Press '4'");
			System.out.println("Print a Matrix:               Press '5'");
			System.out.println("Exit the Program:             Press '0'");
		}

}
