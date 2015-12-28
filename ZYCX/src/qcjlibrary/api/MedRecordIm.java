package qcjlibrary.api;

import qcjlibrary.model.ModelAddCase;
import qcjlibrary.model.ModelAddHistoryCase;
import qcjlibrary.model.ModelAddNowCase;
import qcjlibrary.model.ModelCasePresent;
import qcjlibrary.model.ModelCaseRecord;

import com.loopj.android.http.RequestParams;

/**
 * author：qiuchunjia time：下午4:57:22 接口描述：这个是实现病例
 *
 */

public interface MedRecordIm {
	// 接口需要的操作参数
	public static final String MEDRECORD = "MedRecord";

	public static final String SAVE_INFO = "save_info";
	public static final String INDEX = "index";
	public static final String MY_MED_RECORD = "my_med_record";
	public static final String SAVE_HISTORY = "save_history";
	public static final String SAVE_PRESENT = "save_present";
	public static final String PRESENT_HISTORY = "present_history"; // 病历-历史记录
	public static final String RESULT_INFO = "result_info"; // 病历-历史记录详情

	// 接口需要传的值的键
	public static final String REALNAME = "realname";
	public static final String SEX = "sex";
	public static final String AGE = "age";
	public static final String MARRIAGE = "marriage";
	public static final String NATION = "nation";
	public static final String PROFESSION = "profession";
	public static final String EDUCATION = "education";
	public static final String INSFORM = "insform";
	public static final String NATIVES = "native";
	public static final String DOMICILE = "domicile";
	public static final String HEIGHT = "height";
	public static final String WEIGHT = "weight";

	public static final String MED_HISTORY = "med_history";
	public static final String ALLERGY_HISTORY = "allergy_history";
	public static final String PER_HISTORY = "per_history";
	public static final String EATING_HABIT = "eating_habit";
	public static final String SMOKE = "smoke";
	public static final String SMOKE_AGE = "smoke_age";
	public static final String SMOKE_TIME = "smoke_time";
	public static final String STOP_SMOKE = "stop_smoke";
	public static final String STOP_SMOKE_TIME = "stop_smoke_time";
	public static final String DRINK = "drink";
	public static final String DRINK_AGE = "drink_age";
	public static final String DRINK_CONSUMPTION = "drink_consumption";
	public static final String STOP_DRINK = "stop_drink";
	public static final String STOP_DRINK_TIME = "stop_drink_time";
	public static final String MENARCHE_AGE = "menarche_age";
	public static final String MENARCHE_ETIME = "menarche_etime";
	public static final String AMENORRHOEA_AGE = "amenorrhoea_age";
	public static final String CHILDS = "childs";
	public static final String FAMILY_HISTORY = "family_history";
	public static final String DIAGNOSIS_ETIME = "diagnosis_etime";
	public static final String DIAGNOSIS_HOSPITAL = "diagnosis_hospital";
	public static final String DIAGNOSIS_WAY = "diagnosis_way";
	public static final String LAB_EXAM_PROGRAM = "lab_exam_program";
	public static final String LAB_EXAM_TIME = "lab_exam_time";
	public static final String LAB_EXAM_HOSPITAL = "lab_exam_hospital";
	public static final String IMAGE_EXAM_PROGRAM = "image_exam_program";
	public static final String IMAGE_EXAM_TIME = "image_exam_time";
	public static final String IMAGE_EXAM_HOSPITAL = "image_exam_hospital";
	public static final String DIAGNOSIS = "diagnosis";
	public static final String LAB_EXAM = "lab_exam";
	public static final String IMAGE_EXAM = "image_exam";
	public static final String ID = "id";

	/**
	 * 病历-添加病历
	 * 
	 * @param addCase
	 * @return
	 */
	public RequestParams saveInfo(ModelAddCase addCase);

	/**
	 * 病历-添加既往病史
	 * 
	 * @param historyCase
	 * @return
	 */
	public RequestParams saveHistory(ModelAddHistoryCase historyCase);

	/**
	 * 病历-添加现病史
	 * 
	 * @param nowCase
	 * @return
	 */
	public RequestParams savePresent(ModelAddNowCase nowCase);

	/**
	 * 病历-病历首页
	 * 
	 * @return
	 */
	public RequestParams index();

	/**
	 * 病历-我的病历
	 * 
	 * @return
	 */
	public RequestParams myMedRecord();

	/**
	 * 病历-历史记录
	 * 
	 * @param present
	 * @return
	 */
	public RequestParams presentHistory(ModelCaseRecord record);

	/**
	 * 病历-历史记录详情
	 * 
	 * @param present
	 * @return
	 */
	public RequestParams resultInfo(ModelCaseRecord record);

}
