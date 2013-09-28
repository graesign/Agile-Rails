package UserInterface;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import Data.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeriesCollection;

public class StatisticsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JFreeChart chart;
	private ChartPanel chartPanel;
	private MainUI main;
	private int mod = 0;
	public StatisticsPanel(MainUI main)
	{
		this.main = main;
	}


	public StatisticsPanel(String title, String xAxis, String yAxis,TimeSeriesCollection dataset){
		this.showGraph(title, xAxis, yAxis, dataset);
	}
	
	public void showGraph(String title, String xAxis, String yAxis,TimeSeriesCollection dataset){
		chart = ChartFactory.createTimeSeriesChart(title, xAxis, yAxis, dataset, true, true, false);
		chart.setNotify(false);
		
		
		chartPanel = new ChartPanel(chart);
		
		chartPanel.setPreferredSize(new Dimension(215,150));
		chartPanel.setMaximumDrawWidth(215);
		chartPanel.setMaximumDrawHeight(150);
		
		this.add(chartPanel);
		
		new Timer().scheduleAtFixedRate(new TimerTask() {
		public void run() 
		{
			if(mod % 2 == 0)
			{
				main.setSize(main.getWidth(),main.getHeight()-1);
			}
			else main.setSize(main.getWidth(),main.getHeight()+1);
	
			mod++;
			
		}
		}, 0, 10000); // ELKE 7 seconde nieuwe GEGEVENS :)
		 
	}
	
}
