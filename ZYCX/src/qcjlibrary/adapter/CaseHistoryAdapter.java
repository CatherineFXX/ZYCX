package qcjlibrary.adapter;

import java.util.List;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.adapter.base.BAdapter;
import qcjlibrary.adapter.base.ViewHolder;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.model.ModelCasePresent;
import qcjlibrary.model.ModelCaseRecord;
import qcjlibrary.model.base.Model;
import qcjlibrary.response.DataAnalyze;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午5:06:10
 * 
 * 类描述：呵呵哒的意思
 * 
 *
 */

public class CaseHistoryAdapter extends BAdapter {

	public CaseHistoryAdapter(BaseActivity activity, List<Model> list) {
		super(activity, list);
	}

	public CaseHistoryAdapter(BaseFragment fragment, List<Model> list) {
		super(fragment, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_case_histroy, null);
			initView(convertView, holder);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		bindDataToView(holder, position);
		return convertView;
	}

	/**
	 * 初始化布局
	 * 
	 * @param convertView
	 * @param holder
	 */
	private void initView(View convertView, ViewHolder holder) {
		if (convertView != null && holder != null) {
			holder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
		}
	}

	private void bindDataToView(ViewHolder holder, int position) {
		if (holder != null) {
			ModelCaseRecord caseRecord = (ModelCaseRecord) mList.get(position);
			holder.tv_content.setText(caseRecord.getContent());
			holder.tv_date.setText(caseRecord.getCtime());
		}
	}

	@Override
	public void refreshNew() {
		sendRequest(mApp.getMedRecordImpl().presentHistory(null),
				ModelCaseRecord.class, REQUEST_GET, REFRESH_NEW);
	}

	@Override
	public void refreshHeader(Model item, int count) {
		ModelCaseRecord record = (ModelCaseRecord) item;
		record.setLastid(record.getId());
		sendRequest(mApp.getMedRecordImpl().presentHistory(record),
				ModelCaseRecord.class, REQUEST_GET, REFRESH_HEADER);
	}

	@Override
	public void refreshFooter(Model item, int count) {
		ModelCaseRecord record = (ModelCaseRecord) item;
		record.setMaxid(record.getId());
		sendRequest(mApp.getMedRecordImpl().presentHistory(record),
				ModelCaseRecord.class, REQUEST_GET, REFRESH_FOOTER);
	}

	@Override
	public int getTheCacheType() {
		return 0;
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		return DataAnalyze.parseData(str, class1);
	}

	@Override
	public Object getReallyList(Object object, Class type2) {

		return object;
	}

}
