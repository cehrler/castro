package Functionality;

public abstract class ChineseWhisperClustering {
	
	private static ChineseWhisperClustering implementation;
	
	public abstract void Evaluate(Graph g, int maxNumClusters, int numIterations);
	
	public static ChineseWhisperClustering GetImplementation()
	{
		return implementation;
	}
	
	public static void SetImplementationToBasic()
	{
		implementation = new ChineseWhisperClusteringBasic();
	}
	
	public static void SetImplementationToAdjusted()
	{
		implementation = new ChineseWhisperClusteringAdjusted();
	}

}
