package edu.cmu.sv.arinc838.ui;

import java.util.List;

import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.reader.BdfReader;
import edu.cmu.sv.arinc838.reader.XdfReader;
import edu.cmu.sv.arinc838.ui.item.AbstractMenuItem;
import edu.cmu.sv.arinc838.ui.item.ExitItem;
import edu.cmu.sv.arinc838.ui.item.MenuItem;
import edu.cmu.sv.arinc838.ui.item.SdfReadItem;
import edu.cmu.sv.arinc838.validation.SdfChecker;

public class ComparisonMenu implements Menu {
	private SoftwareDefinitionFileDao firstFile, secondFile;
	private String firstFilename, secondFilename;

	private class LoadFileMenuItem extends AbstractMenuItem {
		private SoftwareDefinitionFileDao dao;
		private SdfReadItem reader;

		public LoadFileMenuItem(String prompt, SoftwareDefinitionFileDao dao, SdfReadItem reader) {
			super(prompt);
			this.dao = dao;
			this.reader = reader;
		}

		@Override
		public MenuItem[] execute(SoftwareDefinitionFileDao sdfDao) throws Exception {
			reader.execute(dao);
			return super.getEmptyItems();
		}
	}

	public ComparisonMenu() {
		firstFile = new SoftwareDefinitionFileDao();
		secondFile = new SoftwareDefinitionFileDao();
	}

	@Override
	public MenuItem[] getItems() {
		SdfReadItem xdf = new SdfReadItem("xdf", new XdfReader());
		SdfReadItem bdf = new SdfReadItem("xdf", new BdfReader());

		return new MenuItem[] { new LoadFileMenuItem("Load first file from XML", firstFile, xdf),
				new LoadFileMenuItem("Load first file from Binary", firstFile, bdf),
				new LoadFileMenuItem("Load second file from XML", secondFile, xdf),
				new LoadFileMenuItem("Load second file from Binary", secondFile, bdf), 
				new AbstractMenuItem ("Compare"){
					@Override
					public MenuItem[] execute(SoftwareDefinitionFileDao sdfDao) throws Exception {
						if (firstFile != null && secondFile != null) {
							SdfChecker checker = new SdfChecker ();
							List<String> results = checker.compare(firstFile, secondFile);
							
							System.out.println("Compared the two files and found the following differences:");
							for(int i=0; i<results.size(); i++) {
								System.out.println("  " + (i+1) + ". " + results.get(i));
							}

						} else {
							System.out.println ("Can't compare without both files specified");
						}
						
						return super.getEmptyItems();
					}
				},
				new ExitItem()
		};
	}
	
	@Override
	public String getHeader() {
		return "First File: " + firstFilename + 
				"\nSecond File: " + secondFilename;
	}
}
