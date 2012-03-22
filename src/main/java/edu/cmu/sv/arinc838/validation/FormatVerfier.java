package edu.cmu.sv.arinc838.validation;

import java.util.ArrayList;
import java.util.List;

import com.arinc.arinc838.SdfFile;

public class FormatVerfier {

	List<VerficationTest> testsToRun;
	
	public List<Exception> runTests (SdfFile sdfFile) {
		List<Exception> retExceptions = new ArrayList<Exception> ();
		
		if (testsToRun != null) { 
			for (VerficationTest test : testsToRun) {
				List<Exception> thisTestsExeptions = test.execute(sdfFile);
				retExceptions.addAll(thisTestsExeptions);
			}
		}
		
		return retExceptions;
	}
}
