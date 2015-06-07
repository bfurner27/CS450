/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nuralNetwork;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Benjamin
 */
public class PlotXYGraph {
    public void plotXYGraph (List<Double> y)  {
        XYSeries xy = new XYSeries("Error Function");
        for (Double i = 0.0; i < y.size(); i++)
        {
            xy.add(y.get(i.intValue()), i);
        }
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(xy);
        
        JFreeChart xyLineChart = ChartFactory.createXYLineChart(
                "Neural Network Error Plot",
                "Number of iterations",
                "Error",
                dataset, 
                PlotOrientation.HORIZONTAL, 
                true, true, false
        );
        
        int width = 480;
        int height = 640;
        
        File XYChart = new File("errorTrackingChart.jpeg");
        try {
            ChartUtilities.saveChartAsJPEG(XYChart, xyLineChart, width, height);
        } catch (IOException ex) {
            Logger.getLogger(PlotXYGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
