package DataModulePackage; 


	import java.io.BufferedReader;
	import java.io.DataInputStream;
	import java.io.DataOutputStream;
	import java.io.FileInputStream;
	import java.io.FileOutputStream;
	import java.io.InputStreamReader;
	import java.util.HashMap;
	import java.util.Iterator;
	import java.util.Map;
	
	public class convertIndex {
		
		private static void convert(String input, String output)
		{
			System.err.println("Converting " + input + " to " + output);
			Double val;
			Double eps = 0.0000001;
			try {
				FileInputStream ios =  new FileInputStream(input);
			    DataInputStream in = new DataInputStream(ios);
		        BufferedReader br = new BufferedReader(new InputStreamReader(in));
	
		        DataOutputStream os = new DataOutputStream(new FileOutputStream(output));
		        
		        
		        String s;
		        Integer numSpeeches;
		        Integer numNEs;
		        
		        s = br.readLine();
		        
		        if (s.contains("speeches:"))
		        {
		        	numSpeeches = Integer.parseInt(s.substring(9));
		        	os.writeInt(numSpeeches);
		        	//System.err.println("Integer: " + numSpeeches);
		        }
		        else
		        {
		        	throw new Exception("invalid first row!");
		        }
		        
		        s = br.readLine();
		        
		        if (s.contains("NEs:"))
		        {
		        	numNEs = Integer.parseInt(s.substring(4));
		        	os.writeInt(numNEs);
		        	//System.err.println("Integer: " + numNEs);
		        }
		        else
		        {
		        	throw new Exception("invalid second row!");
		        }
		        
		        
		        for (int i = 0; i < numSpeeches; i++)
		        {
		            Map<Integer, Double> hm = new HashMap<Integer, Double>();
		        	Integer count = 0;
			        for (int j = 0; j < numNEs; j++)
			        {
			          val = Double.parseDouble(br.readLine());
			          
			          if (val.compareTo(eps) > 0)
			          {
			        	  hm.put(j, val);
			        	  count++;
			          }
			        }
			        
			        s = br.readLine();
			        
			        if (! s.substring(0,3).equals("---"))
			        {
			        	throw new Exception("Wrong input format! -- " + s.substring(0,3));
			        }
			        
			        os.writeInt(count);
		        	//System.err.println("Integer: " + count);
			        
			        Integer count2 = 0;
			        for (Iterator<Integer> it = hm.keySet().iterator(); it.hasNext(); )
			        {
			        	count2++;
			        	Integer pomInt = it.next();
			        	Double val2 = hm.get(pomInt);
			        	os.writeInt(pomInt);
			        	os.writeDouble(val2);
			        	//System.err.println("Double: " + val2);
			        }
			        
			        //System.out.println(count);
			        
			        if (!count.equals(count2))
			        {
			        	throw new Exception("count is not equal to count2! " + count.toString() + " != " + count2.toString());
			        }
			        
			        //throw new Exception("That's enough");
			    }
		        
		        if (br.ready())
		        {
		        	throw new Exception("eof expected, but there is something left!");
		        }
		        
		        os.close();
		        
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		
		}
			
		
		public static void main(String[] args) {
			//../work/PERSONS.tfidf ../work/PERSONS.tfidf.bin
			
			int a = 256;
			byte b = (byte)a;
			
			System.err.println("byte = " + b);
			
			int c = b;
			if (c < 0) c += 256;
			System.err.println("c = " + c);
			
			
			long ble = 124123;
			int bleInt = (int)ble;
			System.err.println(bleInt);
			
			VMindex index = new VMindex("../work/PERSONS.tfidf.bin");
			
			SimMatrixElem sim = SimMatrixElem.CountFromVMindex(index);
			
			System.err.println("similarity(1, 2): " + sim.getSimilarity(1, 2));
			
			sim.SaveToFile("../work/PERSONS.sim.bin");
			
			SimMatrixElem sim2 = SimMatrixElem.LoadFromFile("../work/PERSONS.sim.bin");
			System.err.println("similarity(1, 2): " + sim2.getSimilarity(1, 2));
			
			
			/*
			convert("../work/PERSONS.tfidf", "../work/PERSONS.tfidf.bin");
			convert("../work/LOCATIONS.tfidf", "../work/LOCATIONS.tfidf.bin");
			convert("../work/ORGANIZATIONS.tfidf", "../work/ORGANIZATIONS.tfidf.bin");
			convert("../work/PERSONS.tf", "../work/PERSONS.tf.bin");
			convert("../work/LOCATIONS.tf", "../work/LOCATIONS.tf.bin");
			convert("../work/ORGANIZATIONS.tf", "../work/ORGANIZATIONS.tf.bin");
			*/
		}
	}
