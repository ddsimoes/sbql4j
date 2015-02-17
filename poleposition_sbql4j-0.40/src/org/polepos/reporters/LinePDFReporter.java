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

import java.util.*;
import java.util.List;

import org.polepos.framework.*;

import com.lowagie.text.*;

public class LinePDFReporter extends PDFReporterBase {

	public LinePDFReporter(String path) {
		super(path);
	}
	
	@Override
	protected void renderTableAndGraph(int type, final Graph graph, String unitsLegend) throws BadElementException {
		Paragraph para;
		para=new Paragraph();
		List<TeamCar> teamCars=graph.teamCars();
		final List<TurnSetup> setups=graph.setups();
		Table table = setupTable(graph);
        addTableCell(table, 0, 0, 2, unitsLegend , null,false,true, Element.ALIGN_LEFT);
        int idx=2;
		for(TurnSetup setup : setups) {
            StringBuffer header = new StringBuffer();
            boolean first = true;
            for(SetupProperty sp : setup.properties()){
                if(! first){
                    header.append("\n");
                }
                String name = sp.name();
                if(! name.equals("commitinterval")){
                    header.append(name);
                    header.append(":");
                    header.append(sp.value());
                    first = false;
                }
            }
			addTableCell(table, idx, 0, 1, header.toString(),null, true,true, Element.ALIGN_RIGHT);
			idx++;
		}
		table.endHeaders();
		int vidx=1;
		
		teamCars = new ArrayList<TeamCar>(teamCars);
		Collections.sort(teamCars, new Comparator<TeamCar>() {
			@Override
			public int compare(TeamCar team1, TeamCar team2) {
				long time1 = 0;
				long time2 = 0;
				for(TurnSetup setup : setups) {
					time1 += graph.timeFor(team1, setup);
					time2 += graph.timeFor(team2, setup);
				}
				if(time1 > time2){
					return 1;
				}
				if(time1 < time2){
					return - 1;
				}
				return 0;
			}
		});
		
		for(TeamCar teamCar : teamCars) {
			addTableCell(table,0,vidx,2, teamCar.toString(),teamCar.website(),true,false, Element.ALIGN_LEFT);
			int hidx=2;
			for(TurnSetup setup : setups) {
				String text = reportText(type, graph, teamCar, setup);
				addTableCell(table,hidx,vidx,1, text, null,false,false, Element.ALIGN_RIGHT);
				hidx++;
			}
			vidx++;
		}
		para.add(table);
        para.add(new Chunk("\n",bigFont));
        pdfData().add(para);
	}
	
	@Override
	protected int chartHeight() {
		return 300;
	}



}
