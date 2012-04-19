package edu.cmu.sv.arinc838.ui;

import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.reader.BdfReader;
import edu.cmu.sv.arinc838.reader.XdfReader;
import edu.cmu.sv.arinc838.ui.item.AbstractMenuItem;
import edu.cmu.sv.arinc838.ui.item.ExitItem;
import edu.cmu.sv.arinc838.ui.item.MenuItem;
import edu.cmu.sv.arinc838.ui.item.SaveItem;
import edu.cmu.sv.arinc838.ui.item.SdfReadItem;
import edu.cmu.sv.arinc838.writer.BdfWriter;
import edu.cmu.sv.arinc838.writer.XdfWriter;

public class ConversionMenu implements Menu{
	private static final MenuItem[] items = new MenuItem[] {
		new JoinedItem ("BDF -> XDF", new SdfReadItem("", new BdfReader()), 
									  new SaveItem ("", new XdfWriter())),
		new JoinedItem ("XDF -> BDF", new SdfReadItem("", new XdfReader()), 
									  new SaveItem ("", new BdfWriter())),
		new ExitItem()
	};
	
	private static class JoinedItem extends AbstractMenuItem {
		private MenuItem first, second;
		
		public JoinedItem (String prompt, MenuItem first, MenuItem second) {
			super (prompt);
			this.first = first;
			this.second = second;
		}
		
		@Override
		public MenuItem[] execute(SoftwareDefinitionFileDao sdfDao) throws Exception {
			first.execute(sdfDao);
			second.execute(sdfDao);
			return null;
		}
		
	}
	
	@Override
	public MenuItem[] getItems() {
		return items;
	}

	@Override
	public String getHeader() {
		return "Convert between XDF <-> BDF";
	}

}
