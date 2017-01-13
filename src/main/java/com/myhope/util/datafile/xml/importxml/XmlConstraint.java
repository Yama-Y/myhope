package com.myhope.util.datafile.xml.importxml;

import java.util.Set;

/**
 * XML约束实体类
 */
public class XmlConstraint {
	private Integer dataSize;// 导入最大行数
	private Integer exportWay;// 导出模板方式
	private String exportFilePath;// 导出模板路径
	private Set<ClassNameConstraint> classNameConstraints;// 自定义3

	public XmlConstraint() {

	}

	public Integer getDataSize() {
		return dataSize;
	}

	public void setDataSize(Integer dataSize) {
		this.dataSize = dataSize;
	}

	public Integer getExportWay() {
		return exportWay;
	}

	public void setExportWay(Integer exportWay) {
		this.exportWay = exportWay;
	}

	public String getExportFilePath() {
		return exportFilePath;
	}

	public void setExportFilePath(String exportFilePath) {
		this.exportFilePath = exportFilePath;
	}

	public Set<ClassNameConstraint> getClassNameConstraints() {
		return classNameConstraints;
	}

	public void setClassNameConstraints(Set<ClassNameConstraint> classNameConstraints) {
		this.classNameConstraints = classNameConstraints;
	}

}