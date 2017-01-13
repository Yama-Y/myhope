package com.myhope.model.workschedule;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "WS_T_SCHEDULE_DETAIL", schema = "")
@DynamicInsert(true)
@DynamicUpdate(true)
public class WsTScheduleDetail implements java.io.Serializable {

	private String id;
	private String begintime;
	private String endtime;
	private String time;
	private String timeType;// A:本日;B:次日;
	private String isCard;// A:打卡；B:不打卡；
	private String cardType;// A:上班；B:下班；C:中间休息开始；D:中间休息结束
	private WsTSchedule schedule = new WsTSchedule();

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 36)
	public String getId() {
		if (!StringUtils.isBlank(this.id)) {
			return this.id;
		}
		return UUID.randomUUID().toString();
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "BEGINTIME", length = 8)
	public String getBegintime() {
		return this.begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	@Column(name = "ENDTIME", length = 8)
	public String getEndtime() {
		return this.endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	@Column(name = "TIME", length = 8)
	public String getTime() {
		return this.time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Column(name = "TIME_TYPE", length = 1)
	public String getTimeType() {
		return this.timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	@Column(name = "ISCARD", length = 1)
	public String getIsCard() {
		return this.isCard;
	}

	public void setIsCard(String isCard) {
		this.isCard = isCard;
	}

	@Column(name = "CARD_TYPE", length = 1)
	public String getCardType() {
		return this.cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	@ManyToOne()
	@JoinColumn(name = "SCHEDULE_ID", nullable = false)
	public WsTSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(WsTSchedule schedule) {
		this.schedule = schedule;
	}

}