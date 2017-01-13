package com.myhope.util.datafile.xml.importxml;

import java.util.Set;

/**
 * 导入XML类名约束实体类
 */
public class ClassNameConstraint {
	private String className;
	private Set<ColumnConstraint> columnConstraints;// 导入列约束

	public ClassNameConstraint() {

	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Set<ColumnConstraint> getColumnConstraints() {
		return columnConstraints;
	}

	public void setColumnConstraints(Set<ColumnConstraint> columnConstraints) {
		this.columnConstraints = columnConstraints;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ClassNameConstraint) {
			ClassNameConstraint pojoNameConstraint = (ClassNameConstraint) obj;
			if (this.className.equals(pojoNameConstraint.getClassName())) {
				return true;
			}
		}
		return false;
	}

}