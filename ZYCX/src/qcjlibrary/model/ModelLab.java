package qcjlibrary.model;

import java.util.List;

import qcjlibrary.model.ModelAddNowCase.Result;
import qcjlibrary.model.base.Model;

public class ModelLab extends Model{
	/**
	 * 实验室检查
	 */
	private static final long serialVersionUID = 3218820703307459859L;
	private String lab_exam_hospital;
	private String lab_exam_program;
	private String lab_exam_time;
	private String list_name;
	private List<Result> lab_exam_result;
	public String getLab_exam_hospital() {
		return lab_exam_hospital;
	}
	public void setLab_exam_hospital(String lab_exam_hospital) {
		this.lab_exam_hospital = lab_exam_hospital;
	}
	public String getLab_exam_program() {
		return lab_exam_program;
	}
	public void setLab_exam_program(String lab_exam_program) {
		this.lab_exam_program = lab_exam_program;
	}
	public String getLab_exam_time() {
		return lab_exam_time;
	}
	public void setLab_exam_time(String lab_exam_time) {
		this.lab_exam_time = lab_exam_time;
	}
	public List<Result> getLab_exam_result() {
		return lab_exam_result;
	}
	public void setLab_exam_result(List<Result> lab_exam_result) {
		this.lab_exam_result = lab_exam_result;
	}
	public String getList_name(){
		return list_name;
	}
	public void setList_name(String list_name){
		this.list_name = list_name;
	}
}
