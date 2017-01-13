package com.myhope.util.datafile.writefile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.csvreader.CsvWriter;

public class WriteCsvFile {

	/**
	 * @Title: 导出CSV文件
	 * @param: response
	 * @param: data 数据集
	 * @param: fileName 文件名
	 * @author: YangMing
	 * @date: 2015-9-14
	 */
	public void writeCsvFile(HttpServletResponse response, List<String[]> data, String fileName) {
		OutputStream os = null;
		CsvWriter wr = null;
		try {
			os = response.getOutputStream();
			response.reset();
			response.setCharacterEncoding("UTF-8");// 设置相应内容的编码格式
			fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "GBK") + ".csv");
			response.setContentType("application/msexcel");// 定义输出类型

			wr = new CsvWriter(os, ',', Charset.forName("GBK"));
			for (int i = 0; i < data.size(); i++) {
				wr.writeRecord(data.get(i));
			}
		} catch (IOException e) {
			e.getStackTrace();
		} finally {
			wr.close();
			try {
				os.close();
			} catch (IOException e) {
				e.getStackTrace();
			}
		}
	}
}
