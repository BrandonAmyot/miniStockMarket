// ------------------------------------------------------- 
// Assignment 3
// Written by: Brandon Amyot 26990940
// For COMP 249 Section U – Winter 2016
// -------------------------------------------------------
/*
 *  This class holds the required data for Offers as well as the appropriate 
 *  toString() and printFullDetails() methods to print out Offers
 */
public class OfferOrder extends Order implements anonymous {

	// Constructors
	OfferOrder()
	{
		setID(null);
		setVolume(0);
		setPrice(0);
	}
	
	OfferOrder(String ID, double price, int volume)
	{
		setID(ID);
		setPrice(price);
		setVolume(volume);
	}
	
	// to print details WITHOUT the ID
	@Override
	public String toString()
	{
		return "Off: $" + this.getPrice() + " " + this.getVolume();
	}
	
	// to print the details WITH the ID
	@Override
	public String printFullDetails() 
	{
		return "Off: $" + this.getPrice() + " " + this.getVolume() + " " + this.getID();
	}	
}
