package qcjlibrary.activity;

import com.zhiyicx.zycx.R;

import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import qcjlibrary.activity.base.BaseActivity;

public class BannerDetailActivity extends BaseActivity {
	private WebView view_qikan;
	private String mUrl;
	private WebSettings settings;
	
	@Override
	public String setCenterTitle() {
		return " ";
	}

	@Override
	public void initIntent() {
		mUrl = getIntent().getStringExtra("bannerurl");
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_banner_detail;
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void initView() {
		view_qikan = (WebView) findViewById(R.id.view_qikan);
		view_qikan.getSettings().setJavaScriptEnabled(true);
		settings = view_qikan.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(false);
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf_8");
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//有时会让布局走样
        settings.setUseWideViewPort(true); 
        settings.setLoadWithOverviewMode(true);
	}

	@Override
	public void initData() {
		mUrl = "http://" + mUrl;
		view_qikan.loadUrl(mUrl);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
