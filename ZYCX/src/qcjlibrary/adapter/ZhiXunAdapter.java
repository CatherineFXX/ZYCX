package qcjlibrary.adapter;

import java.util.List;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.adapter.base.BAdapter;
import qcjlibrary.adapter.base.ViewHolder;
import qcjlibrary.api.api.ZhiXunImpl;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.listview.base.CommonListView;
import qcjlibrary.model.ModelQclassDetail;
import qcjlibrary.model.ModelZiXun;
import qcjlibrary.model.ModelZiXunDetail;
import qcjlibrary.model.base.Model;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.utils.Log;
import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午5:06:10
 * 
 * 类描述：这个类是实现专家提问列表
 *
 */

public class ZhiXunAdapter extends BAdapter {

	private ModelZiXunDetail mDetail;
	private final int TYPE_COUNT = 2;
	private final int TYPE_1 = 0; // 普通的item
	private final int TYPE_2 = 1; // 热点的第一个item

	public ZhiXunAdapter(BaseActivity activity, ModelZiXunDetail data) {
		super(activity, null);
		mDetail = data;
	}

	public ZhiXunAdapter(BaseFragment fragment, ModelZiXunDetail data) {
		super(fragment, null);
		mDetail = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		int type = judgeTheType(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = initView(holder, type);
//			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		bindDataToView(holder, position, type);
		return convertView;
	}

	/**
	 * 判断需要加载的item类型
	 * 
	 * @param pos
	 * @return
	 */
	private int judgeTheType(int pos) {
		// 表示热点
		if (pos == 0 && mDetail.getFenlei_id() == 7) {
			return TYPE_2;
		}
		return TYPE_1;
	}

	private void bindDataToView(ViewHolder holder, int position, int type) {
		if (holder != null) {
			ModelZiXunDetail modelZiXunDetail = (ModelZiXunDetail) mList.get(position);
			if (modelZiXunDetail != null) {
				if (type == TYPE_1) {
					mApp.displayImage(modelZiXunDetail.getCover(), holder.iv_photo);
					holder.tv_content.setText(modelZiXunDetail.getTitle());
					holder.tv_date.setText(modelZiXunDetail.getcTime());
					holder.tv_from.setText("来自:" + modelZiXunDetail.getUname());
					String count = modelZiXunDetail.getReadCount();
					if (count.length() > 3) {
						count = "999+";
					}
					holder.tv_num.setText(count);
				} else {
					mApp.displayImage(modelZiXunDetail.getCover(), holder.iv_zixun);
					holder.tv_zixun_title.setText(modelZiXunDetail.getTitle());
					String count = modelZiXunDetail.getReadCount();
					if (count.length() > 3) {
						count = "999+";
					}
					holder.tv_zixun_num.setText(count);
				}
			}
		}
	}

	/**
	 * 初始化布局
	 * 
	 * @param convertView
	 * @param holder
	 */
	private View initView(ViewHolder holder, int type) {
		View view = null;
		if (holder != null) {
			if (type == TYPE_1) {
				view = mInflater.inflate(R.layout.item_zhixun, null);
				holder.iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
				holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
				holder.tv_date = (TextView) view.findViewById(R.id.tv_date);
				holder.tv_from = (TextView) view.findViewById(R.id.tv_from);
				holder.tv_num = (TextView) view.findViewById(R.id.tv_num);
				view.setTag(holder);
			} else {
				view = mInflater.inflate(R.layout.item_zhixun_big, null);
				holder.iv_zixun = (ImageView) view.findViewById(R.id.iv_zixun);
				holder.tv_zixun_title = (TextView) view.findViewById(R.id.tv_zixun_title);
				holder.tv_zixun_num = (TextView) view.findViewById(R.id.tv_zixun_num);
				view.setTag(holder);
			}
		}
		return view;
	}

	@Override
	public void refreshNew() {
		mDetail.setLastid(null);
		mDetail.setMaxid(null);
		requstMessage(mDetail, REFRESH_NEW);
	}

	@Override
	public void refreshHeader(Model item, int count) {
		//requstMessage(mDetail, REFRESH_NEW);
		refreshNew();
	}

	@Override
	public void refreshFooter(Model item, int count) {
		if (item instanceof ModelZiXunDetail) {
			ModelZiXunDetail detail = (ModelZiXunDetail) item;
			mDetail.setLastid(detail.getId());
			requstMessage(mDetail, REFRESH_FOOTER);
		}
	}

	/**
	 * 请求咨询内容
	 * 
	 * @param data
	 * @param type
	 */
	private void requstMessage(ModelZiXunDetail data, int type) {
		ZhiXunImpl xunImpl = new ZhiXunImpl();
		sendRequest(xunImpl.indexItem(data), ModelZiXun.class, 0, type);
	}

	@Override
	public int getTheCacheType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		return judgeTheType(position);
	}

	@Override
	public Object getReallyList(Object object, Class type2) {
		if (object instanceof ModelZiXun) {
			ModelZiXun modelZiXun = (ModelZiXun) object;
			List<ModelZiXunDetail> list = modelZiXun.getList();
			if(isLoading()){
				setLoading(false);
			}
			return list;
		}
		return null;
	}
	
	/**
	 * 更新部分数据
	 * 暂时不更新
	 * **/
	
	public void updataView(int posi, CommonListView listView) {  
        int visibleFirstPosi = listView.getFirstVisiblePosition();  
        int visibleLastPosi = listView.getLastVisiblePosition(); 
        ModelZiXunDetail data = (ModelZiXunDetail) mList.get(posi);
        if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {  
            View view = listView.getChildAt(posi - visibleFirstPosi);  
            ViewHolder holder = (ViewHolder) view.getTag(); 
            int readCount = 0;
            try{
            	readCount = Integer.parseInt(data.getReadCount());
            } catch(Exception e){
            	
            }
            if(readCount > 999 || holder == null){
            	return;
            }
            ++readCount;  
            data.setReadCount(readCount+"");;
            holder.tv_num.setText(readCount+"");
            mList.set(posi, data);  
        } else {  
        	int readCount = 0;
            try{
            	readCount = Integer.parseInt(data.getReadCount());
            } catch(Exception e){
            	
            }  
        	++readCount;  
        	data.setReadCount(readCount+"");
            mList.set(posi, data);  
        }  
    } 

}
