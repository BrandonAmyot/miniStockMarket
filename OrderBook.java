// ------------------------------------------------------- 
// Assignment 3
// Written by: Brandon Amyot 26990940
// For COMP 249 Section U – Winter 2016
// -------------------------------------------------------
/* This program will implement a matching engine for a mock stock market.
 * The program has a pre-implemented OrderBook object of bids and offers.
 * The user may enter a new Order of type Bid or Offer with the price, volume, and ID.
 * The program will then attempt to match the Order to a pre-existing bid or offer to satisfy the order.
 * If a match is found it will modify the Orders accordingly and remove or insert the Orders as required.
 * If no match is found it will simply insert the Order into the correct location.
 * The user may also print out the best Bid or best Offer, or view the Order book without IDs.
 */

import java.util.Scanner;

public class OrderBook {

	// start of inner class
	private class Node {
		private Order data;
		private Node next;
		private Node previous;

		public Node(Order newOrder) {
			data = newOrder;
			next = null;
			previous = null;
		}
	} // end of inner class

	private Node head;
	private int size;
	private Node bestBid = null;
	private Node bestOffer = null;

	public OrderBook() {
		head = null;
		size = 0;
	}

	/*
	 * Add new values to the linked list. It will add orders in order from
	 * highest to lowest price. Offers will be kept before bids
	 */
	public void insert(Order data) {
		if (head == null) // if the list is empty
		{
			head = new Node(data);
			size += 1;
			return;
		}

		if (data.getPrice() > head.data.getPrice()) // to ensure highest price
													// will always show up
													// first
		{
			Node temp = head;
			Node newHead = new Node(data);
			head = newHead;
			head.next = temp;
			temp.previous = newHead;

			size += 1;
			return;
		}

		Node tempNode = head;
		
		if (data instanceof OfferOrder) {	// to index where OFFERS should be inserted
			while (tempNode.next != null && tempNode.next.data.getPrice() > data.getPrice()) {
				tempNode = tempNode.next;
			}			
		}
		else {	// to index where BIDS should be inserted
			while (tempNode.next != null && tempNode.next.data.getPrice() >= data.getPrice()) {
				tempNode = tempNode.next;
			}
		}

		Node previousTemp = tempNode;
		Node insertedNode = new Node(data);

		if (tempNode.next != null) {	// to adjust the pointers in the linked list.
			Node nextTemp = tempNode.next;
			insertedNode.next = nextTemp;
			nextTemp.previous = previousTemp;
		}
		previousTemp.next = insertedNode;
		insertedNode.previous = previousTemp;

		size += 1;
		updateBestOffer();
		updateBestBid();
	}
	// to remove the according Order by adjusting the pointers of the surrounding nodes.
	public void delete(Node best) {
		Node tempPrevious, tempNext; 

		if (best.previous != null && best.next != null) {
			tempPrevious = best.previous;
			tempNext = best.next;
			best.previous = null;
			best.next = null;
			tempPrevious.next = tempNext;
			tempNext.previous = tempPrevious;
		} else if (head.next.data instanceof BidOrder) {
			tempNext = best.next;
			best.next = null;
			best.previous = null;
			head = tempNext;
		} else if (best.next == null) {
			tempPrevious = best.previous;
			best.previous = null;
			tempPrevious.next = null;
		}
		size -= 1;
	}

	// to update the BestOffer
	public void updateBestOffer() {
		Node tempNode = head;
		bestOffer = null;

		if (size == 0) { // if there are no offers left.
			return;
		}
		while (tempNode != null) {
			if (tempNode.data instanceof OfferOrder) {
				if (bestOffer == null) {
					bestOffer = tempNode;
				} else if (tempNode.data.getPrice() < bestOffer.data.getPrice()) {
					bestOffer = tempNode;
				} else {
					tempNode = tempNode.next;
				}
			} else {
				tempNode = tempNode.next;
			}
		}
		return;
	}

	// to update the bestBid
	public void updateBestBid() {
		Node tempNode = head;
		bestBid = null;

		if (size == 0) { // if there are no bids left.
			return;
		}
		if (tempNode.data instanceof BidOrder) {
			bestBid = head;
			return;
		}
		while (tempNode != null) {
			if (tempNode.data instanceof BidOrder) {
				bestBid = tempNode;
				break;
			} else {
				tempNode = tempNode.next;
			}
		}
		return;
	}

	// print the list
	public void outputBook() {
		Node tempNode = head;

		System.out.println("Order Book:");
		while (tempNode != null) {
			System.out.println(tempNode.data);
			tempNode = tempNode.next;
		}
		System.out.println("--------------------");
	}

	// print the BestBid and BestOffer
	public void outputBBO() {
		if (bestBid == null && bestOffer == null) {
			System.out.println("No orders!");
		} else if (bestBid == null && bestOffer != null) {
			System.out.println("Best offer: \n" + bestOffer.data);
			System.out.println("There are no bids.");
		} else if (bestBid != null && bestOffer == null) {
			System.out.println("Best bid: \n" + bestBid.data);
			System.out.println("There are no offers.");
		} else {
			System.out.println("Best bid and best offer: ");
			System.out.println(bestOffer.data + " " + bestBid.data);
		}
		System.out.println("--------------------");
	}

	// to match orders together
	public void matchingEngine(Order order) {
		System.out.println("Matching order:");
		System.out.println(order);
		System.out.println("--------------------");

		if (order instanceof OfferOrder) { // to match an offer to a bid
			if (bestBid == null) { // if there are no bids
				insert(order);
			}
			/* While the order has shares the engine will continue to attempt to match
			 * the order until it has no more shares or there is no match to be made
			 */
			while (order.getVolume() > 0 && bestBid != null) {
				/*
				 * if the order has a lower or equal price to the best bid they are matched together.
				 */
				if (order.getPrice() <= bestBid.data.getPrice()) {
					System.out.println("Match found:");
					System.out.println(order.printFullDetails());
					System.out.println(bestBid.data.printFullDetails());
					System.out.println("--------------------");

					Order tempOrder = new Order();
					tempOrder.setVolume(order.getVolume());
					order.setVolume(order.getVolume() - bestBid.data.getVolume());
					bestBid.data.setVolume(bestBid.data.getVolume() - tempOrder.getVolume());
					/*
					 * if the best bid is empty, remove it then update the best bid and continue matching
					 */
					if (bestBid.data.getVolume() <= 0) { 
						delete(bestBid);
					}
					updateBestBid();
					updateBestOffer();
				} else {
					System.out.println("No match found.");
					System.out.println("--------------------");
					break;
				}
			}
			if (order.getVolume() > 0) {
				insert(order);
			}
		} else // to match a bid to an offer.
		{
			if (bestOffer == null) { // if there are no offers, insert bid.
				insert(order);
			}
			/* While the order has shares the engine will continue to attempt to match
			 * the order until it has no more shares or there is no match to be made
			 */
			while (order.getVolume() > 0 && bestOffer != null) {
				/*
				 * if the order has a lower or equal price to the best bid they are matched together.
				 */
				if (order.getPrice() >= bestOffer.data.getPrice()) {
					System.out.println("Match found:");
					System.out.println(order.printFullDetails());
					System.out.println(bestOffer.data.printFullDetails());
					System.out.println("--------------------");

					Order tempOrder = new Order();
					tempOrder.setVolume(order.getVolume());
					order.setVolume(order.getVolume() - bestOffer.data.getVolume());
					bestOffer.data.setVolume(bestOffer.data.getVolume() - tempOrder.getVolume());
					/*
					 * if the best bid is empty, remove it then update the best bid and continue matching
					 */
					if (bestOffer.data.getVolume() <= 0) {
						delete(bestOffer);
					}
					updateBestOffer();
					updateBestBid();
				} else {
					System.out.println("No match found.");
					System.out.println("--------------------");
					break;
				}
			}
			if (order.getVolume() > 0) {
				insert(order);
			}
		}

		// update the bestBids and Offers at the end then output the book after matches have been made.
		updateBestBid();
		updateBestOffer();
		outputBook();
	}

	// Menu user-interface for user input
	public static void mainMenu(OrderBook book) {
		Scanner keyboard = new Scanner(System.in);

		System.out.println("What would you like to do?");
		System.out.println("\t1. Enter a Bid");
		System.out.println("\t2. Enter an Offer");
		System.out.println("\t3. Display the order book");
		System.out.println("\t4. Display the best offer and best bid");
		System.out.println("\t5. Quit");
		System.out.print("Please enter your choice: ");
		int userChoice = keyboard.nextInt();
		System.out.println("--------------------");
		
		switch (userChoice) { // switch case to handle input
			case 1:
				System.out.print("Enter the price, volume, and name: ");
				double userBidPrice = keyboard.nextDouble();
				int userBidVolume = keyboard.nextInt();
				String userBidName = keyboard.nextLine();
				System.out.println("--------------------");
				book.matchingEngine(new BidOrder(userBidName, userBidPrice, userBidVolume));
				break;
			case 2:
				System.out.print("Enter the price, and volume, and name: ");
				double userOfferPrice = keyboard.nextDouble();
				int userOfferVolume = keyboard.nextInt();
				String userOfferName = keyboard.nextLine();
				System.out.println("--------------------");
				book.matchingEngine(new OfferOrder(userOfferName, userOfferPrice, userOfferVolume));
				break;
			case 3:
				book.outputBook();
				break;
			case 4:
				book.outputBBO();
				break;
			case 5:
				System.out.println("Program ending...");
				keyboard.close();
				System.exit(0);
				break;
			default:
				System.out.println("Pick an option between 1 and 5");
		}
	}

	// driver
	public static void main(String[] args) {
		OrderBook book = new OrderBook();

		// preloading the book with default orders
		book.insert(new OfferOrder("Steve", 155.0, 300));
		book.insert(new BidOrder("Jennifer", 146.60, 100));
		book.insert(new BidOrder("Paul", 147.0, 200));
		book.insert(new OfferOrder("Michelle", 152.50, 120));
		book.insert(new OfferOrder("Ryan", 152.0, 100));
		book.insert(new BidOrder("Alan", 148.0, 75));
		book.insert(new BidOrder("Sarah", 146.50, 50));

		//run the menu method until user ends program
		book.outputBook();
		System.out.println("--------------------------------");
		System.out.println("     Brandon's Stock Market");
		System.out.println("--------------------------------");
		while (true) {
			mainMenu(book);
		}
	}

}
