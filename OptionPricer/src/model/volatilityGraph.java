/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author catherine_Liu
 */
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.BorderArrangement;
import org.jfree.chart.block.EmptyBlock;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.CompositeTitle;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

public class volatilityGraph extends ApplicationFrame{
        private double[][] optionPrice ;//2-dimension array to store option price of different algorithm the user selected.
        private double[] volatility ;//eleven volatility value for drawing graph
        XYSeriesCollection xysc = new XYSeriesCollection();
        public volatilityGraph(String s, int numOfDataset, double[] volatility, double[][] optionPrice) {
            super(s);
            this.optionPrice = new double[numOfDataset][11];
            int i;int j;
            for(i=0;i<numOfDataset;i++){
                for(j=0;j<11;j++){
                    this.optionPrice[i][j]=optionPrice[i][j];
                }
                this.volatility = volatility;
                
            }
            setContentPane(createDemoPanel(numOfDataset,this.xysc));
        }

        private XYDataset createDataset(int numOfDataset, XYSeriesCollection xysc) {           

            XYSeries[] xyseries = new XYSeries[numOfDataset];
            int i; int j;
            for (i = 0; i < numOfDataset; i++) { 
                xyseries[i] = new XYSeries(""+i);
                for(j=0;j<11;j++){
                    xyseries[i].add(this.volatility[j],this.optionPrice[i][j]); 
                }
            }
            for(i=0;i<numOfDataset;i++)
                xysc.addSeries(xyseries[i]);
            return xysc;

        }

        private JFreeChart createChart(int numOfDataset,XYSeriesCollection xysc) {
            int i;
            XYDataset xydataset = createDataset(numOfDataset,xysc);          
            
            JFreeChart jfreechart = ChartFactory.createXYLineChart(
                    "Volatility Graph", "volatility(%)", "Option Price($)", xydataset,
                    PlotOrientation.VERTICAL, false, true, false);
            XYPlot xyplot = (XYPlot) jfreechart.getPlot(); 
            

            NumberAxis numberaxis = (NumberAxis)xyplot.getRangeAxis();
            numberaxis.setAutoRangeIncludesZero(false);
            

            xyplot.mapDatasetToRangeAxis(1, 1);
            XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();

            XYPointerAnnotation xypointerannotation = new XYPointerAnnotation("Annotation 1 (2.0, 167.3)", 5D, 100.30000000000001D, -0.78539816339744828D);
            xypointerannotation.setTextAnchor(TextAnchor.BOTTOM_LEFT);
            xypointerannotation.setPaint(Color.RED);
            xylineandshaperenderer.addAnnotation(xypointerannotation);
            

            LegendTitle legendtitle = new LegendTitle(xylineandshaperenderer);            
            return jfreechart;
        }

        public JPanel createDemoPanel(int numOfDataset, XYSeriesCollection xysc) {
            JFreeChart jfreechart = createChart(numOfDataset,xysc);
            return new ChartPanel(jfreechart);
        }
}
