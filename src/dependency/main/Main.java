package dependency.main;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.runtime.CoreException;

import dependency.core.Architecture;
import dependency.exception.DCLException;
import dependency.util.TxtFileWriter;

public class Main {
	
	String path;
	

	public Main(String path) {
		super();
		this.path = path;
	}
	

	public Collection<String> mainRunner()throws CoreException, IOException, DCLException, InterruptedException {
		String root = "D:\\Academic\\IT mora\\Eclipse Progams\\DepExtTestAA";
		
		if(!path.equals(null)){
			root.equals(path);
		}
		
		File filePath = new File("").getAbsoluteFile();
		String path = filePath.getAbsolutePath();
		String projectPath = "";
		
		File f = new File(root);
		
		if(!f.exists()){
			File f2 = new File(path+"/"+root);
			if(!f2.exists()){
				File f3 = new File(path);
				if(!f3.exists()){
					System.out.println("Wrong Specified Path!");
				}else{
					projectPath = path;
				}
			}else{
				projectPath = path+"/"+root;
			}
		}else{
			projectPath = root;
		}
		
		Collection<String> listDep = new ArrayList<String>();
		if(!projectPath.equals("")){
			Architecture architecture = new Architecture(projectPath);
			listDep = architecture.getDependencies();
			
		}
		return listDep;
	}


//	public static void main(String[] args) throws CoreException, IOException, DCLException, InterruptedException {
//		String root = "D:\\Academic\\IT mora\\Eclipse Progams\\DepExtTestAA";
//		
//		File filePath = new File("").getAbsoluteFile();
//		String path = filePath.getAbsolutePath();
//		String projectPath = "";
//		
//		File f = new File(root);
//		
//		if(!f.exists()){
//			File f2 = new File(path+"/"+root);
//			if(!f2.exists()){
//				File f3 = new File(path);
//				if(!f3.exists()){
//					System.out.println("Wrong Specified Path!");
//				}else{
//					projectPath = path;
//				}
//			}else{
//				projectPath = path+"/"+root;
//			}
//		}else{
//			projectPath = root;
//		}
//		
//		if(!projectPath.equals("")){
//			Architecture architecture = new Architecture(projectPath);
//			Collection<String> listDep = architecture.getDependencies();
//			for (String string : listDep) {
//				System.out.println(string);
//			}
//			
////			TxtFileWriter.writeTxtFile(architecture.getDependencies(), projectPath);
//		}
//		
//	}
}
