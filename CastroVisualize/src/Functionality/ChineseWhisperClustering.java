package Functionality;

public abstract class ChineseWhisperClustering {
	
	private static ChineseWhisperClustering implementation;
	
	public abstract void Evaluate(Graph g, int maxNumClusters, int numIterations);
	
	public static ChineseWhisperClustering GetImplementation()
	{
		return implementation;
	}
	
	public static void SetImplementation(ChineseWhisperTypesEnum en)
	{
		if (en == ChineseWhisperTypesEnum.standard)
		{
			implementation = new ChineseWhisperClusteringBasic();			
		}
		else if (en == ChineseWhisperTypesEnum.modified)
		{
			implementation = new ChineseWhisperClusteringAdjusted();
		}
		else if (en == ChineseWhisperTypesEnum.modifiedNew)
		{
			System.err.println("Not implemented!");
			System.exit(1);
			//implementation = new ChineseWhisperClusteringAdjustedNew();
		}
	}
	
	public enum ChineseWhisperTypesEnum
	{
		standard, modified, modifiedNew
	}

}
