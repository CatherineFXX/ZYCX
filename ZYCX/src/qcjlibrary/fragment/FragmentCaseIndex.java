package qcjlibrary.fragment;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zhiyicx.zycx.R;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import qcjlibrary.activity.CaseHistoryActivity;
import qcjlibrary.activity.FoodWayActivity;
import qcjlibrary.activity.PatientMeActivity;
import qcjlibrary.activity.UseMedicineNotifyActivity;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.model.ModelCaseIndex;
import qcjlibrary.request.base.Request;
import qcjlibrary.util.ToastUtils;

/**
 * author：qiuchunjia time：下午4:03:45 类描述：这个类是实现
 */

public class FragmentCaseIndex extends BaseFragment {

    private RelativeLayout rl_mycase;
    private RelativeLayout rl_down;
    private RelativeLayout rl_history;
    private RelativeLayout rl_my;
    private TextView tv_name;
    private TextView tv_gender;
    private TextView tv_age;
    private TextView tv_update_day;
    private TextView tv_create_day;
    private TextView tv_look;
    private RelativeLayout rl_nodata;
    private TextView tv_edit;
    private LinearLayout ll_notify;
    private LinearLayout ll_food;

    @Override
    public void initIntentData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_case_index;
    }

    @Override
    public void initView() {
        rl_mycase = (RelativeLayout) findViewById(R.id.rl_mycase);
        rl_down = (RelativeLayout) findViewById(R.id.rl_down);
        rl_history = (RelativeLayout) findViewById(R.id.rl_history);
        rl_my = (RelativeLayout) findViewById(R.id.rl_my);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_update_day = (TextView) findViewById(R.id.tv_update_day);
        tv_create_day = (TextView) findViewById(R.id.tv_create_day);
        tv_look = (TextView) findViewById(R.id.tv_look);
        rl_nodata = (RelativeLayout) findViewById(R.id.rl_nodata);
        tv_edit = (TextView) findViewById(R.id.tv_edit);
        ll_notify = (LinearLayout) findViewById(R.id.ll_notify);
        ll_food = (LinearLayout) findViewById(R.id.ll_food);

    }

    @Override
    public void initData() {
        sendRequest(mApp.getMedRecordImpl().index(), ModelCaseIndex.class,
                REQUEST_GET);
    }

    @Override
    public Object onResponceSuccess(String str, Class class1) {
        Object object = super.onResponceSuccess(str, class1);
        if (object instanceof ModelCaseIndex) {
            ModelCaseIndex caseIndex = (ModelCaseIndex) object;
            rl_nodata.setVisibility(View.GONE);
            rl_my.setVisibility(View.VISIBLE);
            addUserDataToView(caseIndex);
        } else {
            judgeTheMsg(object);
        }
        return object;
    }

    /**
     * 添加用户数据到界面
     *
     * @param caseIndex
     */
    private void addUserDataToView(ModelCaseIndex caseIndex) {
        if (caseIndex != null) {
            tv_name.setText(caseIndex.getRealname());
            tv_gender.setText(caseIndex.getSex());
            tv_age.setText(caseIndex.getAge());
            tv_update_day.setText(caseIndex.getUtime());
            tv_create_day.setText(caseIndex.getCtime());
        }
    }

    @Override
    public void initListener() {
        rl_mycase.setOnClickListener(this);
        rl_my.setOnClickListener(this);
        rl_down.setOnClickListener(this);
        rl_history.setOnClickListener(this);
        tv_look.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        ll_notify.setOnClickListener(this);
        ll_food.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_mycase:
                mApp.startActivity_qcj(getActivity(), PatientMeActivity.class, null);
                break;
            case R.id.rl_my:
                mApp.startActivity_qcj(getActivity(), PatientMeActivity.class, null);
                break;

            case R.id.rl_down:

                break;
            case R.id.rl_history:

                mApp.startActivity_qcj(getActivity(), CaseHistoryActivity.class,
                        null);

                break;
            case R.id.tv_look:

                break;
            case R.id.tv_edit:

                mApp.startActivity_qcj(getActivity(), PatientMeActivity.class, null);
                break;
            case R.id.ll_notify:
                mApp.startActivity_qcj(getActivity(),
                        UseMedicineNotifyActivity.class, null);
                break;
            case R.id.ll_food:
                mApp.startActivity_qcj(mActivity, FoodWayActivity.class, null);
                break;

        }

    }

}
