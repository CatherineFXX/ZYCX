package qcjlibrary.model;

import qcjlibrary.model.base.Model;

public class ModelAlertData extends Model{

	/**
	 * 用药提醒数据实体类
	 */
	private static final long serialVersionUID = 7231591816696089340L;
	
	private int id;
	private String userName;//用药者名称
	private String medicineName;//药品名称
	private String timeList;//重复时间列表
	private String startTime;//开始时间
	private String repeatDaily;//重复频率
	private String repeatCount;//重复次数
	private boolean isExit;//是否已经存在
	private boolean isOpen;//是否开启
	
	
	public String getRepeatCount() {
		return repeatCount;
	}
	public void setRepeatCount(String repeatCount) {
		this.repeatCount = repeatCount;
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	public boolean isExit() {
		return isExit;
	}
	public void setExit(boolean isExit) {
		this.isExit = isExit;
	}
	public String getRepeatDaily() {
		return repeatDaily;
	}
	public void setRepeatDaily(String repeatDaily) {
		this.repeatDaily = repeatDaily;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMedicineName() {
		return medicineName;
	}
	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}
	
	public String getTimeList() {
		return timeList;
	}
	public void setTimeList(String timeList) {
		this.timeList = timeList;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	
}
