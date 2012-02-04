package edu.cmu.sv.arinc838;

public class HelloTeam {
	
	
	private double myNumber;


	public HelloTeam(double myNumber)
	{
		this.myNumber = myNumber;
	}
	
	public double getMyNumber()
	{
		return myNumber;
	}
	
	public void setMyNumber(double newNumber)
	{
		myNumber = newNumber;
	}
	
	@Override
	public String toString()
	{
		return "My spoon is too big.";
	}
	
	
	/**
	 * Method for demonstrating testing for exceptions
	 * @param o
	 */
	public void throwException(Object o)
	{
		System.out.println(o.toString());
	}
	
	
	public static void main (String[] args) {
		System.out.println ("Hi Team - this is a place holder!");
	}
}
