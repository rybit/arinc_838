package edu.cmu.sv.arinc838.ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;

public class MenuRunner {
	private SoftwareDefinitionFileBuilder builder;
	private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	private MenuRunner() {
		builder = new SoftwareDefinitionFileBuilder();
	}

	private void runMenu (Menu menu) {
		if (menu != null) {
			StringBuffer sb = new StringBuffer();
			
			if (menu.getHeader() != null) { 
				sb.append(menu.getHeader() + "\n");
			}
			
			MenuItem[] items = menu.getItems();
			for (int i = 0; i < items.length; ++i) {
				sb.append(i + ": " + items[i].getPrompt() + "\n");
			}

			Menu subMenu= null;
			try {
				System.out.println(sb);
				int convertedValue = inInt (br);
				// read in
				// convert to ##
				MenuItem itemToDo = menu.getItems()[convertedValue];

				subMenu = itemToDo.execute(builder);
			} catch (Exception e) {
				// try again?
				e.printStackTrace();
			}

			runMenu  (subMenu);
		}
	}

	public static int inInt(BufferedReader br) throws Exception{
		String inString = br.readLine();
		return Integer.valueOf(inString.trim()).intValue();
	}


	public static void main(String[] args) {
		MenuRunner mr = new MenuRunner();

		mr.runMenu (new InitalMenu ());
	}
}
