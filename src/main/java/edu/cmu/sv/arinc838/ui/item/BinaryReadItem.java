package edu.cmu.sv.arinc838.ui.item;

import java.io.File;

import edu.cmu.sv.arinc838.binary.BdfFile;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;

public class BinaryReadItem extends AbstractMenuItem{
	
	public BinaryReadItem(String prompt) {
		super(prompt);
	}
	
	@Override
	public MenuItem[] execute(SoftwareDefinitionFileDao sdfDao) throws Exception {		
		String fileName = promptForResponse("Which file?");
						
		BdfFile binary = new BdfFile(new File(fileName));
				
		sdfDao.initialize(binary);
		
		System.out.println("Successfully read in " + fileName);
		
		return super.getEmptyItems();
	}

}
