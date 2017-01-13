package com.myhope.util.datafile.xml.importxml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ExplainXml {
	private static String FILE_PATH = "dataImportExport.xml";

	/**
	 * 获得方便于校验的约束Map
	 */
	public Map<Integer, ColumnConstraint> makeValidateMap(Map<String, List<ColumnConstraint>> constraintMap) {
		Map<Integer, ColumnConstraint> validateMap = new HashMap<Integer, ColumnConstraint>();
		Set<String> keySet = constraintMap.keySet();
		for (Iterator<String> iter = keySet.iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			List<ColumnConstraint> cCList = (List<ColumnConstraint>) constraintMap.get(key);
			for (int i = 0; i < cCList.size(); i++) {
				ColumnConstraint cC = cCList.get(i);
				validateMap.put(cC.getColNum(), cC);
			}
		}
		return validateMap;
	}

	/**
	 * 解析xml的document对象，获得相应的约束Map
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, List<ColumnConstraint>> makeXml2ConstraintMap(String importElement) {
		Document document = getXmlDocument();
		Map<String, List<ColumnConstraint>> constraintMap = new HashMap<String, List<ColumnConstraint>>();
		List childNodes = document.selectNodes("/datas_forImport/" + importElement + "/pojo");
		for (Object obj : childNodes) {
			Element node = (Element) obj;
			String mapKeyString = node.attributeValue("class").toString();

			List<ColumnConstraint> cClList = new ArrayList<ColumnConstraint>();
			List columnList = node.selectNodes("column");
			for (Object columnObj : columnList) {
				Element column = (Element) columnObj;
				ColumnConstraint cC = new ColumnConstraint();
				// TODO 此处现暂时为非动态 时间问题 保留后续修改
				cC.setColNum(Integer.valueOf(column.attributeValue("colNum")));
				cC.setColName(column.attributeValue("colName"));
				cC.setIsEncrypt(Integer.valueOf(column.attributeValue("isEncrypt")));
				cC.setIsNull(Integer.valueOf(column.attributeValue("isNull")));
				cC.setIsRepeat(Integer.valueOf(column.attributeValue("isRepeat")));
				// 1.1
				cC.setIsImport(Integer.valueOf(column.attributeValue("isImport")));
				cC.setMsg(column.attributeValue("msg"));
				cC.setPojoName(column.attributeValue("pojoName"));
				cC.setRegularExpression(column.attributeValue("regularExpression"));
				cC.setValue(column.attributeValue("value"));
				String you1 = column.attributeValue("you1");
				String you2 = column.attributeValue("you2");
				String you3 = column.attributeValue("you3");
				if (you1 != null) {
					cC.setYou1(you1);
				}
				if (you2 != null) {
					cC.setYou2(you2);
				}
				if (you3 != null) {
					cC.setYou3(you3);
				}
				cClList.add(cC);
			}
			constraintMap.put(mapKeyString, cClList);
		}
		return constraintMap;
	}

	/**
	 * 解析Xml获得Document
	 */
	private Document getXmlDocument() {
		Document document = null;
		SAXReader saxReader = new SAXReader();
		InputStream iReader = Thread.currentThread().getContextClassLoader().getResourceAsStream(FILE_PATH);
		try {
			document = saxReader.read(iReader);
		} catch (DocumentException e) {
			e.getStackTrace();
		} finally {
			try {
				iReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			saxReader = null;
		}
		return document;
	}
}
