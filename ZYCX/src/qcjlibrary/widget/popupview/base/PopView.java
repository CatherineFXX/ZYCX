package qcjlibrary.widget.popupview.base;

import com.zhiyicx.zycx.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

/**
 * author：qiuchunjia time：上午11:42:38
 * 类描述：这个类是实现PopupWindow的封装，作为activity或者fragment的一种行为！
 * 
 * 这样可以降低耦合
 *
 * 考虑到可变太多（加载的布局，初始化控件，设置监听器，等），就采用模版模式来实现
 * 
 * TODO 后期代码重构的时候再来书写这一块
 */

public abstract class PopView implements PopInterface {
	public Activity mActivity;
	private LayoutInflater mInflater;
	public PopupWindow mPopWindow;
	private View mView;

	private Object mData;
	private PopResultListener mResultListener;

	/**
	 * @param context
	 *            上下文
	 * @param xml
	 *            布局文件
	 */
	public PopView(Activity activity, Object object, PopResultListener resultListener) {
		this.mActivity = activity;
		this.mInflater = LayoutInflater.from(mActivity);
		this.mData = object;
		this.mResultListener = resultListener;
		initPopWindow();
	}

	public PopView(Context context, Object object, PopResultListener mResultListener) {
		this.mActivity = (Activity) context;
		this.mInflater = LayoutInflater.from(context);
		this.mData = object;
		this.mResultListener = mResultListener;
		initPopWindow();
	}

	/**
	 * 初始化popWindow
	 */
	/**
	 * @param xml
	 */
	public void initPopWindow() {
		if (mPopWindow == null) {
			mView = mInflater.inflate(getLayoutId(), null);
			mPopWindow = setPopWidthAndHeight(mView);
			mPopWindow.setBackgroundDrawable(new ColorDrawable(0));
			mPopWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					setWindowAlpha(1.0f);

				}
			});
			initPopView();
			initPopData(mData);
			setPopLisenter(mResultListener);
			setAnimationStyle(mPopWindow);
		}
	}

	/**
	 * 设置pop的动画
	 * 
	 * @param popupWindow
	 */
	public void setAnimationStyle(PopupWindow popupWindow) {
		if (popupWindow != null) {
			mPopWindow.setAnimationStyle(R.style.pop_anim_style_up_and_down);
		}
	}

	/**
	 * 设置pop的宽度和高度
	 * 
	 * @param dataView
	 * @return
	 */
	public PopupWindow setPopWidthAndHeight(View dataView) {
		PopupWindow popupWindow = new PopupWindow(dataView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		return popupWindow;
	}

	public PopupWindow setPopWidthAndHeight2(View dataView) {
		PopupWindow popupWindow = new PopupWindow(dataView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		return popupWindow;
	}

	public PopupWindow setPopWidthAndHeight3(View dataView) {
		PopupWindow popupWindow = new PopupWindow(dataView, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		return popupWindow;
	}

	public PopResultListener getResultListener() {
		return mResultListener;
	}

	public void setResultListener(PopResultListener mResultListener) {
		this.mResultListener = mResultListener;
	}

	/**
	 * @param id
	 *            view的id
	 *
	 * @return 代表改id的控件
	 */
	public View findViewbyId(int id) {
		if (mView != null) {
			return mView.findViewById(id);
		}
		return null;
	}

	/**
	 * 显示popWindow
	 */
	@SuppressLint("NewApi")
	public void showPop(View parent, int gravity, int x, int y) {
		if (mPopWindow != null) {
			// 设置popwindow显示位置
			if (parent != null && gravity == -1) {
				mPopWindow.showAsDropDown(parent, x, y);
			} else {
				mPopWindow.showAtLocation(parent, gravity, x, y);
			}
			// 获取popwindow焦点
			mPopWindow.setFocusable(true);
			// 设置popwindow如果点击外面区域，便关闭。
			mPopWindow.setOutsideTouchable(true);
			mPopWindow.update();
			setWindowAlpha(0.7f);
		}
	}

	/**
	 * 设置背景
	 * 
	 * @param alpha
	 */
	public void setWindowAlpha(float alpha) {
		if (mActivity != null) {
			WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
			params.alpha = alpha;
			mActivity.getWindow().setAttributes(params);
		}
	}

	/**
	 * @author qcj
	 *
	 */
	public interface PopResultListener {
		/**
		 * 返回数据给activity或者fragment或者其它的，具体更加情况而定
		 * 
		 * @param object
		 * @return
		 */
		Object onPopResult(Object object);
	}

}
