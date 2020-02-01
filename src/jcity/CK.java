package jcity;

import com.github.mauricioaniche.ck.metric.ClassLevelMetric;
import com.github.mauricioaniche.ck.metric.MethodLevelMetric;
import com.google.common.collect.Lists;

import jcity.util.FileUtils;
import jcity.util.MetricsFinder;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class CK {

	private static final int MAX_AT_ONCE;
	
	static {
		String jdtMax = System.getProperty("jdt.max");
		if(jdtMax!=null) {
			MAX_AT_ONCE = Integer.parseInt(jdtMax);
		} else {
			long maxMemory= Runtime.getRuntime().maxMemory() / (1 << 20); // in MiB
			
			if      (maxMemory >= 2000) MAX_AT_ONCE= 400;
			else if (maxMemory >= 1500) MAX_AT_ONCE= 300;
			else if (maxMemory >= 1000) MAX_AT_ONCE= 200;
			else if (maxMemory >=  500) MAX_AT_ONCE= 100;
			else                        MAX_AT_ONCE=  25;
		}
	}


	private static Logger log = Logger.getLogger(CK.class);

	Callable<List<ClassLevelMetric>> classLevelMetrics;
	Callable<List<MethodLevelMetric>> methodLevelMetrics;

	public CK(Callable<List<ClassLevelMetric>> classLevelMetrics, Callable<List<MethodLevelMetric>> methodLevelMetrics) {
		this.classLevelMetrics = classLevelMetrics;
		this.methodLevelMetrics = methodLevelMetrics;
	}

	public CK() {
		MetricsFinder finder = new MetricsFinder();

		this.classLevelMetrics = () -> finder.allClassLevelMetrics();
		this.methodLevelMetrics = () -> finder.allMethodLevelMetrics();
	}

	public void calculate(String path, CKNotifier notifier) {
		this.calculate(path, false, notifier);
	}

	public void calculate(String path, boolean useJars, CKNotifier notifier) {
		String[] srcDirs = FileUtils.getAllDirs(path);
		String[] javaFiles = FileUtils.getAllJavaFiles(path);
		String[] allDependencies = useJars ? FileUtils.getAllJars(path) : null;

		log.info("Found " + javaFiles.length + " java files");
		if(useJars)
			log.info("Found " + allDependencies.length + " jar dependencies");
		
		MetricsExecutor storage = new MetricsExecutor(classLevelMetrics, methodLevelMetrics, notifier);
		
		List<List<String>> partitions = Lists.partition(Arrays.asList(javaFiles), MAX_AT_ONCE);
		log.debug("Max partition size: " + MAX_AT_ONCE + ", total partitions=" + partitions.size());

		for(List<String> partition : partitions) {
			log.debug("Next partition");
			ASTParser parser = ASTParser.newParser(AST.JLS11);
			
			parser.setResolveBindings(true);
			parser.setBindingsRecovery(true);
			
			Map<String, String> options = JavaCore.getOptions();
			JavaCore.setComplianceOptions(JavaCore.VERSION_11, options);
			parser.setCompilerOptions(options);
			parser.setEnvironment(allDependencies, srcDirs, null, true);
			parser.createASTs(partition.toArray(new String[partition.size()]), null, new String[0], storage, null);
		}
		
		log.info("Finished parsing");
    }

}
