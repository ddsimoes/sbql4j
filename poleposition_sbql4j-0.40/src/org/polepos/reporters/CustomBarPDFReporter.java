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

import com.db4o.*;
import com.db4o.config.*;
import com.db4o.ta.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class CustomBarPDFReporter extends PDFReporterBase {

	public CustomBarPDFReporter(String path) {
		super(path);
	}
	
	public static void main(String[] args) {
		CustomBarPDFReporter reporter = new CustomBarPDFReporter(DefaultReporterFactory.defaultReporterOutputPath());
		
		
	    EmbeddedConfiguration cfg = Db4oEmbedded.newConfiguration();
	    cfg.common().add(new TransparentPersistenceSupport());
		
	    // EmbeddedObjectContainer db = Db4oEmbedded.openFile(cfg, "ClientServerGraph.db4o");
	    EmbeddedObjectContainer db = Db4oEmbedded.openFile(cfg, "EmbeddedGraph.db4o");
	    // EmbeddedObjectContainer db = Db4oEmbedded.openFile(cfg, "ConcurrentGraph.db4o");
		PersistentGraphs pg = db.query(PersistentGraphs.class).iterator().next();
		
		reporter.graphs(pg.graphs());
		
		reporter.render();
		reporter.endReport();

		db.close();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void report(Graph graph) {
        
		renderCircuitPresentation(graph);
        
        try {
			pdfData().add(render(graph));
		} catch (BadElementException e) {
			throw new RuntimeException(e);
		}
        pdfData().add(new NewPageLabel());
        
	}

	private Object render(Graph graph) throws BadElementException {
        PdfContentByte cb = writer().getDirectContent();
		PdfTemplate tp = cb.createTemplate(chartWidth(), chartHeight());
		Graphics2D graphics = tp.createGraphics(chartWidth(), chartHeight(), new DefaultFontMapper());
		
		int height = new CustomBarRender(graph).render(graphics);
		
		tp = cb.createTemplate(chartWidth(), height);
		graphics = tp.createGraphics(chartWidth(), height, new DefaultFontMapper());
		
		new CustomBarRender(graph).render(graphics);
		
		
		
		graphics.dispose();
		return new ImgTemplate(tp);
	}

	
	@Override
	protected void renderTableAndGraph(int type, Graph graph, String unitsLegend) throws BadElementException {
	}

	@Override
	protected int chartHeight() {
		return 600;
	}
	
}
