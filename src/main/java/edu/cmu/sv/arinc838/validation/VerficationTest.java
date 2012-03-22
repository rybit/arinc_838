package edu.cmu.sv.arinc838.validation;

import java.util.List;

public interface VerficationTest<T> {
	public List<Exception> execute (T sdfFile);
}
