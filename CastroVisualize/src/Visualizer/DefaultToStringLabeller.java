package Visualizer;

import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class DefaultToStringLabeller extends ToStringLabeller<Functionality.Node>
{
	public String transform(Functionality.Node n)
	{
		return n.getSpeech_id().toString();
	}
	

}
