// ------------------------------------------------------- 
// Assignment 3
// Written by: Brandon Amyot 26990940
// For COMP 249 Section U – Winter 2016
// -------------------------------------------------------
/*
 * This interface differentiates between the two methods for printing Order data either privately or publically.
 */
public interface anonymous {

	public String toString();	// prints details without ID
	
	public String printFullDetails(); // prints details WITH ID
}
