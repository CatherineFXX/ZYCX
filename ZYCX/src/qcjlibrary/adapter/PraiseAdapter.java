package qcjlibrary.adapter;

import java.util.List;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.adapter.base.BAdapter;
import qcjlibrary.adapter.base.ViewHolder;
import qcjlibrary.fragment.base.BaseFragment;

import qcjlibrary.model.ModelNotifyDig;
import qcjlibrary.model.base.Model;
import qcjlibrary.response.DataAnalyze;
import qcjlibrary.widget.RoundImageView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午5:06:10
 * 
 * 类描述：这个类是实现专家提问列表
 *
 */

public class PraiseAdapter extends BAdapter {

	public PraiseAdapter(BaseActivity activity, List<Model> list) {
		super(activity, list);
	}

	public PraiseAdapter(BaseFragment fragment, List<Model> list) {
		super(fragment, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_msg_praise, null);
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

			ModelNotifyDig dig = (ModelNotifyDig) mList.get(position);
			if (dig != null) {
				mApp.displayImage(dig.getUserface(), holder.riv_msg_icon);
				holder.tv_user.setText(dig.getUsername());
				holder.tv_date.setText(dig.getTime());
				holder.tv_other_replay.setText(dig.getMyname() + ":"
						+ dig.getFeed_data());
			}

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
			holder.riv_msg_icon = (RoundImageView) convertView
					.findViewById(R.id.riv_msg_icon);
			holder.tv_user = (TextView) convertView.findViewById(R.id.tv_user);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			holder.tv_other_replay = (TextView) convertView
					.findViewById(R.id.tv_other_replay);
		}
	}

	@Override
	public void refreshNew() {

		sendRequest(mApp.getNotifyImpl().digglist(null), ModelNotifyDig.class,
				REQUEST_GET, REFRESH_NEW);

	}

	@Override
	public void refreshHeader(Model item, int count) {


	}

	@Override
	public void refreshFooter(Model item, int count) {
		// TODO Auto-generated method stub
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
