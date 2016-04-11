package qcjlibrary.activity;

import com.zhiyicx.zycx.R;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.util.UIUtils;

/**
 * 病历模版页
 * */
public class RequestModelActivity extends BaseActivity{
	
	private ImageView iv_model;

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String setCenterTitle() {
		// TODO Auto-generated method stub
		return "病历模板";
	}

	@Override
	public void initIntent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_request_model;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		iv_model = (ImageView) findViewById(R.id.iv_model);
		int width = UIUtils.getWindowWidth(this);
		int height = (int) (width * 1.58);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
		iv_model.setLayoutParams(params);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		
	}

}
