
import DataModulePackage.*;

public class MichalMain {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//"com.mysql.jdbc.Driver"


		
		VMindex vmindex = new VMindex("../work/ORGANIZATIONS.tfidf.bin");
		
		DataModule.Init();
		DataModule.getGraph("NULL", "NULL", "NULL", "NULL", "interview", 0.0);
		
		System.err.println("OK!");
		
	}
}