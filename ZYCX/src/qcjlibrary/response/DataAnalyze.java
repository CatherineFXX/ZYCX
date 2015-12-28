package qcjlibrary.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import qcjlibrary.model.ModelMsg;
import qcjlibrary.util.JsonUtils;
import android.util.Log;

import com.google.gson.Gson;

/**
 * author：qiuchunjia time：下午2:44:40
 * 
 * 类描述：这个类是实现请求的返回的数据解析
 *
 */

public class DataAnalyze {
	public static final String[] flag = { "data", "result", "other" }; // 获取json里面想要的数据，这里面可以添加，然后实现循环获取东西

	/**
	 * 该方法通过传递进来的类型，来解析相应的数据对象，当为数组就解析为数据对象，为单个的就是解析为单个的
	 * 
	 * @param str
	 *            网络数据
	 * @param class1
	 *            需要解析的数据类型
	 * @return
	 */
	public static Object parseData(String str, Class class1) {
		if (str != null) {
			try {
				Log.i("parseData", str);
				JSONObject jsonObject = new JSONObject(str);
				for (int i = 0; i < flag.length; i++) {
					if (jsonObject.has(flag[i])) {
						Object judgeObject = jsonObject.get((flag[i]));
						if (!(judgeObject instanceof Boolean)) {
							String judgeStr = jsonObject.getString(flag[i]);
							if (judgeStr != null) {
								if (judgeStr.indexOf("[") == 0) {
									JSONArray dataArray = jsonObject
											.getJSONArray(flag[i]);
									// 当为数组的时候就返回
									if (dataArray != null) {
										return JsonUtils.parseJsonArray(
												dataArray, class1);
									}
								}
								JSONObject dataJson = jsonObject
										.getJSONObject(flag[i]);
								if (dataJson != null) {
									return JsonUtils.parseJsonObject(dataJson,
											class1);
								}

							}
						}
						return JsonUtils.parseJsonObject(jsonObject,
								ModelMsg.class);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 通过Gson来解析json
	 * 
	 * @param str
	 * @param class1
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object parseDataByGson(String str, Class class1) {
		if (str != null) {
			try {
				Log.i("Result", str);
				JSONObject jsonObject = new JSONObject(str);
				for (int i = 0; i < flag.length; i++) {
					if (jsonObject.has(flag[i])) {
						Gson gson = new Gson();
						Object judgeObject = jsonObject.get((flag[i]));
						if (!(judgeObject instanceof Boolean)) {
							String judgeStr = judgeObject.toString();
							if (judgeStr != null) {
								return gson.fromJson(judgeStr, class1);
							}
						}
						return gson.fromJson(jsonObject.toString(),
								ModelMsg.class);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
