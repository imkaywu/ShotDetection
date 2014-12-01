package com.blakay;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class DistChart extends JFrame {

	private String title;
	
	public DistChart(String title, float [] dist) {
		super(title);
		// TODO Auto-generated constructor stub
		this.title = title;
		final XYDataset dataset = createDataset(dist);
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(500,270));
		setContentPane(chartPanel);
	}
	
	private XYDataset createDataset(float [] dist) {
		final XYSeries series = new XYSeries("dist");
		int i = 1;
		for(float f: dist){
			series.add(i++, f);
		}
			
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		
		return dataset;
	}
	
	private JFreeChart createChart(final XYDataset dataset) {
		final JFreeChart chart = ChartFactory.createXYLineChart(
				title,
				"Ind",
				"Dist",
				dataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				false
				);
		
		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        //rangeAxis.setRange(0.0, 0.2);
        //rangeAxis.setTickUnit(new NumberTickUnit(0.005));
        rangeAxis.setRange(0.0, 0.1);
        rangeAxis.setTickUnit(new NumberTickUnit(0.01));
		
		return chart;
	}
	
}
