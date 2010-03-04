package Visualizer;

import edu.uci.ics.jung.visualization.VisualizationViewer;

public interface VertexListener<Node> {
    void setVertexAndView(Node v, VisualizationViewer visView);    
}
