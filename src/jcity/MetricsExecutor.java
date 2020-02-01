package jcity;

import com.github.mauricioaniche.ck.metric.ClassLevelMetric;
import com.github.mauricioaniche.ck.metric.MethodLevelMetric;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class MetricsExecutor extends FileASTRequestor {

	private Callable<List<ClassLevelMetric>> classLevelMetrics;
	private Callable<List<MethodLevelMetric>> methodLevelMetrics;
	private CKNotifier notifier;

	private static Logger log = Logger.getLogger(MetricsExecutor.class);
	
	public MetricsExecutor(Callable<List<ClassLevelMetric>> classLevelMetrics, Callable<List<MethodLevelMetric>> methodLevelMetrics, CKNotifier notifier) {
		this.classLevelMetrics = classLevelMetrics;
		this.methodLevelMetrics = methodLevelMetrics;
		this.notifier = notifier;
	}


	@Override
	public void acceptAST(String sourceFilePath, 
			CompilationUnit cu) {
		
		try {
			CKVisitor visitor = new CKVisitor(sourceFilePath, cu, classLevelMetrics, methodLevelMetrics);

			cu.accept(visitor);
			Set<JClassResult> collectedClasses = visitor.getCollectedClasses();

			for (JClassResult collectedClass : collectedClasses) {
				log.info(collectedClass);
				notifier.notify(collectedClass);
			}
		} catch(Exception e) {
			log.error("error in " + sourceFilePath, e);
		}
	}
	
}
