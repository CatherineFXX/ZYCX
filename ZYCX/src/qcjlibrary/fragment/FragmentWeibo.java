package qcjlibrary.fragment;

import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.sociax.component.CommentList;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import qcjlibrary.fragment.base.BaseFragment;

public class FragmentWeibo extends BaseFragment{

	private CommentList lv_search_weibo;
	
	@Override
	public void onClick(View v) {
		
	}

	@Override
	public void initIntentData() {
		
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_weibo;
	}

	@Override
	public void initView() {
		lv_search_weibo = (CommentList) findViewById(R.id.lv_search_weibo);
	}

	@Override
	public void initListener() {
		
		lv_search_weibo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
			}
		});
	}

	@Override
	public void initData() {
		
	}

}
