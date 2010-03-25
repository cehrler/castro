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
	
	/*private Set<String> getNEText(Set<NamedEntity> sne)
	{
		NamedEntity ne;
		Set<String> ret = new HashSet<String>();
		for (Iterator<NamedEntity> it = sne.iterator(); it.hasNext(); )
		{
			ne = it.next();
			ret.add(ne.getText());
			
		}
		return ret;
	}*/
	
	private String makeStringForSetOfNEs(Set<NamedEntity> sneID, String color)
	{
		String text = "";
		NamedEntity ne;
		for (Iterator<NamedEntity> it = sneID.iterator(); it.hasNext(); )
		{
			ne = it.next();
			if (ne.getExpanded())
			{
				text += "<span style=\"color:" + color + "\">" + ne.getText() + "</span><br/>";
			}
			else
			{
				text += "<span style=\"color:" + color + "\"><b>" + ne.getText() + "</b></span><br/>";				
			}
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
			text += makeStringForSetOfNEs(sp, neTypeColors.getPersonsString());
			
			text += "<br/><i>organizations:</i><br/>";
			Set<NamedEntity> so = n.getNamedEntitiesOrganizations();
			text += makeStringForSetOfNEs(so, neTypeColors.getOrganizationsString());
			
			text += "<br/><i>locations:</i><br/>";
			Set<NamedEntity> sl = n.getNamedEntitiesLocations();
			text += makeStringForSetOfNEs(sl, neTypeColors.getLocationsString());
			
			editor.setText(text);
		}
		else
		{
			Node n = nodes.get(0);
			List<Set<NamedEntity>> ls = new ArrayList<Set<NamedEntity>>(); 
			ls.add(n.getNamedEntitiesPerson());
			ls.add(n.getNamedEntitiesOrganizations());
			ls.add(n.getNamedEntitiesLocations());
			
			List<Set<NamedEntity>> aktLS;
			List<Set<NamedEntity>> newLS;
			
			for (int i = 1; i < nodes.size(); i++)
			{
				n = nodes.get(i);
				aktLS = new ArrayList<Set<NamedEntity>>();
				aktLS.add(n.getNamedEntitiesPerson());
				aktLS.add(n.getNamedEntitiesOrganizations());
				aktLS.add(n.getNamedEntitiesLocations());

				newLS = new ArrayList<Set<NamedEntity>>();
				
				NamedEntity ne;
				for (int j = 0; j < 3; j++)
				{
					newLS.add(new HashSet<NamedEntity>());
					
					for (Iterator<NamedEntity> it = aktLS.get(j).iterator(); it.hasNext(); )
					{
						ne = it.next();
						
						if (ls.get(j).contains(ne))
						{
							newLS.get(j).add(ne);
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
			text += makeStringForSetOfNEs(ls.get(0), neTypeColors.getPersonsString());
			text += "<br/><i>organizations:</i><br/>";
			text += makeStringForSetOfNEs(ls.get(1), neTypeColors.getOrganizationsString());
			text += "<br/><i>locations:</i><br/>";
			text += makeStringForSetOfNEs(ls.get(2), neTypeColors.getLocationsString());
			editor.setText(text);
		}
		
		
	}

}
