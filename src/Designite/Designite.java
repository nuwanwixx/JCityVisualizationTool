package Designite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Designite.SourceModel.*;

/**
 * 
 * This is the start of the bug finder
 */
public class Designite {
	public static Map<String, HashMap<String, ArrayList<String>>> designiteRun(String inputpath) throws IOException {
		String inputPath = inputpath;

		InputArgs argsObj =  new InputArgs(inputPath);
		SM_Project project = new SM_Project(argsObj); //get source name and path for save result - argsObj


		project.parse();
		project.resolve();
		project.computeMetrics();
		project.detectCodeSmells();
		
		SM_Type sm_type =  new SM_Type();
		
		return sm_type.getBuglist();
	}
}
