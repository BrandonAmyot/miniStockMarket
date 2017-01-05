// ------------------------------------------------------- 
// Assignment 3
// Written by: Brandon Amyot 26990940
// For COMP 249 Section U – Winter 2016
// -------------------------------------------------------
/*
 *  This class holds the data required for Orders
 */
public class Order implements anonymous {
	
	private String ID;
	private double price;
	private int volume;
	
	// getters
	public String getID() {return ID;}
	public double getPrice() {return price;}
	public int getVolume() {return volume;}
	
	// setters
	public void setID(String ID) {this.ID = ID;}
	public void setPrice(double price) {this.price = price;}
	public void setVolume(int volume) {this.volume = volume;}
	
	// Constructors
	public Order()
	{
		ID = null;
		price = 0.00;
		volume = 0;
	}
	
	public Order(String ID, double price, int volume)
	{
		this.ID = ID;
		this.price = price;
		this.volume = volume;
	}
	
	// to print details WITHOUT the ID
	public String toString()
	{
		return this.getPrice() + " " + this.getVolume();
	}
	
	// to print the details WITH the ID
	public String printFullDetails() 
	{
		return this.getPrice() + " " + this.getVolume() + " " + this.getID();
	}
}
