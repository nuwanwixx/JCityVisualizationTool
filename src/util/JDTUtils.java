package util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

public class JDTUtils {
	public static int getStartLine(CompilationUnit cu, MethodDeclaration node) {
		return node.getBody() != null ?
				cu.getLineNumber(node.getBody().getStartPosition()) :
				cu.getLineNumber(node.getStartPosition());
	}

	public static int getStartLine(CompilationUnit cu, Initializer node) {
		return node.getBody() != null ?
				cu.getLineNumber(node.getBody().getStartPosition()) :
				cu.getLineNumber(node.getStartPosition());
	}

	public static String getMethodFullName(IMethodBinding node) {
		String methodName = node.getName();

		int parameterCount = node.getParameterTypes()==null ? 0 : node.getParameterTypes().length;
		List<String> parameterTypes = new ArrayList<>();

		if(parameterCount > 0) {
			for(ITypeBinding binding : node.getParameterTypes()) {

				String v = binding.getQualifiedName();

				parameterTypes.add(v);
			}
		}

		return String.format("%s/%d%s%s%s",
				methodName,
				parameterCount,
				(parameterCount > 0 ? "[" : ""),
				(parameterCount > 0 ? String.join(",", parameterTypes) : ""),
				(parameterCount > 0 ? "]" : "")
		);
	}

	public static String getMethodFullName(MethodDeclaration node) {
		String methodName = node.getName().getFullyQualifiedName();

		int parameterCount = node.parameters()==null ? 0 : node.parameters().size();
		List<String> parameterTypes = new ArrayList<>();

		if(parameterCount > 0) {
			for(Object p0 : node.parameters()) {
				SingleVariableDeclaration parameter = (SingleVariableDeclaration) p0;

				ITypeBinding binding = parameter.getType().resolveBinding();

				String v;
				if(binding == null || binding.isRecovered())
					v = parameter.getType().toString();
				else
					v = binding.getQualifiedName();

				if(parameter.isVarargs()) v+="[]";

				parameterTypes.add(v);
			}
		}

		return String.format("%s/%d%s%s%s",
				methodName,
				parameterCount,
				(parameterCount > 0 ? "[" : ""),
				(parameterCount > 0 ? String.join(",", parameterTypes) : ""),
				(parameterCount > 0 ? "]" : "")
		);
	}

	public static long countLoc(ASTNode type) {
		return type.toString().chars().filter(x -> x == '\n').count() + 1;
	}
}
