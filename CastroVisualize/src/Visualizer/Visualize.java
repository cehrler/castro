package Visualizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.vecmath.Point2d;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.algorithms.layout3d.AbstractLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.MultiLayerTransformer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.BasicRenderer;
import edu.uci.ics.jung.visualization.renderers.BasicVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

import Functionality.*; 
import GUI.CastroGUI;

public class Visualize implements ItemListener, MouseListener {
	
	MyVisualizationViewer<Functionality.Node,Functionality.Edge> vv;
	
	Graph<Functionality.Node, Functionality.Edge> qt;
	
	private SpringLayoutWeighted layout;
	private Functionality.Graph myGraph;
	
	private int layout_width;
	private int layout_height;
	private Predicate<Context<Graph<Functionality.Node, Functionality.Edge>, Functionality.Node>> vdp = new VertexDisplayPredicateNone();
	private Predicate<Context<Graph<Functionality.Node, Functionality.Edge>, Functionality.Edge>> eip = new EdgeIncludePredicate(true, true, true);
	
	//public double thick_edge_theshold = 0.0; 
	//public double normal_edge_threshold = 0.0;
	
	@SuppressWarnings("unchecked")
	public Visualize(Functionality.Graph g, int _layout_width, int _layout_height ) {
		init(g, _layout_width, _layout_height);
		
		
	}
	
	public void init(Functionality.Graph g, int _layout_width, int _layout_height ) {
		myGraph = g;
		layout_width = _layout_width;
		layout_height = _layout_height;
		qt = new UndirectedSparseGraph();
		
		Iterator<Functionality.Node> nodeIterator = g.getNodes().iterator();
		while(nodeIterator.hasNext()) {
			qt.addVertex(nodeIterator.next());
		}
		
		Iterator<Functionality.Edge> edgeIterator = g.getEdges().iterator();
		while(edgeIterator.hasNext()) {
			Functionality.Edge currEdge = edgeIterator.next();
			qt.addEdge(currEdge, currEdge.getNode1(), currEdge.getNode2());
		}
		
		vv = null;
		layout = null;
		
		
	}
	
	public void graphTranslation(int _x, int _y)
	{
		MutableTransformer modelTransformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
		modelTransformer.translate(_x, _y);
	}
	
	public void layoutResize(int _width, int _height)
	{
		//layout.setSize(new Dimension(_width, _height));
	}
	
	public JComponent actualizeGraph(boolean lockLayout)
	{
		//myGraph = g;
		
		System.err.println("Location: x = " + vv.getLocation().x + ", y = " + vv.getLocation().y);
		
		MultiLayerTransformer mlt = vv.getRenderContext().getMultiLayerTransformer();
		Map<Functionality.Node, Point2d> locSet = new HashMap<Functionality.Node, Point2d>();
		
	
		
		PickedState<Node> psn = vv.getPickedVertexState();
		
		List<Functionality.Node> ln = DataModule.displayedGraph.getNodes();
		for (int i = 0; i < ln.size(); i++)
		{
			locSet.put(ln.get(i), new Point2d(layout.getX(ln.get(i)), layout.getY(ln.get(i))) );
		}
		
		init(DataModule.displayedGraph, layout_width, layout_height);
		drawGraph();
		
		for (int i = 0; i < ln.size(); i++)
		{
			layout.setLocation(ln.get(i), locSet.get(ln.get(i)).x, locSet.get(ln.get(i)).y);
		}
		
		PickedState<Node> newPsn = vv.getPickedVertexState();
		
		for (int i = 0; i < ln.size(); i++)
		{
			newPsn.pick(ln.get(i), psn.isPicked(ln.get(i)));
		}
		
		if (vdp instanceof VertexDisplayPredicateDistance)
		{
			((VertexDisplayPredicateDistance)vdp).Actualise();
		}
		vv.getRenderContext().setVertexIncludePredicate(vdp);
		vv.getRenderContext().setEdgeIncludePredicate(eip);
		
		vv.setPickedVertexState(newPsn);
		vv.getRenderContext().setMultiLayerTransformer(mlt);
		layout.lock(lockLayout);
		
	    MyGraphZoomScrollPane graphPane = new MyGraphZoomScrollPane(vv);
	    
		return graphPane;
	}
	
	public void LayoutStart()
	{
		layout.lock(false);
		vv.setRedrawing(true);

	}
	
	public void LayoutStop()
	{
		layout.lock(true);
		vv.setRedrawing(false);
		
	}
	
	public JComponent drawGraph() {
		// System.out.println("The graph qt"+qt.toString()); // DEBUG
		// ISOMLayout
		layout = new SpringLayoutWeighted(this.qt);
		layout.setSize(new Dimension(layout_width, layout_height));
		//layout.setSize(new Dimension(layout_width, layout_height));
		
		vv = new MyVisualizationViewer<Functionality.Node,Functionality.Edge>(layout);
		vv.setBackground(Color.WHITE);
		vv.setPreferredSize(new Dimension(layout_width, layout_height));
		vv.setIgnoreRepaint(true);
		
		// Show vertex and edge labels
		vv.getRenderContext().setVertexLabelTransformer(new DefaultToStringLabeller());
		//BasicVertexLabelRenderer<Functionality.Node, Functionality.Edge> bvlr =  new (BasicVertexLabelRenderer<Functionality.Node, Functionality.Edge>)(vv.getRenderContext().getVertexLabelRenderer());
		
		BasicRenderer<Functionality.Node, Functionality.Edge> br = new BasicRenderer<Node, Edge>();
		//br.setVertexLabelRenderer(new BasicVertexLabelRenderer<Functionality.Node, Functionality.Edge>(Position.CNTR));
		
		
		vv.setRenderer(br);
		
		//MyVertexLabelRenderer dvlr = new MyVertexLabelRenderer(Color.red);
		
		//vv.getRenderContext().setVertexLabelRenderer(dvlr);
		
		
		//bvlr.setPosition(Position.CNTR);
		
		//vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
		vv.setDoubleBuffered(true);
		
		//layout = new SpringLayout2<Functionality.Node, Functionality.Edge>(this.qt);
		layout.initialize();
	    layout.step();
		layout.step();
		layout.step();
		layout.step();
		layout.lock(true);
		
		
		// Create a graph mouse and add it to the visualization component
		//DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
		DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
		gm.setMode(ModalGraphMouse.Mode.PICKING);
		vv.addMouseListener(this);
		VertexMouseMenu vMouseMenu = new VertexMouseMenu();
		JPopupMenu vertexMenu = new MyVertexMenu();
		vMouseMenu.setVertexPopup(vertexMenu);
		gm.add(vMouseMenu);
		
		vv.setGraphMouse(gm);

		setEdgeWeightStrokeFunction();
	    vv.getRenderContext().setVertexShapeTransformer(new VertexShapeSizeAspect<Functionality.Node, Functionality.Edge>(myGraph) );
	
	    MyGraphZoomScrollPane graphPane = new MyGraphZoomScrollPane(vv);
	    
		return graphPane;
	}
	
	public void setEdgeWeightStrokeFunction()
	{
		double _normalEdgeThreshold = DataModule.displayedGraph.getNormalEdgeThreshold();
		double _thickEdgeThreshold = DataModule.displayedGraph.getThickEdgeThreshold();
		EdgeWeightStrokeFunction<Functionality.Edge> ewsf = new EdgeWeightStrokeFunction<Functionality.Edge>(_normalEdgeThreshold, _thickEdgeThreshold);
		vv.getRenderContext().setEdgeStrokeTransformer(ewsf);
		vv.repaint();
		System.err.println("edge stroke fc: " + _normalEdgeThreshold + ", " + _thickEdgeThreshold);
	}
	
	public void setEdgeFilter(boolean dotted, boolean normal, boolean thick)
	{
		eip = new EdgeIncludePredicate(dotted, normal, thick);
		vv.getRenderContext().setEdgeIncludePredicate(eip);
		vv.repaint();
	}
	
	public void setDistanceFilter(int _depth, VertexDisplayPredicateMode _vdpm, EdgeTypeEnum coreEdgeType)
	{
		
		vdp = new VertexDisplayPredicateDistance(_depth, _vdpm, vv.getPickedVertexState().getPicked(), coreEdgeType);
		vv.getRenderContext().setVertexIncludePredicate(vdp);
		
		//filter_setting_depth = _depth;
		//filter_setting_filter = _filter;
		vv.repaint();
		System.err.println("SetDistanceFilter");
		
	}
	
	public void setNoneFilter()
	{
		vdp = new VertexDisplayPredicateNone();
		vv.getRenderContext().setVertexIncludePredicate(vdp);
		vv.repaint();
	}
	
	private static Set<Functionality.Node> getNodesInDistance(Functionality.Node focusNode, int maxDistance, EdgeTypeEnum coreEdgeType)
	{
		Set<Functionality.Node> visitedNodes = new HashSet<Functionality.Node>();
		visitedNodes.add(focusNode);

		Set<Functionality.Node> retNodes = new HashSet<Functionality.Node>();
		retNodes.add(focusNode);
		
		List<Functionality.Node> proceedNodes = new ArrayList<Functionality.Node>();
		List<Integer> proceedDist = new ArrayList<Integer>();
		
		int nodePointer = 0;
		Node currNode;
		proceedNodes.add(focusNode);
		proceedDist.add(0);
		
		while (nodePointer < proceedNodes.size() && proceedDist.get(nodePointer) <= maxDistance - 1)
		{
			currNode = proceedNodes.get(nodePointer);
			for (Iterator<Node> it = currNode.getNeighbors().iterator(); it.hasNext(); )
			{
				Node neib = it.next();
				
				if (coreEdgeType == EdgeTypeEnum.normal && DataModule.displayedGraph.edgeIsDotted(currNode, neib) ||
					coreEdgeType == EdgeTypeEnum.thick && (DataModule.displayedGraph.edgeIsDotted(currNode, neib) || DataModule.displayedGraph.edgeIsNormal(currNode, neib)))
				{
					continue;
				}
				
				if (visitedNodes.contains(neib) == false)
				{
					visitedNodes.add(neib);
					proceedDist.add(proceedDist.get(nodePointer) + 1);
					proceedNodes.add(neib);
				}
			}
			nodePointer++;
		}
		
		
		
		return visitedNodes;
		
	}
	
	public void FocusNodes(Set<Functionality.Node> nodes)
	{
		PickedState<Functionality.Node> ps = vv.getPickedVertexState();
		List<Functionality.Node> ln = DataModule.displayedGraph.getNodes();
		List<Functionality.Node> pickedList = new ArrayList<Functionality.Node>();
		for (int i = 0; i < ln.size(); i++)
		{
			if (nodes.contains(ln.get(i)))
			{
				ps.pick(ln.get(i), true);
				pickedList.add(ln.get(i));
			}
			else
			{
				ps.pick(ln.get(i), false);
			}
		}
		
		CastroGUI.setSelectedNodesDetail(pickedList);
		
		vv.setPickedVertexState(ps);
		
		
		//DataModule.displayedGraph.setCenter(n);
		
		if (vdp instanceof VertexDisplayPredicateDistance)
		{
			((VertexDisplayPredicateDistance) vdp).setCentralNodes(ps.getPicked());
		}
		
		//vv.repaint();*/
		//vv.getRenderContext().setVertexFillPaintTransformer(previousTransformer);

	}
	
	
	private final static class VertexDisplayPredicateNone  implements Predicate<Context<Graph<Functionality.Node, Functionality.Edge>, Functionality.Node>>
	{

		public boolean evaluate(Context<Graph<Functionality.Node,Functionality.Edge>,Functionality.Node> context) {
			return true;
		}
		
	}
	
	private final static class EdgeIncludePredicate implements Predicate<Context<Graph<Functionality.Node, Functionality.Edge>, Functionality.Edge>>
	{

		private boolean dotted;
		private boolean normal;
		private boolean thick;
		
		public EdgeIncludePredicate(boolean _dotted, boolean _normal, boolean _thick)
		{
			dotted = _dotted;
			normal = _normal;
			thick = _thick;
		}

		public boolean evaluate(Context<Graph<Functionality.Node, Functionality.Edge>, Functionality.Edge> arg0) 
		{
			if (DataModule.displayedGraph.edgeIsDotted(arg0.element.getNode1(), arg0.element.getNode2()))
			{
				return dotted;
			}
			else if (DataModule.displayedGraph.edgeIsNormal(arg0.element.getNode1(), arg0.element.getNode2()))
			{
				return normal;
			}
			else if (DataModule.displayedGraph.edgeIsDouble(arg0.element.getNode1(), arg0.element.getNode2()))
			{
				return thick;
			}
			
			return false;
		}
		
	}
	
    private final static class VertexDisplayPredicateDistance implements Predicate<Context<Graph<Functionality.Node, Functionality.Edge>, Functionality.Node>>
	{
	    protected int maxDepth;
	    
	    private VertexDisplayPredicateMode vdpm = VertexDisplayPredicateMode.conjunction;
	    private Set<Functionality.Node> displayedNodes;
	    private Set<Functionality.Node> centralNodes;
	    
	    private EdgeTypeEnum coreEdgeType = EdgeTypeEnum.dotted;
	    
	    public VertexDisplayPredicateDistance(int _maxDepth, VertexDisplayPredicateMode _vdpm, Set<Functionality.Node> centralNodes, EdgeTypeEnum _ete)
	    {
	        this.maxDepth = _maxDepth;
	        vdpm = _vdpm;
	        coreEdgeType = _ete;
	        setCentralNodes(centralNodes);
	    }
	    
	    public void Actualise()
	    {
	    	setCentralNodes(centralNodes);
	    }
	    
	    public void setCentralNodes(Set<Functionality.Node> sn)
	    {
	    	centralNodes = sn;
			Functionality.Node n;
			
			Set<Functionality.Node> retNodes;
			
			if (vdpm == VertexDisplayPredicateMode.disjunction)
			{
				retNodes = DataModule.displayedGraph.getNodesSet();
			}
			else
			{
				retNodes = new HashSet<Functionality.Node>();
			}
			
			for (Iterator<Functionality.Node> it = sn.iterator(); it.hasNext(); )
			{
				n = it.next();
				Set<Functionality.Node> pomS = getNodesInDistance(n, maxDepth, coreEdgeType);
				
				if (vdpm == VertexDisplayPredicateMode.disjunction)
				{
					Functionality.Node n2;
					Set<Functionality.Node> newRet = new HashSet<Functionality.Node>();
					for (Iterator<Functionality.Node> it2 = pomS.iterator(); it2.hasNext(); )
					{
						n2 = it2.next();
						if (retNodes.contains(n2)) newRet.add(n2);
					}
					
					retNodes = newRet;
				}
				else
				{
					Functionality.Node n2;
					for (Iterator<Functionality.Node> it2 = pomS.iterator(); it2.hasNext(); )
					{
						n2 = it2.next();
						if (!retNodes.contains(n2)) retNodes.add(n2);
					}
				}
				
			}
			
			displayedNodes = retNodes;

	    }
	    
	    public boolean evaluate(Context<Graph<Functionality.Node,Functionality.Edge>,Functionality.Node> context) {
	    	Functionality.Node v = context.element;
	    	
	    	if (maxDepth > 0)
	    		return displayedNodes.contains(v);
	    	else
	    		return true;
	    }
	}
    
    private final static class EdgeWeightStrokeFunction<E> implements Transformer<E,Stroke>
    {
        protected static final Stroke basic = new BasicStroke((float)1);
        protected static final Stroke heavy = new BasicStroke(2);
        protected static final Stroke dotted = RenderContext.DOTTED;
        
        private double thick_threshold;
        private double normal_threshold;
                 
        public EdgeWeightStrokeFunction(double _normal_threshold, double _thick_threshold)
        {
        	thick_threshold = _thick_threshold;
        	normal_threshold = _normal_threshold;
        }
        
        public Stroke transform(E e)
        {
        	if (((Functionality.Edge)e).getStrength() > thick_threshold)
        	{
        		return heavy;
        	}
        	else if (((Functionality.Edge)e).getStrength() > normal_threshold)
        	{
        		return basic;
        	}
        	else
        	{
        		return dotted;
        	}
        }
        
        
    }
    
    private final static class VertexShapeSizeAspect<V,E>
    extends AbstractVertexShapeTransformer <V>
    implements Transformer<V,Shape>  {
    	
        protected boolean scale = true;
        protected boolean funny_shapes = true;
        protected Functionality.Graph graph;
        private double maxRelevance = 0;
//        protected AffineTransform scaleTransform = new AffineTransform();
        
        public VertexShapeSizeAspect(Functionality.Graph graphIn)
        {
        	for (int i = 0; i < graphIn.getNodes().size(); i++)
        	{
        		if (graphIn.getNodes().get(i).GetRelevance() > maxRelevance) maxRelevance = graphIn.getNodes().get(i).GetRelevance();
        	}
        	
        	this.graph = graphIn;
            setSizeTransformer(new Transformer<V,Integer>() {

				public Integer transform(V v) {
		            if (scale)
		            {
		            	//return 20;
		            	int retScale = (int)( (((Functionality.Node)v).GetRelevance() / maxRelevance) * 30) + 20;
		                return retScale;
		            }
		            else
		                return 20;

				}});
            setAspectRatioTransformer(new Transformer<V,Float>() {

				public Float transform(V v)
				{
		                return 1.0f;
				}
			});
        }
        
        public void setScaling(boolean scale)
        {
            this.scale = scale;
        }
        
        public void useFunnyShapes(boolean use)
        {
            this.funny_shapes = use;
        }
        
        public Shape transform(V v)
        {
        	Functionality.Node nod = (Functionality.Node)v;
        	
        	if (nod == null) return factory.getEllipse(v);
        	
            if (funny_shapes)
            {
            	if (nod.getDocument_type() == null)
            	{
            		return factory.getEllipse(v);
            	}
                if (nod.getDocument_type().equals("SPEECH"))
                {	
                	return factory.getRegularStar(v, 5);
                }
                else if (nod.getDocument_type().equals("MEETING"))
                {
                    return factory.getRegularPolygon(v, 5);
                }
                else return factory.getEllipse(v);
            }
            else
            {
                return factory.getEllipse(v);
        	}
        }
    }

	public void itemStateChanged(ItemEvent arg0) 
	{
	}

	public void mouseClicked(MouseEvent arg0) {
		PickedState<Functionality.Node> pickedState = vv.getPickedVertexState();
		Set<Functionality.Node> ns = pickedState.getPicked();
		
		
		Functionality.Node n;
		for (Iterator<Node> it = ns.iterator(); it.hasNext(); )
		{
			n = it.next();
			//FocusNode(n.getSpeech_id());
			break;
		}
		
		GUI.CastroGUI.updateTableSelection(ns);
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}


	
}
