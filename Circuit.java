package juggleFast;

import java.util.Comparator;
import java.util.PriorityQueue;

import juggleFast.Circuit.AddResult.Result;

/**
 * Representation of a Circuit. 
 * 
 * @author AriSharon
 *
 */
public class Circuit {
	private int H;
	private int E;
	private int P;
	private PriorityQueue<HeapEntry> jugglersHeap; 
	private int numOfJugglers;
	private int capacity;
	
	/**
	 *  Constructor
	 */
	public Circuit(int h, int e, int p)
	{
		numOfJugglers = 0;
		H = h;
		E = e;
		P = p;
		
		// Implement comprator for mean heap
		jugglersHeap = new PriorityQueue<HeapEntry>(10, new Comparator<HeapEntry>() {

			@Override
			public int compare(HeapEntry o1, HeapEntry o2) {
				if (o1.totalSkill < o2.totalSkill)
				{
					return -1;
				}
				
				if (o1.totalSkill > o2.totalSkill)
				{
					return 1;
				}
				
				return 0;
			}

			
		});
		capacity = -1; // capacity is set later
	}
	
	/*
	 * Accessors
	 */
	
	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getH() {
		return H;
	}

	public int getE() {
		return E;
	}

	public int getP() {
		return P;
	}

	public int getNumOfJugglers() {
		return numOfJugglers;
	}
	
	/**
	 *   @param Juggler, total skill for this circuit
	 *   @return Addresult 
	 *   				
	 */
	public AddResult TryAdd (Juggler j, int totalSkill)
	{
		AddResult res = null;
		if (numOfJugglers < capacity)
		{
			jugglersHeap.add(new HeapEntry(totalSkill,j));
			res = new AddResult(Result.ADDED, null);
			numOfJugglers++;
		}
		
		else if (numOfJugglers >= capacity)
		{
			HeapEntry worstJuggler = jugglersHeap.peek();
			
			// insert juggler only if it has totalSkill bigger than the worst Juggler in this 
			// circuit
			if (totalSkill > worstJuggler.totalSkill)
			{
				jugglersHeap.remove();
				jugglersHeap.add(new HeapEntry(totalSkill, j));
				res = new AddResult(Result.REPLACED, worstJuggler.jugler);
			}
			else 
			{
				res = new AddResult(Result.FAILED, null);
			}
		}
		
		return res;
	}
	
	/**
	 *  @return True if the Circuit is Full, False otherwise.
	 */
	public boolean isFull()
	{
	  return (numOfJugglers >= capacity);
	}
	
	/**
	 * 
	 * @return string of all Jugglers in the Circuit in the desired format
	 */
	public String printJugglers()
	{
		StringBuilder sb = new StringBuilder();
		
		for (HeapEntry entry : jugglersHeap) {
		  	sb.append((entry.jugler.printJuggler() + ","));
		}
		
		// Delete the last ',' char 
		if (sb.length() > 1) {
			sb.replace(sb.length() -1, sb.length(), "");
		}
		
		return sb.toString();
		
	}
	
	@Override
	public String toString() {
		return "Circuit [H=" + H + ", E=" + E + ", P=" + P + ", numOfJugglers="
				+ numOfJugglers + "]";
	}
	/**
	 *  Inner Class
	 *  HeapEntry holds a juggler and his total skill for this circuit
	 */
	public static class HeapEntry
	{
		int totalSkill;
		Juggler jugler;
		@Override
		public String toString() {
			return "HeapEntry [totalSkill=" + totalSkill + ", jugler=" + jugler
					+ "]";
		}
		public HeapEntry(int totalSkill, Juggler jugler) {
			this.totalSkill = totalSkill;
			this.jugler = jugler;
		}
		
	}
	
	/**
	 *  Inner Class
	 *  Addresult is the returned object after TryAdd method
	 */
	public static class AddResult
	{
		public enum Result {
			FAILED, ADDED, REPLACED
		}
		
		Result res;
		public Result getRes() {
			return res;
		}

		public Juggler getJ() {
			return j;
		}

		Juggler j;
		
		public AddResult(Result status, Juggler jugIn)
		{
			j = jugIn;
			res = status;
		}
	}

 }
