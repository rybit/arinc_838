package edu.cmu.sv.arinc838.ui;

class Keyin {

	// *******************************
	// support methods
	// *******************************
	// Method to display the user's prompt string
	public static void printPrompt(String prompt) {
		System.out.print(prompt + " ");
		System.out.flush();
	}

	// Method to make sure no data is available in the
	// input stream
	public static void inputFlush() {
		int dummy;
		int bAvail;

		try {
			while ((System.in.available()) != 0)
				dummy = System.in.read();
		} catch (java.io.IOException e) {
			System.out.println("Input error");
		}
	}

	// ********************************
	// data input methods for
	// string, int, char, and double
	// ********************************
	public static String inString(String prompt) {
		inputFlush();
		printPrompt(prompt);
		return inString();
	}

	public static String inString() {
		int aChar;
		String s = "";
		boolean finished = false;

		while (!finished) {
			try {
				aChar = System.in.read();
				if (aChar < 0 || (char) aChar == '\n')
					finished = true;
				else if ((char) aChar != '\r')
					s = s + (char) aChar; // Enter into string
			}

			catch (java.io.IOException e) {
				System.out.println("Input error");
				finished = true;
			}
		}
		return s;
	}

	public static int inInt(String prompt) {
		while (true) {
			inputFlush();
			printPrompt(prompt);
			try {
				return Integer.valueOf(inString().trim()).intValue();
			}

			catch (NumberFormatException e) {
				System.out.println("Invalid input. Not an integer");
			}
		}
	}

	public static char inChar(String prompt) {
		int aChar = 0;

		inputFlush();
		printPrompt(prompt);

		try {
			aChar = System.in.read();
		}

		catch (java.io.IOException e) {
			System.out.println("Input error");
		}
		inputFlush();
		return (char) aChar;
	}

	public static double inDouble(String prompt) {
		while (true) {
			inputFlush();
			printPrompt(prompt);
			try {
				return Double.valueOf(inString().trim()).doubleValue();
			}

			catch (NumberFormatException e) {
				System.out
						.println("Invalid input. Not a floating point number");
			}
		}
	}
}