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

public class ChooseLabWayActivity extends BaseActivity {
	private ListView mListView;
	private StringAdapter mAdapter;
	private String[] mData = new String[] { "肝功能", "肾功能", "血糖", "血脂", "血氨",
			"血氨", "血电解质", "心肌酶谱" };

	@Override
	public String setCenterTitle() {
		return "实验检查";
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
				setReturnResultSeri(data, Config.TYPE_LAB_PROJECT);
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
