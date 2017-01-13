package com.myhope.util.datafile.importfile;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.myhope.dao.base.BaseDaoI;
import com.myhope.util.base.DateUtil;
import com.myhope.util.base.EncryptUtil;
import com.myhope.util.base.ReUtil;
import com.myhope.util.base.StringUtil;
import com.myhope.util.datafile.readfile.ReadCsvFile;
import com.myhope.util.datafile.writefile.WriteCsvFile;
import com.myhope.util.datafile.xml.importxml.ColumnConstraint;
import com.myhope.util.datafile.xml.importxml.ExplainXml;

/**
 * 提供了导入CSV文件的基础封装 可以按照自身需求追加校验
 * 
 * @author：YangMing
 */
/**
 * Description: 提供了导入CSV文件的基础封装 可以按照自身需求追加校验 Version: 1.2 <br>
 * Version: 1.1 追加行数限制;增加字段标示isImport:是否导入 <br>
 * Version: 1.2 追加导入数据库出错提示;开放入库字段追加重写方法;追加log;优化捕捉错误代码,跳出循环 <br>
 * Version: 1.3 优化文件目录结构，提高扩展性，优化部分稳定性 <br>
 * Date: 2015-8-31 <br>
 * Copyright (C) 2015 BJDV <br>
 * 
 * @author: YangMing
 * @email: ming_yang1@bjdv.com
 */
public class ImportCsvUtil {

	private static Logger log = Logger.getLogger(ImportCsvUtil.class);
	private BaseDaoI<Object> baseDao;
	private Import4You import4You = null;

	// 验证重复SET
	private Map<Integer, Set<String>> vRepeatSetMap = new HashMap<Integer, Set<String>>();

	public ImportCsvUtil() {
	}

	/**
	 * @Title: 导出模板
	 * @param: response
	 * @param: importElement 导入规则xml标签名
	 * @param: fileName 文件名
	 * @author: YangMing
	 * @date: 2015-8-31
	 */
	public void exportTempletCsv(HttpServletResponse response, String importElement, String fileName) {
		ExplainXml explainXml = new ExplainXml();
		Map<Integer, ColumnConstraint> validateMap = explainXml.makeValidateMap(explainXml.makeXml2ConstraintMap(importElement));
		// 创建导入的列数
		Integer size = validateMap.keySet().size();
		List<String[]> allData = new ArrayList<String[]>();
		String[] data = new String[size];
		for (int i = 0; i < size; i++) {
			data[i] = validateMap.get(i).getColName();
		}
		allData.add(data);
		if (import4You != null) {
			List<String[]> addData = import4You.addExportData4You();
			if (addData != null) {
				allData.addAll(addData);
			}
		}
		WriteCsvFile writeCsvFile = new WriteCsvFile();
		writeCsvFile.writeCsvFile(response, allData, fileName);
	}

	/**
	 * @Title: 导入csv文件(导入之前校验)
	 * @param: file csv文件对象
	 * @param: importElement 导入规则xml标签名
	 * @return: Object[0]:boolean导入情况 Object[1]:String错误信息
	 * @author: YangMing
	 * @date: 2015-8-31
	 */
	public Object[] importCsvFile(File file, String importElement) {
		Object[] result = new Object[2];
		ReadCsvFile explainCsvFile = new ReadCsvFile();
		List<String[]> csvList = explainCsvFile.getCsvList(file);
		explainCsvFile = null;
		// 初始参数XML数据读取
		ExplainXml explainXml = new ExplainXml();
		Map<String, List<ColumnConstraint>> constraintMap = explainXml.makeXml2ConstraintMap(importElement);
		explainXml = null;
		result = validateCsvList(csvList, constraintMap);
		if (!(Boolean) result[0]) {
			return result;
		} else {
			result = ImportData(csvList, constraintMap);
		}
		return result;
	}

	/**
	 * 校验csv文件(校验并对数据进行处理)
	 * 
	 * @param file
	 *            csv文件对象
	 * @param importElement
	 *            导入规则xml标签名
	 * @return Object[0]:boolean导入情况 Object[1]:String错误信息
	 */
	private Object[] validateCsvList(List<String[]> csvList, Map<String, List<ColumnConstraint>> constraintMap) {
		Object[] result = new Object[2];
		result[0] = true;
		StringBuffer sb = new StringBuffer();
		int msgSum = 0;
		ExplainXml explainXml = new ExplainXml();
		Map<Integer, ColumnConstraint> validateMap = explainXml.makeValidateMap(constraintMap);
		explainXml = null;

		// 验证表格标题合法性
		String[] headStrings = csvList.get(0);
		for (int i = 0; i < headStrings.length; i++) {
			if (!StringUtil.trim((validateMap.get(i).getColName())).equals(StringUtil.trim(headStrings[i]))) {
				result[0] = false;
				sb.append((i + 1) + "列: 表头不符合，请勿自行增删列。");
				break;
			}
		}
		// 1.1
		Integer dataSize = import4You.getDataSize();
		if (dataSize > 0) {
			if (csvList.size() - 1 > dataSize) {
				result[0] = false;
				sb.append("导入数据行数不能超过").append(dataSize).append("行。");
			}
		}
		if (!(Boolean) result[0]) {
			result[1] = sb.toString();
			return result;
		}
		// 验证数据合法性
		for (int i = 1; i < csvList.size(); i++) {
			if (msgSum > 15) {
				break;
			}
			Object[] oneResult = validateOneDatas(csvList.get(i), validateMap);
			if (!(Boolean) oneResult[0]) {
				result[0] = false;
				sb.append(i + 1).append("行: ");
				sb.append(oneResult[1].toString());
				sb.append("</br>");
				msgSum++;
			}
		}
		result[1] = sb.toString();
		return result;
	}

	/**
	 * 验证单条数据
	 */
	private Object[] validateOneDatas(String[] OneData, Map<Integer, ColumnConstraint> validateMap) {
		Object[] result = new Object[2];
		result[0] = true;
		StringBuffer sb = new StringBuffer();
		Object[] youResult = import4You.validateOneDatas4You(OneData);
		if (youResult != null) {
			if (!(Boolean) youResult[0]) {
				result[0] = false;
				sb.append(youResult[1].toString());
				result[1] = sb.toString();
				return result;
			}
		}
		for (int i = 0; i < OneData.length; i++) {
			ColumnConstraint cC = validateMap.get(i);
			Object[] oneResult = validateOneValue(OneData[i], cC);
			OneData[i] = oneResult[2].toString();
			if (!(Boolean) oneResult[0]) {
				result[0] = false;
				sb.append(oneResult[1].toString()).append("&nbsp");
			}
		}
		result[1] = sb.toString();
		return result;
	}

	/**
	 * 验证单个字段
	 */
	private Object[] validateOneValue(String value, ColumnConstraint cC) {
		Object[] result = new Object[3];
		result[0] = true;
		String headString = cC.getColNum() + 1 + "列-" + cC.getColName() + ": ";
		if (!"".equals(cC.getValue())) {
			if ("".equals(value)) {
				value = cC.getValue();
			}
		}
		value = StringUtil.trim(value);
		result[2] = value;
		if ("".equals(value)) {
			if (cC.getIsNull() == 1) {
				result[0] = false;
				result[1] = headString + "不能为空";
				return result;
			}
		} else {
			// 验证重复
			if (cC.getIsRepeat() == 1) {
				if (vRepeatSetMap.containsKey(cC.getColNum())) {
					Set<String> vRepeatSet = vRepeatSetMap.get(cC.getColNum());
					if (vRepeatSet.contains(value)) {
						result[0] = false;
						result[1] = headString + "与其他重复";
						return result;
					} else {
						vRepeatSet.add(value);
					}
				} else {
					Set<String> vRepeatSet = new HashSet<String>();
					vRepeatSet.add(value);
					vRepeatSetMap.put(cC.getColNum(), vRepeatSet);
				}
			}

			// 正则
			String RE = cC.getRegularExpression();
			if (!"".equals(RE)) {
				ReUtil reUtil = new ReUtil();
				if (!reUtil.checkRE(RE, value)) {
					result[0] = false;
					result[1] = headString + cC.getMsg();
					reUtil = null;
					return result;
				}
			}

			// 自定义1
			if (cC.getYou1() != null) {
				Object[] you1Result = import4You.validateOneValue4You1(value, cC.getYou1());
				if (you1Result != null) {
					if (!(Boolean) you1Result[0]) {
						result[0] = false;
						result[1] = you1Result[1];
						return result;
					}
				}
			}
			// 自定义2
			if (cC.getYou2() != null) {
				Object[] you2Result = import4You.validateOneValue4You2(value, cC.getYou2());
				if (you2Result != null) {
					if (!(Boolean) you2Result[0]) {
						result[0] = false;
						result[1] = you2Result[1];
						return result;
					}
				}
			}
			// 自定义3
			if (cC.getYou3() != null) {
				Object[] you3Result = import4You.validateOneValue4You3(value, cC.getYou3());
				if (you3Result != null) {
					if (!(Boolean) you3Result[0]) {
						result[0] = false;
						result[1] = you3Result[1];
						return result;
					}
				}
			}
		}

		if ((Boolean) result[0]) {
			if (cC.getIsEncrypt() == 0) {
				value = EncryptUtil.encrypt(value);
				result[2] = value;
			}
		}
		return result;
	}

	/**
	 * 入库
	 */
	@SuppressWarnings("unchecked")
	private <T> Object[] ImportData(List<String[]> dataList, Map<String, List<ColumnConstraint>> constraintMap) {
		Object[] result = new Object[2];
		result[0] = true;
		Date startDate = new Date();
		Integer size = dataList.size();
		StringBuffer sb = new StringBuffer();
		Integer sum = 0;
		Set<String> keySet = constraintMap.keySet();
		for (int i = 1; i < size; i++) {
			String[] dataStrings = dataList.get(i);
			for (Iterator<String> iter = keySet.iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				Class<T> c = null;
				T data = null;
				try {
					c = (Class<T>) Class.forName(key);
					data = c.newInstance();
				} catch (Exception e) {
					log.error("入库时创建实体类出错。" + e.getStackTrace());
					break;
				}
				// 1.2
				if (import4You != null && import4You.getImportDatas4YouMap() != null) {
					Map<String, List<Object[]>> importDatas4YouMap = import4You.getImportDatas4YouMap();
					if (importDatas4YouMap.containsKey(key)) {
						List<Object[]> importDataList = importDatas4YouMap.get(key);
						for (int j = 0; j < importDataList.size(); j++) {
							Object[] importData = importDataList.get(j);
							String pojoName = importData[0].toString();
							pojoName = pojoName.replaceFirst(pojoName.substring(0, 1), pojoName.substring(0, 1).toUpperCase());
							String methodName = "set" + pojoName;
							Method method = null;
							try {
								method = c.getMethod(methodName, importData[1].getClass());
								method.invoke(data, importData[1]);
							} catch (Exception e) {
								log.error("自定义入库字段出错。" + e.getStackTrace());
								break;
							}
						}
					}
				}

				List<ColumnConstraint> cCList = (List<ColumnConstraint>) constraintMap.get(key);
				for (int j = 0; j < dataStrings.length; j++) {
					ColumnConstraint cC = cCList.get(j);
					// 1.1
					if (cC.getIsImport() == 0) {
						Integer colNum = cC.getColNum();
						String pojoName = cC.getPojoName();
						String value = dataStrings[colNum];
						pojoName = pojoName.replaceFirst(pojoName.substring(0, 1), pojoName.substring(0, 1).toUpperCase());
						String methodName = "set" + pojoName;
						try {
							// TODO 现在只实现String
							Method method = c.getMethod(methodName, String.class);
							method.invoke(data, value);
						} catch (Exception e) {
							log.error("导入文件中数据入库字段出错。" + e.getStackTrace());
							break;
						}
						// 1.1
					}
				}
				try {
					baseDao.save(data);
					sum++;
				} catch (Exception e) {
					result[0] = false;
					log.error("数据入库时出错。" + e.getStackTrace());
					break;
				}
			}
		}
		// 1.2
		if ((Boolean) result[0]) {
			Date endDate = new Date();
			sb.append("导入成功。共").append(size - 1).append("条;成功").append(sum).append("条;耗时:")
					.append(DateUtil.timeDifference4mmss(startDate, endDate));
		} else {
			sb.append("导入数据库出错。");
		}
		result[1] = sb.toString();
		return result;
	}

	public Import4You getImport4You() {
		return import4You;
	}

	public void setImport4You(Import4You import4You) {
		this.import4You = import4You;
	}

}
