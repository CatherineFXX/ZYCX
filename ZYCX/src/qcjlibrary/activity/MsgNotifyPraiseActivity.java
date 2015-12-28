package qcjlibrary.activity;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.fragment.FragmentNotify;
import qcjlibrary.fragment.FragmentPraise;
import qcjlibrary.fragment.FragmentReplay;

import android.view.View;
import android.widget.TextView;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午3:05:41 类描述：这个类是实现
 *
 */

public class MsgNotifyPraiseActivity extends BaseActivity {

	private TextView tv_msg;
	private TextView tv_praise;
	private TextView tv_notify;

	private FragmentNotify mNotifyFg;
	private FragmentPraise mPraiseFg;
	private FragmentReplay mReplayFg;

	@Override
	public String setCenterTitle() {
		return "消息";
	}

	@Override
	public void initIntent() {

	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_msg_notify_praise;
	}

	@Override
	public void initView() {
		tv_msg = (TextView) findViewById(R.id.tv_msg);
		tv_praise = (TextView) findViewById(R.id.tv_praise);
		tv_notify = (TextView) findViewById(R.id.tv_notify);
	}

	@Override
	public void initData() {
		if (mReplayFg == null) {
			mReplayFg = new FragmentReplay();
		}
		replaceFragment(R.id.rl_content, mReplayFg);
		tv_msg.setBackgroundResource(R.drawable.view_border_green_left_solid_3);

		tv_msg.setTextColor(getResources().getColor(R.color.text_white));

	}

	@Override
	public void initListener() {
		tv_msg.setOnClickListener(this);
		tv_praise.setOnClickListener(this);
		tv_notify.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_msg:
			resetTextBg();
			tv_msg.setBackgroundResource(R.drawable.view_border_green_left_solid_3);
			tv_msg.setTextColor(getResources().getColor(R.color.text_white));
			if (mReplayFg == null) {
				mReplayFg = new FragmentReplay();
			}
			replaceFragment(R.id.rl_content, mReplayFg);
			break;

		case R.id.tv_praise:
			resetTextBg();
			tv_praise
					.setBackgroundResource(R.drawable.view_border_green_solid_0);
			tv_praise.setTextColor(getResources().getColor(R.color.text_white));
			if (mPraiseFg == null) {
				mPraiseFg = new FragmentPraise();
			}
			replaceFragment(R.id.rl_content, mPraiseFg);

			break;
		case R.id.tv_notify:
			resetTextBg();
			tv_notify
					.setBackgroundResource(R.drawable.view_border_green_right_solid_3);
			tv_notify.setTextColor(getResources().getColor(R.color.text_white));
			if (mNotifyFg == null) {
				mNotifyFg = new FragmentNotify();
			}
			replaceFragment(R.id.rl_content, mNotifyFg);
			break;
		}

	}

	/**
	 * 重置textview的背景
	 */
	private void resetTextBg() {
		tv_msg.setTextColor(getResources().getColor(R.color.text_more_gray));
		tv_praise.setTextColor(getResources().getColor(R.color.text_more_gray));
		tv_notify.setTextColor(getResources().getColor(R.color.text_more_gray));
		tv_msg.setBackgroundResource(R.drawable.view_border_green_left3);
		tv_praise.setBackgroundResource(R.drawable.view_border_green_0);
		tv_notify.setBackgroundResource(R.drawable.view_border_green_right_3);
	}

}
