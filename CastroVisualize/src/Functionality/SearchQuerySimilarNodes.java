package Functionality;

public class SearchQuerySimilarNodes implements SearchQuery {
	public int CentralNodeID;
	public int maxNumNodes;
	public String CentralNodeHeadline;
	
	public SearchQuerySimilarNodes(int _centralNodeID, int _maxNumNodes, String _centralNodeHeadline)
	{
		CentralNodeID = _centralNodeID;
		maxNumNodes = _maxNumNodes;
		CentralNodeHeadline = _centralNodeHeadline;
	}
	
	@Override
	public String toString()
	{
		return "Most similar nodes to: " + CentralNodeHeadline + " (ID = " + CentralNodeID + "), maximal number of results = " + maxNumNodes;
	}

	public int compareTo(SearchQuery o) 
	{
		if (o instanceof SearchQueryStandard)
		{
			return 1;
		}
		
		SearchQuerySimilarNodes sq = (SearchQuerySimilarNodes)o;
		
		if (sq.CentralNodeID == CentralNodeID && sq.maxNumNodes == maxNumNodes)
		{
			return 0;
		}
		else
		{
			return 1;
		}
		
	}

	
	
}
