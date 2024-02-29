/* Fill in your Name and GNumber in the following two comment fields
 * Name: Joon Kyung Yeo
 * GNumber: G00953081
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>

#include "codec.h"
#include "protocol.h"
#include "tag_data.h"

void parity_word_bit_decoder(long int raw_data, int shifter, long int *parityword_bit, int number_of_data, int number_of_columns);
void decoded_data_parity_calculator(long int decoded_data[], int parity[], int number_of_data, int data_length);


/* Read the Project Document for all of the instructions.
 * You may add any functions that you like to this file.
 * - Recommendations: Add a function to calculate your parity and a function 
 *   to get bit X from an int.
 *
 * Decode to extract the Tag ID (TID) according to the project documentation
 * Fill in workbook completely for full credit.
 * If any parity bits are wrong, set *is_valid to 0 and return immediately.
 *   Else, set *is_valid to 1 and return when workbook is completely filled in.
 */
void codec_decode_tid(long int raw_data, Decode_Workbook *workbook, int *is_valid) {
	long int raw_data_copy = raw_data;				// copy of raw data to not accidentally modify incoming raw data value
	
	/* working with END bit */
	long int end_bit_mask = 0x0000000000000001; 			// mask to extract the LSB
	long int decoded_end_bit = (raw_data_copy & end_bit_mask);	// extracting the LSB
	workbook->end = decoded_end_bit;				// storing the end bit to the data struct
	
	if(workbook->end != 0)	// if the end bit is not 0, return
	{
		*is_valid = 0;
		return;
	}


	
	/* working with PREAMBLE bits */
	long int decoded_preamble = 0;					// variable to store the decoded premable
	long int preamble_mask = 0x00000000000001ff;			// mask to extract the preamble bits
	decoded_preamble = raw_data_copy;				// setting copy variable to be the same as raw data to be able to work with it
	decoded_preamble = (decoded_preamble >> 55);			// right shift to move premable bits to the LSB's
	decoded_preamble = (decoded_preamble & preamble_mask);		// bitwise AND to extract final 9 bits
	workbook->preamble = decoded_preamble;				// storing the premable to the data struct

	if(workbook->preamble != 0x1ff)		// if the preamble is not correct, return
	{
		*is_valid = 0;
		return;
	}

	

	/* working with DECODE SEGMENTS */
	long int decoded_segment_mask = 0x1f; 				// mask for extracting last 5 LSB's
	long int decoded_segment[10];
	
	decoded_segment[0] = ((raw_data_copy >> 50) & decoded_segment_mask);	// extracting the 5 bit decoded segments from raw data
	decoded_segment[1] = ((raw_data_copy >> 45) & decoded_segment_mask);
	decoded_segment[2] = ((raw_data_copy >> 40) & decoded_segment_mask);
	decoded_segment[3] = ((raw_data_copy >> 35) & decoded_segment_mask);
	decoded_segment[4] = ((raw_data_copy >> 30) & decoded_segment_mask);
	decoded_segment[5] = ((raw_data_copy >> 25) & decoded_segment_mask);
	decoded_segment[6] = ((raw_data_copy >> 20) & decoded_segment_mask);
	decoded_segment[7] = ((raw_data_copy >> 15) & decoded_segment_mask);
	decoded_segment[8] = ((raw_data_copy >> 10) & decoded_segment_mask);
	decoded_segment[9] = ((raw_data_copy >> 5) & decoded_segment_mask);

	/* DATA portion of decode segments */
	long int decoded_data[10];	// setting an array to hold each of the 4 bit decoded data from the 5 bit segments
	for(int i = 0; i < 10; i++)	// extracting the 4 bit data from the 5 bit segments
	{
		decoded_data[i] = ((decoded_segment[i] >> 1) & 0xf);
	}
	
	for(int i = 0; i < 10; i++)	// storing each 4 bit decoded bit to the workbook
	{
		workbook->segment[i].data = decoded_data[i];
	}
	
	/* PARITY portion of decode segments */
	int received_parity[10];	// setting an array to hold RECEIVED parity bits from the raw data
	for(int i = 0; i < 10; i++)	// extracting the parity bits from the decoded segments
	{
		received_parity[i] = (decoded_segment[i] & 1);
	}

	
	int calculated_parity[10];	// an array to hold manually calculated parity bits from the 4 bit data values
	decoded_data_parity_calculator(decoded_data, calculated_parity, 10, 4);
	
	
	for(int i = 0; i < 10; i++)	// storing each of the parity bits to the workbook
	{
		workbook->segment[i].parity = received_parity[i];
	}
	
	for(int i = 0; i < 10; i++)	// if the received parity bits and calculated parity bits do not match, return
	{
		if(workbook->segment[i].parity != calculated_parity[i])
		{
			*is_valid = 0;
			return;
		}
	}



	/* working with PARITY WORD bits */
	long int decoded_parity_word = 0;				// variable for decoded parity word from raw data
	long int parity_word_mask = 0x000000000000000f;			// mask for extracting parity word from raw data
	decoded_parity_word = (raw_data_copy >> 1);			// shift to get rid of the end bit
	decoded_parity_word = (decoded_parity_word & parity_word_mask);	
	workbook->parity_word = decoded_parity_word;			// storing the value of the parity word bits into the workbook

	long int calculated_parity_word_bit_0 = 0;				// set of variables for manually decoding the parity word bits
	long int calculated_parity_word_bit_1 = 0;
	long int calculated_parity_word_bit_2 = 0;
	long int calculated_parity_word_bit_3 = 0;

	parity_word_bit_decoder (raw_data_copy, 6, &calculated_parity_word_bit_0, 10, 5);	// set of function calls to helper function to manually calculate parity word bits by columns
	parity_word_bit_decoder (raw_data_copy, 7, &calculated_parity_word_bit_1, 10, 5);
	parity_word_bit_decoder (raw_data_copy, 8, &calculated_parity_word_bit_2, 10, 5);
	parity_word_bit_decoder (raw_data_copy, 9, &calculated_parity_word_bit_3, 10, 5);

	long int calculated_parity_array[4];		// creating an array for the calculated bits to work with in for loop
	calculated_parity_array[0] = calculated_parity_word_bit_0;
	calculated_parity_array[1] = calculated_parity_word_bit_1;
	calculated_parity_array[2] = calculated_parity_word_bit_2;
	calculated_parity_array[3] = calculated_parity_word_bit_3;

	long int calculated_parity_word_string = 0;	// using shifts in a for loop to string together the bits for the parity word
	calculated_parity_word_string = (calculated_parity_word_string | calculated_parity_array[3]);
	for(int i = 2; i >= 0; i--)	// starting from the MSB and then adding in the subsequence bits from the right
	{
		calculated_parity_word_string = ((calculated_parity_word_string << 1) | calculated_parity_array[i]);
	}
	
	if(workbook->parity_word != calculated_parity_word_string)	// if the received parity word and the calculated parity word does not match, return
	{
		*is_valid = 0;
		return;
	}
	


	/* creating the DECODED DATA */
	long int decoded_data_number_string = 0;
	decoded_data_number_string = (decoded_data_number_string | decoded_data[0]);		// creating the decoded data using shifts
	for(int i = 1; i < 10; i++)
	{
		decoded_data_number_string = ((decoded_data_number_string << 4) | decoded_data[i]);
	}
	workbook->decoded_data = decoded_data_number_string;	// storing the resulting decoded data hex string into the workbook


	*is_valid = 1;
}



/* Encodes the fields with parity bits and combines them according to the 
 * project documentation
 */
void codec_encode_read(int command_code, int address_code, long int *raw_data) {
	long int command_code_copy[1];		// setting arrays to be able to be used with my helper function
	long int address_code_copy[1];
	int command_code_parity[1];
	int address_code_parity[1];
	long int raw_data_temp = 0;

	command_code_copy[0] = command_code;	// setting first index of each array
	address_code_copy[0] = address_code;
	
	decoded_data_parity_calculator(command_code_copy, command_code_parity, 1, 3);	// calling helper function to calculate the parity for each
	decoded_data_parity_calculator(address_code_copy, address_code_parity, 1, 4);	// command code and address code ints
	
	raw_data_temp = (raw_data_temp | command_code_copy[0]);				// using shifts to form the raw_data int string
	raw_data_temp = ((raw_data_temp << 1) | command_code_parity[0]);
	raw_data_temp = ((raw_data_temp << 4) | address_code_copy[0]);
	raw_data_temp = ((raw_data_temp << 3) | address_code_parity[0]);
	
	*raw_data = raw_data_temp;	
}



/* Decodes to extract the Addressed Data according to the project documentation
 * Fill in workbook completely for full credit.
 * If any parity bits are wrong, set *is_valid to 0 and return immediately
 *   Else, set *is_valid to 1 and return when workbook is completely filled in.
 */
void codec_decode_data(long int raw_data, Decode_Workbook *workbook, int *is_valid) {
	long int raw_data_copy = raw_data;				// copy of raw data to not accidentally modify incoming raw data value

	/* working with END bit */
	long int end_bit_mask = 1; 			// mask to extract the LSB
	long int decoded_end_bit = (raw_data_copy & end_bit_mask);	// extracting the LSB
	workbook->end = decoded_end_bit;				// storing the end bit to the data struct
	
	if(workbook->end != 0)	// if the end bit is not 0, return
	{
		*is_valid = 0;
		return;
	}



	/* working with PREAMBLE bits */
	long int decoded_preamble = 0;					// variable to store the decoded premable
	long int preamble_mask = 0xa;					// mask to extract the preamble bits
	decoded_preamble = raw_data_copy;				// setting copy variable to be the same as raw data to be able to work with it
	decoded_preamble = (decoded_preamble >> 45);			// right shift to move premable bits to the LSB's
	decoded_preamble = (decoded_preamble & preamble_mask);		// bitwise AND to extract final 9 bits
	workbook->preamble = decoded_preamble;				// storing the premable to the data struct

	if(workbook->preamble != 0x0a)		// if the preamble is not correct, return
	{
		*is_valid = 0;
		return;
	}



	/* working with DECODE SEGMENTS */
	long int decoded_segment_mask = 0x1ff; 				// mask for extracting last 9 LSB's
	long int decoded_segment[4];
	
	decoded_segment[0] = ((raw_data_copy >> 36) & decoded_segment_mask);	// extracting the 9 bit decoded segments from raw data
	decoded_segment[1] = ((raw_data_copy >> 27) & decoded_segment_mask);
	decoded_segment[2] = ((raw_data_copy >> 18) & decoded_segment_mask);
	decoded_segment[3] = ((raw_data_copy >> 9) & decoded_segment_mask);

	/* DATA portion of decode segments */
	long int decoded_data[4];	// setting an array to hold each of the 8 bit decoded data from the 9 bit segments
	for(int i = 0; i < 4; i++)	// extracting the 8 bit data from the 9 bit segments
	{
		decoded_data[i] = ((decoded_segment[i] >> 1) & 0xff);
	}
	
	for(int i = 0; i < 4; i++)	// storing each 4 bit decoded bit to the workbook
	{
		workbook->segment[i].data = decoded_data[i];
	}
	
	/* PARITY portion of decode segments */
	int received_parity[4];		// setting an array to hold RECEIVED parity bits from the raw data
	for(int i = 0; i < 4; i++)	// extracting the parity bits from the decoded segments
	{
		received_parity[i] = (decoded_segment[i] & 1);
	}

	
	int calculated_parity[4];	// an array to hold manually calculated parity bits from the 4 bit data values
	decoded_data_parity_calculator(decoded_data, calculated_parity, 4, 8);
	
	
	for(int i = 0; i < 4; i++)	// storing each of the parity bits to the workbook
	{
		workbook->segment[i].parity = received_parity[i];
	}
	
	for(int i = 0; i < 4; i++)	// if the received parity bits and calculated parity bits do not match, return
	{
		if(workbook->segment[i].parity != calculated_parity[i])
		{
			*is_valid = 0;
			return;
		}
	}

	

	/* working with PARITY WORD bits */
	long int decoded_parity_word = 0;				// variable for decoded parity word from raw data
	long int parity_word_mask = 0xff;				// mask for extracting parity word from raw data
	decoded_parity_word = (raw_data_copy >> 1);			// shift to get rid of the end bit
	decoded_parity_word = (decoded_parity_word & parity_word_mask);	
	workbook->parity_word = decoded_parity_word;			// storing the value of the parity word bits into the workbook

	long int calculated_parity_word_bit_0 = 0;				// set of variables for manually decoding the parity word bits
	long int calculated_parity_word_bit_1 = 0;
	long int calculated_parity_word_bit_2 = 0;
	long int calculated_parity_word_bit_3 = 0;
	long int calculated_parity_word_bit_4 = 0;
	long int calculated_parity_word_bit_5 = 0;
	long int calculated_parity_word_bit_6 = 0;
	long int calculated_parity_word_bit_7 = 0;

	parity_word_bit_decoder (raw_data_copy, 10, &calculated_parity_word_bit_0, 4, 9);	// set of function calls to helper function to manually calculate parity word bits by columns
	parity_word_bit_decoder (raw_data_copy, 11, &calculated_parity_word_bit_1, 4, 9);
	parity_word_bit_decoder (raw_data_copy, 12, &calculated_parity_word_bit_2, 4, 9);
	parity_word_bit_decoder (raw_data_copy, 13, &calculated_parity_word_bit_3, 4, 9);
	parity_word_bit_decoder (raw_data_copy, 14, &calculated_parity_word_bit_4, 4, 9);
	parity_word_bit_decoder (raw_data_copy, 15, &calculated_parity_word_bit_5, 4, 9);
	parity_word_bit_decoder (raw_data_copy, 16, &calculated_parity_word_bit_6, 4, 9);
	parity_word_bit_decoder (raw_data_copy, 17, &calculated_parity_word_bit_7, 4, 9);

	long int calculated_parity_array[8];		// creating an array for the calculated bits to work with in for loop
	calculated_parity_array[0] = calculated_parity_word_bit_0;
	calculated_parity_array[1] = calculated_parity_word_bit_1;
	calculated_parity_array[2] = calculated_parity_word_bit_2;
	calculated_parity_array[3] = calculated_parity_word_bit_3;
	calculated_parity_array[4] = calculated_parity_word_bit_4;
	calculated_parity_array[5] = calculated_parity_word_bit_5;
	calculated_parity_array[6] = calculated_parity_word_bit_6;
	calculated_parity_array[7] = calculated_parity_word_bit_7;

	long int calculated_parity_word_string = 0;	// using shifts in a for loop to string together the bits for the parity word
	calculated_parity_word_string = (calculated_parity_word_string | calculated_parity_array[7]);
	for(int i = 6; i >= 0; i--)
	{
		calculated_parity_word_string = ((calculated_parity_word_string << 1) | calculated_parity_array[i]);
	}
	
	if(workbook->parity_word != calculated_parity_word_string)	// if the received parity word and the calculated parity word does not match, return
	{
		*is_valid = 0;
		return;
	}
	


	/* creating the DECODED DATA */
	long int decoded_data_number_string = 0;
	decoded_data_number_string = (decoded_data_number_string | decoded_data[0]);		// creating the decoded data using shifts
	for(int i = 1; i < 4; i++)
	{
		decoded_data_number_string = ((decoded_data_number_string << 8) | decoded_data[i]);
	}
	workbook->decoded_data = decoded_data_number_string;	// storing the resulting decoded data hex string into the workbook


	*is_valid = 1; 

}



/* Helper function to be able to manually calculate the odd parity for the  columns of 
 * the parity word bit using the long int raw_data
 */
void parity_word_bit_decoder(long int raw_data, int shifter, long int *parityword_bit, int number_of_data, int number_of_columns)
{
	long int temp;
	int bit_checker = 0;
	int counter = 0;
	
	temp = (raw_data >> shifter);	// depending on which column i am calculating the parity, the shifter will move
					// the appropriate starting bit to the LSB

	for(int i = 0; i < number_of_data; i++)	// will iterate the total number of decode segment rows
	{
		bit_checker = (temp & 1);
		if(bit_checker % 2 == 1)	// calculates each of the 10 column bits to see if its a 1 or a 0
		{
			 counter++;		// if the bit is a 1, then the counter is increased
		}
		temp = (temp >> number_of_columns);		// shifts the raw data according to the right number of column bits
	}
		
	if(counter % 2 == 1)			// if there is an odd number of 1's, parity bit is set to 1
	{
		*parityword_bit = 0x1;
	}
	else					// if there is an even number of 1's, parity bit is set to 0
	{
		*parityword_bit = 0x0;
	}
}



/* Helper function to be able to manually calculate the odd parity for each of the 
 * data sections of the decode segments
 */

void decoded_data_parity_calculator(long int decoded_data[], int parity[], int number_of_data, int data_length)
{
	long int temp;
	int counter = 0;
	int bit_checker = 0;

	for(int i = 0; i < number_of_data; i++) // will iterate through up to the number of total decoded data
	{
		temp = decoded_data[i];

		for(int j = 0; j < data_length; j++) // data_length represents the number of bits for the decoded data segment
		{
			bit_checker = (temp & 1); // for each decoded data, all the  bits will be checked if its a 1 or 0
			if(bit_checker % 2 == 1)
			{
				counter++;	// if the bit is a 1 counter is increased
			}
			temp = (temp >> 1); // shifting to check each bit
		}
		if(counter % 2 == 1)	// if counter is odd, parity is set to 1
		{
			parity[i] = 1;
		}
		else			// if counter is even, parity is set to 0
		{
			parity[i] = 0;
		}
		
		counter = 0;		// resetting counter and bit_checker for next iteration of decoded_data
		bit_checker = 0;
	}
}
