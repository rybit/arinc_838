package edu.cmu.sv.arinc838.ui;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.arinc.arinc838.SdfFile;

import edu.cmu.sv.arinc838.xml.XdfWriter;

public class UiRunner {
	private static SdfFile sdfFile;

	public static void main(String[] args) throws Exception {
		mainMenu();
	}

	private static void mainMenu() throws Exception {
		System.out.println("============================");
		System.out.println("| Options:                 ");
		System.out.println("|        1. Select XML File");
		System.out.println("|        2. Exit");
		System.out.println("============================");
		int option = Keyin.inInt(" Select option: ");

		switch (option) {
		case 1:
			String fileName = Keyin.inString(" Enter XML File:");
			File inFile = new File(fileName);
			if (!inFile.exists()) {
				System.out.println("File " + fileName + " does not exist.");
				mainMenu();
			} else {
				JAXBContext jaxbContext = JAXBContext.newInstance(SdfFile.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				sdfFile = (SdfFile) jaxbUnmarshaller.unmarshal(inFile);

				xmlMenu();
			}
			break;
		case 2:
			break;
		}
	}

	private static void xmlMenu() throws Exception {
		System.out.println("============================");
		System.out.println("| Options:");
		System.out.println("|        1. Generate XML File");
		System.out.println("|        2. Verify Data (not yet implemented)");
		System.out.println("|        3. Back");
		System.out.println("|        4. Exit");
		System.out.println("============================");
		int option = Keyin.inInt(" Select option: ");

		switch (option) {
		case 1:
			String outFileName = Keyin.inString("Enter output XML file name:");
			XdfWriter writer = new XdfWriter(sdfFile);
			writer.write(outFileName);
			System.out.println("Wrote file to " + outFileName);
			xmlMenu();
			break;
		case 2:
			System.out.println("Not yet implemented. Select another option.");
			xmlMenu();
			break;
		case 3:
			mainMenu();
			break;
		case 4:
			break;
		}
	}
}
