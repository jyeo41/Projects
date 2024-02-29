/* Fill in your Name and GNumber in the following two comment fields
 * Name: Joon Yeo
 * GNumber: G00953081
 */
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "fp.h"

void swap_int_array(int array[], int i, int j);
void swap_float_array(float array[], int i, int j);


/* Checks if -0.0 on input value.
 * Returns 1 if -0.0
 * Returns 0 otherwise.
 */
static int is_negative_zero(float val) {
  return (val == 0.0 && ((1.f / val) < 0));
}


int compute_fp(float val) {
  /* Implement this function */
	float temp = val;
	int S = 0;
	int bias = 15; // (2^5-1) - 1
	int E = 0;
	int frac = 0;
	int return_val = 0;
	int exp = 0;
	
	
	if (val > 0)	// if the floating point is a postiive float
	{
		S = 0;	// sign bit = 0
		if (fabs(val) < 1)	// using absolute value for easier calculation
		{
		
			// if the floating point is less than 1
			while (temp < 1)
			{
				temp = temp * 2;	// keep multiplying by 2 to adjust to be > 1.0
				E--;			// decrement E for adjustment
			}
		}
		else if (fabs(val) > 2)
		{
		
			// if the floating point is greater than 2
			while (temp > 2)
			{
				temp = temp / (float)2;	// keep dividing by 2 to adjust to be < 2.0
				E++;			// increment E for adjustment
			}
		}
		
		temp -= 1;	// subtracting by 1 to get the frac part of the mantissa
		frac = (int)(temp * 512); // multiplying by 512 to get the numerator of the frac and int casting to truncate
		
	}
	else if (val < 0)	// if the floating point is a negative float
	{
		S = 1;		// sign is == 1
		if (fabs(val) < 1)	// using absolute value to make the arithmetic easier
		{		
			while (fabs(temp) < 1)	
			{
				temp = temp * 2;
				E--;
			}
		}
		else if (fabs(val) > 2)
		{
			while (fabs(temp) > 2)
			{
				temp = temp / (float)2;	// float casting to not lose precision
				E++;
			}
		}
		temp += 1;	// adding 1 in this case because its -1.frac to extract the frac
		frac = abs((int)(temp * 512));
		
	}
	
	else
	{
		return 0;
	}
	
	exp = E + bias;
	if (exp == 0)	// if denormalized float return 0
	{
		return 0;
	}

	if(exp >= 31)	// if infinity overflow
	{
		if (S == 0)	// if positive
		{
			return 0x3E00;	// return positive infinity
		}
		else
		{
			return 0x7E00;	// if negative return negative infinity
		}
	}
	return_val = return_val | S;
	return_val = return_val << 5;
	return_val = return_val | exp;
	return_val = return_val << 9;
	return_val = return_val | frac;
	return_val = return_val | 0x00000000;
	
	printf("Return Value: %d\n", return_val);
  return return_val;
}


float get_fp(int val) {
  /* Implement this function */
	int temp = val;
	float mantissa = 0;
	int S = 0;
	int bias = 15; // (2^5-1) - 1
	int E = 0;
	float frac = 0;
	float return_val = 0;
	int exp = 0;
	
	if ((val >> 14) == 0)	// checking if the int val is negative or positive 
	{			// utilizing shifts
		S = 0;
	}
	else
	{
		S = 1;
	}

	exp = temp >> 9;	// extracting the exp
	exp = exp & 0x1f;

	if (exp == 0)
	{
		return 0;
	}
	
	if (exp >= 31)	// if overflow
	{
		if(S == 0)	// depending on positive or negative sign bit
		{
			return INFINITY;	// return pos INF
		}
		else
		{
			return -INFINITY;	// return neg INF
		}
	}

	frac = temp & 0x1ff;	// extracting the frac
	frac = frac / (float)512;	// float cast to not lose precision
	mantissa = 1 + frac;		// adding 1 to frac to calculate the mantissa
	E = exp - bias;			// calculating E
	return_val = pow(-1, S);	// multiplying everything using the formula
	return_val *= pow(2, E);
	return_val *= mantissa;

  return return_val;
}


int mult_vals(int source1, int source2) {
  /* Implement this function */
	
	int bias = 15;
	int exp_array[2];
	int sign_array[2];
	float mantissa_array[2];
	float frac = 0;
	int M_frac_extract = 0;
	int E_array[2];
	int source_array[2];
	int val = 0;
	int return_val = 0;
	int i = 0;
	int exp = 0;

	source_array[0] = source1;
	source_array[1] = source2;

	for(i = 0; i < 2; i++)	// using for loop to extract data from both sources
	{
		val = source_array[i]; // this is my temp variable
		if ((val >> 14) == 0)	// extracting the sign bit
		{
			sign_array[i] = 0;
		}
		else if ((val >> 14) == 1)
		{
			sign_array[i] = 1;
		}

		exp_array[i] = val >> 9;	// extracting the exp
		exp_array[i] = exp_array[i] & 0x1f;

		if (exp_array[i] == 0)
		{
			return 0;
		}
		
		frac = val & 0x1ff;		// extracting the frac
		frac = frac / (float)512;	// float casting to not lose precision
		mantissa_array[i] = 1 + frac;	// calculating the mantissa
		E_array[i] = exp_array[i] - bias;	// calculating the E using the equation
	}
	
	int sign = sign_array[0] ^ sign_array[1];	// doing multiplcation arithmetic from here on out
	float mantissa = mantissa_array[0] * mantissa_array[1];
	int E = E_array[0] + E_array[1];

	if (mantissa < 1)	// mantissa adjustment to fix the E
	{
	
		// if the floating point is less than 1
		while (mantissa < 1)
		{
			mantissa = mantissa * 2;
			E--;
		}
	}
	else if (mantissa >= 2)
	{
	
		// if the floating point is greater than 2
		while (mantissa >= 2)
		{
			mantissa = mantissa / (float)2;
			E++;
		}
	}
	
	exp = E + bias;		// calculating new exp in order to create the 15 bit integer representation
	if(exp >= 31)	// if infinity overflow
	{
		if (sign == 0)	// if positive
		{
			return 0x3E00;	// return positive infinity
		}
		else
		{
			return 0x7E00;	// if negative return negative infinity
		}
	}

	frac = mantissa - 1;	// extracting the frac numerator
	M_frac_extract = abs((int)(frac * 512));	
	return_val = return_val | sign;		// shift and bitwise OR's to string the 15 bit representation
	return_val = return_val << 5;
	return_val = return_val | exp;
	return_val = return_val << 9;
	return_val = return_val | M_frac_extract;
	return_val = return_val | 0x00000000;
	return return_val;	// return the int value
}


int add_vals(int source1, int source2) {
  /* Implement this function */
	
	int bias = 15;
	int exp_array[2];
	int sign_array[2];
	int sign = 0;
	float mantissa_array[2];
	float frac = 0;
	int M_frac_extract = 0;
	int E_array[2];
	int source_array[2];
	int val = 0;
	int return_val = 0;
	int i = 0;
	int exp = 0;

	source_array[0] = source1;
	source_array[1] = source2;
	
	if((source1 & 0x3fff) > (source2 & 0x3fff))
	{
		sign = (source1 >> 14);
	}
	
	else
	{
		sign = (source2 >> 14);
	}
	
	for(i = 0; i < 2; i++)	// using for loop to extract data from both sources
	{
		val = source_array[i]; // this is my temp variable
		if ((val >> 14) == 0)	// extracting the sign bit
		{
			sign_array[i] = 0;
		}
		else if ((val >> 14) == 1)
		{
			sign_array[i] = 1;
		}

		exp_array[i] = val >> 9;	// extracting the exp
		exp_array[i] = exp_array[i] & 0x1f;

		if (exp_array[i] == 0)		// if the exp = is 0, just return 0
		{
			return 0;
		}
		
		if (exp_array[i] >= 31)
		{
			if (sign_array[i] == 0)	// if positive
			{
				return 0x3E00;	// return positive infinity
			}
			else
			{
				return 0x7E00;	// if negative return negative infinity
			}
		}	

		frac = val & 0x1ff;		// extracting the frac
		frac = frac / (float)512;	// float casting to not lose precision
		mantissa_array[i] = 1 + frac;	// calculating the mantissa
		E_array[i] = exp_array[i] - bias;	// calculating the E using the equation
	}
	
	if (!(E_array[0] >= E_array[1]))	// switching E1 and E2 if E2 is bigger than E1
	{
		swap_int_array(exp_array, 0, 1);
		swap_int_array(sign_array, 0, 1);
		swap_int_array(E_array, 0, 1);
		swap_float_array(mantissa_array, 0, 1);
	}
	if(E_array[1] != E_array[0])
	{
		while(E_array[1] != E_array[0])		// fixing the mantissa to make the E1 and E2 match
		{
			mantissa_array[1] = mantissa_array[1] / (float)2;
			E_array[1]++;
		}
	}
	
	int E = E_array[0];	// using E1 as our designated E to calculate the exp
	float mantissa = 0;	// new variable to calculate mantissa
	if (((sign_array[0] == 0) && (sign_array[1] == 0)) || ((sign_array[0] == 1) && (sign_array[1] == 1)))  // if both numbers are positive or negative, then you add the mantissas
	{
	
		mantissa = mantissa_array[0] + mantissa_array[1];
	}
	
	else	// otherwise subtract the mantissas
	{
		mantissa = mantissa_array[0] - mantissa_array[1];
	}
	
	if (mantissa == 0)
	{
		return 0;
	}

	else if (mantissa < 1)	// mantissa adjustment to fix the E
	{
	
		// if the floating point is less than 1
		while (mantissa < 1)
		{
			mantissa = mantissa * 2;
			E--;
		}
	}
	else if (mantissa >= 2)
	{
	
		// if the floating point is greater than 2
		while (mantissa >= 2)
		{
			mantissa = mantissa / (float)2;
			E++;
		}
	}

	exp = E + bias;		// calculating new exp in order to create the 15 bit integer representation
	frac = mantissa - 1;
	M_frac_extract = abs((int)(frac * 512));
	return_val = return_val | sign;		// shift and bitwise OR's to string the 15 bit representation
	return_val = return_val << 5;
	return_val = return_val | exp;
	return_val = return_val << 9;
	return_val = return_val | M_frac_extract;
	return_val = return_val | 0x00000000;
	return return_val;	// return the int value

  return return_val;
}

// swap functions to be used with E1 >= E2 check for addition 
void swap_int_array(int array[], int i, int j)
{
	int temp = array[i];
	array[i] = array[j];
	array[j] = temp;
}


void swap_float_array(float array[], int i, int j)
{
	float temp = array[i];
	array[i] = array[j];
	array[j] = temp;
}
