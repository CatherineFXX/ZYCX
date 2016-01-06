package qcjlibrary.widget.popupview;

import com.zhiyicx.zycx.R;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import qcjlibrary.widget.popupview.base.PopView;

public class PopCommonProgress extends PopView {
	private ProgressBar progressBar1;
	private TextView tv_progress;

	public PopCommonProgress(Activity activity, Object object, PopResultListener resultListener) {
		super(activity, object, resultListener);
	}

	@Override
	public int getLayoutId() {
		return R.layout.pop_common_progress;
	}

	@Override
	public void initPopView() {
		progressBar1 = (ProgressBar) findViewbyId(R.id.progressBar1);
		tv_progress = (TextView) findViewbyId(R.id.tv_progress);
	}

	public void setProgress(long bytesWritten, long totalSize) {
		int value=(int) (bytesWritten*100/(float)totalSize);
		Log.i("value","value="+value);
		if (value >= 99) {
			
//			mPopWindow.dismiss();
		} else {
			tv_progress.setText(value + "%");
		}
	}

	@Override
	public void initPopData(Object object) {

	}
	@Override
	public PopupWindow setPopWidthAndHeight(View dataView) {
		return setPopWidthAndHeight2(dataView);
	}

	@Override
	public void setPopLisenter(PopResultListener listener) {

	}

}
