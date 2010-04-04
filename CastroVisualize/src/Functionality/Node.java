// Name        : ChineseWhisperClustering.java
// Author      : Carsten Ehrler; Michal Richter
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
// Description : The class storing nodes
//               
//============================================================================

package Functionality;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;


public class Node implements Comparable<Object> {
	
	private Integer id;
	
	// metadata from the database
	private String author;
	private String headline;
	private String report_date;
	private String source;
	private String place;
	private String document_type;
	private String speech_text = null;
	private String speech_date;
	private Double relevance;
	
	//context informtion
	private boolean marked; 
	
	// list of neighbors
	private Map<Node, Double> neighbors = new HashMap<Node, Double>();
	
	public int visited = 0;
	public int depth = 0;
	private int clusterID = -1;
	
	public int GetCluster()
	{
		return clusterID;
	}
	
	public void SetCluster(int _clusterID)
	{
		clusterID = _clusterID;
	}
	
	public void SetRelevance(Double rel)
	{
		relevance = rel;
	}
	
	public Double GetRelevance()
	{
		return relevance;
	}
	
	//context informtion
	
	public Node(Integer id) {
		this.id = id;
		this.marked = false;        //assumes new node is unmarked 
	}
	
	public Node(Integer _id, String _author, String _headline, String _report_date,
			    String _source, String _place, String _document_type, String _speech_date)
	{
		id = _id;
		author = _author;
		headline = _headline;
		report_date = _report_date;
		source = _source;
		place = _place;
		document_type = _document_type;
		speech_date = _speech_date;
		
		
	}
		
	public Set<NamedEntity> getNamedEntitiesPerson() 
	{
		return DataModule.getPersonsInDocument(this);
	}
	

	public Set<NamedEntity> getNamedEntitiesLocations()
	{
		return DataModule.getLocationsInDocument(this);
	}

	public Set<NamedEntity> getNamedEntitiesOrganizations() 
	{
		return DataModule.getOrganizationsInDocument(this);
	}

	// 0 = doesn't contain, 1 = expanded, 2 = present in the text  
	public int containsNE(String neString)
	{
		return DataModule.documentContainsNE(this.id, neString);
	}

	public String getAuthor() {
		return author;
	}

	public String getHeadline() {
		return headline;
	}

	public String getReport_date() {
		return report_date;
	}

	public String getSource() {
		return source;
	}

	public String getPlace() {
		return place;
	}

	public String getDocument_type() {
		return document_type;
	}

	public String getSpeech_date() {
		return speech_date;
	}

	public String getSpeech_text() 
	{
		if (speech_text == null)
		{
			speech_text = DataModule.getSpeechText(id);
		}
		
		return speech_text;
	}
	
	public Integer getSpeech_id() {
		return id;
	}

	public boolean getMarked() {
		return this.marked;
	}
	
	public void setMarked(boolean marked) {
		this.marked = marked;
	}
	
	public int compareTo(Object obj) {
		if(obj == null) {
			return -1;
		}
		if(obj instanceof Node) {
			Node that = (Node) obj;
			
			return this.GetRelevance().compareTo(that.GetRelevance());
		}
		
		return -1;
	}

	public void addEdge(Node v, Double strength) {
		this.neighbors.put(v, strength);
	}
	
	public Set<Node> getNeighbors() {
		return this.neighbors.keySet();
	}
	
	public Map<Node,Double> getNeighborsMap() {
		return this.neighbors;
	}
	
	public Double getStrength(Node v) {
		assert this.neighbors.containsKey(v);
		return this.neighbors.get(v);
	}

	public void clearNeighborsSet() {
		this.neighbors = new HashMap<Node, Double>();
	}
	@Override
	public String toString() {
		String s = "---- ID = " + id + ", Date = " + this.getSpeech_date() + ", "  + headline + "\n";
		
		/*List<NamedEntity> pers = new ArrayList<NamedEntity>(this.getNamedEntitiesPerson());
		List<NamedEntity> locs = new ArrayList<NamedEntity>(this.getNamedEntitiesLocations());
		List<NamedEntity> orgs = new ArrayList<NamedEntity>(this.getNamedEntitiesOrganizations());
		
		Comparator<NamedEntity> comparator = new Comparator<NamedEntity>() {

			private Double pom1;
			private Double pom2;
			
			public int compare(NamedEntity arg0, NamedEntity arg1) {
				pom1 = arg0.getWeight();
				pom2 = arg1.getWeight();
				return pom1.compareTo(pom2);
			}
		};
		
		Collections.sort(orgs, comparator); 
		Collections.reverse(orgs);
		
		Collections.sort(locs, comparator);
		Collections.reverse(locs);
		
		Collections.sort(pers, comparator);
		Collections.reverse(pers);
		
		s = s + "10 persons: ";
		for (int i = 0; i < Math.min(10, pers.size()); i++)
		{
			if (i > 0) s = s + ", ";
			s += pers.get(i).getText();
		}
		
		s += "\n10 locations: ";
		for (int i = 0; i < Math.min(10, locs.size()); i++)
		{
			if (i > 0) s = s + ", ";
			s += locs.get(i).getText();
		}

		s += "\n10 organizations: ";
		for (int i = 0; i < Math.min(10, pers.size()); i++)
		{
			if (i > 0) s = s + ", ";
			s += pers.get(i).getText();
		}
		s += "\n";*/

		return s;//+ this.id;
	}

}
