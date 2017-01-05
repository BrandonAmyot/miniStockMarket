// ------------------------------------------------------- 
// Assignment 3
// Written by: Brandon Amyot 26990940
// For COMP 249 Section U – Winter 2016
// -------------------------------------------------------
/*
 *  This class holds the required data for Bids as well as the appropriate 
 *  toString() and printFullDetails() methods to print out Bids
 */
public class BidOrder extends Order implements anonymous {
	
	// Constructors
	BidOrder()
	{
		setID(null);
		setVolume(0);
		setPrice(0);
	}
	
	BidOrder(String ID, double price, int volume)
	{
		setID(ID);
		setPrice(price);
		setVolume(volume);
	}
	
	// to print details WITHOUT the ID
	@Override
	public String toString()
	{
		return "Bid: $" + this.getPrice() + " " + this.getVolume();
	}
	// to print the details WITH the ID
	@Override
	public String printFullDetails() 
	{
		return "Bid: $" + this.getPrice() + " " + this.getVolume() + " " + this.getID();
	}
}
