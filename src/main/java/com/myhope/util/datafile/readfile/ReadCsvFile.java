package com.myhope.util.datafile.readfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.csvreader.CsvReader;

public class ReadCsvFile {
	private static Logger log = Logger.getLogger(ReadCsvFile.class);

	/**
	 * 解析FILE获得CsvList
	 */
	public List<String[]> getCsvList(File file) {
		FileInputStream fis = null;
		List<String[]> csvList = new ArrayList<String[]>();
		try {
			fis = new FileInputStream(file);
			CsvReader reader = new CsvReader(fis, ',', Charset.forName("GBK"));
			// 跳过表头
			// reader.readHeaders();
			while (reader.readRecord()) {
				csvList.add(reader.getValues());
			}
			reader.close();
		} catch (IOException e) {
			log.error("解析FILE获得CsvList出错。" + e.getStackTrace());
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				log.error("关闭文件读取流出错。" + e.getStackTrace());
			}
		}
		return csvList;
	}
}
