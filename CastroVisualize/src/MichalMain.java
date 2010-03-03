
import java.util.Iterator;
import java.util.Set;

import DataModulePackage.*;

public class MichalMain {
	
	private static void makeBinIndexes()
	{
		VMindex.ConvertTextToBin("../work/PERSONS.tf", "../DataModuleData/PERSONS.tf.bin");
		VMindex.ConvertTextToBin("../work/LOCATIONS.tf", "../DataModuleData/LOCATIONS.tf.bin");
		VMindex.ConvertTextToBin("../work/ORGANIZATIONS.tf", "../DataModuleData/ORGANIZATIONS.tf.bin");

		VMindex.ConvertTextToBin("../work/PERSONS.tfidf", "../DataModuleData/PERSONS.tfidf.bin");
		VMindex.ConvertTextToBin("../work/LOCATIONS.tfidf", "../DataModuleData/LOCATIONS.tfidf.bin");
		VMindex.ConvertTextToBin("../work/ORGANIZATIONS.tfidf", "../DataModuleData/ORGANIZATIONS.tfidf.bin");
		
	}

	private static void RunTest()
	{
		VMindex.ConvertTextToBin("../work/test.tf", "../work/test.tf.bin");
		
		VMindex pomI = new VMindex("../work/test.tf.bin");
		
		for (int i = 0; i < pomI.NumDocs(); i++)
			for (int j = 0; j < pomI.NumTerms(); j++)
			{
				System.err.println("(" + i + ", " + j + ") = " + pomI.GetValue(i, j));
			}
		
		SimMatrixElem sim = SimMatrixElem.CountFromVMindex(pomI);
		
		System.err.println("");
		System.err.println("Similarities:");
			
		for (int i = 0; i < sim.getNumDocs(); i++)
			for (int j = 0; j < sim.getNumDocs(); j++)
			{
				System.err.println("(" + i + ", " + j + ") = " + sim.getSimilarity(i, j));				
			}
		
		sim.SaveToFile("../work/test.tf.sim");
		SimMatrixElem sim2 = SimMatrixElem.LoadFromFile("../work/test.tf.sim");
		
		System.err.println("");
		System.err.println("Similarities after save/load:");
			
		for (int i = 0; i < sim.getNumDocs(); i++)
			for (int j = 0; j < sim.getNumDocs(); j++)
			{
				System.err.println("(" + i + ", " + j + ") = " + sim.getSimilarity(i, j) + " -> " + sim2.getSimilarity(i,j));				
			}
	
		VMindex persI = new VMindex("../DataModuleData/PERSONS.tfidf.bin");
		
		for (int i = 0; i < 100; i++)
		{
			int a =  (int)Math.round(Math.floor(Math.random() * persI.NumDocs()));
			
			Set<Integer> si = persI.GetNonzeroCells(a);
			
			
			int b = (int)Math.round(Math.floor(Math.random() * si.size()));
			
			int ind;
			
			Iterator<Integer> it = si.iterator();
			
			for (int g = 0; g < b; g++)
			{
				it.next();
			}
			ind = it.next();
			
			System.err.println("(doc: " + a + ", term: " + b + ") = " + persI.GetValue(a, ind));
		}
		
		SimMatrixElem simPers = SimMatrixElem.CountFromVMindex(persI);
		
		
		simPers.SaveToFile("../DataModuleData/PERSONS.tfidf.sim");
		
		SimMatrixElem simPersLoaded = SimMatrixElem.LoadFromFile("../DataModuleData/PERSONS.tfidf.sim");

		for (int i = 0; i < 100; i++)
		{
			int a = (int)Math.round(Math.floor(Math.random() * simPers.getNumDocs()));
			int b = (int)Math.round(Math.floor(Math.random() * simPers.getNumDocs()));
			
			System.err.println("sim(" + a + ", " + b + ") = " + simPers.getSimilarity(a, b) + " -> " + simPersLoaded.getSimilarity(a, b));
		}

		
		return;				
	}
	
	private static void makeSimMatrices()
	{
		VMindex ind = new VMindex("../DataModuleData/PERSONS.tf.bin");
		SimMatrixElem sim = SimMatrixElem.CountFromVMindex(ind);
		sim.SaveToFile("../DataModuleData/PERSONS.tf.sim");

		ind = new VMindex("../DataModuleData/PERSONS.tfidf.bin");
		sim = SimMatrixElem.CountFromVMindex(ind);
		sim.SaveToFile("../DataModuleData/PERSONS.tfidf.sim");
		
		ind = new VMindex("../DataModuleData/LOCATIONS.tf.bin");
		sim = SimMatrixElem.CountFromVMindex(ind);
		sim.SaveToFile("../DataModuleData/LOCATIONS.tf.sim");

		ind = new VMindex("../DataModuleData/LOCATIONS.tfidf.bin");
		sim = SimMatrixElem.CountFromVMindex(ind);
		sim.SaveToFile("../DataModuleData/LOCATIONS.tfidf.sim");

		ind = new VMindex("../DataModuleData/ORGANIZATIONS.tf.bin");
		sim = SimMatrixElem.CountFromVMindex(ind);
		sim.SaveToFile("../DataModuleData/ORGANIZATIONS.tf.sim");

		ind = new VMindex("../DataModuleData/ORGANIZATIONS.tfidf.bin");
		sim = SimMatrixElem.CountFromVMindex(ind);
		sim.SaveToFile("../DataModuleData/ORGANIZATIONS.tfidf.sim");

	}
	
	public static void main(String[] args) {
		//makeBinIndexes();
		//makeSimMatrices();
		DataModule.Init();
		DataModule.getGraph("NULL", "NULL", "NULL", "NULL", "NULL", 0.50);
		
		//System.err.println("OK!");
		
	}
}