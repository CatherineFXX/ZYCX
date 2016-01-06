package qcjlibrary.model;

import qcjlibrary.model.base.Model;

/**
 * author：qiuchunjia time：下午4:15:36 类描述：这个类是实现
 *
 */

public class ModelCaseIndex extends Model {

	/**
	 * {"data":{"realname":"fdsaf","sex":"\u7537","age":"fdaf","ctime":"2015-12-30 14:30",
	 * "utime":"\u4eca\u592915:41",
	 * 
	 * "url":"http:\/\/qingko-img.b0.upaiyun.com\/568b73bbe029f.jpg"},"message":"ok","code":0}

	 */
	private static final long serialVersionUID = 1L;
	private String realname;
	private String sex;
	private String age;
	private String ctime;
	private String utime;
	private String url;

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	

}
