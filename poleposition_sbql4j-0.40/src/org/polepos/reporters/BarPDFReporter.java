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
import java.util.*;
import java.util.List;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.*;
import org.jfree.data.category.*;
import org.polepos.framework.*;

import com.lowagie.text.*;

public class BarPDFReporter extends PDFReporterBase {

	public BarPDFReporter(String path) {
		super(path);
	}

	@Override
	protected void renderTableAndGraph(int type, Graph graph, String unitsLegend) throws BadElementException {
	}

	@Override
	protected int chartHeight() {
		return 600;
	}
	
	
	protected List<JFreeChart> createTimeChart(Graph graph) {
		List<JFreeChart> list = new ArrayList<JFreeChart>();
		
		boolean bestOnTop = true;
		
		CategoryDataset dataset = createBarDataset(graph, bestOnTop);
		list.add(createBarChart(dataset));
		return list;
	}

	
	
	private static CategoryDataset createBarDataset(final Graph graph, boolean bestOnTop) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		final List<TurnSetup> setups = graph.setups();
		int setupCount = setups.size();
		double[] best = new double[setupCount];

		int i = 0;
		List<TeamCar> teamCars = graph.teamCars();
		for (TurnSetup setup : setups) {
			best[i] = Double.MAX_VALUE;
			for (TeamCar teamCar : teamCars) {
				double time = graph.timeFor(teamCar, setup);
				if (time != 0 && time < best[i]) {
					best[i] = time;
				}
			}
			i++;
		}

		for (TurnSetup setup : setups) {
			String legend = legend(setup);
			dataset.addValue(0, "", legend);
		}
		
		
		teamCars = new ArrayList<TeamCar>(teamCars);
		
		
		i = 0;
		for (final TurnSetup setup : setups) {
			String legend = legend(setup);
			final double bestForRun = best[i++];
			Collections.sort(teamCars, new Comparator<TeamCar>() {
				
				@Override
				public int compare(TeamCar team1, TeamCar team2) {
					long time1 = graph.timeFor(team1, setup);
					long time2 = graph.timeFor(team2, setup);
					if(time1 > time2){
						return 1;
					}
					if(time1 < time2){
						return - 1;
					}
					return 0;
				}
			});
			for (TeamCar teamCar : teamCars) {
				double time = graph.timeFor(teamCar, setup);
				dataset.addValue(bestForRun == 0 ? 0 : time / bestForRun, teamCar.toString(), legend);
			}
		}
		plot(dataset);
		return dataset;
	}

    private static void plot(DefaultCategoryDataset dataset) {
    	
		System.out.print("|");
		System.out.print(String.format("%-30s","."));
		System.out.print("|");

		System.out.print("|");
    	for(int r=0;r <dataset.getRowCount();r++) {
    		System.out.print(String.format("%30s", dataset.getRowKey(r)));
    		System.out.print("|");
    	}
		System.out.println();

    	for(int c=0;c<dataset.getColumnCount();c++) {
    		System.out.print("|");
    		System.out.print(String.format("%-30s",dataset.getColumnKey(c)));
    		System.out.print("|");
        	for(int r=0;r <dataset.getRowCount();r++) {
        		System.out.print(String.format("%30.1f", dataset.getValue(r, c)));
        		System.out.print("|");
        	}
    		System.out.println();
    		
    	}
		
	}

	private static JFreeChart createBarChart(CategoryDataset dataset) {

        // create the chart...
        JFreeChart chart = ChartFactory.createBarChart(
             null,       // chart title
            "Category",               // domain axis label
            "Value",                  // range axis label
            dataset,                  // data
            PlotOrientation.HORIZONTAL, // orientation
            false,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );
        
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        
        plot.setBackgroundPaint(Color.white);
        plot.setOutlineVisible(false);
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
          renderer.setDrawBarOutline(false);
          renderer.setShadowVisible(false);
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
        	protected String generateLabelString(CategoryDataset dataset, int row, int column) {
				Number value = dataset.getValue(row, column);
        		return row == 0 ? (dataset.getColumnKey(column)+"") : String.format("%.2fx", value);
        	}
        });
        renderer.setBaseItemLabelsVisible(true);
//        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE1, TextAnchor.TOP_LEFT));
//        renderer.setBaseNegativeItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE1, TextAnchor.TOP_RIGHT));

//        renderer.setBaseItemLabelFont(new Font("Verdana", Font.PLAIN, 12));
        
        

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
//        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        // ******************************************************************
        //  More than 150 demo applications are included with the JFreeChart
        //  Developer Guide...for more information, see:
        //
        //  >   http://www.object-refinery.com/jfreechart/guide.html
        //
        // ******************************************************************
        
        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
//        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        // set up gradient paints for series...
//        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue,
//                0.0f, 0.0f, new Color(0, 0, 64));
//        GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green,
//                0.0f, 0.0f, new Color(0, 64, 0));
//        GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red,
//                0.0f, 0.0f, new Color(64, 0, 0));
//        renderer.setSeriesPaint(0, gp0);
//        renderer.setSeriesPaint(1, gp1);
//        renderer.setSeriesPaint(2, gp2);
        
        NumberAxis numberAxis = new NumberAxis();
        numberAxis.setTickUnit(new NumberTickUnit(1) {
			private static final long serialVersionUID = 1L;

			@Override
			public String valueToString(double value) {
				return String.format("%.0fx", value);
			}
        	
        });
		plot.setRangeAxis(numberAxis);
        
        SubCategoryAxis axis = new SubCategoryAxis("");
        for(int i=0;i<dataset.getRowCount();i++) {
        	axis.addSubCategory(i == 0 ? "" : dataset.getRowKey(i)+"");
        }
        axis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
//		axis.setCategoryLabelPositions(
//          CategoryLabelPositions.createUpRotationLabelPositions(
//          Math.PI / 6.0));

        plot.setDomainAxis(axis);
        
        
//        domainAxis.setCategoryLabelPositions(
//                CategoryLabelPositions.createUpRotationLabelPositions(
//                        Math.PI / 6.0));
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;

    }


}
