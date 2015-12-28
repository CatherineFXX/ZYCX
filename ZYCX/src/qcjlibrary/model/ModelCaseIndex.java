package qcjlibrary.model;

import qcjlibrary.model.base.Model;

/**
 * author：qiuchunjia time：下午4:15:36 类描述：这个类是实现
 *
 */

public class ModelCaseIndex extends Model {

	/**
	 * "realname": "方三电费", "sex": "男", "age": "12", "ctime": "12月22日 17:58",
	 * "utime": "12月23日 18:05"
	 */
	private static final long serialVersionUID = 1L;
	private String realname;
	private String sex;
	private String age;
	private String ctime;
	private String utime;

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getUtime() {
		return utime;
	}

	public void setUtime(String utime) {
		this.utime = utime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
