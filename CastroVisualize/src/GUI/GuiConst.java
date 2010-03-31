package GUI;

public class GuiConst {

	public static String search_button_tooltip = "Search for documents";
	public static String table_search_tooltip = "Table containing query results, documents are sorted according to their relevance to the query";
	public static String search_year_start_tooltip = "Limit the historical period.";
	public static String search_year_end_tooltip = "Limit the historical period.";
	public static String search_type_tooltip = "Select the type of document.";
	public static String NE_textField_tooltip = "Type in the query terms you want to search for. Put multiword named entities inside the paranthesis (\"Communist party\", etc.)";
	public static String maxDocsTB_tooltip = "Maximum number of documents to be retrieved.";
	public static String filterCB_tooltip = "Select the document filter - Document filter allows you to display subparts of the whole graph.";
	public static String distanceSlider_tooltip = "Select the maximum document distance from the selected document.";
	public static String distanceFilterTypeCB_tooltip = "Determines the behaviour of the filter when more documents are selected. (See the user manual)";
	public static String coreEdgeTypeCB_tooltip = "Determines the types of edges that can be used to visit documents. (See the user manual)";
	public static String edgeDisplayTypeCB_tooltip = "Selects whether the number of edges and their appearence is adjusted by edge density (relative mode) or edge threshold (absolute mode)";
	public static String edgeDensitySlider_tooltip = "Adjust link density.";
	public static String edgeThresholdSlider_tooltip = "Adjust link threshold.";
	public static String dottedEdgeChB_tooltip = "Show dotted links.";
	public static String normalEdgeChB_tooltip = "Show normal links.";
	public static String thickEdgeChB_tooltip = "Show thick links.";
	public static String layoutStartStopBtn_tooltip = "Start or stop the layouting process which tries to put connected documents closer together.";
	public static String jep_tooltip = "Named entities contained in the document. It shows common named entities when more than one document is selected.";
	private GuiConst() {}

}
