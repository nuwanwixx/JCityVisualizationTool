package jcity;

import java.io.IOException;
import java.util.HashMap;
import util.ResultWriter;



public class Runner {

	public static void main(String[] args) throws IOException {
		String path = "D:\\Academic\\IT mora\\Eclipse Progams\\DependencyTestOne";
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
			    
			    JCity jList = new JCity(file, className, methods, loc, variables);
			    newList.put(file, jList);
			    System.out.println(jList.toString());
 
			    
			   
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
		
		System.out.println(newList);
		
		writer.flushAndClose();

	}

}
