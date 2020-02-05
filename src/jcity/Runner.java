package jcity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;

import com.google.common.collect.Multimap;

import dependency.exception.DCLException;
import dependency.main.Main;
import jcity.util.ResultWriter;

public class Runner {

	public static void main(String[] args) throws IOException, CoreException, DCLException, InterruptedException { 


		String path = "D:\\Academic\\IT mora\\Course Modules\\4th Year\\Research Project\\Dependency Extractor\\2019.112.16\\CK NEW\\ck-master";
		boolean useJars = false;
		if(args.length == 2)
			useJars = Boolean.parseBoolean(args[1]);
		
		ResultWriter writer = new ResultWriter("class.csv");
		
	
		HashMap<String, JCity> newList = new HashMap<String, JCity>();
	
		new CK().calculate(path, useJars, result -> {
			try {
			    writer.printResult(result);
			    String file = result.getFile();
			    String className = result.getClassName();
			    int loc = result.getLoc();
			    int methods = result.getNumberOfMethods();
			    int variables = result.getVariablesQty();
			 
			    
			    JCity ckList = new JCity(file, className, methods, loc, variables);
			    newList.put(file, ckList);
			    System.out.println(ckList.toString());
 
			    
			   
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
		
		Main main = new Main();
		
		Collection<String> depLsit =  main.mainRunner(path);
		
		HashMap<String, String> extendList = main.getExtendedlist(depLsit);
		Multimap<String, String> implementList = main.getImplementedlist(depLsit);
		
		for (HashMap.Entry<String, String> item : extendList.entrySet()) {
			String className =  item.getKey();
			String superClass = item.getValue();
			
			
			for (HashMap.Entry<String, JCity> jcity : newList.entrySet()) {
				JCity classBuilding = jcity.getValue();
				
				if (classBuilding.getClassName().equalsIgnoreCase(className)) {
					classBuilding.setSuperClass(superClass);
					
				}	
				
				
			}
		}
		
		
		for (HashMap.Entry<String, JCity> jcity : newList.entrySet()) {
			JCity classBuilding = jcity.getValue();
			ArrayList<String> interfaceList = new ArrayList<String>();
			for (Map.Entry<String, String> item : implementList.entries()) {
				String className = item.getKey();
				String interfaceName = item.getValue();
				
				if (classBuilding.getClassName().equalsIgnoreCase(className)) {
					interfaceList.add(interfaceName);	
				}	
					
			}
			classBuilding.setInterfaceList(interfaceList);
		}
		
		for (String string : depLsit) {
			System.out.println(string);
		}
	
		for (HashMap.Entry<String, JCity> jcity : newList.entrySet()) {
			JCity classBuilding = jcity.getValue();
			
			System.out.println(classBuilding.toString());
				
		}	
		
		
		
		writer.flushAndClose();
	}
}
