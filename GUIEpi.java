package epidemiology;

import java.awt.Color;

import spaces.Spaces;
import sweep.GUIStateSweep;
import sweep.SimStateSweep;

public class GUIEpi extends GUIStateSweep {

	public GUIEpi(SimStateSweep state, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor,
			boolean agentPortrayal) {
		super(state, gridWidth, gridHeight, backdrop, agentDefaultColor, agentPortrayal);
		// TODO Auto-generated constructor stub
	}

	public GUIEpi(SimStateSweep state) {
		super(state);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] title = {"Spread of Infection"};//A string array, where every entry is the title of a chart
		 String[] x = {"Number of Infected People"};//A string array, where every entry is the x-axis title
		 String[] y = {"Compliancy and Base Infection Rate"};//A string array, where every entry is the y-axis title
		 //AgentsGUI.initializeArrayTimeSeriesChart(number of charts, chart titles, x-axis titles, y-axis titles);
		 GUIEpi.initializeArrayHistogramChart(1, title, y, x, new int[100]);
GUIEpi.initialize(Environment.class,Experimenter.class, GUIEpi.class, 500, 500, Color.WHITE, Color.blue, true, Spaces.SPARSE);
	}

}
