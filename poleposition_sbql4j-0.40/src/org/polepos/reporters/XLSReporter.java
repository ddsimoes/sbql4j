/* 
This file is part of the PolePosition database benchmark
http://www.polepos.org

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public
License along with this program; if not, write to the Free
Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
MA  02111-1307, USA. */

package org.polepos.reporters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.polepos.framework.*;


public class XLSReporter extends GraphReporter {
 

	public XLSReporter(String path) {
		super(path);
	}

	protected void report(Graph graph){
		try{
	        new File(path()).mkdirs();
	        
	        String fileName = path() + File.separatorChar+"PolePosition.xls";
	        
			File file = new File(fileName);
			
			HSSFWorkbook workbook = null;
			if(file.exists()){
				workbook = new HSSFWorkbook(new FileInputStream(file));
			} else {
				workbook = new HSSFWorkbook();
			}
		    
			String sheetName = graph.circuit().name()+ " " +graph.lap().name();
			
			// Names are limited to 31 characters
			sheetName = sheetName.replaceAll(ConcurrencyCircuit.NAME_ADD_ON + " ", "C");
			
			HSSFSheet sheet = workbook.getSheet(sheetName);
			
			if(sheet == null){
				sheet = workbook.createSheet(sheetName);
			}
			
      		HSSFRow row = sheet.createRow(0);
      		
      		HSSFCell cell = null;			
			
      		cell = row.createCell((short) 0);
			cell.setCellValue("Team");
			
			int i = 1;
			for(TurnSetup setup : graph.setups()) {
				String setupDescription = "";
	            for(SetupProperty sp : setup.properties()){
	            	setupDescription += sp.name()+":"+sp.value()+" ";
	            }
	            cell = row.createCell((short) i++);
	            cell.setCellValue(setupDescription);
			}
			
			for(TeamCar teamCar : graph.teamCars()) {
				
				// append result after last result
				row = sheet.createRow(sheet.getPhysicalNumberOfRows());
    			cell = row.createCell((short) 0);
    			cell.setCellValue(teamCar.toString());
    			
    			int j =1;
				for(TurnSetup setup : graph.setups()) {
					cell = row.createCell((short) j++);
	    			cell.setCellValue(graph.timeFor(teamCar,setup));
	            }
	        }

			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	protected void finish(List <TeamCar> cars) {
	}

}
