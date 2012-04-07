package edu.cmu.sv.arinc838.ui;

import java.util.List;

public class ErrorPrinter {
	public static void printErrors(String filename, List<Exception> errorList) {
		System.out.println("Failed to read in file '" + filename
				+ "'. Encountered " + errorList.size() + " error(s):");

		for(int i=0; i<errorList.size(); i++) {
			System.out.println("  " + (i+1) + ". " + getExceptionMessage(errorList.get(i)));
		}
	}

	private static String getExceptionMessage(Throwable exception) {
		if(exception == null) {
			return "Unknown error.";
		}
		
		if(exception.getMessage() != null && !exception.getMessage().trim().equals("")) {
			return exception.getMessage();
		}
		
		return getExceptionMessage(exception.getCause());
	}
}
