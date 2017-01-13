package com.myhope.util.base;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 邮件工具类
 * 
 * @author YangMing
 * 
 */
public class MailUtil extends Thread {

	List<String> tos = new ArrayList<String>();
	List<String> contents = new ArrayList<String>();

	public MailUtil() {
	}

	@Override
	public void run() {
		for (int i = 0; i < tos.size(); i++) {
			sendMail(tos.get(i), contents.get(i));
		}
	}

	public void sendMail(String to, String content) {
		// 接收方不为空
		if (!"".equals(to)) {
			Properties prop = new Properties();
			prop.setProperty("mail.host", "smtp.sina.com");
			prop.setProperty("mail.transport.protocol", "smtp");
			prop.setProperty("mail.smtp.auth", "true");
			// 创建session
			Session session = Session.getInstance(prop);
			// 通过session得到transport对象
			Transport ts = null;
			try {
				// 通过session得到transport对象
				ts = session.getTransport();
				// 使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
				ts.connect("smtp.sina.com", "myhope_0@sina.com", "myhope");
				// 创建邮件
				Message message = createMail(session, to, content);
				// 发送邮件
				ts.sendMessage(message, message.getAllRecipients());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					ts.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public MimeMessage createMail(Session session, String to, String content) throws Exception {
		// 创建邮件对象
		MimeMessage message = new MimeMessage(session);
		// 指明邮件的发件人
		String nick = "";
		try {
			nick = javax.mail.internet.MimeUtility.encodeText("MyHope系统");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		message.setFrom(new InternetAddress(nick + " <myhope_0@sina.com>"));
		// 指明邮件的收件人
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		// 邮件的标题
		message.setSubject("MyHope系统邮件");
		// 邮件的文本内容
		message.setContent(
				"<h2>MyHope提醒您：</h2><h3>"
						+ content
						+ "</h3><br><br><h3>MyHope预祝您工作愉快。</h3><br><a href=\"http://192.168.10.102:9090/\">内网登录请点我</a><br><a href=\"http://myhope.wicp.net\">外网登录请点我</a>",
				"text/html;charset=UTF-8");
		// 返回创建好的邮件对象
		return message;
	}

	public void add(String to, String content) {
		if (to != null && content != null && !"".equals(to) && !"".equals(content)) {
			tos.add(to);
			contents.add(content);
		}
	}
}
