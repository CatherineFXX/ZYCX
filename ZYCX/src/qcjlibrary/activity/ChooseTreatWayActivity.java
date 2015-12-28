package qcjlibrary.activity;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.adapter.StringAdapter;
import qcjlibrary.config.Config;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午3:19:06 类描述：这个类是实现
 *
 */

public class ChooseTreatWayActivity extends BaseActivity {
	private ListView mListView;
	private StringAdapter mAdapter;
	private String[] mData = new String[] { "X线", "CT", "MR",
			"PET-CT（正电子发射体层摄影检查)", "超声检查（B超）", "血管造影（DSA）", "骨扫描（ECT）",
			"病理/细胞学检查", "鼻咽镜", "鼻内镜", "喉镜", "放射性同位素检查", "食管内镜", "支气管镜检查",
			"电子胃镜", "电子肠镜", "胸腔镜检查", "纵膈镜检查", "近红外线影像检查", "肝动脉造影" };

	@Override
	public String setCenterTitle() {
		return "检查项目";
	}

	@Override
	public void initIntent() {

	}

	@Override
	public int getLayoutId() {
		return R.layout.listview_layout;
	}

	@Override
	public void initView() {
		mListView = (ListView) findViewById(R.id.mListView);
		mAdapter = new StringAdapter(this, mData);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String data = (String) parent.getItemAtPosition(position);
				setReturnResultSeri(data, Config.TYPE_VIDEO_PROJECT);
				onBackPressed();
			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

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
