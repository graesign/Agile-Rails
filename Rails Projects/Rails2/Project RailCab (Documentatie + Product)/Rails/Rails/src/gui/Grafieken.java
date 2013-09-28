package gui;

import java.util.ArrayList;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.plot.*;

import simulatie.*;

public class Grafieken extends JPanel{
	/**
	 * The class "Grafieken" draws multiple diagrams with the data
	 * of the other classes in this system. 
	 */
	private static final long serialVersionUID = 1L;
	
	// Attributes
	DefaultPieDataset dataset, dataset2;				// Designates the datasets with DefaultPieDataset
	XYSeriesCollection dataset3, dataset4, dataset5;						// Designate the other dataset with XYSeriesCollection
	XYSeries series = new XYSeries("Aantal vervoerde passagiers");
    XYSeries series2 = new XYSeries("Gemiddelde Wachttijd");
    XYSeries series3 = new XYSeries("Gemiddelde Reistijd");
	ChartPanel c1, c2, c3;								// c1, c2 & c3 are the three different charts that will be generated
	Simulatie sim;
	ArrayList<Integer> wachttijden;
	ArrayList<Integer> reistijden;
	UpdateGrafieken ut;
	int chartindex =0;										// Index => X-Axis of the XY Line diagram
	int chartindex2 =0;
	int chartindex3 =0;
	JLabel wt, rt, vp;
	int totaleWachttijd = 0;
	int totaleReistijd = 0;
	int totaleVervoerde = 0;
	
	// Constructor
	public Grafieken(Simulatie sim){
		this.ut = new UpdateGrafieken(this);
		this.sim = sim;
		wachttijden = new ArrayList<Integer>();
		reistijden = new ArrayList<Integer>();
		
        
        XYSeriesCollection dataset3 = new XYSeriesCollection();
		dataset3.addSeries(series);
		
		XYSeriesCollection dataset4 = new XYSeriesCollection();
		dataset4.addSeries(series2);
		
		XYSeriesCollection dataset5 = new XYSeriesCollection();
		dataset5.addSeries(series3);

        c1 = new ChartPanel(ChartFactory.createXYLineChart("Gemiddelde Reistijd", "x", "y", dataset5, PlotOrientation.VERTICAL, true, true, false));
        c2 = new ChartPanel(ChartFactory.createXYLineChart("Gemiddelde Wachttijd", "x", "y", dataset4, PlotOrientation.VERTICAL, true, true, false));
        c3 = new ChartPanel(ChartFactory.createXYLineChart("Aantal vervoerde passagiers", "tijd", "aantal", dataset3, PlotOrientation.VERTICAL, true, true, false));

        add(c1);
        add(c2);
        add(c3);
        
		rt = new JLabel("gemiddeld: ");
		rt.setBounds(260, 70, 100, 15);
		wt = new JLabel("gemiddeld: ");
		wt.setBounds(260, 280, 100, 15);
		vp = new JLabel("gemiddeld: ");
		vp.setBounds(260, 490, 100, 15);
		
        add(wt);
        add(rt);
        add(vp);
        
        ut.start();
        setLayout(null);
        c1.setBounds(0, 0, 250, 200);
        c2.setBounds(0, 210, 250, 200);
        c3.setBounds(0, 420, 250, 200);
	}

	// Methods
	public int updateWachttijden(){
		int totaleWachttijd = 0;
		int gemiddeldeWachttijd = 0;
		
		for (Trein t : sim.getTreinen()){
			for (Integer i : t.getWachtTijden()){
				wachttijden.add(i.intValue());
			}
			t.getWachtTijden().clear();
		}
		
		for(Integer i: wachttijden){
			totaleWachttijd += i.intValue();
		}
		
		if(wachttijden.size() > 0){
		gemiddeldeWachttijd = totaleWachttijd / wachttijden.size();
		this.totaleWachttijd += gemiddeldeWachttijd;
		wachttijden.clear();
		}
		return gemiddeldeWachttijd;
	}

	public int updateReistijden(){
		int totaleReistijd = 0;
		int gemiddeldeReistijd = 0;

		for (Trein t : sim.getTreinen()){
			for (Integer i : t.getReisTijden()){
				reistijden.add(i.intValue());
			}
			t.getReisTijden().clear();
		}
		
		for(Integer i: reistijden){
			totaleReistijd += i.intValue();
		}
		if(reistijden.size() > 0){
			gemiddeldeReistijd = totaleReistijd / reistijden.size();
			this.totaleReistijd += gemiddeldeReistijd;
			reistijden.clear();
		}
		return gemiddeldeReistijd;
	}

	
	public void updateCharts(){
		 int totaal = 0;
				
		if(chartindex%60==0){
			// Number of people who's been transported
			for(Trein t: sim.getTreinen()) {
				totaal += t.getAantalPassagiersPer();
				t.resetAantalPassagiersPer();
			}
			totaleVervoerde += totaal;
			series.addOrUpdate(chartindex / 60, totaal);
			if( this.totaleVervoerde != 0 )
				vp.setText( "gemiddeld: " + Math.ceil((double)this.totaleVervoerde / (chartindex / 60)));
			series.setMaximumItemCount(10);
		}
		chartindex++;
	
		if(chartindex2%60==0){
			// gemiddelde wachttijd
			System.out.println("Gemiddelde Wachttijd: " );
			series2.addOrUpdate(chartindex2 / 60, updateWachttijden());
			if( this.totaleWachttijd != 0  )
				wt.setText( "gemiddeld: " + this.totaleWachttijd / (chartindex2 / 60));
			series2.setMaximumItemCount(10);
		}
		chartindex2++;
	
		if(chartindex3%60==0){
			// gemiddelde reistijd
			series3.addOrUpdate(chartindex3 / 60, updateReistijden());
			if( this.totaleReistijd != 0 )
				rt.setText( "gemiddeld: " + this.totaleReistijd / (chartindex3 / 60));
			series3.setMaximumItemCount(10);
		}
		chartindex3++;

	}
}