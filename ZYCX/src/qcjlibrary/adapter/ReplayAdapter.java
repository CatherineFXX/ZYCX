package qcjlibrary.adapter;

import java.util.List;


import qcjlibrary.activity.NotifySingleReplyActivity;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.adapter.base.BAdapter;
import qcjlibrary.adapter.base.ViewHolder;
import qcjlibrary.fragment.base.BaseFragment;

import qcjlibrary.model.ModelNotifyCommment;
import qcjlibrary.model.base.Model;
import qcjlibrary.response.DataAnalyze;
import qcjlibrary.widget.RoundImageView;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午5:06:10
 * 
 * 类描述：这个类是实现专家提问列表
 *
 */

public class ReplayAdapter extends BAdapter {

	public ReplayAdapter(BaseActivity activity, List<Model> list) {
		super(activity, list);
	}

	public ReplayAdapter(BaseFragment fragment, List<Model> list) {
		super(fragment, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_msg_replay, null);
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

			ModelNotifyCommment commment = (ModelNotifyCommment) mList
					.get(position);
			if (commment != null) {
				mApp.displayImage(commment.getUserface(), holder.riv_msg_icon);
				holder.tv_user.setText(commment.getUsername());
				holder.tv_title.setText(commment.getContent());
				holder.tv_other_replay.setText(commment.getMyname()
						+ commment.getOriginal_content());
				holder.tv_date.setText(commment.getTime());
				holder.tv_replay.setTag(commment);
				holder.tv_replay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ModelNotifyCommment commment = (ModelNotifyCommment) v
								.getTag();
						mApp.startActivity_qcj(mBaseActivity,
								NotifySingleReplyActivity.class,
								mBaseActivity.sendDataToBundle(commment, null));
						// 进行评论
					}
				});
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
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			holder.tv_other_replay = (TextView) convertView
					.findViewById(R.id.tv_other_replay);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tv_replay = (TextView) convertView
					.findViewById(R.id.tv_replay);
		}
	}

	@Override
	public void refreshNew() {

		sendRequest(mApp.getNotifyImpl().commentlist(null),
				ModelNotifyCommment.class, REQUEST_GET, REFRESH_NEW);

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
		// TODO Auto-generated method stub
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
