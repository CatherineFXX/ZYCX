package qcjlibrary.model;

import java.util.List;

import qcjlibrary.model.ModelAddNowCase.Result;
import qcjlibrary.model.base.Model;

public class ModelImage extends Model{
	/**
	 * 影像检查
	 */
	private static final long serialVersionUID = 2481255748987462833L;
	private String image_exam_hospital;
	private String image_exam_program;
	private String image_exam_time;
	private String list_name;
	private List<Result> image_exam_result;
	public String getImage_exam_hospital() {
		return image_exam_hospital;
	}
	public void setImage_exam_hospital(String image_exam_hospital) {
		this.image_exam_hospital = image_exam_hospital;
	}
	public String getImage_exam_program() {
		return image_exam_program;
	}
	public void setImage_exam_program(String image_exam_program) {
		this.image_exam_program = image_exam_program;
	}
	public String getImage_exam_time() {
		return image_exam_time;
	}
	public void setImage_exam_time(String image_exam_time) {
		this.image_exam_time = image_exam_time;
	}
	public List<Result> getImage_exam_result() {
		return image_exam_result;
	}
	public void setImage_exam_result(List<Result> image_exam_result) {
		this.image_exam_result = image_exam_result;
	}
	public String getList_name(){
		return list_name;
	}
	public void setList_name(String list_name){
		this.list_name = list_name;
	}
}
