package com.myhope.util.datafile.importfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Import4You {
	private Integer dataSize = 0;

	private Map<String, List<Object[]>> importDatas4YouMap = new HashMap<String, List<Object[]>>();

	/**
	 * @Title: 导入模板追加数据重写方法
	 * @return
	 * @author: YangMing
	 * @date: 2015-8-31
	 */
	public List<String[]> addExportData4You() {
		// List<String[]> addData = new ArrayList<String[]>();
		// return addData;
		return null;
	}

	/**
	 * @Title: 验证单条数据的重写方法
	 * @param: OneData 一行数据的String数组
	 * @return: Object[0] Boolean 成功/失败 Object[1] 失败的错误信息
	 * @author: YangMing
	 * @date: 2015-8-31
	 */
	public Object[] validateOneDatas4You(String[] OneData) {
		// Object[] result = new Object[2];
		// result[0] = true;
		// result[1] = "";
		// return result;
		return null;
	}

	/**
	 * @Title: 验证单个值的重写方法(you1)
	 * @param: value 数据值
	 * @param: you1 参数值
	 * @return: Object[0] Boolean 成功/失败 Object[1] 失败的错误信息
	 * @author: YangMing
	 * @date: 2015-8-31
	 */
	public Object[] validateOneValue4You1(String value, String you1) {
		// Object[] result = new Object[2];
		// result[0] = true;
		// result[1] = "";
		// return result;
		return null;
	}

	/**
	 * @Title: 验证单个值的重写方法(you2)
	 * @param: value 数据值
	 * @param: you2 参数值
	 * @return: Object[0] Boolean 成功/失败 Object[1] 失败的错误信息
	 * @author: YangMing
	 * @date: 2015-8-31
	 */
	public Object[] validateOneValue4You2(String value, String you2) {
		// Object[] result = new Object[2];
		// result[0] = true;
		// result[1] = "";
		// return result;
		return null;
	}

	/**
	 * @Title: 验证单个值的重写方法(you3)
	 * @param: value 数据值
	 * @param: you3 参数值
	 * @return: Object[0] Boolean 成功/失败 Object[1] 失败的错误信息
	 * @author: YangMing
	 * @date: 2015-8-31
	 */
	public Object[] validateOneValue4You3(String value, String you3) {
		// Object[] result = new Object[2];
		// result[0] = true;
		// result[1] = "";
		// return result;
		return null;
	}

	/**
	 * @Title: 追加入库数据重写方法
	 * @return
	 * @author: YangMing
	 * @date: 2015-9-1
	 */
	public void addImportData4You(String className, String pojoName, Object parameter) {
		List<Object[]> importDataList = null;
		if (importDatas4YouMap.containsKey(className)) {
			importDataList = importDatas4YouMap.get(className);
			Object[] importData = { pojoName, parameter };
			importDataList.add(importData);
			importDatas4YouMap.remove(className);
			importDatas4YouMap.put(className, importDataList);
		} else {
			importDataList = new ArrayList<Object[]>();
			Object[] importData = { pojoName, parameter };
			importDataList.add(importData);
			importDatas4YouMap.put(className, importDataList);
		}
	}

	public Map<String, List<Object[]>> getImportDatas4YouMap() {
		return importDatas4YouMap;
	}

	public void setImportDatas4YouMap(Map<String, List<Object[]>> importDatas4YouMap) {
		this.importDatas4YouMap = importDatas4YouMap;
	}

	public Integer getDataSize() {
		return dataSize;
	}

	public void setDataSize(Integer dataSize) {
		this.dataSize = dataSize;
	}
}
