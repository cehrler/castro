package Visualizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
	
	private String makeStringForStringMap(Map<String, Boolean> sneID, String color)
	{
		String text = "";
		String ne;
		for (Iterator<String> it = sneID.keySet().iterator(); it.hasNext(); )
		{
			ne = it.next();
			if (sneID.get(ne))
			{
				text += "<span style=\"color:" + color + "\">" + ne + "</span><br/>";
			}
			else
			{
				text += "<span style=\"color:" + color + "\"><b>" + ne + "</b></span><br/>";				
			}
		}
		return text;
	}

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
			
			Set<NamedEntity> persNE = n.getNamedEntitiesPerson();
			Set<NamedEntity> locsNE = n.getNamedEntitiesLocations();
			Set<NamedEntity> orgsNE = n.getNamedEntitiesOrganizations();

			Map<String, Boolean> persMap = new HashMap<String, Boolean>();
			Map<String, Boolean> locsMap = new HashMap<String, Boolean>();
			Map<String, Boolean> orgsMap = new HashMap<String, Boolean>();
			
			System.err.println("persNE.size" + (new ArrayList<NamedEntity>(persNE)).size());
			
			NamedEntity ne;
			for (Iterator<NamedEntity> it = persNE.iterator(); it.hasNext(); )
			{
			  ne = it.next();
			  persMap.put(ne.getText(), ne.getExpanded());
			}

			for (Iterator<NamedEntity> it = locsNE.iterator(); it.hasNext(); )
			{
			  ne = it.next();
			  locsMap.put(ne.getText(), ne.getExpanded());
			}

			for (Iterator<NamedEntity> it = orgsNE.iterator(); it.hasNext(); )
			{
			  ne = it.next();
			  orgsMap.put(ne.getText(), ne.getExpanded());
			}

			String bleS;
			int pomI;
			for (int i = 1; i < nodes.size(); i++)
			{
				n = nodes.get(i);

				Map<String, Boolean> newPersMap = new HashMap<String, Boolean>();
				Map<String, Boolean> newLocsMap = new HashMap<String, Boolean>();
				Map<String, Boolean> newOrgsMap = new HashMap<String, Boolean>();
				
				for (Iterator<String> it = persMap.keySet().iterator(); it.hasNext(); )
				{
					bleS = it.next();
					pomI = n.containsNE(bleS);
					
					if (pomI == 1)
					{
						newPersMap.put(bleS, true);
					}
					else if (pomI == 2)
					{						
						newPersMap.put(bleS, persMap.get(bleS));
					}
				}

				for (Iterator<String> it = locsMap.keySet().iterator(); it.hasNext(); )
				{
					bleS = it.next();
					pomI = n.containsNE(bleS);
					
					if (pomI == 1)
					{
						newLocsMap.put(bleS, true);
					}
					else if (pomI == 2)
					{
						newLocsMap.put(bleS, locsMap.get(bleS));
					}
				}

				for (Iterator<String> it = orgsMap.keySet().iterator(); it.hasNext(); )
				{
					bleS = it.next();
					pomI = n.containsNE(bleS);
					
					if (pomI == 1)
					{
						orgsMap.put(bleS, true);
					}
					else if (pomI == 2)
					{
						newOrgsMap.put(bleS, orgsMap.get(bleS));
					}
				}
				
				persMap = newPersMap;
				locsMap = newLocsMap;
				orgsMap = newOrgsMap;
			}
			
			String text = "Selected docs IDs:<br/>";// = n.getHeadline() + "<br/><br/>";
			
			for (int i = 0; i < nodes.size(); i++)
			{
				if (i != 0) text += ", ";
				text += nodes.get(i).getSpeech_id();
			}
			
			text += "<br/><br/><b>Common named entities:</b><br/>";
			
			text += "<i>persons:</i><br/>";
			text += makeStringForStringMap(persMap, neTypeColors.getPersonsString());
			text += "<br/><i>organizations:</i><br/>";
			text += makeStringForStringMap(orgsMap, neTypeColors.getOrganizationsString());
			text += "<br/><i>locations:</i><br/>";
			text += makeStringForStringMap(locsMap, neTypeColors.getLocationsString());
			editor.setText(text);
		}
		
		
	}

}
