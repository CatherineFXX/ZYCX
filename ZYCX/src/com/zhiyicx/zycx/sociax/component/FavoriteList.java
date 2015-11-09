package com.zhiyicx.zycx.sociax.component;

import com.zhiyicx.zycx.sociax.adapter.UserListAdapter;
import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.sociax.android.Thinksns;
import com.zhiyicx.zycx.sociax.android.user.ThinksnsUserInfo;
import com.zhiyicx.zycx.sociax.modle.User;
import com.zhiyicx.zycx.sociax.unit.Anim;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;

public class FavoriteList extends SociaxList {
	private Context context;

	public FavoriteList(Context context) {
		super(context);
		this.context = context;
	}

	public FavoriteList(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onClick(View view, int position, long id) {
		if (view.getId() == R.id.footer_content) {
			ImageView iv = (ImageView) view.findViewById(R.id.anim_view);
			iv.setVisibility(View.VISIBLE);
			Anim.refresh(getContext(), iv);

			HeaderViewListAdapter headerAdapter = (HeaderViewListAdapter) this
					.getAdapter();
			UserListAdapter weiboLisAdapter = (UserListAdapter) headerAdapter
					.getWrappedAdapter();
			weiboLisAdapter.animView = iv;
			weiboLisAdapter.doRefreshFooter();
		} else {
            /*
			Thinksns app = (Thinksns) getContext().getApplicationContext();
			User user = (User) this.getItemAtPosition(position);
			Bundle bundle = new Bundle();
			bundle.putInt("uid", user.getUid());
			app.startActivity(getActivityObj(), ThinksnsUserInfo.class, bundle);
            */
		}
	}
}
