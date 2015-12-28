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

public class ChooseSurgeryWayActivity extends BaseActivity {
	private ListView mListView;
	private StringAdapter mAdapter;
	private String[] mData = new String[] { "手术", "化学治疗", "放射治疗", "中医药治疗",
			"分子靶向药物治疗", "生物免疫治疗", "经血管介入治疗", "内分泌治疗", "骨转移药物治疗", "无水乙醇瘤内注射治疗",
			"热治疗", "氩氦刀治疗", "光动力治疗", "止痛药物治疗", "其他治疗" };

	@Override
	public String setCenterTitle() {
		return "方式";
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
				setReturnResultSeri(data, Config.TYPE_CHECK_WAY);
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
