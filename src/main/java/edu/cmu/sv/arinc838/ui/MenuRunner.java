/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 18, 2012
 */
package edu.cmu.sv.arinc838.ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;
import edu.cmu.sv.arinc838.ui.item.MenuItem;

public class MenuRunner {
	private SoftwareDefinitionFileBuilder builder;
	private static final BufferedReader br = new BufferedReader(
			new InputStreamReader(System.in));

	private MenuRunner() {
		builder = new SoftwareDefinitionFileBuilder();
	}

	private void runMenu(Menu menu) {
		while (menu != null) {
			StringBuffer sb = new StringBuffer();

			if (menu.getHeader() != null) {
				sb.append(menu.getHeader() + "\n");
			}

			MenuItem[] items = menu.getItems();
			for (int i = 0; i < items.length; ++i) {
				sb.append(i + ": " + items[i].getPrompt() + "\n");
			}

			System.out.println("\n\n" + sb);
			System.out.print("Please make a selection: ");

			Menu subMenu = null;

			try {
				int convertedValue = inInt(br);

				MenuItem itemToDo = menu.getItems()[convertedValue];

				subMenu = itemToDo.execute(builder);
			} catch (Exception e) {
				// try again?
				e.printStackTrace();
			}

			if (subMenu != null) {
				runMenu(subMenu);
			} else {
				break;
			}
		}
	}

	public static int inInt(BufferedReader br) throws Exception {
		String inString = br.readLine();
		return Integer.valueOf(inString.trim()).intValue();
	}

	public static void main(String[] args) {
		MenuRunner mr = new MenuRunner();

		mr.runMenu(new InitalMenu());
	}
}
