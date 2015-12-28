package qcjlibrary.adapter;

import java.util.List;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.adapter.base.BAdapter;
import qcjlibrary.adapter.base.ViewHolder;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.model.ModelAlertData;
import qcjlibrary.model.base.Model;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午5:06:10
 * 
 * 类描述：这个类是实现专家提问列表
 *
 */

public class UseMedicineNotifyAdapter extends BAdapter {

	
	public UseMedicineNotifyAdapter(BaseActivity activity, List<Model> list) {
		super(activity, list);
	}

	public UseMedicineNotifyAdapter(BaseFragment fragment, List<Model> list) {
		super(fragment, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_use_medicine, null);
			initView(convertView, holder);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		bindDataToView(holder, position);
		return convertView;
	}

	private void bindDataToView(ViewHolder holder, int position) {
		if (holder != null) {
			ModelAlertData mData = (ModelAlertData) mList.get(position);
			holder.tv_user_name.setText(mData.getUserName());
			holder.tv_medicine_name.setText(mData.getMedicineName());
			holder.tv_user_time.setText(mData.getRepeatDaily()+mData.getRepeatCount());
		}
	}

	/**
	 * 初始化布局
	 * 
	 * @param convertView
	 * @param holder
	 */
	private void initView(View convertView, ViewHolder holder) {
		if (convertView != null && holder != null) {
			holder.iv_medicine_notify = (ImageView) convertView
					.findViewById(R.id.iv_medicine_notify);
			holder.tv_user_name = (TextView) convertView
					.findViewById(R.id.tv_user_name);
			holder.tv_medicine_name = (TextView) convertView
					.findViewById(R.id.tv_medicine_name);
			holder.tv_user_time = (TextView) convertView
					.findViewById(R.id.tv_user_time);

		}
	}

	@Override
	public void refreshNew() {
		//sendRequest(null, null, 1, 1);
	}

	@Override
	public void refreshHeader(Model item, int count) {
		//sendRequest(null, null, 1, 1);
	}

	@Override
	public void refreshFooter(Model item, int count) {
	}

	@Override
	public int getTheCacheType() {
		return 0;
	}

	@Override
	public List<Model> getReallyList(Object object, Class type2) {
		return null;
	}

}
