package qcjlibrary.model;

import java.util.List;

import qcjlibrary.model.base.Model;

/**
 * author：qiuchunjia time：下午6:03:18 类描述：这个类是实现
 *
 */

public class ModelAddNowCase extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// diagnosis_stime 诊断起始时间
	// diagnosis_etime 诊断截止时间
	// diagnosis_hospital 检查医院
	// diagnosis_way 诊断方式
	// lab_exam_program 实验室检查项目
	// lab_exam_time 实验室检查时间
	// lab_exam_hospital 实验室检查医院
	// image_exam_program 影像学检查项目
	// image_exam_time 影像学检查时间
	// image_exam_hospital 影像学检查医院
	// 图片文件上传
	// diagnosis 诊断图片
	// lab_exam 实验室检查图片
	// image_exam 影像学检查图片
	private String diagnosis_stime;
	private String diagnosis_etime;
	private String diagnosis_hospital;
	private String diagnosis_way;
	private String lab_exam_program;
	private String lab_exam_time;
	private String lab_exam_hospital;
	private String image_exam_program;
	private String image_exam_time;
	private String image_exam_hospital;
	private List<String> diagnosisList;
	private List<String> lab_examList;
	private List<String> image_examList;
	
	private List<ModelCaseResult> result;

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

	public String getLab_exam_hospital() {
		return lab_exam_hospital;
	}

	public void setLab_exam_hospital(String lab_exam_hospital) {
		this.lab_exam_hospital = lab_exam_hospital;
	}

	public String getImage_exam_program() {
		return image_exam_program;
	}

	public void setImage_exam_program(String image_exam_program) {
		this.image_exam_program = image_exam_program;
	}

	public List<String> getDiagnosisList() {
		return diagnosisList;
	}

	public void setDiagnosisList(List<String> diagnosisList) {
		this.diagnosisList = diagnosisList;
	}

	public List<String> getLab_examList() {
		return lab_examList;
	}

	public void setLab_examList(List<String> lab_examList) {
		this.lab_examList = lab_examList;
	}

	public List<String> getImage_examList() {
		return image_examList;
	}

	public void setImage_examList(List<String> image_examList) {
		this.image_examList = image_examList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getImage_exam_time() {
		return image_exam_time;
	}

	public void setImage_exam_time(String image_exam_time) {
		this.image_exam_time = image_exam_time;
	}

	public String getImage_exam_hospital() {
		return image_exam_hospital;
	}

	public void setImage_exam_hospital(String image_exam_hospital) {
		this.image_exam_hospital = image_exam_hospital;
	}

	public List<ModelCaseResult> getResult() {
		return result;
	}

	public void setResult(List<ModelCaseResult> result) {
		this.result = result;
	}

	
}
