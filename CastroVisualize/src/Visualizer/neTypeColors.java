// Name        : neTypeColors.java
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
//Description  : class storing the name of the colours that are used for named entity highlighting.
//               It is used for adding html tags to text panels.
//===============================================================================================


package Visualizer;

public class neTypeColors {
	private static String personsColorString = "red";
	private static String organizationsColorString = "green";
	private static String locationsColorString = "blue";
	
	public static String getPersonsString()
	{
		return personsColorString;
	}
	
	public static String getOrganizationsString()
	{
		return organizationsColorString;
	}
	
	public static String getLocationsString()
	{
		return locationsColorString;
	}
	
}
