package qcjlibrary.model;

import qcjlibrary.model.base.Model;

/**
 * author：qiuchunjia time：上午10:20:39 类描述：这个类是实现
 *
 */

public class ModelFood extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String type_name;
	private String sort;
	private String count;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;

	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
