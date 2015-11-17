package qcjlibrary.fragment;

import qcjlibrary.activity.MeCenterActivity;
import qcjlibrary.activity.MePerioActivity;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.img.RoundImageView;
import qcjlibrary.model.base.Model;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.activity.GuideActivity;
import com.zhiyicx.zycx.sociax.android.Thinksns;
import com.zhiyicx.zycx.sociax.unit.Anim;

/**
 * author：qiuchunjia time：下午5:27:29 类描述：这个类是实现
 *
 */

public class FragmentMenu extends BaseFragment {
	private RelativeLayout rl_user;
	private RoundImageView riv_user_icon;
	private TextView tv_username;
	private ImageView iv_edit;
	private RelativeLayout rl_home;
	private RelativeLayout rl_question;
	private RelativeLayout rl_app;
	private RelativeLayout rl_cycle;
	private RelativeLayout rl_periodical;
	private Button btn_quit;

	@Override
	public void initIntentData() {

	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_slide_menu;
	}

	@Override
	public void initView() {
		rl_user = (RelativeLayout) findViewById(R.id.rl_user);
		riv_user_icon = (RoundImageView) findViewById(R.id.riv_user_icon);
		tv_username = (TextView) findViewById(R.id.tv_username);
		iv_edit = (ImageView) findViewById(R.id.iv_edit);
		rl_home = (RelativeLayout) findViewById(R.id.rl_home);
		rl_question = (RelativeLayout) findViewById(R.id.rl_question);
		rl_app = (RelativeLayout) findViewById(R.id.rl_app);
		rl_cycle = (RelativeLayout) findViewById(R.id.rl_cycle);
		rl_periodical = (RelativeLayout) findViewById(R.id.rl_periodical);
		btn_quit = (Button) findViewById(R.id.btn_quit);

	}

	@Override
	public void initListener() {
		riv_user_icon.setOnClickListener(this);
		iv_edit.setOnClickListener(this);
		rl_home.setOnClickListener(this);
		rl_question.setOnClickListener(this);
		rl_app.setOnClickListener(this);
		rl_cycle.setOnClickListener(this);
		rl_periodical.setOnClickListener(this);
		btn_quit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.riv_user_icon:

			break;
		case R.id.iv_edit:
			mApp.startActivity_qcj(getActivity(), MeCenterActivity.class,
					mActivity.sendDataToBundle(new Model(), null));

			break;
		case R.id.rl_home:

			break;
		case R.id.rl_question:

			break;
		case R.id.rl_app:

			break;
		case R.id.rl_cycle:

			break;
		case R.id.rl_periodical:
			mApp.startActivity_qcj(getActivity(), MePerioActivity.class,
					mActivity.sendDataToBundle(new Model(), null));
			break;
		case R.id.btn_quit:
			quitLogin();
			break;
		}
	}

	/**
	 * 退出登录
	 */
	private void quitLogin() {
		final Activity obj = getActivity();
		AlertDialog.Builder builder = new AlertDialog.Builder(obj);
		builder.setMessage("确定要注销此帐户吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Thinksns app = (Thinksns) obj.getApplicationContext();
						app.getUserSql().clear();
						// Thinksns.exitApp();
						Intent intent = new Intent(obj, GuideActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						obj.startActivity(intent);
						Anim.in(obj);
						obj.finish();
					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	@Override
	public void initData() {

	}

}