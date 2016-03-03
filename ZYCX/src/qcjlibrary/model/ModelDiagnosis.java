package qcjlibrary.model;

import java.util.List;

import qcjlibrary.model.ModelAddNowCase.Result;
import qcjlibrary.model.base.Model;

public class ModelDiagnosis extends Model{
	/**
	 * 诊断过程，当diagnosis_result为空时，不显示数据
	 */
	private static final long serialVersionUID = 1235154590238698589L;
	private String diagnosis_hospital;
	private String diagnosis_way;
	private String diagnosis_stime;
	private String diagnosis_etime;
	private String list_name;
	private List<Result> diagnosis_result;
	public String getDiagnosis_hospital() {
		return diagnosis_hospital;
	}
	public void setDiagnosis_hospital(String diagnosis_hospital) {
		this.diagnosis_hospital = diagnosis_hospital;
	}
	public String getDiagnosis_way() {
		return diagnosis_way;
	}
	public void setDiagnosis_way(String diagnosis_way) {
		this.diagnosis_way = diagnosis_way;
	}
	public String getDiagnosis_stime() {
		return diagnosis_stime;
	}
	public void setDiagnosis_stime(String diagnosis_stime) {
		this.diagnosis_stime = diagnosis_stime;
	}
	public String getDiagnosis_etime() {
		return diagnosis_etime;
	}
	public void setDiagnosis_etime(String diagnosis_etime) {
		this.diagnosis_etime = diagnosis_etime;
	}
	public List<Result> getDiagnosis_result() {
		return diagnosis_result;
	}
	public void setDiagnosis_result(List<Result> diagnosis_result) {
		this.diagnosis_result = diagnosis_result;
	}
	public String getList_name(){
		return list_name;
	}
	public void setList_name(String list_name){
		this.list_name = list_name;
	}
}
