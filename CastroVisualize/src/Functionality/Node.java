package Functionality;

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
	private String speech_text;
	private String speech_date;
	private Double relevance;
	
	// date for named entities
	private Map<String, Integer> ne_person;
	private Map<String, Integer> ne_location;
	private Map<String, Integer> ne_organization;

	//context informtion
	private boolean marked; 
	
	// list of neighbors
	private Map<Node, Double> neighbors = new HashMap<Node, Double>();
	
	public void SetRelevance(Double rel)
	{
		relevance = rel;
	}
	
	public Double GetRelevance()
	{
		return relevance;
	}
	
	public Node(Integer id) {
		this.id = id;
		this.ne_person = null;//new TreeMap<String, Integer>();
		this.ne_location = null; //new TreeMap<String, Integer>();
		this.ne_organization = null; //new TreeMap<String, Integer>();
		this.marked = false;        //assumes new node is unmarked 
	}
	
	public Node(Integer _id, String _author, String _headline, String _report_date,
			    String _source, String _place, String _document_type, String _speech_text, String _speech_date)
	{
		id = _id;
		author = _author;
		headline = _headline;
		report_date = _report_date;
		source = _source;
		place = _place;
		document_type = _document_type;
		speech_text = _speech_text;
		speech_date = _speech_date;
		
	}
		
	public Set<String> getNamedEntitiesPerson() {
		return this.ne_person.keySet();
	}
	
	public Set<String> getNamedEntitiesLocation() {
		return this.ne_location.keySet();
	}
	
	public Set<String> getNamedEntitiesOrganization() {
		return this.ne_organization.keySet();
	}
	
	public Integer getCountPerson(String ne) {
		return this.ne_person.get(ne);
	}
	
	public Integer getCountLocation(String ne) {
		return this.ne_location.get(ne);
	}
	
	public Integer getCountOrganization(String ne) {
		return this.ne_organization.get(ne);
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

	public String getSpeech_text() {
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
	
	public Double getStrength(Node v) {
		assert this.neighbors.containsKey(v);
		return this.neighbors.get(v);
	}

	@Override
	public String toString() {
		return "" + this.id;
	}

}
