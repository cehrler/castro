// Name        : SearchQuerySimilarNodes.java
// Author      : Michal Richter
// Version     : 2.5.1
// Copyright   : All rights regarding the source material are reserved by the authors: With the exception
//               of Caroline Sporleder and Martin Schreiber at Saarland University in Germany and for
//               research and teaching at Saarland University in general, explicit permission must be
//               obtained before do. Usage or reference to this work or any part thereof must feature
//               credit to all the authors. Without explicit permission from the authors beforehand, this
//               software, its source and documentation may not be distributed, incorporated into other
//               products or used to create derived works.
//               However, the authors hope that this project may be of interest and use to others,
//               and so are glad to grant permission to people wishing to incorporate this project into
//               others or to use it for other purposes, and are asked to contact the authors for these
//               permissions.
// Description : Data type that represents the search query that retrieves the most similar
//               documents to the given document.
//============================================================================

package Functionality;

public class SearchQuerySimilarNodes implements SearchQuery {
	public int CentralNodeID;
	public int maxNumNodes;
	public String CentralNodeHeadline;
	public String YearFrom;
	public String YearUntil;
	public String SpeechType;
	
	public SearchQuerySimilarNodes(int _centralNodeID, int _maxNumNodes, String _centralNodeHeadline, String _yearFrom, String _yearUntil, String _speechType)
	{
		CentralNodeID = _centralNodeID;
		maxNumNodes = _maxNumNodes;
		CentralNodeHeadline = _centralNodeHeadline;
		YearFrom = _yearFrom;
		YearUntil = _yearUntil;
		SpeechType = _speechType;
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
