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

import java.awt.*;
import java.awt.Font;
import java.io.*;
import java.util.*;
import java.util.List;

import org.jfree.chart.*;
import org.polepos.framework.*;
import org.polepos.teams.jdbc.*;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;


public abstract class PDFReporterBase extends GraphReporter {

	private Document _document;
	private PdfWriter _writer;
	
	@SuppressWarnings("rawtypes")
	private LinkedList _pdfData = new LinkedList();
	protected static final com.lowagie.text.Font h1Font = FontFactory.getFont(FontFactory.HELVETICA,15,Font.BOLD);
	protected static final com.lowagie.text.Font h2Font = FontFactory.getFont(FontFactory.HELVETICA,12,Font.BOLD);
    protected static final com.lowagie.text.Font bigFont = FontFactory.getFont(FontFactory.HELVETICA,10,Font.BOLD);
    protected static final com.lowagie.text.Font smallFont = FontFactory.getFont(FontFactory.HELVETICA,9,Font.PLAIN);
    protected static final com.lowagie.text.Font smallBoldFont = FontFactory.getFont(FontFactory.HELVETICA,9,Font.BOLD);
    protected static final com.lowagie.text.Font codeFont = FontFactory.getFont(FontFactory.COURIER,8,Font.PLAIN);

	public PDFReporterBase(String path) {
		super(path);
		
		// Fail early if the document is still open in Acrobat Reader and can't be written to. 
		setupDocument(path);
	}

    protected void report(Graph graph) {
        
        List<JFreeChart>timeCharts = createTimeChart(graph);
        try {
			renderTimeTable(graph);
			for (JFreeChart timeChart : timeCharts) {
				
				// white if you like
		        // timeChart.setBackgroundPaint(null);
				
				_pdfData.add(renderChart(timeChart));	
			}
			_pdfData.add(new NewPageLabel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// for overview
		createMemoryChart(graph);
    }
    
    private void renderFirstPage(List<TeamCar> cars) throws DocumentException{        
        Paragraph para=new Paragraph();
        CircuitSettings circuitSettings = new CircuitSettings();
        String configurationName = circuitSettings.configurationName();
        String title = "PolePosition";
        if(configurationName != null && configurationName.length() > 0){
        	title = title + " - " + configurationName;
        }
        title = title + " Results\n";
        para.add(new Chunk(title,h1Font));
        para.add(new Chunk("Results from running the Poleposition open source database benchmark\n\n\n",smallFont));
        para.add(new Chunk("Related Links\n\n",h2Font));
        para.add(new Chunk("Poleposition website\n", smallBoldFont));
        para.add(linked(new Chunk(ReporterConstants.WEBSITE + "\n", smallFont), ReporterConstants.WEBSITE));
        para.add(new Chunk("Explanation how the benchmarks are run\n", smallBoldFont));
        para.add(linked(new Chunk(ReporterConstants.CIRCUITS_PAGE + "\n\n\n", smallFont), ReporterConstants.CIRCUITS_PAGE ));
        para.add(new Chunk("Databases benchmarked in this run\n\n",h2Font));
        
        int i = 0;
        _pdfData.add(i++, para);
        
        List <Object> printed = new ArrayList <Object>();
        
        for(TeamCar teamCar : cars){
            Team team = teamCar.getTeam();
            String webSite = team.website();
            if(webSite != null){
                if(! printed.contains(team)){
                    printed.add(team);
                    _pdfData.add(i++, renderTeam(team.name(), team.description(), webSite));
                }
            }else{
                Car car = teamCar.getCar();
                webSite = car.website();
                if(webSite != null){
                    if(! printed.contains(car)){
                        printed.add(car);
                        _pdfData.add(i++, renderTeam(car.name(), car.description(), webSite));
                    }
                }
            }
        }
        _pdfData.add(i++, new NewPageLabel());
    }
    
    private Element renderTeam(String name, String description, String website) throws DocumentException{
        Paragraph para=new Paragraph();
        para.add(linked(new Chunk(name + "\n",smallBoldFont), website));
        if(description != null){
            para.add(linked(new Chunk(description + "\n",smallFont), website));
        }
        if(website != null){
            para.add(linked(new Chunk(website + "\n", smallFont), website));
        }
        para.add(new Chunk("\n",smallFont));
        return para;
    }
    
    private Element linked(Chunk chunk, String link){
        if(link == null){
            return chunk;
        }
        Anchor anchor = new Anchor(chunk);
        anchor.setReference(link);
        return anchor;
    }

	private void setupDocument(String path) {
		if(_document != null){
			return;
		}
		String fileName = path + "/" + "PolePosition.pdf";
		File file = new File(fileName);
		file.delete();
		_document = new Document();
		try {
			_writer = PdfWriter.getInstance(_document, new FileOutputStream(file));
			_document.open();
		} catch (Exception exc) {
			_document=null;
			System.err.println("*****************************************************\nError writing PDF document. Do you have an open copy?\n*****************************************************");
			throw new RuntimeException(exc);
		}
	}

    @Override
	public void endSeason() {
        super.endSeason();
        endReport();
	}

	protected void endReport() {
		if(_document != null){
            _document.close();
        }
        if(_writer != null){
            _writer.close();
        }
	}

	private void renderTimeTable(Graph graph) throws DocumentException {
		String unitsLegend = "t [time in ms]";
		renderTable(ReporterConstants.TIME, graph, unitsLegend);
	}
	
	private void renderMemoryTable(Graph graph) throws DocumentException {
		String unitsLegend = "m [memory in bytes]";
		renderTable(ReporterConstants.MEMORY, graph, unitsLegend);
	}

	private void renderTable(int type, final Graph graph, String unitsLegend) throws BadElementException, DocumentException {
		renderCircuitPresentation(graph);
        renderTableAndGraph(type, graph, unitsLegend);
	}

	protected abstract void renderTableAndGraph(int type, Graph graph, String unitsLegend) throws BadElementException;

	@SuppressWarnings("unchecked")
	protected void renderCircuitPresentation(final Graph graph) {
		Paragraph para=new Paragraph();
        Circuit circuit = graph.circuit();
        Lap lap = graph.lap();
        
        // para.add(new Chunk("Circuit: " + circuit.name()+ "\n",bigFont));
        para.add(new Chunk(circuit.name()+ "\n",bigFont));
        para.add(new Chunk(circuit.description() + "\n\n",smallFont));
        
        // para.add(new Chunk("Lap: " + lap.name()+ "\n",bigFont));
        para.add(new Chunk(lap.name()+ "\n",bigFont));
        _pdfData.add(para);
        
        renderCode(lap);
	}

	private void renderCode(Lap lap) {
		if(true){
			return;
		}
		Paragraph para;
		String code = lap.code();
        if(code != null){
        	para=new Paragraph();
        	para.setSpacingBefore(5f);
        	para.setLeading(11f);
			para.add(new Chunk(code,codeFont));
			para.add(new Chunk("\n",codeFont));
			_pdfData.add(para);
        }
	}
	
	protected String reportText(int type, Graph graph, TeamCar teamCar, TurnSetup setup) {
		String text = null;
		switch (type) {
		case ReporterConstants.TIME:
			text = String.valueOf(graph.timeFor(teamCar, setup));
			break;
		case ReporterConstants.MEMORY:
			text = String.valueOf(graph.memoryFor(teamCar, setup));
		}
		return text;
	}


	protected void addTableCell(Table table, int hidx, int vidx, int colspan, String text, String link, boolean bold, boolean header, int hAlign ) throws BadElementException {
        Chunk chunk = new Chunk(text,FontFactory.getFont(FontFactory.HELVETICA,9,(bold ? Font.BOLD : Font.PLAIN)));
        chunk.setTextRise(3);
		Cell cell=new Cell(linked(chunk, link));
		cell.setHeader(header);
        if(! header){
            cell.setNoWrap(true);
        }
        cell.setVerticalAlignment(Element.ALIGN_BASELINE);
        cell.setHorizontalAlignment(hAlign);
        cell.setColspan(colspan);
		table.addCell(cell,new Point(vidx,hidx));
	}
	
	private Element renderChart(JFreeChart chart) throws DocumentException, BadElementException {
		return renderChart(chart, chartWidth(), chartHeight());
	}

	protected int chartWidth() {
		return 522;
	}

	protected abstract int chartHeight();
	
	private Element renderChart(JFreeChart chart, int width, int height) throws DocumentException, BadElementException {
        PdfContentByte cb = _writer.getDirectContent();
		PdfTemplate tp = cb.createTemplate(width, height);
		Graphics2D graphics = tp.createGraphics(width, height, new DefaultFontMapper());
		java.awt.Rectangle area = new java.awt.Rectangle(0, 0, width, height);
		chart.draw(graphics, area);
		graphics.dispose();
		return new ImgTemplate(tp);
	} 

	protected Table setupTable(Graph graph) throws BadElementException {
		Table table=new Table(graph.setups().size()+2);
		table.setAutoFillEmptyCells(true);
		table.setSpaceInsideCell(2);
        
		table.setBorderWidth(0);
		table.setWidth(100);
		table.setDefaultCellBorder(1);
		table.setTableFitsPage(true);
		return table;
	}

	protected void finish(List<TeamCar> cars) {
		try {
			JFreeChart overviewSizeChart = createChart(_overviewSizeDataset,
					ReporterConstants.SIZE_CHART_LEGEND);
			

			// Overviews don't really look nice and useful if lots of circuits are run.  
			
			
//			renderOverviewPage(overviewSizeChart,
//					ReporterConstants.SIZE_OVERVIEW_LEGEND);
//
//			JFreeChart overviewMemoryChart = createChart(
//					_overviewMemoryDataset,
//					ReporterConstants.MEMORY_CHART_LEGEND);
//			renderOverviewPage(overviewMemoryChart,
//					ReporterConstants.MEMORY_OVERVIEW_LEGEND);
//
//			JFreeChart overviewTimeChart = createChart(_overviewTimeDataset,
//					ReporterConstants.TIME_CHART_LEGEND);
//			renderOverviewPage(overviewTimeChart,
//					ReporterConstants.TIME_OVERVIEW_LEGEND);

			renderFirstPage(cars);
			
			renderPDFFile();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	private void renderPDFFile() throws DocumentException {
        @SuppressWarnings("rawtypes")
		Iterator iter = _pdfData.iterator();
        while(iter.hasNext()) {
        	Object obj = iter.next();
        	if(isNewPage(obj)) {
        		_document.newPage();
        	} else {
        		_document.add((Element)obj);
        	}
        }
	}

	private boolean isNewPage(Object obj) {
		return obj instanceof NewPageLabel;
	}

	protected void renderOverviewPage(JFreeChart chart, String legend) throws DocumentException {
		Paragraph para = new Paragraph();
		para.add(new Chunk(legend));
		int i = 0;
		_pdfData.add(i++, para);
		_pdfData.add(i++, renderChart(chart));
		_pdfData.add(i++, new NewPageLabel());
	}

	protected LinkedList pdfData() {
		return _pdfData;
	}

	public PdfWriter writer() {
		return _writer;
	}
}
