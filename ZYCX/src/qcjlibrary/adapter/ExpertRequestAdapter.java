package qcjlibrary.adapter;

import java.util.List;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.adapter.base.BAdapter;
import qcjlibrary.adapter.base.ViewHolder;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.model.ModelMsg;
import qcjlibrary.model.ModelRequestMyAsk;
import qcjlibrary.model.base.Model;
import qcjlibrary.response.DataAnalyze;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午5:06:10
 * <p/>
 * 类描述：这个类是实现专家提问列表
 */

public class ExpertRequestAdapter extends BAdapter {

    public ExpertRequestAdapter(BaseActivity activity, List<Model> list) {
        super(activity, list);
    }

    public ExpertRequestAdapter(BaseFragment fragment, List<Model> list) {
        super(fragment, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_expert_request, null);
            initView(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        bindDataToView(holder, position);
        return convertView;
    }

    private void bindDataToView(ViewHolder holder, int position) {
        if (holder != null) {
            final ModelRequestMyAsk myAsk = (ModelRequestMyAsk) mList.get(position);
            holder.rl_agree.setVisibility(View.GONE);
            holder.tv_agree.setVisibility(View.GONE);
            holder.tv_noagree.setVisibility(View.GONE);
            holder.tv_expert_answer.setVisibility(View.GONE);
            if (myAsk != null) {
                holder.tv_title.setText(myAsk.getQuestion_content());
                holder.tv_answer.setText(myAsk.getQuestion_detail());
                if (!TextUtils.isEmpty(myAsk.getAnswercontent())) {
                    holder.tv_expert_answer.setText(myAsk.getAnswercontent());
                    holder.tv_expert_answer.setVisibility(View.VISIBLE);
                }
                holder.tv_date.setText(myAsk.getTime());
                if (myAsk.getEvaluate() != null) {
                    if (myAsk.getEvaluate().equals("1")) {
                        holder.tv_agree.setText("满意评价");
                        holder.tv_agree.setVisibility(View.VISIBLE);
                    } else if (myAsk.getEvaluate().equals("1")) {
                        holder.tv_noagree.setText("不满意评价");
                        holder.tv_noagree.setVisibility(View.VISIBLE);
                    } else {
                        holder.rl_agree.setVisibility(View.VISIBLE);
                        holder.iv_yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //TODO
                                myAsk.setEvaluate("1");
                                mBaseActivity.sendRequest(mApp.getUserImpl().evaQuestion(myAsk), ModelMsg.class, REQUEST_GET);
                            }
                        });
                        holder.iv_no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //TODO
                                myAsk.setEvaluate("2");
                                mBaseActivity.sendRequest(mApp.getUserImpl().evaQuestion(myAsk), ModelMsg.class, REQUEST_GET);
                            }
                        });
                    }
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
    private void initView(View convertView, ViewHolder holder) {
        if (convertView != null && holder != null) {
            holder.tv_title = (TextView) convertView
                    .findViewById(R.id.tv_title);
            holder.tv_answer = (TextView) convertView
                    .findViewById(R.id.tv_answer);
            holder.tv_expert_answer = (TextView) convertView
                    .findViewById(R.id.tv_expert_answer);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.rl_agree = (RelativeLayout) convertView
                    .findViewById(R.id.rl_agree);
            holder.iv_yes = (ImageView) convertView.findViewById(R.id.iv_yes);
            holder.iv_no = (ImageView) convertView.findViewById(R.id.iv_no);
            holder.tv_agree = (TextView) convertView
                    .findViewById(R.id.tv_agree);
            holder.tv_noagree = (TextView) convertView
                    .findViewById(R.id.tv_noagree);
        }
    }

    @Override
    public void refreshNew() {
        sendRequest(mApp.getUserImpl().myQuestion(null), ModelRequestMyAsk.class, REQUEST_GET, REFRESH_NEW);
    }

    @Override
    public void refreshHeader(Model item, int count) {
        sendRequest(mApp.getUserImpl().myQuestion(null), ModelRequestMyAsk.class, REQUEST_GET, REFRESH_NEW);
    }

    @Override
    public void refreshFooter(Model item, int count) {
        ModelRequestMyAsk myAsk = (ModelRequestMyAsk) item;
        myAsk.setLastid(myAsk.getQuestion_id());
        sendRequest(mApp.getUserImpl().myQuestion(myAsk), ModelRequestMyAsk.class, REQUEST_GET, REFRESH_FOOTER);
    }

    @Override
    public int getTheCacheType() {
        return 0;
    }

    @Override
    public Object onResponceSuccess(String str, Class class1) {
        return DataAnalyze.parseData(str, class1);
    }

    @Override
    public Object getReallyList(Object object, Class type2) {
        return object;
    }

    @Override
    public void addHeadList(List<Model> list) {
        addHeadListWay2(list);
    }
}
