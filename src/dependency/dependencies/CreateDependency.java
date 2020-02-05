package dependency.dependencies;

import dependency.enums.DependencyType;

import dependency.util.DCLUtil;

@SuppressWarnings("serial")
public class CreateDependency extends Dependency {
	public CreateDependency(String classNameA, String classNameB, Integer lineNumberA, Integer offset, Integer length) {
		super(classNameA, classNameB, lineNumberA, offset, length);
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.CREATE;
	}
	
	@Override
	public String toShortString() {
		return "The creation of " + DCLUtil.getSimpleClassName(this.classNameB) + " is disallowed for this location w.r.t. the architecture";
	}
}