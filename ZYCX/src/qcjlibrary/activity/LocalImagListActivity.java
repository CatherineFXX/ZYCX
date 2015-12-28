package qcjlibrary.activity;

import java.util.ArrayList;
import java.util.List;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.util.localImageHelper.LocalImage;
import qcjlibrary.util.localImageHelper.PhotoDirInfo;
import qcjlibrary.util.localImageHelper.adapter.ImageListAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午4:01:50 类描述：这个类是实现
 *
 */

public class LocalImagListActivity extends BaseActivity {

	private ListView imageListView;
	private ArrayList<PhotoDirInfo> mDirInfos;
	private ImageListAdapter adapter;
	ArrayList<String> mPhotoPathlist;
	private String mReturnType;

	@Override
	public void onClick(View v) {

	}

	@Override
	public String setCenterTitle() {
		return "相册";
	}

	@Override
	public void initIntent() {
		Object object = getDataFromIntent(getIntent(), null);
		if (object instanceof String) {
			mReturnType = (String) object;
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_localimagelist_layout;
	}

	@Override
	public void initView() {
		imageListView = (ListView) findViewById(R.id.imageListView);
		mDirInfos = LocalImage.getImageDir(getApplicationContext());
		adapter = new ImageListAdapter(this, mDirInfos);
		imageListView.setAdapter(adapter);
		imageListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PhotoDirInfo photoDirInfo = mDirInfos.get(position);
				Intent intent = new Intent(getApplicationContext(),
						LocalAlbumActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				Bundle bundle = new Bundle();
				bundle.putString("bucketId", photoDirInfo.getDirId());
				intent.putExtras(bundle);
				startActivityForResult(intent, BaseActivity.ACTIVTIY_TRANFER);
			}
		});

	}

	@Override
	public void initListener() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Object object = getReturnResultSeri(resultCode, data, null);
		if (object instanceof List<?>) {
			mPhotoPathlist = (ArrayList<String>) object;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mPhotoPathlist != null) {
			Log.i("returntype", mReturnType + "");
			if (!TextUtils.isEmpty(mReturnType)) {
				setReturnResultSeri(mPhotoPathlist, mReturnType);
			} else {
				setReturnResultSeri(mPhotoPathlist, null);
			}
			onBackPressed();
		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
