package com.zhiyicx.zycx;

import java.net.URLEncoder;

import org.json.JSONObject;

import com.zhiyicx.zycx.activity.HomeActivity;
import com.zhiyicx.zycx.config.MyConfig;
import com.zhiyicx.zycx.net.JsonDataListener;
import com.zhiyicx.zycx.net.NetComTools;
import com.zhiyicx.zycx.sociax.android.Thinksns;
import com.zhiyicx.zycx.sociax.db.UserSqlHelper;
import com.zhiyicx.zycx.sociax.modle.User;
import com.zhiyicx.zycx.sociax.unit.Anim;
import com.zhiyicx.zycx.tools.PublicMethods;
import com.zhiyicx.zycx.util.PreferenceUtil;
import com.zhiyicx.zycx.util.Utils;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.model.ModelUser;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 注册类
 *
 * @author Mr . H
 *
 */
public class RegisterActivity2 extends BaseActivity implements JsonDataListener {

	private static final String TAG = "RegisterActivity2";
	private EditText et_pwd;
	private EditText et_pwd_sure;
	private Button btn_register;

	ModelUser mUser;
	private String mType_token;
	private String mType;
	private String mType_uid;

	@Override
	public String setCenterTitle() {
		return "注册";
	}

	@Override
	public void initIntent() {
		// TODO
		// Intent intent = getIntent();
		// String type_uid = intent.getStringExtra("type_uid");
		// String type = intent.getStringExtra("type");
		// String token = intent.getStringExtra("access_token");
		mUser = (ModelUser) getDataFromIntent(getIntent(), null);
		if (mUser != null) {
			mType_token = mUser.getToken();
			mType = mUser.getType();
			mType_uid = mUser.getType_uid();
		}

	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_register2;
	}

	@Override
	public void initView() {
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		et_pwd_sure = (EditText) findViewById(R.id.et_pwd_sure);
		btn_register = (Button) findViewById(R.id.btn_register);
	}

	@Override
	public void initData() {
		
	}

	@Override
	public void initListener() {
		btn_register.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_register:
			register();
			break;
		}

	}

	/**
	 * 注册方法
	 */
	public void register() {
		if (!et_pwd.getText().toString().trim()
				.equals(et_pwd_sure.getText().toString().trim())) {
			Toast.makeText(this, "两次输入密码不同", Toast.LENGTH_SHORT).show();
		} else {
			String str_phonenum = mUser.getMobile();
			String str_code = mUser.getVetifyCode();
			String str_userpasswd = PublicMethods
					.encryptPassWd(
							PublicMethods.encryptMD5(et_pwd.getText()
									.toString().trim()), "THINKSNS");

			String path = MyConfig.REGISTER_URL + "mobile="
					+ URLEncoder.encode(str_phonenum) + "&verifyCode="
					+ str_code + "&passwd=" + URLEncoder.encode(str_userpasswd);
			if (mType_token != null && mType != null && mType_uid != null) {
				// String token = mType_token.substring(0,
				// mType_token.indexOf('|')-1);
				// String refresh_token =
				// mType_token.substring(mType_token.indexOf('|')+1,
				// mType_token.length()-1);
				// Log.d(TAG, "token=" + token);
				// Log.d(TAG, "refresh_token=" + refresh_token);
				path += "&type=" + mType + "&type_uid=" + mType_uid
						+ "&access_token=" + mType_token;
			}
			NetComTools.getInstance(this).getNetJson(path, this);
		}
	}

	@Override
	public void OnReceive(JSONObject jsonObject) {
		Log.d(TAG, "Register response:" + jsonObject.toString());
		try {
			int code = jsonObject.getInt("code");
			if (code != 0) {
				String msg = jsonObject.optString("message");
				if (!TextUtils.isEmpty(msg))
					Utils.showToast(this, msg);
				else
					Utils.showToast(this, "注册失败");
			} else {
				Log.d(TAG, "Register success!");
				Utils.showToast(this, "注册成功");
				JSONObject jsonObject1 = jsonObject.getJSONObject("data");
				String oauth_token = jsonObject1.getString("oauth_token");
				String oauth_token_secret = jsonObject1
						.getString("oauth_token_secret");
				int uid = jsonObject1.getInt("uid");
				if (TextUtils.isEmpty(oauth_token)
						|| TextUtils.isEmpty(oauth_token_secret)) {
					Utils.showToast(this, "服务器返回错误");
					return;
				}
				// TODO 这里做了修改
				User tmpuser = new User(uid, "呵呵哒", et_pwd.getText().toString()
						.trim(), oauth_token, oauth_token_secret);
				// User tmpuser = new User(uid, et_register_name.getText()
				// .toString().trim(), et_passwd.getText().toString()
				// .trim(), oauth_token, oauth_token_secret);
				Thinksns.setMy(tmpuser);
				UserSqlHelper db = UserSqlHelper.getInstance(this);
				PreferenceUtil preferenceUtil = PreferenceUtil
						.getInstance(this);
				db.addUser(tmpuser, true);
				preferenceUtil.saveString("oauth_token_secret",
						tmpuser.getSecretToken());
				preferenceUtil.saveString("oauth_token", tmpuser.getToken());
				preferenceUtil.saveInt("uid", tmpuser.getUid());
				Intent intent = new Intent(this, HomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				this.startActivity(intent);
				Anim.in(this);
				this.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void OnError(String error) {
		Log.d(TAG, "Register error :" + error);
	}
}
