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
import java.awt.geom.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.polepos.framework.*;

import com.db4o.*;

public class CustomBarRender {
	private Graphics graphics;
	private GraphData graphData;
	private List<TurnData> runs;
	private final Graph graph;

	
	static class TurnData {
		public TurnData(TurnSetup turn, long best) {
			this.turn = turn;
			this.best = best;
		}
		TurnSetup turn;
		long best;
		public List<TeamData> teams = new ArrayList<TeamData>();
	}
	
	static class TeamData {
		public TeamData(TeamCar team, double orderOfMagnitude, long time) {
			this.teamCar = team;
			this.orderOfMagnitude = orderOfMagnitude;
			this.time = time;
		}
		TeamCar teamCar;
		double orderOfMagnitude;
		long time;
		
		public Color barColor() {
			int rgb = teamCar.color();
			return new Color(rgb);
		}
		
		
	}
	
	static class GraphData {
		
		double maxTeamNameWidth = 0;
		double barHeight = 15;
		Font teamNameFont;
		Font turnLegendFont;
		
		double paddingX = 3;
		
		double maxMagnitude = 5;
		
		double paddingY = 2;
		
		double markerHeight = 5;
		
	}
	
	double stride() {
		return graphData().barHeight + graphData().paddingY;
	}
	
	double barX() {
		return graphData().maxTeamNameWidth+1;
	}
	
	double maxBarWidth() {
		return graphics.getClipBounds().getWidth() - barX() - textWidth("10000x")/2;
	}
	
	double widthPerOrderOfMagnitude() {
		return maxBarWidth() / graphData().maxMagnitude;
	}
	
	
	public static void main(String[] args) {
		
		
		EmbeddedObjectContainer db = Db4oEmbedded.openFile("graph.db4o");
		
		Graph graph = db.query(Graph.class).iterator().next();
		db.activate(graph, Integer.MAX_VALUE);
		db.close();
		
		
		final CustomBarRender renderer = new CustomBarRender(graph);
		JFrame w = new JFrame("Polepos preview") {
			private static final long serialVersionUID = 1L;
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				renderer.render(g);
			}
		};
		w.setSize(522, 600);
		w.setVisible(true);
		
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public CustomBarRender(Graph graph) {
		this.graph = graph;
	}

	public int render(Graphics graphics) {
		this.graphics = graphics;

		graphics.setFont(graphData().teamNameFont);

		double y = 0;
		
		for (TurnData turnData : runs()) {
			if (y > 0) {
				y += stride();
			}
			y = renderTurn(graphics, turnData, y);
			
		}
		
		y = renderAxis(graphics, (int) y);
		
		return (int) y;

	}

	private int renderAxis(Graphics graphics, int y) {
		int axisX = (int) graphData().maxTeamNameWidth;
		graphics.setColor(Color.BLACK);
		graphics.drawLine(axisX, 0, axisX, y);
		graphics.drawLine(axisX, y, (int) graphics.getClipBounds().getWidth(), y);
		
		for(int i=0;i<=graphData().maxMagnitude;i++) {
			
			int x = (int) (i * widthPerOrderOfMagnitude()) + axisX;
			
			graphics.drawLine(x, y, x, (int) (y+graphData().markerHeight));

			String legend;
			
			if (i == 0) {
				graphics.setFont(graphData().turnLegendFont);
				legend = "magnitude";
			} else {
				graphics.setFont(graphData().teamNameFont);
				legend = String.format("%.0fx", Math.pow(10, i-1));
			}
			
			graphics.drawString(legend, (int)(x-textWidth(legend)/2.), (int)(y+graphData().markerHeight+textHeight(legend)));
		}
		
		y += stride()*2;
		
		return y;
		
	}

	private double renderTurn(Graphics graphics, TurnData turnData, double y) {

		String legend = CustomBarPDFReporter.legend(turnData.turn);
		
		graphics.setFont(graphData().turnLegendFont);
		graphics.setColor(Color.BLACK);
		graphics.drawString(legend, (int) (barX()+graphData().paddingX), (int) (y + graphData().barHeight-graphData().paddingY));


		y += stride();

		for (TeamData teamData : turnData.teams) {

			graphics.setFont(graphData().teamNameFont);
			graphics.setColor(Color.BLACK);
			
			
			int textY = (int) (graphData.paddingY+(float) (y + graphData().barHeight - textHeight(teamData.teamCar.name())/2));
			graphics.drawString(teamData.teamCar.name(), (int) (graphData().maxTeamNameWidth-textWidth(teamData.teamCar.name())-graphData().paddingX), textY);

			graphics.setColor(teamData.barColor());

			
			int barWidth = (int) (barUnits(teamData.orderOfMagnitude) * widthPerOrderOfMagnitude());

			graphics.fillRect((int)barX(), (int) y, barWidth, (int) graphData().barHeight);
			
			String magnitude = String.format("%.1fx", teamData.orderOfMagnitude);
			
			graphics.setColor(Color.BLACK);
			graphics.drawString(magnitude, (int)(barX()+barWidth+graphData().paddingX), textY);

			if (teamData.time > 0) {
				String absolute = teamData.time+"ms";
				graphics.setColor(Color.WHITE);
				graphics.drawString(absolute, (int)(barX()+barWidth-textWidth(absolute)-graphData().paddingX), textY);
			}

			y += stride();
		}
		return y;

	}

	private double textWidth(String string) {
		return textBounds(graphics, string, graphics.getFont()).getWidth();
	}

	private double textHeight(String string) {
		return textBounds(graphics, string, graphics.getFont()).getHeight();
	}

	double barUnits(double orderOfMagnitude) {
		if (orderOfMagnitude == 0) {
			return 0;
		}
		return Math.log10(orderOfMagnitude) + 1;
	}

	private Rectangle2D textBounds(Graphics g, String string, Font font) {
		
	    FontMetrics metrics = g.getFontMetrics(font);
	    int hgt = metrics.getHeight();
	    int adv = metrics.stringWidth(string);
	    Dimension size = new Dimension(adv+2, hgt+2);

	    return new Rectangle(size);
	}

	private GraphData prepareGraphData(Graphics graphics, final Graph graph) {

		GraphData graphData = new GraphData();

		graphData.teamNameFont = new Font("Helvetica", Font.PLAIN, 9);
		graphData.turnLegendFont = new Font("Helvetica", Font.BOLD, 9);

		for (TeamCar teamCar : graph.teamCars()) {
			Rectangle2D textBounds = textBounds(graphics, teamCar.name(), graphData.teamNameFont);
			graphData.maxTeamNameWidth = Math.max(graphData.maxTeamNameWidth, textBounds.getWidth()+graphData.paddingX*2);
			graphData.barHeight = Math.max(graphData.barHeight, textBounds.getHeight());
		}

		return graphData;
	}

	private List<TurnData> prepareTurnsData(final Graph graph) {
		List<TurnData> runs = new ArrayList<TurnData>();

		final List<TurnSetup> setups = graph.setups();
		List<TeamCar> teamCars = graph.teamCars();

		for (final TurnSetup setup : setups) {

			TurnData turnData = new TurnData(setup, 0);
			long best = Long.MAX_VALUE;
			for (TeamCar teamCar : teamCars) {
				long time = graph.timeFor(teamCar, setup);
				TeamData teamData = new TeamData(teamCar, 0, time);
				if (time != 0 && time < best) {
					best = time;
				}
				turnData.teams.add(teamData);
			}
			turnData.best = best == 0 ? 1 : best;

			for (TeamData teamData : turnData.teams) {
				teamData.orderOfMagnitude = teamData.time / (double)turnData.best;
			}

			Collections.sort(turnData.teams, new Comparator<TeamData>() {

				@Override
				public int compare(TeamData team1, TeamData team2) {
					long time1 = graph.timeFor(team1.teamCar, setup);
					long time2 = graph.timeFor(team2.teamCar, setup);

					if (time1 > time2) {
						return 1;
					}
					if (time1 < time2) {
						return -1;
					}
					return 0;
				}
			});

			runs.add(turnData);
		}
		return runs;
	}

	public GraphData graphData() {
		if (graphData == null) {
			this.graphData = prepareGraphData(graphics, graph);
		}
		return graphData;
	}

	public List<TurnData> runs() {
		if (runs == null) {
			this.runs = prepareTurnsData(graph);
		}
		return runs;
	}

}
