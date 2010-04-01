package Functionality;

import java.util.List;

public class SearchQueryStandard implements SearchQuery {

	public List<String> QueryTerms;
	public String YearFrom;
	public String YearUntil;
	public String DocType;
	public int MaxNumDocs;
	
	public SearchQueryStandard(List<String> _queryTerms, String _yearFrom, String _yearUntil, String _docType, int _maxNumDocs)
	{
		QueryTerms = _queryTerms;
		YearFrom = _yearFrom;
		YearUntil = _yearUntil;
		DocType = _docType;
		MaxNumDocs = _maxNumDocs;
		
	}
	
	@Override
	public String toString()
	{
		String s = "{";
		
		for (int i = 0; i < QueryTerms.size(); i++)
		{
			if (i != 0) s += ", ";
			s += QueryTerms.get(i);
		}
		s += "}, ";
		s += YearFrom + " - " + YearUntil + ", " + DocType + ", retrieved docs: " + MaxNumDocs;
		
		return s;
	}

	public int compareTo(SearchQuery o) {
		
		if (o instanceof SearchQuerySimilarNodes)
		{
			return 1;
		}
		
		SearchQueryStandard sq = (SearchQueryStandard)o;
		
		if (sq.QueryTerms.size() != QueryTerms.size())
		{
			return 1;
		}
		
		for (int i = 0; i < QueryTerms.size(); i++)
		{
			if (! QueryTerms.get(i).equals(sq.QueryTerms.get(i)))
			{
				return 1;
			}
		}
		
		if (! YearFrom.equals(sq.YearFrom)) return 1;
		if (! YearUntil.equals(sq.YearUntil)) return 1;
		if (! DocType.equals(sq.DocType)) return 1;
		if (  MaxNumDocs != sq.MaxNumDocs) return 1;
		
		return 0;
	}
}
