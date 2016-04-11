package qcjlibrary.activity;

import java.util.List;

import com.zhiyicx.zycx.R;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.model.ModelRequestAnswerComom;
import qcjlibrary.model.ModelRequestDetailExpert;
import qcjlibrary.model.ModelRequestItem;
import qcjlibrary.model.ModelRequestMyAsk;
import qcjlibrary.model.base.Model;
import qcjlibrary.util.SpanUtil;

/**
 * 他人的专家问答界面
 * @author tan 2016 4 11
 * */

public class RequestOhterDetailExpertActivity extends BaseActivity{
	
	private TextView expert_tv_other_title;
	private TextView expert_tv_other_content;
	private TextView tv_expert_other_user;
	private TextView tv_expert_other_anwser_content;
	private ModelRequestMyAsk myAsk;
	// 数据model赛
	private ModelRequestItem mRequestItem;
	// 返回的数据
	private ModelRequestDetailExpert mDetailExpert;

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String setCenterTitle() {
		// TODO Auto-generated method stub
		return "问题详情";
	}

	@Override
	public void initIntent() {
		// TODO Auto-generated method stub
		myAsk = (ModelRequestMyAsk) getDataFromIntent(getIntent(), null);
		mRequestItem = new ModelRequestItem();
		mRequestItem.setQuestion_id(myAsk.getQuestion_id());
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_other_expert;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		expert_tv_other_title = (TextView) findViewById(R.id.expert_tv_other_title);
		expert_tv_other_content = (TextView) findViewById(R.id.expert_tv_other_content);
		tv_expert_other_user = (TextView) findViewById(R.id.tv_expert_other_user);
		tv_expert_other_anwser_content = (TextView) findViewById(R.id.tv_expert_other_anwser_content);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		sendRequest(mApp.getRequestImpl().answer(mRequestItem), ModelRequestDetailExpert.class, REQUEST_GET);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Object onResponceSuccess(String str, Class class1) {
		// TODO Auto-generated method stub
		Object object = super.onResponceSuccess(str, class1);
		if (object instanceof ModelRequestDetailExpert) {
			mDetailExpert = (ModelRequestDetailExpert) object;
			expert_tv_other_title.setText("");
			ModelRequestItem question = mDetailExpert.getQuestion();
			Drawable drawable = getResources().getDrawable(R.drawable.q);
			expert_tv_other_title.append(SpanUtil.setImageSpan("xx", 0, 0, drawable));
			expert_tv_other_title.append("  " + question.getQuestion_content());
			expert_tv_other_content.setText(question.getQuestion_detail());
			ModelRequestAnswerComom anwser = mDetailExpert.getAnswer();
			addDataToExpertAnswer(anwser, mDetailExpert.getCommentlist());
		}
		return object;
	}
	
	private void addDataToExpertAnswer(ModelRequestAnswerComom data, List<ModelRequestAnswerComom> answers) {
		tv_expert_other_user.setText("");
		Drawable drawable = getResources().getDrawable(R.drawable.a_yellow);
		tv_expert_other_user.append(SpanUtil.setImageSpan("x", 0, 0, drawable));
		tv_expert_other_user.append("  " + "专家建议");
		if (data != null) {
			if(data.getAnswer_content() != null && !data.getAnswer_content().equals(" ") && !data.getAnswer_content().equals("")){
				tv_expert_other_anwser_content.setText(data.getAnswer_content());
			}
		}
	}

}
