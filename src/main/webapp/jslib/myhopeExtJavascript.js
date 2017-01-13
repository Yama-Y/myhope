var myhope = myhope || {};

/**
 * 去字符串空格
 * 
 * @author YangMing
 */
myhope.trim = function(str) {
	return str.replace(/(^\s*)|(\s*$)/g, '');
};
myhope.ltrim = function(str) {
	return str.replace(/(^\s*)/g, '');
};
myhope.rtrim = function(str) {
	return str.replace(/(\s*$)/g, '');
};

/**
 * 日期格式化
 * 
 * @author YangMing
 */
//对Date的扩展，将 Date 转化为指定格式的String   
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，   
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)   
//例子：   
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423   
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18   
Date.prototype.Format = function(fmt) { //author: meizz
	var o = {
		"M+": this.getMonth() + 1,
		//月份   
		"d+": this.getDate(),
		//日   
		"h+": this.getHours(),
		//小时   
		"m+": this.getMinutes(),
		//分   
		"s+": this.getSeconds(),
		//秒   
		"q+": Math.floor((this.getMonth() + 3) / 3),
		//季度   
		"S": this.getMilliseconds() //毫秒   
	};
	if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o) if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

/**
 * 判断开始字符是否是XX
 * 
 * @author YangMing
 */
myhope.startWith = function(source, str) {
	var reg = new RegExp("^" + str);
	return reg.test(source);
};
/**
 * 判断结束字符是否是XX
 * 
 * @author YangMing
 */
myhope.endWith = function(source, str) {
	var reg = new RegExp(str + "$");
	return reg.test(source);
};

/**
 * iframe自适应高度
 * 
 * @author YangMing
 * 
 * @param iframe
 */
myhope.autoIframeHeight = function(iframe) {
	iframe.style.height = iframe.contentWindow.document.body.scrollHeight + "px";
};

/**
 * 设置iframe高度
 * 
 * @author YangMing
 * 
 * @param iframe
 */
myhope.setIframeHeight = function(iframe, height) {
	iframe.height = height;
};

/**
 * 取得url传递来的参数
 * 
 * @author YangMing
 * 
 * @param name
 */
myhope.getUrlData = function(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null){
    	return  unescape(r[2]);
    } else {
    	return null;
    }
};

/**
 * 日期时间字符串转成时间字符串
 * 
 * @author YangMing
 * 
 * @param dateStr
 */
myhope.dateStr2TimeStr = function(dateStr){
	return dateStr.substr(dateStr.length - 8, dateStr.length);
}