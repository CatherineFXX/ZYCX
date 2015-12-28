package qcjlibrary.listview.base;

/***********************************************************************
 * Module:  BaseListView.java
 * Author:  qcj qq:260964739
 * Purpose: Defines the Class BaseListView
 ***********************************************************************/

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.adapter.base.BAdapter;
import qcjlibrary.listview.base.swipelistview.SwipeMenuAdapter;
import qcjlibrary.listview.base.xlistview.XListView;
import qcjlibrary.model.base.Model;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListAdapter;

import com.zhiyicx.zycx.sociax.android.Thinksns;

/** listview的基类 ，任何listview都可以继承它，减少代码的冗余 */
public abstract class BaseListView extends XListView implements
		XListView.IXListViewListener {
	private static final int MAX_COUNT = 12; // 小于这个就隐藏footer或者显示出来

	public BaseListView(Context context) {
		super(context, null);
		initSet(context);
		initXListView();
	}

	public BaseListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initSet(context);
		initXListView();
	}



	// /** mlist數據 */
	private List<Model> mList;
	private BAdapter mAdapter;
	private SwipeMenuAdapter menuAdapter;
	public Thinksns mApp;
	public BaseActivity mActivity;

	/** 初始化设置 */
	public void initSet(Context context) {
		this.setScrollbarFadingEnabled(true);
		this.setDividerHeight(1);
		this.setVerticalScrollBarEnabled(false);
		mApp = (Thinksns) context.getApplicationContext();
		mActivity = (BaseActivity) mApp.getActivity();
	}

	private void initXListView() {
		this.setPullRefreshEnable(true);
		this.setPullLoadEnable(true);
		this.setAutoLoadEnable(true);
		this.setFooterGone();
		this.setRefreshTime(getTime());
		this.setXListViewListener(this);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		if (adapter instanceof BAdapter) {
			this.mAdapter = (BAdapter) adapter;
			mAdapter.setListView(this);
			this.mList = ((BAdapter) adapter).getList();
		}
		if (adapter instanceof SwipeMenuAdapter) {
			menuAdapter = (SwipeMenuAdapter) adapter;
			menuAdapter.mAdapter.setListView(this);
			this.mList = menuAdapter.mAdapter.getList();
		}

	}

	@Override
	public int getCount() {
		int count = super.getCount();
		isSetFooterGone(count);
		return count;
	}

	public void isSetFooterGone(int count) {
		if (count < MAX_COUNT) {
			this.setFooterGone();
		} else {
			this.setFooterVisable();
		}
	}

	@Override
	public void onRefresh() {
		if (mAdapter != null) {
			mAdapter.doRefreshHeader();
		}
		if (menuAdapter != null) {
			menuAdapter.doRefreshHeader();
		}
	}

	@Override
	public void onLoadMore() {
		if (mAdapter != null) {
			mAdapter.doRefreshFooter();
		}
		if (menuAdapter != null) {
			menuAdapter.doRefreshFooter();
		}

	}

	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}

	/**
	 * 取消下拉刷新 或者加载更多
	 */
	public void onLoad() {
		this.stopRefresh();
		this.stopLoadMore();
		this.setRefreshTime(getTime());
	}
}
