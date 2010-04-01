package Visualizer;

import java.awt.Color;
import java.awt.Paint;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.Transformer;

import Functionality.Node;
import GUI.SettingsWindow;

public class VertexColorTransformer implements Transformer<Functionality.Node, Paint>{

	private Set<Node> selectedNodes = new HashSet<Node>();
	private Map<Node, Double> markedNodes = new HashMap<Node, Double>();
	private Double minSimilarity = null;
	private Double maxSimilarity = null;
	
	public static VertexColorTransformer vctInstance = new VertexColorTransformer();
	
	private VertexColorTransformer() {}
	
	public void setSelectedNodes(Set<Node> sn)
	{
		selectedNodes = sn;
		markedNodes = null;
	}
	
	public void setMarkedNodes(Map<Node, Double> mn)
	{
		markedNodes = mn;
		
		Node n;
		minSimilarity = 1000.0;
		maxSimilarity = -1000.0;
		for (Iterator<Node> it = mn.keySet().iterator(); it.hasNext(); )
		{
			n = it.next();
			
			if (minSimilarity > mn.get(n))
			{
				minSimilarity = mn.get(n);
			}
			
			if (maxSimilarity < mn.get(n))
			{
				maxSimilarity = mn.get(n);
			}
			
		}
	}
	
	public Paint transform(Node arg0) {
		
		if (markedNodes != null && markedNodes.containsKey(arg0))
		{
			double scale = 1 - (markedNodes.get(arg0) - minSimilarity) / (maxSimilarity - minSimilarity);
			scale = Math.max(0, scale);
			return Color.getHSBColor(0.0f, 0f, (float)scale);
		}
		else
		{
			if (selectedNodes.contains(arg0))
			{
				return Color.YELLOW;
			}
			else
			{
				if (SettingsWindow.useDifferentColorsForClusters == false)
				{
					return Color.red;
				}
				
				if (arg0.GetCluster() == -1)
					return Color.RED;
				else if (arg0.GetCluster() == 0)
					return Color.PINK;
				else if (arg0.GetCluster() == 1)
					return Color.MAGENTA;
				else if (arg0.GetCluster() == 2)
					return Color.GREEN;
				else if (arg0.GetCluster() == 3)
					return Color.CYAN;
				else
					return Color.BLUE;
			}
		}
	}

}
