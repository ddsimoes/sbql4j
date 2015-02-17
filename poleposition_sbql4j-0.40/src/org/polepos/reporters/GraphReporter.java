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
import java.io.*;
import java.util.*;
import java.util.List;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.*;
import org.jfree.chart.title.*;
import org.jfree.data.category.*;
import org.jfree.ui.*;
import org.polepos.framework.*;
import org.polepos.util.*;

import com.db4o.*;


public abstract class GraphReporter extends ReporterBase{
    
    public GraphReporter(String path) {
		super(path);
		
	}

	private Map<CircuitLap,Graph> mGraphs;
    private java.util.List<Circuit> mCircuits;
	protected final DefaultCategoryDataset _overviewTimeDataset = new DefaultCategoryDataset();
	protected final DefaultCategoryDataset _overviewMemoryDataset = new DefaultCategoryDataset();
	protected final DefaultCategoryDataset _overviewSizeDataset = new DefaultCategoryDataset();
	
	public static int timeIndex = 0;
	public static int memoryIndex = 0;
	public static int sizeIndex = 0;
    
    @Override
    public void startSeason() {
    }
    
    @Override
    public boolean append() {
        return false;
    }
    
    @Override
    public void reportTaskNames(String[] taskNames){
        // do nothing
    }

    @Override
    public void reportTeam(Team team) {
        // do nothing
    }

    @Override
    public void reportCar(Car car) {
        // do nothing
    }
    
    @Override
    public void beginResults() {
    }
    
    @Override
    public void reportResult(Result result) {
        
        if(mGraphs == null){
            mGraphs = new LinkedHashMap<CircuitLap,Graph>();
        }
        
        if(mCircuits == null){
            mCircuits = new ArrayList <Circuit>();
        }
        
        Circuit circuit = result.getCircuit();
        
        if(! mCircuits.contains(circuit)){
            mCircuits.add(circuit);
        }
        
        CircuitLap cl = new CircuitLap(circuit, result.getLap());
        Graph graph = mGraphs.get(cl);
        if(graph == null){
            graph = new Graph(result);
            mGraphs.put(cl, graph);
        }
        graph.addResult(_teamCar, result);
        
    }
    
    @Override
    public void endSeason() {
    	persist(mGraphs);
    	render();
    }

	protected void render() {
		List<TeamCar> cars = null;
        if(mGraphs != null){
            OverViewChartBuilder overViewChartBuilder = new OverViewChartBuilder();
            for (Graph graph : mGraphs.values()) {
                if(graph != null){
                	if(cars == null) {
                		cars = graph.teamCars();
                	}
                    report(graph);
                    reportOverviewDatabaseSize(graph);
                    overViewChartBuilder.report(graph);
                }
			}
            overViewChartBuilder.createJPGs(path());
			finish(cars);
        }
	}
    
    private void persist(Map<CircuitLap, Graph> graphs) {
		new File("graph.db4o").delete();
		EmbeddedObjectContainer db = Db4oEmbedded.openFile("graph.db4o");
		db.store(new PersistentGraphs(graphs));
		db.commit();
		db.close();
	}

	protected abstract void report(Graph graph);
	protected abstract void finish(List <TeamCar> cars);

	protected List<JFreeChart> createTimeChart(Graph graph) {
		List<JFreeChart> list = new ArrayList<JFreeChart>();
		// list.add(createChart(createInverseLogTimeDataset(graph), ReporterConstants.OLD_LOGARITHMIC_TIME_CHART_LEGEND));
		
		boolean bestOnTop = true;
		String legendText = bestOnTop ? ReporterConstants.TIME_CHART_LEGEND_BEST_ON_TOP : ReporterConstants.TIME_CHART_LEGEND_BEST_BELOW;
		
		// list.add(createOrderOfMagnitudeChart(createBaseLineTimeDataset(graph, bestOnTop), legendText, bestOnTop));
		
		list.add(
			createBestAsBaseLineChart(
					createBestAsBaseLineTimeDataset(graph, bestOnTop), 
					ReporterConstants.BEST_AS_BASELINE_CHART_LEGEND, 
					bestOnTop));
		
		return list;
	}
	
	protected JFreeChart createMemoryChart(Graph graph) {
		CategoryDataset dataset = createMemoryDataset(graph);
		return createChart(dataset, ReporterConstants.MEMORY_CHART_LEGEND);
	}
	
	public CategoryDataset createInverseLogTimeDataset(Graph graph) {
		DefaultCategoryDataset dataset=new DefaultCategoryDataset();
		int currentTimeIndex = timeIndex;
		for(TeamCar teamCar : graph.teamCars()) {
			timeIndex = currentTimeIndex;
			for(TurnSetup setup : graph.setups()) {
				String legend = "" + setup.getMostImportantValueForGraph();
	            double time = graph.timeFor(teamCar,setup);
	            double logedTime = MathUtil.toLogedValue(time);
	            dataset.addValue(logedTime,teamCar.toString(),legend);
	            String xName = ""+ ++timeIndex;
				_overviewTimeDataset.addValue(logedTime,teamCar.toString(),xName);
	        }
	    }
		return dataset;
	}
	
	public CategoryDataset createBaseLineTimeDataset(final Graph graph, boolean bestOnTop) {
		DefaultCategoryDataset dataset=new DefaultCategoryDataset();
		
		int setupCount = graph.setups().size();
		double[] averages = new double[setupCount];
		
		int i = 0;
		for(TurnSetup setup : graph.setups()) {
			double sum = 0;
			for(TeamCar teamCar : graph.teamCars()) {
	            double time = graph.timeFor(teamCar,setup);
	            sum+=time;
	        }
			averages[i] = sum/graph.teamCars().size();
			i++;
	    }
		
		for(TeamCar teamCar : graph.teamCars()) {
			i = 0;
			for(TurnSetup setup : graph.setups()) {
				String legend = "" + setup.getMostImportantValueForGraph();
	            double time = graph.timeFor(teamCar,setup);
	            double graphValue = logarithmicMagnitudeGraphValue(averages[i], time, bestOnTop);
	            dataset.addValue(graphValue,teamCar.toString(),legend);
	            i++;
	        }
	    }
		return dataset;
	}
	
	public CategoryDataset createBestAsBaseLineTimeDataset(final Graph graph, boolean bestOnTop) {
		DefaultCategoryDataset dataset=new DefaultCategoryDataset();
		
		int setupCount = graph.setups().size();
		double[] best = new double[setupCount];
		
		int i = 0;
		for(TurnSetup setup : graph.setups()) {
			best[i] = Double.MAX_VALUE;
			for(TeamCar teamCar : graph.teamCars()) {
	            double time = graph.timeFor(teamCar,setup);
	            if(time < best[i]){
	            	best[i] = time;
	            }
	        }
			i++;
	    }
		
		for(TeamCar teamCar : graph.teamCars()) {
			i = 0;
			for(TurnSetup setup : graph.setups()) {
				String legend = "" + setup.getMostImportantValueForGraph();
	            double time = graph.timeFor(teamCar,setup);
	            double graphValue = logarithmicMagnitudeGraphValue(best[i], time, bestOnTop);
	            dataset.addValue(graphValue,teamCar.toString(),legend);
	            i++;
	        }
	    }
		return dataset;
	}

	

	protected static String legend(TurnSetup setup) {
		String legend = "";
		boolean first = true;
		for (SetupProperty sp : setup.properties()) {
			if (!first) {
				legend += ", ";
			}
			String name = sp.name();
			if (!name.equals("commitinterval")) {
				legend += name + "=" + sp.value();
				first = false;
			}
		}

		return legend;
	}

	
	double logarithmicMagnitudeGraphValue(double average, double time, boolean bestOnTop){
		if(average == time){
			return 0;
		}
		if(time > average){
			double magnitude = time / average;
			magnitude = Math.log10(magnitude);
			return bestOnTop ? -magnitude : magnitude;
		}
		double magnitude = average / time;
		magnitude = Math.log10(magnitude);
		return bestOnTop ? magnitude : -magnitude;
	}

	private CategoryDataset createMemoryDataset(Graph graph) {
		DefaultCategoryDataset dataset=new DefaultCategoryDataset();
		int currentMemoryIndex = memoryIndex;
		for(TeamCar teamCar : graph.teamCars()) {
			memoryIndex = currentMemoryIndex;
			for(TurnSetup setup : graph.setups()) {
	            String legend = "" + setup.getMostImportantValueForGraph();
	            double memory = graph.memoryFor(teamCar,setup);
	            double logedMemory = MathUtil.toLogedValue(memory);
				dataset.addValue(logedMemory,(teamCar.toString()),legend);
				String xName = ""+ ++memoryIndex;
				_overviewMemoryDataset.addValue(logedMemory,(teamCar.toString()),xName);
	        }
	    }
		return dataset;
	}

	private void reportOverviewDatabaseSize(Graph graph) {
		int currentSizeIndex = sizeIndex;
		for(TeamCar teamCar : graph.teamCars()) {
			sizeIndex = currentSizeIndex;
			for(TurnSetup setup : graph.setups()) {
	            double databaseSize = graph.sizeFor(teamCar,setup);
	            double logedSize = MathUtil.toLogedValue(databaseSize);
	            String xName = "" + ++sizeIndex;
				_overviewSizeDataset.addValue(logedSize,(teamCar.toString()),xName);
	        }
	    }
	}
	
	public JFreeChart createChart(CategoryDataset dataset, String legendText) {
		CategoryAxis categoryAxis = new CategoryAxis("");
		categoryAxis.setLabelFont(ReporterConstants.CATEGORY_LABEL_FONT);
		categoryAxis.setTickLabelFont(ReporterConstants.CATEGORY_TICKLABEL_FONT);
		String yLegendText =  legendText;
		ValueAxis valueAxis = new NumberAxis(yLegendText);
		valueAxis.setLabelFont(ReporterConstants.VALUE_LABEL_FONT);
		valueAxis.setTickLabelFont(ReporterConstants.VALUE_TICKLABEL_FONT);
		LineAndShapeRenderer renderer = new LineAndShapeRenderer(true, false);
		CategoryPlot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, renderer);
		plot.setOrientation(PlotOrientation.VERTICAL);
		JFreeChart chart = new JFreeChart("", ReporterConstants.TITLE_FONT, plot, true);
		LegendTitle legend = chart.getLegend();
		legend.setItemFont(ReporterConstants.LEGEND_FONT);
		legend.setMargin(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
		legend.setBackgroundPaint(Color.white);
		return chart;
	}
	
	public JFreeChart createOrderOfMagnitudeChart(CategoryDataset dataset, String legendText, boolean bestOnTop) {
		CategoryAxis categoryAxis = new CategoryAxis("");
		categoryAxis.setLabelFont(ReporterConstants.CATEGORY_LABEL_FONT);
		categoryAxis.setTickLabelFont(ReporterConstants.CATEGORY_TICKLABEL_FONT);
		String yLegendText =  legendText;
		ValueAxis valueAxis = new NumberAxis(yLegendText);
		valueAxis.setLabelFont(ReporterConstants.VALUE_LABEL_FONT);
		valueAxis.setTickLabelFont(ReporterConstants.VALUE_TICKLABEL_FONT);
		LineAndShapeRenderer renderer = new LineAndShapeRenderer(true, false);
		CategoryPlot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, renderer);
		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.getRangeAxis().centerRange(0);
		plot.getRangeAxis().setRange(-3, 3);
		JFreeChart chart = new JFreeChart("", ReporterConstants.TITLE_FONT, plot, true);
		LegendTitle legend = chart.getLegend();
		legend.setItemFont(ReporterConstants.LEGEND_FONT);
		legend.setMargin(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
		legend.setBackgroundPaint(Color.white);
		return chart;
	}
	
	public JFreeChart createBestAsBaseLineChart(CategoryDataset dataset, String legendText, boolean bestOnTop) {
		CategoryAxis categoryAxis = new CategoryAxis("");
		categoryAxis.setLabelFont(ReporterConstants.CATEGORY_LABEL_FONT);
		categoryAxis.setTickLabelFont(ReporterConstants.CATEGORY_TICKLABEL_FONT);
		
		String yLegendText =  legendText;
		ValueAxis valueAxis = new NumberAxis(yLegendText);
		valueAxis.setLabelFont(ReporterConstants.VALUE_LABEL_FONT);
		valueAxis.setTickLabelFont(ReporterConstants.VALUE_TICKLABEL_FONT);
		LineAndShapeRenderer renderer = new LineAndShapeRenderer(true, false);
		CategoryPlot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, renderer);
		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.getRangeAxis().centerRange(-2);
		plot.getRangeAxis().setRange(-4, 1);
		
		JFreeChart chart = new JFreeChart("", ReporterConstants.TITLE_FONT, plot, true);
		LegendTitle legend = chart.getLegend();
		legend.setItemFont(ReporterConstants.LEGEND_FONT);
		legend.setMargin(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
		legend.setBackgroundPaint(Color.white);
		return chart;
	}

	public void graphs(Map<CircuitLap,Graph> mGraphs) {
		this.mGraphs = mGraphs;
	}

	public Map<CircuitLap,Graph> graphs() {
		return mGraphs;
	}


}
