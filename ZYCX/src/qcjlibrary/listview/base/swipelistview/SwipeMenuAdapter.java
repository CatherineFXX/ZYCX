package qcjlibrary.listview.base.swipelistview;



import qcjlibrary.adapter.base.BAdapter;
import qcjlibrary.listview.base.swipelistview.SwipeMenuListView.OnMenuItemClickListener;
import qcjlibrary.listview.base.swipelistview.SwipeMenuView.OnSwipeItemClickListener;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

/**
 * 
 * @author baoyz
 * @date 2014-8-24
 * 
 */
public class SwipeMenuAdapter implements WrapperListAdapter,
		OnSwipeItemClickListener {

	public BAdapter mAdapter;
	private Context mContext;
	private OnMenuItemClickListener onMenuItemClickListener;

	public SwipeMenuAdapter(Context context, ListAdapter adapter) {
		mAdapter = (BAdapter) adapter;
		mContext = context;
	}

	@Override
	public int getCount() {
		return mAdapter.getCount();
	}

	@Override
	public Object getItem(int position) {
		return mAdapter.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return mAdapter.getItemId(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SwipeMenuLayout layout = null;
		if (convertView == null) {
			View contentView = mAdapter.getView(position, convertView, parent);
			SwipeMenu menu = new SwipeMenu(mContext);
			menu.setViewType(mAdapter.getItemViewType(position));
			createMenu(menu);
			SwipeMenuView menuView = new SwipeMenuView(menu,
					(SwipeMenuListView) parent);
			menuView.setOnSwipeItemClickListener(this);
			SwipeMenuListView listView = (SwipeMenuListView) parent;
			layout = new SwipeMenuLayout(contentView, menuView,
					listView.getCloseInterpolator(),
					listView.getOpenInterpolator());
			layout.setPosition(position);
		} else {
			layout = (SwipeMenuLayout) convertView;
			layout.closeMenu();
			layout.setPosition(position);
			View view = mAdapter.getView(position, layout.getContentView(),
					parent);
		}
		return layout;
	}

	public void createMenu(SwipeMenu menu) {
		// Test Code
		SwipeMenuItem item = new SwipeMenuItem(mContext);
		item.setTitle("Item 1");
		item.setBackground(new ColorDrawable(Color.GRAY));
		item.setWidth(300);
		menu.addMenuItem(item);

		item = new SwipeMenuItem(mContext);
		item.setTitle("Item 2");
		item.setBackground(new ColorDrawable(Color.RED));
		item.setWidth(300);
		menu.addMenuItem(item);
	}

	@Override
	public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index) {
		if (onMenuItemClickListener != null) {
			onMenuItemClickListener.onMenuItemClick(view.getPosition(), menu,
					index);
		}
	}

	public void setOnMenuItemClickListener(
			OnMenuItemClickListener onMenuItemClickListener) {
		this.onMenuItemClickListener = onMenuItemClickListener;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		mAdapter.registerDataSetObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		mAdapter.unregisterDataSetObserver(observer);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return mAdapter.areAllItemsEnabled();
	}

	@Override
	public boolean isEnabled(int position) {
		return mAdapter.isEnabled(position);
	}

	@Override
	public boolean hasStableIds() {
		return mAdapter.hasStableIds();
	}

	@Override
	public int getItemViewType(int position) {
		return mAdapter.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		return mAdapter.getViewTypeCount();
	}

	@Override
	public boolean isEmpty() {
		return mAdapter.isEmpty();
	}

	@Override
	public ListAdapter getWrappedAdapter() {
		return mAdapter;
	}

	public void doRefreshHeader() {
		if (mAdapter != null) {
			mAdapter.doRefreshHeader();
		}
	}

	public void doRefreshFooter() {
		if (mAdapter != null) {
			mAdapter.doRefreshFooter();
		}
	}

}
