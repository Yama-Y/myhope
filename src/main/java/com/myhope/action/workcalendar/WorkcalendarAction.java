package com.myhope.action.workcalendar;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Autowired;

import com.myhope.action.BaseAction;
import com.myhope.model.base.SessionInfo;
import com.myhope.model.base.TOrganization;
import com.myhope.model.base.TUser;
import com.myhope.model.easyui.Json;
import com.myhope.model.workcalendar.WcTWorkcalendar;
import com.myhope.model.workcalendar.WcWorkXls;
import com.myhope.service.workcalendar.WorkcalendarServiceI;
import com.myhope.util.base.BeanUtils;
import com.myhope.util.base.ConfigUtil;
import com.myhope.util.base.DateUtil;
import com.myhope.util.base.HqlFilter;

/**
 * 工作日历
 * 
 * action访问地址是/workcalendar.myhope
 * 
 * @author YangMing
 * 
 */
@Namespace("/")
@Action
public class WorkcalendarAction extends BaseAction<WcTWorkcalendar> {

	/**
	 * 注入业务逻辑，使当前action调用service.xxx的时候，直接是调用基础业务逻辑
	 * 
	 * 如果想调用自己特有的服务方法时，请使用((TServiceI) service).methodName()这种形式强转类型调用
	 * 
	 * @param service
	 */
	@Autowired
	public void setService(WorkcalendarServiceI service) {
		this.service = service;
	}

	/**
	 * 添加记录
	 */
	public void addEvent() {
		SessionInfo sessionInfo = (SessionInfo) getSession().getAttribute(ConfigUtil.getSessionInfoName());
		data.setUser(sessionInfo.getUser());
		Json json = new Json();
		service.save(data);
		json.setSuccess(true);
		json.setObj(data);
		writeJson(json);
	}

	@Override
	public void update() {
		Json json = new Json();
		String reflectId = null;
		try {
			if (data != null) {
				reflectId = (String) FieldUtils.readField(data, "id", true);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if (!StringUtils.isBlank(reflectId)) {
			WcTWorkcalendar t = service.getById(reflectId);
			BeanUtils.copyNotNullProperties(data, t, new String[] { "createdatetime" });
			service.update(t);
			json.setSuccess(true);
			json.setMsg("更新成功！");
			json.setObj(t);
		}
		writeJson(json);
	}

	public void getWorkcalendars() {
		SessionInfo sessionInfo = (SessionInfo) getSession().getAttribute(ConfigUtil.getSessionInfoName());
		HqlFilter hqlFilter = new HqlFilter(getRequest());
		if (id == null) {
			id = sessionInfo.getUser().getId();
		}
		hqlFilter.addFilter("QUERY_user#id_S_EQ", id);
		List<WcTWorkcalendar> workcalendars = ((WorkcalendarServiceI) service).findWorkcalendarByFilter(hqlFilter);
		writeJson(workcalendars);
	}

	/**
	 * 导出周报
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void doNotNeedSecurity_exportExcel() throws FileNotFoundException, IOException {

		String filename = getRealPath() + "/templet/work.xls";
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(filename));
		HSSFSheet sheet = workbook.getSheetAt(0);
		writeSheet(sheet);
		Date nowDate = DateUtil.stringToDate(id, "yyyy-MM-dd");
		Date endDate = DateUtil.getWeekBegin(nowDate, 4);
		SessionInfo sessionInfo = (SessionInfo) getSession().getAttribute(ConfigUtil.getSessionInfoName());
		// 存盘
		try {
			String fname = "周报-" + sessionInfo.getUser().getName() + "-" + DateUtil.dateToString(endDate, "yyyyMMdd") + ".xls";
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");// 设置相应内容的编码格式
			fname = java.net.URLEncoder.encode(fname, "UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + fname);
			OutputStream os = response.getOutputStream();
			workbook.write(os);
			// 结束关闭
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void writeSheet(HSSFSheet sheet) throws IOException {
		HqlFilter hqlFilter = new HqlFilter(getRequest());
		SessionInfo sessionInfo = (SessionInfo) getSession().getAttribute(ConfigUtil.getSessionInfoName());
		hqlFilter.addFilter("QUERY_user#id_S_EQ", sessionInfo.getUser().getId());
		String[] times = ids.split(",");
		hqlFilter.addFilter("QUERY_t#start_D_GE", times[0] + " 00:00:00");
		hqlFilter.addFilter("QUERY_t#end_D_LE", times[1] + " 23:59:59");
		List<WcTWorkcalendar> workcalendars = ((WorkcalendarServiceI) service).findWorkcalendarByFilter(hqlFilter);
		Collections.sort(workcalendars, new Comparator<WcTWorkcalendar>() {// 排序
			@Override
			public int compare(WcTWorkcalendar w1, WcTWorkcalendar w2) {
				return w1.getStart().compareTo(w2.getStart());
			}
		});
		HSSFRow row;
		HSSFCell cell;

		WcWorkXls workXls = new WcWorkXls();

		TUser user = sessionInfo.getUser();
		Set<TOrganization> organizations = user.getOrganizations();
		String deptName = "";
		for (TOrganization organization : organizations) {
			deptName = deptName + organization.getName() + "、";
		}
		workXls.setDeptName(deptName.substring(0, deptName.length() - 1));
		workXls.setWriteName(user.getName());
		workXls.setDate(id);

		row = sheet.getRow(workXls.getHeaderRow());
		cell = row.getCell(workXls.getHeaderCell());
		cell.setCellValue(workXls.getHeaderValue());

		Date nowDate = DateUtil.stringToDate(id, "yyyy-MM-dd");
		Date beginDate = DateUtil.getWeekBegin(nowDate, 7);

		Integer planRow = 5;
		Integer planCell1 = 1;
		Integer planCell2 = 11;
		// 工作记录行数
		Integer workRow = 12;
		// 工作记录尺寸
		Integer rowSize = 3;
		Integer workCell1 = 3;
		Integer workCell2 = 11;
		// 定义每天记录的现有行位置
		Map<Integer, Integer> workRowMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < 5; i++) {
			Date workDate = DateUtil.getWeekBegin(nowDate, i);
			row = sheet.getRow(workRow + i * rowSize);
			cell = row.getCell(2);
			cell.setCellValue(DateUtil.dateToString(workDate, "MM.dd"));
			workRowMap.put(i, workRow + i * rowSize);
		}

		// 项目饼图数据集初始化
		Map<String, Long> projectMap = new HashMap<String, Long>();

		for (WcTWorkcalendar tWorkcalendar : workcalendars) {
			if (tWorkcalendar.getContent() == null || "".equals(tWorkcalendar.getContent())) {
				continue;
			}
			// 写记录
			if ("A".equals(tWorkcalendar.getType())) {
				// 记录
				for (int i = 0; i < 5; i++) {
					Date workDate = DateUtil.getWeekBegin(nowDate, i);
					if (DateUtil.dateToString(workDate, "yyyy-MM-dd").equals(DateUtil.dateToString(tWorkcalendar.getStart(), "yyyy-MM-dd"))) {
						if (workRowMap.get(i) < (workRow + (i + 1) * rowSize)) {
							row = sheet.getRow(workRowMap.get(i));
							cell = row.getCell(workCell1);
							cell.setCellValue((workRowMap.get(i) - (workRow + i * rowSize) + 1) + "、" + tWorkcalendar.getCellVal());
							cell = row.getCell(workCell2);
							cell.setCellValue(tWorkcalendar.getProject().getName());
							workRowMap.put(i, workRowMap.get(i) + 1);
						}
						// 记录比例（按照项目分配）
						String projectName = tWorkcalendar.getProject().getName() + "@" + tWorkcalendar.getProject().getId();
						Long timeDifference = tWorkcalendar.getEnd().getTime() - tWorkcalendar.getStart().getTime();
						if (projectMap.containsKey(projectName)) {
							Long oldtimeD = projectMap.get(projectName);
							projectMap.remove(projectName);
							projectMap.put(projectName, oldtimeD + timeDifference);
						} else {
							projectMap.put(projectName, timeDifference);
						}
					}
				}
			}
			// 写计划
			else if ("B".equals(tWorkcalendar.getType())) {
				if (DateUtil.dateToString(beginDate, "yyyy-MM-dd").equals(DateUtil.dateToString(tWorkcalendar.getStart(), "yyyy-MM-dd"))) {
					if (planRow <= 9) {
						row = sheet.getRow(planRow);
						cell = row.getCell(planCell1);
						cell.setCellValue(tWorkcalendar.getCellVal());
						cell = row.getCell(planCell2);
						cell.setCellValue(tWorkcalendar.getProject().getName());
						planRow++;
					}
				}
			}
		}
		JFreeChart pieChart = getPieChart(projectMap);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ChartUtilities.writeChartAsJPEG(byteArrayOutputStream, pieChart, 520, 476);

		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		HSSFClientAnchor anchor = new HSSFClientAnchor(200, 200, 100, 165, (short) 14, 3, (short) 20, 17);
		anchor.setAnchorType(3);

		patriarch
				.createPicture(anchor, sheet.getWorkbook().addPicture(byteArrayOutputStream.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
		byteArrayOutputStream.close();

	}

	private JFreeChart getPieChart(Map<String, Long> projectMap) throws IOException {
		DefaultPieDataset data = new DefaultPieDataset();
		// 数据初始化
		Set<String> keySet = projectMap.keySet();
		for (String string : keySet) {
			data.setValue(string.split("@")[0], ((double) projectMap.get(string)) / (1000 * 60 * 60));
		}

		PiePlot plot = new PiePlot(data);// 生成一个饼图
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}  ({1}小时-{2})", NumberFormat.getNumberInstance(),
				new DecimalFormat("0.00%")));
		plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}  ({1}小时-{2})"));
		JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, true);// 生成一个图
		chart.setBackgroundPaint(java.awt.Color.white);// 可选，设置图片背景色

		SessionInfo sessionInfo = (SessionInfo) getSession().getAttribute(ConfigUtil.getSessionInfoName());
		chart.setTitle("本周工作时间分配-" + sessionInfo.getUser().getName());// 可选，设置图片标题

		return chart;
	}

	/**
	 * 工作项目分布报表
	 */
	public void workChart_project() {
		List<WcTWorkcalendar> workcalendars = getWorkcalendarDatas();
		// 项目饼图数据集初始化
		Map<String, Long> projectMap = new HashMap<String, Long>();
		for (WcTWorkcalendar tWorkcalendar : workcalendars) {
			// 记录比例（按照项目分配）
			String projectName = tWorkcalendar.getProject().getName() + "@" + tWorkcalendar.getProject().getId();
			Long timeDifference = tWorkcalendar.getEnd().getTime() - tWorkcalendar.getStart().getTime();
			if (projectMap.containsKey(projectName)) {
				Long oldtimeD = projectMap.get(projectName);
				projectMap.remove(projectName);
				projectMap.put(projectName, oldtimeD + timeDifference);
			} else {
				projectMap.put(projectName, timeDifference);
			}
		}

		List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();

		for (String key : projectMap.keySet()) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("name", key.split("@")[0]);
			m.put("projectId", key.split("@")[1]);
			m.put("y", ((double) projectMap.get(key)) / (1000 * 60 * 60));
			m.put("sliced", false);
			m.put("selected", false);
			l.add(m);
		}
		writeJson(l);
	}


	/**
	 * 工作类别分布报表
	 */
	public void workChart_type() {
		List<WcTWorkcalendar> workcalendars = getWorkcalendarDatas();
		// 类别饼图数据集初始化
		Map<String, Long> typeMap = new HashMap<String, Long>();
		for (WcTWorkcalendar tWorkcalendar : workcalendars) {
			// 记录比例（按照项目分配）
			String type = tWorkcalendar.getTitle();
			Long timeDifference = tWorkcalendar.getEnd().getTime() - tWorkcalendar.getStart().getTime();
			if (typeMap.containsKey(type)) {
				Long oldtimeD = typeMap.get(type);
				typeMap.remove(type);
				typeMap.put(type, oldtimeD + timeDifference);
			} else {
				typeMap.put(type, timeDifference);
			}
		}

		List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();

		for (String key : typeMap.keySet()) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("name", key);
			m.put("y", ((double) typeMap.get(key)) / (1000 * 60 * 60));
			m.put("sliced", false);
			m.put("selected", false);
			l.add(m);
		}
		writeJson(l);
	}

	/**
	 * 工作详细
	 */
	public void doNotNeedSecurity_workDetail() {
		List<WcTWorkcalendar> workcalendars = getWorkcalendarDatas();
		writeJson(workcalendars);
	}
	
	/**
	 * 根据request获得的hqlFilter查询Workcalendar数据集
	 */
	private List<WcTWorkcalendar> getWorkcalendarDatas() {
		HqlFilter hqlFilter = new HqlFilter(getRequest());
		if (data != null && data.getUser() != null) {
			hqlFilter.addFilter("QUERY_user#id_S_EQ", data.getUser().getId());
		} else {
			SessionInfo sessionInfo = (SessionInfo) getSession().getAttribute(ConfigUtil.getSessionInfoName());
			hqlFilter.addFilter("QUERY_user#id_S_EQ", sessionInfo.getUser().getId());
		}
		hqlFilter.addFilter("QUERY_t#type_S_EQ", "A");
		// hqlFilter.addFilter("QUERY_t#start_D_GE", "2015-12-4 8:51:58");
		// hqlFilter.addFilter("QUERY_t#end_D_LE", "2015-12-31 10:51:58");
		List<WcTWorkcalendar> workcalendars = ((WorkcalendarServiceI) service).findWorkcalendarByFilter(hqlFilter);

		return workcalendars;
	}
}
