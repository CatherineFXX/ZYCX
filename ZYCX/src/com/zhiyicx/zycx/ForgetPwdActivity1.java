package com.zhiyicx.zycx;

import java.net.URLEncoder;

import org.json.JSONObject;

import qcjlibrary.activity.base.BaseActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyicx.zycx.config.MyConfig;
import com.zhiyicx.zycx.modle.HttpReturnData;
import com.zhiyicx.zycx.net.JsonDataListener;
import com.zhiyicx.zycx.net.NetComTools;
import com.zhiyicx.zycx.util.Utils;

/**
 * 设置新密码类
 *
 * @author Mr . H
 *
 */
public class ForgetPwdActivity1 extends BaseActivity {
	final private static String TAG = "SetNewPd";
	private Button mIbBtn_next;
	private TextView mIB_forget_getcode;
	private EditText et_phoneOrmail, et_forget_phonecode;
	String accountNumber, codeNumber;
	HttpReturnData httpReturnData = new HttpReturnData();

	@Override
	public String setCenterTitle() {
		return "找回密码";
	}

	@Override
	public void initIntent() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_register;
	}

	@Override
	public void initView() {
		mIbBtn_next = (Button) findViewById(R.id.btn_next);
		mIB_forget_getcode = (TextView) findViewById(R.id.tv_vertify_code);
		et_phoneOrmail = (EditText) findViewById(R.id.et_mobile);
		et_forget_phonecode = (EditText) findViewById(R.id.et_vertify);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListener() {
		mIbBtn_next.setOnClickListener(this);
		mIB_forget_getcode.setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_next:
			btn_Next();
			break;

		case R.id.tv_vertify_code:
			getCode();
			break;
		}
	}

	/**
	 * 下一步，发送手机或者邮箱和验证码
	 */
	public void btn_Next() {
		// TODO

		accountNumber = et_phoneOrmail.getText().toString().trim();
		httpReturnData.setStrForgetAccountNum(accountNumber);
		codeNumber = et_forget_phonecode.getText().toString().trim();
		// httpReturnData.setStrForgetPhoneCode(codeNumber);
		String path = MyConfig.FORGET_BTN_NEXT_URL + "&uid="
				+ URLEncoder.encode(accountNumber) + "&verify="
				+ URLEncoder.encode(codeNumber);

		NetComTools.getInstance(this).getNetJson(path, new JsonDataListener() {
			@Override
			public void OnReceive(JSONObject jsonObject) {
				Log.d(TAG, "User test jsonObject:" + jsonObject.toString());
				try {
					boolean success = (Boolean) jsonObject.get("data");
					if (success) {
						Log.d(TAG, "User test success!");
						String numsString = jsonObject.getString("code");
						httpReturnData.setStrForgetBtnNextRData(numsString);
						jumpResetWdUI();
					} else {
						String txt = jsonObject.optString("message", "验证失败");
						Utils.showToast(ForgetPwdActivity1.this, txt);
						// Log.d(TAG, "User test fail," +
						// (String)jsonObject.get("message"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void OnError(String error) {
				Log.d(TAG, "User test error, " + error);
			}
		});
	}

	/**
	 * 跳转重新设置密码界面
	 */
	public void jumpResetWdUI() {
		if (httpReturnData.getStrForgetBtnNextRData().equals("0")) {
			Intent intent = new Intent();
			intent.setClass(ForgetPwdActivity1.this, ForgetPwdActivity2.class);
			Bundle bundle = new Bundle();
			bundle.putString("accountNum", et_phoneOrmail.getText().toString()
					.trim());
			bundle.putString("accountCode", et_forget_phonecode.getText()
					.toString().trim());
			intent.putExtras(bundle);
			startActivity(intent);
			Toast.makeText(getApplicationContext(), "跳转重新设置密码界面", 0).show();
		} else {
			Toast.makeText(getApplicationContext(), "验证码错误", 0).show();
		}
	}

	/**
	 * 获取。找回密码界面中的手机验证码获取邮箱验证码
	 */
	public void getCode() {
		String str = et_phoneOrmail.getText().toString();
		if (str.indexOf("@") > -1) {
			accountNumber = et_phoneOrmail.getText().toString().trim();
			// String pictureCodeString = et_forget_picturecode.getText()
			// .toString().trim();
			// String path = MyConfig.FORGETMAIL_URL + "&email="
			// + URLEncoder.encode(accountNumber) + "&notify=mail_code"
			// + "&type=recover" + "&verify=" + pictureCodeString
			// + "&_debug=hequanli";
			String path = MyConfig.FORGETMAIL_URL + "&email="
					+ URLEncoder.encode(accountNumber) + "&notify=mail_code"
					+ "&type=recover" + "&_debug=hequanli";
			NetComTools.getInstance(this).getNetJson(path,
					new JsonDataListener() {
						@Override
						public void OnReceive(JSONObject jsonObject) {
							try {
								boolean success = (Boolean) jsonObject
										.get("data");
								if (success) {
									// Log.d(TAG, "Send code to mail success!");
									Utils.showToast(ForgetPwdActivity1.this,
											"验证码发送成功");
								} else {
									String txt = jsonObject.optString(
											"message", "验证码发送失败");
									Utils.showToast(ForgetPwdActivity1.this,
											txt);
									// Log.d(TAG, "Send code to mail fail," +
									// (String)jsonObject.get("message"));
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						@Override
						public void OnError(String error) {
							Utils.showToast(ForgetPwdActivity1.this, "验证码发送失败");
							Log.d(TAG, "Send code to mail error, " + error);
						}
					});
		} else {
			accountNumber = et_phoneOrmail.getText().toString().trim();
			// String pictureCodeString = et_forget_picturecode.getText()
			// .toString().trim();
			// String path = MyConfig.FORGETPHONE_URL + "&mobile="
			// + URLEncoder.encode(accountNumber) + "&notify=SEND_CODE"
			// + "&type=recover" + "&verify=" + pictureCodeString
			// + "&_debug=hequanli";
			String path = MyConfig.FORGETPHONE_URL + "&mobile="
					+ URLEncoder.encode(accountNumber) + "&notify=SEND_CODE"
					+ "&type=recover" + "&_debug=hequanli";
			NetComTools.getInstance(this).getNetJson(path,
					new JsonDataListener() {
						@Override
						public void OnReceive(JSONObject jsonObject) {
							try {
								boolean success = (Boolean) jsonObject
										.get("data");
								if (success) {
									// Log.d(TAG,
									// "Send code to phone success!");
									Utils.showToast(ForgetPwdActivity1.this,
											"验证码发送成功");
								} else {
									// Log.d(TAG, "Send code to phone fail," +
									// (String)jsonObject.get("message"));
									String txt = jsonObject.optString(
											"message", "验证码发送失败");
									Utils.showToast(ForgetPwdActivity1.this,
											txt);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						@Override
						public void OnError(String error) {
							Utils.showToast(ForgetPwdActivity1.this, "验证码发送失败");
							Log.d(TAG, "Send code to phone error, " + error);
						}
					});
		}
	}

}
