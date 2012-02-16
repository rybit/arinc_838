package edu.cmu.sv.arinc838.ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;

public class XmlReadItem implements MenuItem {
	private String prompt;
	
	public XmlReadItem (String prompt) {
		this.prompt = prompt;
	}
	
	@Override
	public Menu execute(SoftwareDefinitionFileBuilder builder) throws Exception {
		System.out.println ("Which file? ");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String ret = br.readLine();
		System.out.println (ret);
		// open 
		// unmarshal
		// create new builder?
//		builder.initialize (unmarshalledFile);

		// evntually do something more
		return null;
	}
	
	@Override
	public String getPrompt() {
		return prompt;
	}
	
}
