package qcjlibrary.activity;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.util.ToastUtils;
import qcjlibrary.util.UIUtils;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.util.ArrayList;

import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.fragment.WebAtomFragment;
import com.zhiyicx.zycx.widget.WeiboList;

/**
 * author：qiuchunjia time：下午4:07:29 类描述：这个类是实现
 *
 */

public class SearchNewActivity extends BaseActivity {

	private EditText et_search;
	private TextView tv_sure;
	private TextView tv_webo;
	private TextView tv_request;
	private TextView tv_zhixun;
	private TextView tv_qclass;
	private TextView tv_food;
	private TextView tv_line;
	private ViewPager mViewpager;
	private ImageView iv_search;
	
	private ArrayList<Fragment> mFragList;
	private WebAtomFragment mWebFrag;

	@Override
	public String setCenterTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initIntent() {

	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_search;
	}

	@Override
	public void initView() {
		et_search = (EditText) findViewById(R.id.et_search);
		tv_sure = (TextView) findViewById(R.id.tv_sure);
		tv_webo = (TextView) findViewById(R.id.tv_webo);
		tv_request = (TextView) findViewById(R.id.tv_request);
		tv_zhixun = (TextView) findViewById(R.id.tv_zhixun);
		tv_qclass = (TextView) findViewById(R.id.tv_qclass);
		tv_food = (TextView) findViewById(R.id.tv_food);
		tv_line = (TextView) findViewById(R.id.tv_line);
		mViewpager = (ViewPager) findViewById(R.id.mViewpager);
		iv_search = (ImageView)findViewById(R.id.iv_search);
		tv_line.getLayoutParams().width = UIUtils
				.getWindowWidth(getApplicationContext()) / 5;
	}

	@Override
	public void initData() {
		mFragList = new ArrayList<Fragment>();
		mWebFrag = new WebAtomFragment();
	}

	@Override
	public void initListener() {
		tv_sure.setOnClickListener(this);
		tv_webo.setOnClickListener(this);
		tv_request.setOnClickListener(this);
		tv_zhixun.setOnClickListener(this);
		tv_qclass.setOnClickListener(this);
		tv_food.setOnClickListener(this);
		iv_search.setOnClickListener(this);
		//监听回车按钮,将回车按钮改为搜索按钮
		et_search.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_SEARCH || 
						(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER )){
					
				}
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_sure:
			
			break;
		case R.id.iv_search:
			//搜索按钮
			String key = et_search.getText().toString();
			if(key != null){
				
			} else{
				ToastUtils.showLongToast(this, "请输入关键字");
			}
			break;
		case R.id.tv_webo:
			mViewpager.setCurrentItem(0);
			break;
		case R.id.tv_request:
			mViewpager.setCurrentItem(1);
			break;
		case R.id.tv_zhixun:
			mViewpager.setCurrentItem(2);
			break;
		case R.id.tv_qclass:
			mViewpager.setCurrentItem(3);
			break;
		case R.id.tv_food:
			mViewpager.setCurrentItem(4);
			break;
		default:
			break;
		}
	}
}
