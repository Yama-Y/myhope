package com.myhope.util.datafile.xml.importxml;

/**
 * 导入列约束实体类
 */
public class ColumnConstraint {
	private Integer colNum;// 列数
	private String colName;// 列名
	private String pojoName;// 实体类对象名
	private String value;// 默认值
	private Integer isNull;// 是否为空 0:允许空; 1:非空;
	private Integer isEncrypt;// 是否加密 0:加密; 1:非加密;
	private Integer isRepeat;// 是否加密 0:允许重复; 1:非重;
	private Integer isImport;// 是否导入 0:导入; 1:不导入;
	private String regularExpression;// 校验正则
	private String msg;// 不符合正则返回的验证信息
	private String you1;// 自定义1
	private String you2;// 自定义2
	private String you3;// 自定义3

	public ColumnConstraint() {

	}

	public Integer getColNum() {
		return colNum;
	}

	public void setColNum(Integer colNum) {
		this.colNum = colNum;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getPojoName() {
		return pojoName;
	}

	public void setPojoName(String pojoName) {
		this.pojoName = pojoName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getIsNull() {
		return isNull;
	}

	public void setIsNull(Integer isNull) {
		this.isNull = isNull;
	}

	public Integer getIsEncrypt() {
		return isEncrypt;
	}

	public void setIsEncrypt(Integer isEncrypt) {
		this.isEncrypt = isEncrypt;
	}

	public Integer getIsRepeat() {
		return isRepeat;
	}

	public void setIsRepeat(Integer isRepeat) {
		this.isRepeat = isRepeat;
	}

	// 1.1
	public Integer getIsImport() {
		return isImport;
	}

	// 1.1
	public void setIsImport(Integer isImport) {
		this.isImport = isImport;
	}

	public String getRegularExpression() {
		return regularExpression;
	}

	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getYou1() {
		return you1;
	}

	public void setYou1(String you1) {
		this.you1 = you1;
	}

	public String getYou2() {
		return you2;
	}

	public void setYou2(String you2) {
		this.you2 = you2;
	}

	public String getYou3() {
		return you3;
	}

	public void setYou3(String you3) {
		this.you3 = you3;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ColumnConstraint) {
			ColumnConstraint columnConstraint = (ColumnConstraint) obj;
			if (this.colNum.equals(columnConstraint.getColNum())) {
				return true;
			}
		}
		return false;
	}

}