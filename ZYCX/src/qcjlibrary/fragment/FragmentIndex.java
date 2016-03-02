package qcjlibrary.fragment;

import java.util.ArrayList;
import java.util.List;
import com.zhiyicx.zycx.LoginActivity;
import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.activity.HomeActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import qcjlibrary.activity.FoodWayActivity;
import qcjlibrary.activity.RequestAnwerCommonActivity;
import qcjlibrary.activity.UseMedicineNotifyActivity;
import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.model.ModelAds;
import qcjlibrary.model.base.Model;
import qcjlibrary.response.DataAnalyze;
import qcjlibrary.util.ToastUtils;
import qcjlibrary.util.UIUtils;
import qcjlibrary.widget.ads.MyADView;
import qcjlibrary.widget.ads.MyADViewModel;

/**
 * author：qiuchunjia time：上午10:32:51 类描述：这个类是实现
 */

public class FragmentIndex extends BaseFragment {
	private RelativeLayout rl_ads;
	private MyADView ad_view_ads;
	private RelativeLayout rl_1;
	private LinearLayout ll_first;
	private LinearLayout ll_second;
	private RelativeLayout rl_2;
	private RelativeLayout rl_3;
	private RelativeLayout rl_4;
	private RelativeLayout rl_5;
	private RelativeLayout rl_6;
	private HomeActivity mHomeActivity;
	private int height;
	private int width;

	@Override
	public void initIntentData() {
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_index;
	}

	@Override
	public void initView() {
		rl_ads = (RelativeLayout) findViewById(R.id.rl_ads);
		ad_view_ads = (MyADView) findViewById(R.id.ad_view_ads);
		ll_first = (LinearLayout) findViewById(R.id.ll_first);
		ll_second = (LinearLayout) findViewById(R.id.ll_second);
		rl_1 = (RelativeLayout) findViewById(R.id.rl_1);
		rl_2 = (RelativeLayout) findViewById(R.id.rl_2);
		rl_3 = (RelativeLayout) findViewById(R.id.rl_3);
		rl_4 = (RelativeLayout) findViewById(R.id.rl_4);
		rl_5 = (RelativeLayout) findViewById(R.id.rl_5);
		rl_6 = (RelativeLayout) findViewById(R.id.rl_6);
//		height = UIUtils.getWindowHeight(getActivity());
//		width = UIUtils.getWindowWidth(getActivity());
//		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,(height-400)/3);
//		ll_first.setLayoutParams(params);
//		ll_second.setLayoutParams(params);
	}

	@Override
	public void initListener() {
		rl_1.setOnClickListener(this);
		rl_2.setOnClickListener(this);
		rl_3.setOnClickListener(this);
		rl_4.setOnClickListener(this);
		rl_5.setOnClickListener(this);
		rl_6.setOnClickListener(this);
	}

	@Override
	public void initData() {
		if (mActivity instanceof HomeActivity) {
			mHomeActivity = (HomeActivity) mActivity;
		}
		sendRequest(mApp.getZhiXunImpl().appBanner(), ModelAds.class, BaseActivity.REQUEST_GET);
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = DataAnalyze.parseData(str, class1);
		if (object instanceof List) {
			List<ModelAds> modelAdses = (List<ModelAds>) object;
			List<MyADViewModel> myADViewModels = new ArrayList<MyADViewModel>();
			for (int i = 0; i < modelAdses.size(); i++) {
				MyADViewModel viewModel = new MyADViewModel();
				ModelAds modelAds = modelAdses.get(i);
				viewModel.setImgUrl(modelAds.getBannerpic());
				viewModel.setBannerurl(modelAds.getBannerurl());
				myADViewModels.add(viewModel);
			}
			try {
				ad_view_ads.setData(myADViewModels);
			} catch (Exception e) {
				ToastUtils.showToast("图片数据返回为空！");
				e.printStackTrace();
			}
		}
		return object;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_1:

			//setTabFragement(HomeActivity.index_qustion);
			mApp.startActivity_qcj(mActivity, RequestAnwerCommonActivity.class, mActivity.sendDataToBundle(new Model(), null));
			break;

		case R.id.rl_2:
			if(mHomeActivity != null){
				mHomeActivity.isExit = HomeActivity.qcl;
			}
			setTabFragement(HomeActivity.index_qclass);
			break;
		case R.id.rl_3:
			// TODO
			mApp.startActivity_qcj(mActivity, FoodWayActivity.class, mActivity.sendDataToBundle(new Model(), null));
			break;
		case R.id.rl_4:
			if(mHomeActivity != null){
				mHomeActivity.isExit = HomeActivity.exp;
			}
			setTabFragement(HomeActivity.index_qikan);
			break;
		case R.id.rl_5:
			if (isLogin()) {
				if(mHomeActivity != null){
					mHomeActivity.isExit = HomeActivity.cas;
				}
				setTabFragement(HomeActivity.index_web);
//				mApp.startActivity_qcj(mActivity, PatientMeActivity.class,
//						mActivity.sendDataToBundle(new Model(), null));
			} else {
				mApp.startActivity_qcj(mActivity, LoginActivity.class, null);
			}
			break;
		case R.id.rl_6:
			if (isLogin()) {
				mApp.startActivity_qcj(getActivity(), UseMedicineNotifyActivity.class,
						mActivity.sendDataToBundle(new Model(), null));
			} else {
				mApp.startActivity_qcj(mActivity, LoginActivity.class, null);
			}
			break;

		}
	}
	
	/**
	 * @param 跳转页的index
	 * 		首页点击跳转后切换标题与底部图片
	 * */
	private void setTabFragement(int index) {
		if (mHomeActivity != null) {
			mHomeActivity.setTabSelection(index);
			mHomeActivity.setTabImg(index);
		}
	}
}
