package qcjlibrary.activity;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.model.ModelCaseRecord;
import qcjlibrary.model.ModelCaseRecordResult;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：上午9:41:59 类描述：这个类是实现
 *
 */

public class CaseHistoryDetailActivity extends BaseActivity {

	private TextView tv_date;
	private TextView tv_detail_title;
	private TextView tv_content;
	private ModelCaseRecord mRecord;

	@Override
	public String setCenterTitle() {
		return "记录详情";
	}

	@Override
	public void initIntent() {
		mRecord = (ModelCaseRecord) getDataFromIntent(getIntent(), null);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_case_histroy_detail;
	}

	@Override
	public void initView() {
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_detail_title = (TextView) findViewById(R.id.tv_detail_title);
		tv_content = (TextView) findViewById(R.id.tv_content);

	}

	@Override
	public void initData() {
		sendRequest(mApp.getMedRecordImpl().resultInfo(mRecord),
				ModelCaseRecordResult.class, REQUEST_GET);
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		if (object instanceof ModelCaseRecordResult) {
			ModelCaseRecordResult result = (ModelCaseRecordResult) object;
			addDataToView(result);
		} else {
			judgeTheMsg(object);
		}
		return object;
	}

	/**
	 * 添加数据到界面
	 * 
	 * @param result
	 */
	private void addDataToView(ModelCaseRecordResult result) {
		if (result != null) {
			tv_date.setText(result.getTime());
			tv_detail_title.setText(result.getTitle());
			String content = result.getContent();
			if (content != null && content.contains("/n")) {
				content = content.replaceAll("/n", "\n");
			}
			tv_content.setText(content);
		}
	}

	@Override
	public void initListener() {

	}

	@Override
	public void onClick(View v) {

	}

}
