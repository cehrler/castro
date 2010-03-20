package Visualizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JEditorPane;

import Functionality.NamedEntity;
import Functionality.Node;

public class SpeechDetailPanel 
{
	JEditorPane editor;
	
	private Set<String> getNEText(Set<NamedEntity> sne)
	{
		NamedEntity ne;
		Set<String> ret = new HashSet<String>();
		for (Iterator<NamedEntity> it = sne.iterator(); it.hasNext(); )
		{
			ne = it.next();
			ret.add(ne.getText());
			
		}
		return ret;
	}
	
	private String makeStringForSetOfStrings(Set<String> sneID, String color)
	{
		String text = "";
		for (Iterator<String> it = sneID.iterator(); it.hasNext(); )
		{
			text += "<span style=\"color:" + color + "\">" + it.next() + "</span><br/>";
		}
		return text;
	}
	
	public SpeechDetailPanel(JEditorPane _editor)
	{
		editor = _editor;
		editor.setContentType("text/html");
		
	}
	
	public void setText(List<Functionality.Node> nodes)
	{
		
		if (nodes.size() == 1)
		{
			Node n = nodes.get(0); 
			String text = "<b>document ID: " + n.getSpeech_id() + "<br/>"; 
			text += n.getHeadline() + "</b><br/><br/>";
			
			text += "<i>persons:</i><br/>";
			Set<NamedEntity> sp = n.getNamedEntitiesPerson();
			text += makeStringForSetOfStrings(getNEText(sp), neTypeColors.getPersonsString());
			
			text += "<br/><i>organizations:</i><br/>";
			Set<NamedEntity> so = n.getNamedEntitiesOrganizations();
			text += makeStringForSetOfStrings(getNEText(so), neTypeColors.getOrganizationsString());
			
			text += "<br/><i>locations:</i><br/>";
			Set<NamedEntity> sl = n.getNamedEntitiesLocations();
			text += makeStringForSetOfStrings(getNEText(sl), neTypeColors.getLocationsString());
			
			editor.setText(text);
		}
		else
		{
			Node n = nodes.get(0);
			List<Set<String>> ls = new ArrayList<Set<String>>(); 
			ls.add(getNEText(n.getNamedEntitiesPerson()));
			ls.add(getNEText(n.getNamedEntitiesOrganizations()));
			ls.add(getNEText(n.getNamedEntitiesLocations()));
			
			List<Set<String>> aktLS;
			List<Set<String>> newLS;
			
			for (int i = 1; i < nodes.size(); i++)
			{
				n = nodes.get(i);
				aktLS = new ArrayList<Set<String>>();
				aktLS.add(getNEText(n.getNamedEntitiesPerson()));
				aktLS.add(getNEText(n.getNamedEntitiesOrganizations()));
				aktLS.add(getNEText(n.getNamedEntitiesLocations()));

				newLS = new ArrayList<Set<String>>();
				
				String neString;
				for (int j = 0; j < 3; j++)
				{
					newLS.add(new HashSet<String>());
					
					for (Iterator<String> it = aktLS.get(j).iterator(); it.hasNext(); )
					{
						neString = it.next();
						
						if (ls.get(j).contains(neString))
						{
							newLS.get(j).add(neString);
						}
					}
					
					ls.set(j, newLS.get(j));
				}
			}
			
			String text = "Selected docs IDs:<br/>";// = n.getHeadline() + "<br/><br/>";
			
			for (int i = 0; i < nodes.size(); i++)
			{
				if (i != 0) text += ", ";
				text += nodes.get(i).getSpeech_id();
			}
			text += "<br/><br/><b>Common named entities:</b><br/>";
			
			text += "<i>persons:</i><br/>";
			text += makeStringForSetOfStrings(ls.get(0), neTypeColors.getPersonsString());
			text += "<br/><i>organizations:</i><br/>";
			text += makeStringForSetOfStrings(ls.get(1), neTypeColors.getOrganizationsString());
			text += "<br/><i>locations:</i><br/>";
			text += makeStringForSetOfStrings(ls.get(2), neTypeColors.getLocationsString());
			editor.setText(text);
		}
		
		
	}

}
