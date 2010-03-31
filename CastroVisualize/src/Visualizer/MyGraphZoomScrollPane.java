/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 * 
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 * Created on Feb 2, 2005
 *
 */
package Visualizer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Set;

import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JScrollBar;

import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.transform.BidirectionalTransformer;
import edu.uci.ics.jung.visualization.transform.shape.Intersector;



@SuppressWarnings("serial")
public class MyGraphZoomScrollPane extends GraphZoomScrollPane {

	 public MyGraphZoomScrollPane(VisualizationViewer vv) {
 		super(vv);	
		 horizontalScrollBar.removeAdjustmentListener(horizontalScrollBar.getAdjustmentListeners()[0]);
		 verticalScrollBar.removeAdjustmentListener(verticalScrollBar.getAdjustmentListeners()[0]);

         verticalScrollBar.addAdjustmentListener(new VerticalAdjustmentListenerImpl());
         horizontalScrollBar.addAdjustmentListener(new HorizontalAdjustmentListenerImpl());

	 }

	class HorizontalAdjustmentListenerImpl implements AdjustmentListener {
        int previous = 0;
        public void adjustmentValueChanged(AdjustmentEvent e) {
        	System.err.println("Horizontal adjustment");
            int hval = e.getValue();
            float dh = previous - hval;
            previous = hval;
            if(dh != 0 && scrollBarsMayControlAdjusting) {
                // get the uniform scale of all transforms
                float layoutScale = (float) vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).getScale();
                dh *= layoutScale;
                AffineTransform at = AffineTransform.getTranslateInstance(dh, 0);
                vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).preConcatenate(at);
            }
            vv.repaint();
        }
    }
    
    /**
     * Listener for adjustment of the vertical scroll bar.
     * Sets the translation of the VisualizationViewer
     */
    class VerticalAdjustmentListenerImpl implements AdjustmentListener {
        int previous = 0;
        public void adjustmentValueChanged(AdjustmentEvent e) {
        	System.err.println("Vertical adjustment");
            JScrollBar sb = (JScrollBar)e.getSource();
            BoundedRangeModel m = sb.getModel();
            int vval = m.getValue();
            float dv = previous - vval;
            previous = vval;
            if(dv != 0 && scrollBarsMayControlAdjusting) {
            
                // get the uniform scale of all transforms
                float layoutScale = (float) vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).getScale();
                dv *= layoutScale;
                AffineTransform at = AffineTransform.getTranslateInstance(0, dv);
                vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).preConcatenate(at);
            }
            vv.repaint();
        }
    }
    

}