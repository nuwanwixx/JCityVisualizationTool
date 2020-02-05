package dependency.dependencies;

import dependency.enums.DependencyType;

import dependency.util.DCLUtil;

@SuppressWarnings("serial")
public class AccessDependency extends HandleDependency {

	public AccessDependency(String classNameA, String classNameB, Integer lineNumberA, Integer offset, Integer length) {
		super(classNameA, classNameB, lineNumberA, offset, length);
	}
	
	@Override
	public DependencyType getDependencyType() {
		return DependencyType.ACCESS;
	}
	
	@Override
	public String toShortString() {
		return "The access to " + DCLUtil.getSimpleClassName(this.classNameB) + " is disallowed for this location w.r.t. the architecture";
	}
	
}